package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;


public class ParallelStreamsExample {

    public List<String> stringTransform(List<String> namesList){
        return namesList
                .stream()
                .map(this::addNameLengthTransform)
                // .parallel()
                .collect(Collectors.toList());
    }

    // To process using parallelStreams use parallelStreams() instead of streams, it is that simple.
    public List<String> stringTransformUsingParallelStreams(List<String> namesList){
        return namesList
                .parallelStream()
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }

   /* public List<String> stringTransform_1(List<String> namesList, boolean isParallel) {

        Stream<String> nameStream = namesList.stream();

        if(isParallel)
            nameStream.parallel();

        return nameStream
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }*/

    private String addNameLengthTransform(String name) {
        delay(500);
        //log("Transforming : " + name);
        return name.length() + " - " + name;
    }

    public static void main(String[] args) {

        // using java streams.
        List<String> namesList = DataSet.namesList();
        log("namesList : " + namesList);
        startTimer();
        ParallelStreamsExample parallelismExample = new ParallelStreamsExample();
        List<String> resultList =parallelismExample.stringTransform(namesList);
        System.out.println("Time taken when java streams are used.");
        timeTaken();
        log("resultList : " + resultList);

        // using java parallel streams
        List<String> namesList1 = DataSet.namesList();
        log("namesList : " + namesList1);
        startTimer();
        ParallelStreamsExample parallelismExample1 = new ParallelStreamsExample();
        List<String> resultList1 =parallelismExample1.stringTransformUsingParallelStreams(namesList1);
        System.out.println("Time taken when java parallel streams are used.");
        timeTaken();
        log("resultList : " + resultList1);
    }
}
