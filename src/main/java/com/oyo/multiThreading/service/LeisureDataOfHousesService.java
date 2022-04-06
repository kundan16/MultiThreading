package com.oyo.multiThreading.service;

import org.apache.commons.collections4.ListUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

@Service
public class LeisureDataOfHousesService {

    private ExecutorService executors = null;
    private static final Integer SINGLE_REQUEST_SIZE = 10;
    private static final Integer THREAD_COUNT = 5;
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

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

    // pass List of propertyIds for which you want to fetch DataOfHouses APIs
    public void syncDataOfHouses(List<String> activePropertyIds){

        // divide the propertyList in batch of 10 properties
        List<List<String>> propertiesPerThread = ListUtils.partition(activePropertyIds, SINGLE_REQUEST_SIZE);

        // Collection to collect return value of each threads
        List<Future<Pair<String, List<String>>>> results = new ArrayList<>();

        for(List<String> propertyIds : propertiesPerThread){
            Future<Pair<String, List<String> > > syncResult = executors.submit(
                    new Callable<Pair<String,List<String>>>() {
                        @Override
                        public Pair<String,List<String>> call() {

                            //call dataofhouses API for propertyIds
                            // and process the data

                            // return the result of the process
                            return Pair.of(SUCCESS,propertyIds);

                        }
                    });
            // save the return value in List.
            results.add(syncResult);
        }

        // iterate the return value
        for (Future<Pair<String, List<String> > > syncResult : results) {
            try {
                Pair<String, List<String> > response = syncResult.get();
                // process the result(response)
            } catch (InterruptedException | ExecutionException e) {
                // catch exception
            }
        }
    }
}

