package com.rejnowski.bluemedia.downloading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PageDownloader {

    public static final int MAX_REQUESTS_BEING_EXECUTED_AMOUNT =1;
    public static ExecutorService executorService = Executors.newFixedThreadPool(MAX_REQUESTS_BEING_EXECUTED_AMOUNT);

    public static void startDownloading(String urlString, String description){
       executorService.execute(new DownloadingRunnable(urlString,description));
    }
}
