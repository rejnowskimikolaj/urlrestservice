package com.rejnowski.bluemedia;

import com.rejnowski.bluemedia.db.model.WebsiteResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadingRunnable implements Runnable{
    private String urlString;
    private String description;

    public DownloadingRunnable(String urlString, String description) {
        this.urlString = urlString;
        this.description = description;
    }

    @Override
    public void run() {

        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        StringBuilder builder = new StringBuilder();
        try {
            url = new URL(urlString);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                builder.append(line);
                System.out.println(line);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            description = mue.getMessage();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            description = ioe.getMessage();
        }
        finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {}
        }

        WebsiteResource resource = new WebsiteResource(urlString,builder.toString(),description,System.currentTimeMillis());
        DBDao dao = new DBDao();
        dao.addResource(resource);

    }
}
