package com.oyo.multiThreading.controller;

import com.oyo.multiThreading.service.ExampleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger logger = LogManager.getLogger(TestController.class);

    @Autowired
    private ExampleService exampleService;


    @RequestMapping(value = "timeTaken", method = RequestMethod.GET)
    public Long totalTimeTaken(@RequestParam Long timeOfOneIteration, @RequestParam Integer noOfIteration,
                               @RequestParam(required = false,defaultValue = "5") Integer noOfThreads){
        logger.info("received request for Iteration Count {} where one Iteration takes {}ms " +
                "with Thread count {} ", noOfIteration,timeOfOneIteration, noOfThreads);
        Long totalTimeTaken =  exampleService.totalTimeTaken(timeOfOneIteration,noOfIteration, noOfThreads);
        logger.info("total time taken to complete {} iteration where one Iteration takes {} with Thread count {}, is {}",
                noOfIteration, timeOfOneIteration,noOfThreads,totalTimeTaken);
        return totalTimeTaken;
    }
}
