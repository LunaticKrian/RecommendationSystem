package com.mislab.core.systemcore;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ascendable
 * @since 2022/10/23
 */
public class StreamTest {
    public static void main(String[] args) {
        Map<Integer,Integer> map = new HashMap<>();
        map.put(1,12);
        map.put(2,8);
        map.put(3,15);
        Map<Integer, Integer> collect = map.entrySet().stream()
                .distinct()
                .filter(o -> o.getValue() > 10)
                /*.map(o->o.getValue()+"===>ascend")
                .map(s->s.substring(0,5))*/
                /*.forEach(System.out::println);*/
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));


        collect.forEach((key, value) -> {

        });
        /*.forEach(o-> System.out.println(o.getKey()+"===>"+o.getValue()));*///终结操作

    }
}
