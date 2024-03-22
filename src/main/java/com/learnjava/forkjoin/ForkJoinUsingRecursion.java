package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

// First we have to mention the type of task we submit to fork/join work queue.
// are we expecting some result back or not? that can be set using Recursive Task or Recursive action,
// recursiveTask if we are expecting result back, in our case yes, so we are extending the class as RecursiveTask
// what type of data are we expecting will be specified as type, in our case List<String>
public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> inputList;

    public ForkJoinUsingRecursion(List<String> inputList) {
        this.inputList = inputList;
    }


    @Override
    /**
     * Recursively split the list and runs each half as a ForkJoinTask
     * Right way of using Fork/Join Task
     *
     * Idea is to split the array to 2 sub arrays,
     * invoke compute method on right sub array recursively.
     * invoke fork() on left sub array method asynchronously arranges this task in the deque,
     * meaning it submits the task for execution in the ForkJoinPool.
     * This task will handle the left subarray recursively.
     * The right subarray is processed recursively by calling compute()
     * Once the right subarray computation is complete, the current task waits for the result of the left subarray task that is by using .join()
     * once left sub array has completed it's execution results of left and right sub arrays are merged.
     */
    protected List<String> compute() {

        if (this.inputList.size() <= 1) {
            List<String> resultList = new ArrayList<>();
            // If array size is 1, then invoke the findLengthOfString() add output to result list.
            inputList.forEach(name -> resultList.add(findLengthOfString(name)));
            return resultList;
        }
        int midPoint = inputList.size() / 2;
        ForkJoinTask<List<String>> leftInputList = new ForkJoinUsingRecursion(inputList.subList(0, midPoint)) //left side of the list
                .fork(); // 1. asynchronously arranges this task in the deque,
        inputList = inputList.subList(midPoint, inputList.size()); //right side of the list
        List<String> rightResult = compute();
        List<String> leftResult = leftInputList.join();
        log("leftResult : "+ leftResult);
        leftResult.addAll(rightResult);
        return leftResult;
    }

    private String findLengthOfString(String name) {
        delay(500);
        return name.length() + " - " + name;
    }

    public static void main(String[] args) {

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinUsingRecursion forkJoinExampleUsingRecursion = new ForkJoinUsingRecursion(DataSet.namesList());
        stopWatch.start();

        // Start things running and get the result back, This is blocked until the results are calculated.
        List<String> resultList = forkJoinPool.invoke(forkJoinExampleUsingRecursion); // invoke -> Add the task to the shared queue from which all the other qu

        log("resultList : " + resultList);

        stopWatch.stop();
        log("Total time taken : " + stopWatch.getTime());
    }

}
