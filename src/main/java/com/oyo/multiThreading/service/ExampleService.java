package com.oyo.multiThreading.service;

import org.apache.juli.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;


@Service
public class ExampleService {
    private ExecutorService executors = null;
    private static final Integer SINGLE_REQUEST_SIZE = 10;
    private static final Integer THREAD_COUNT = 5;
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    private static final Logger logger = LogManager.getLogger(ExampleService.class);

    @PostConstruct
    private void initialize() {
        //create a thread pool after initialization of the bean
        executors = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    @PreDestroy
    private void destroy() {
        // shutdowm / kill thread pool before destroying the bean
        executors.shutdown();
    }

    public Long totalTimeTaken(Long timeOfOneCall, Integer noOfIteration, Integer noOfThread) {

        if(Objects.nonNull(noOfThread)){
            // if Thread count provided in the request
            executors = Executors.newFixedThreadPool(noOfThread);
        }


        Instant start = Instant.now();
        List<Future<Integer>> futureResults = new ArrayList<>();
        for(int iterationNo = 0; iterationNo < noOfIteration; iterationNo++){
            int finalIterationNo = iterationNo;
            Future<Integer> result = executors.submit(
                    new Callable<Integer>() {
                        @Override
                        public Integer call() throws InterruptedException {
                            Thread.sleep(timeOfOneCall);
                            return finalIterationNo;
                        }
                    }
            );
            futureResults.add(result);
        }
        for(Future<Integer> result : futureResults){
            try {
                Integer response = result.get();
                logger.info("iteration no {} time taken from start {}", response,
                        Duration.between(start, Instant.now()).toMillis());
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
        return Duration.between(start, Instant.now()).toMillis();
    }
}
