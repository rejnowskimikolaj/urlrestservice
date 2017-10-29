package com.rejnowski.bluemedia;

import com.rejnowski.bluemedia.db.DBDao;
import com.rejnowski.bluemedia.db.WebsiteResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PageDownloader {

    public static final int MAX_REQUESTS_BEING_EXECUTED_AMOUNT =1;
    public static ExecutorService executorService = Executors.newFixedThreadPool(MAX_REQUESTS_BEING_EXECUTED_AMOUNT);

    public static void startDownloading(String urlString, String description){
       executorService.execute(new DownloadingRunnable(urlString,description));
    }
}
