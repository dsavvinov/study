package ru.spbau.mit;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by dsavv on 01.05.2016.
 */
public class ThreadPoolImpl {

    public ThreadPoolImpl(int n) {
        tasksQueue     = new LinkedList<>();
        threads        = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            threads.add(new Thread(new Worker()));
            threads.get(threads.size() - 1).start();
        }
    }

    public <R> LightFuture<R> submit(Supplier<R> todo) {
        LightFutureImpl<R> future = new LightFutureImpl<>(this);
        Task<R> t = new Task<>(todo, future, null);
        addTask(t);
        return future;
    }

    public <R, D> LightFuture<R> submitDependent(Supplier<R> dependentTodo, LightFutureImpl<D> dependency) {
        LightFutureImpl<R> future = new LightFutureImpl<>(this);
        Task<R> t = new Task<>(dependentTodo, future, dependency);
        addTask(t);
        return future;
    }

    public void shutdown() {
        threads.stream().forEach(Thread::interrupt);
    }

    private void addTask(Task t) {
        synchronized (tasksQueue) {
            tasksQueue.add(t);
            tasksQueue.notify();
        }
    }

    private Task getTask() {
        Task s;
        synchronized (tasksQueue) {
            while (tasksQueue.isEmpty()) {
                try {
                    tasksQueue.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            s = tasksQueue.remove();
        }
        return s;
    }
    private final List<Thread> threads;
    private final Queue<Task> tasksQueue;

    class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                Task s = getTask();
                try {
                    // if task doesn't have dependency, or this dependency finished successfully, then run supplier
                    if (s.dependency == null || (s.dependency.isReady() && !s.dependency.isFailed()) ) {
                        Object res = s.todo.get();
                        s.boundedFuture.updateResult(res);
                        continue;
                    }
                    // if dependency finished, but crashed, crash 'this'-task and mark it as failed with same result
                    if (s.dependency.isReady() && s.dependency.isFailed()) {
                        s.boundedFuture.markAsFailed(s.dependency.getCauseOfFail());
                        continue;
                    }
                    // if dependency is not finished yet, then add task to end of queue
                    addTask(s);
                }
                catch (Throwable e) {
                    s.boundedFuture.markAsFailed(e);
                }
            }
        }
    }


    private class Task<R> {
        Supplier<R> todo;
        LightFutureImpl<R> boundedFuture;
        LightFutureImpl dependency;

        Task(Supplier s, LightFutureImpl f, LightFutureImpl dep) {
            todo = s;
            boundedFuture = f;
            dependency = dep;
        }
    }

}
