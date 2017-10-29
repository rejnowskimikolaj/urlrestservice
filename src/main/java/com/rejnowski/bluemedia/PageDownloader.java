package com.rejnowski.bluemedia;

import com.rejnowski.bluemedia.db.DBDao;
import com.rejnowski.bluemedia.db.WebsiteResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class PageDownloader {

    public static void startDownloading(String urlString){
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {}
        }

        WebsiteResource resource = new WebsiteResource(urlString,builder.toString(),"blabla,",System.currentTimeMillis());
        DBDao dao = new DBDao();
        dao.addResource(resource);
    }
}
