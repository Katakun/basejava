package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class MainStream {
    public static void main(String[] args) {
        Integer[] nums = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(nums));
        System.out.println(oddOrEven(Arrays.asList(nums)));
    }

    static int minValue(Integer[] values) {
        int res = Arrays.stream(values)
                .sorted()
                .distinct()
                .reduce(0, (a, b) -> a * 10 + b);
        return res;
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Map<String, List<Integer>> oddAndEven = integers.stream()
                .collect(groupingBy(i -> {
                    if (i % 2 == 0) return "even";
                    else return "odd";
                }));
        return oddAndEven.get("odd").size() % 2 == 0 ?
                oddAndEven.get("odd") : oddAndEven.get("even");
    }
}
