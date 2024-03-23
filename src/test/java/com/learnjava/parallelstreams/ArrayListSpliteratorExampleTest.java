package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSpliteratorExampleTest {

    ArrayListSpliteratorExample arrayListSpliteratorExample = new ArrayListSpliteratorExample();

    @RepeatedTest(5)
    // repeated test is an annotation where the same test will be executed specified number of times.
    // idea behind is when the load increases on the machine, it tries to use the cache,
    // it may result in performance improvement, what would be the best result possible.
    void arrayListSeparatorExample_sequential() {

        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        List<Integer> resultList = arrayListSpliteratorExample.multiplyEachValue(inputList, 2, false);

        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void arrayListSeparatorExample_parallel() {

        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        List<Integer> resultList = arrayListSpliteratorExample.multiplyEachValue(inputList, 2, true);

        assertEquals(size, resultList.size());
    }
}