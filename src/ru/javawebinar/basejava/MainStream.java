package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        Integer[] nums = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(nums));
        System.out.println(oddOrEven(Arrays.asList(nums)));
    }

    static int minValue(Integer[] values) {
        return Arrays.stream(values)
                .sorted()
                .distinct()
                .reduce(0, (a, b) -> a * 10 + b);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> oddAndEven = integers.stream()
                .collect(Collectors.partitioningBy(i -> i % 2 == 0));

        return oddAndEven.get(false).size() % 2 == 0 ?
                oddAndEven.get(false) : oddAndEven.get(true);
    }
}
