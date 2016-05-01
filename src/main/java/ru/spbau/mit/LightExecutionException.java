package ru.spbau.mit;

/**
 * Created by dsavv on 01.05.2016.
 */
public class LightExecutionException extends Exception {
    LightExecutionException() {}
    LightExecutionException(String msg, Throwable e) {
        super(msg, e);
    }
}
