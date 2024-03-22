package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    // parameterized test is used when we want to test the same method with some parameter value changed.
    // in our case we need to test with isParallel true and false.
    // it will execute different test for each value present in @ValueSource.
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void stringTransform_1(boolean isParallel) {

        List<String> inputList = DataSet.namesList();
        List<String> result = parallelStreamsExample.stringTransform_1(inputList, isParallel);

        assertEquals(4, result.size());
    }
}