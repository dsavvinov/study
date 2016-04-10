/**
 * Created by dsavv on 05.04.2016.
 */
package sp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class SecondPartTasks {

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {

        // Я ведь прав, что по-хорошему, мы не должны ловить здесь это исключение, поскольку мы не знаем,
        // что с ним здесь делать?
        // Просто с одной стороны, вроде как выводить в catch-блоке только стектрейс и ничего больше -- явный
        // признак, что что-то пошло не так.
        // С другой стороны, я не могу не ловить это исключение, т.к. метод findQuotes не объявлен бросающим исключения

        return paths.stream()
                .flatMap(file -> {
                    try {
                        return Files.lines(Paths.get(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter((String str) -> str.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random randGen = new Random();
        int count = 10000;
        return (double) randGen.doubles(count)
                .map(x -> Math.pow(x - 0.5, 2) + Math.pow(randGen.nextDouble() - 0.5, 2))
                .filter(dist -> dist < 0.25)
                .count()
                /
                count;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().stream()
                        .mapToInt(String::length)
                        .sum())
                )
                .max((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .map(AbstractMap.SimpleEntry::getKey)
                .orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream()
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a + b));
    }
}

