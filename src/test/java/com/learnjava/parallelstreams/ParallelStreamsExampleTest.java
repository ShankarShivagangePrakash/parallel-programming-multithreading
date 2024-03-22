package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsExampleTest {

    ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();

    @Test
    void stringTransform() {
        List<String> inputList = DataSet.namesList();
        List<String> result = parallelStreamsExample.stringTransform(inputList);

        assertEquals(4, result.size());

        result.forEach(name ->
                assertTrue(name.contains("-")));
    }
}