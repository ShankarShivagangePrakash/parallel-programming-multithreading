package com.learnjava.parallelstreams;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamResultOrder {


    // accepts list and return list.
    public static List<Integer> listOrder(List<Integer> inputList){
        return inputList.parallelStream()
                .map(i->i*2)
                .collect(Collectors.toList());

    }

    // accept Set and returns set.
    public static Set<Integer> setOrder(Set<Integer> inputList){
        return inputList.parallelStream()
                .map(i->i*2)
                .collect(Collectors.toSet());
    }

    // accepts the list and returns set.
    public static Set<Integer> listOrder_Set(List<Integer> inputList){
        return inputList.parallelStream()
                .map(i->i*2)
                .collect(Collectors.toSet());

    }

    // accept set and returns list.
    public static List<Integer> setOrder_List(Set<Integer> inputList){
        return inputList.parallelStream()
                .map(i->i*2)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        System.out.println("Order List using Input list and output list");
        List<Integer> input = List.of(1,2,3,4,5,6,7,8,9);
        log("inputList : "+input);
        List<Integer> result = listOrder(input);
        log("result : "+result);

        System.out.println("Un Order set using Input set and output set");
        Set<Integer> SetInput = Set.of(1,2,3,4,5,6,7,8,9);
        log("inputList : "+SetInput);
        Set<Integer> setOutput = setOrder(SetInput);
        log("result : "+ setOutput);

        System.out.println("Un Order set using Input list and output set");
        List<Integer> listInput = List.of(1,2,3,4,5,6,7,8,9);
        log("inputList : "+listInput);
        //List<Integer> result = listOrder_Set(input);
        log("result : "+listOrder_Set(listInput));

        System.out.println("Order List using Input set and output list");
        Set<Integer> setInput1 = Set.of(1,2,3,4,5,6,7,8,9);
        log("inputList : "+setInput1);
        log("result : "+setOrder_List(setInput1));
    }
}
