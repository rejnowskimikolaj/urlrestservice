package com.rejnowski.bluemedia;

import com.google.gson.Gson;
import com.rejnowski.bluemedia.db.WebsiteResource;
import com.rejnowski.bluemedia.model.UrlPostRequestResponse;
import com.rejnowski.bluemedia.model.UrlsGetResponse;
import com.rejnowski.bluemedia.model.WebsiteGetResponse;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyResponseBuilder {

    public static Response noUrlArgumentResponse(){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        WebsiteGetResponse websiteGetResponse = new WebsiteGetResponse("no url argument",null);
        builder.entity(parser().toJson(websiteGetResponse));
        return builder.status(Response.Status.OK).build();
    }

    public static Response successfulGetUrlResponse(WebsiteResource websiteResource){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        List<WebsiteResource> resourceList = new ArrayList<>();
        resourceList.add(websiteResource);
        WebsiteGetResponse websiteGetResponse = new WebsiteGetResponse("success",resourceList );
        builder.entity(parser().toJson(websiteGetResponse));
        return builder.status(Response.Status.OK).build();
    }

    public static Response succesfulGetUrlsResponse(List<String> urls){

      return getUrlsResponse(urls,"success");
    }

    public static Response unSuccessfulGetUrlsResponse(){

        return getUrlsResponse(Collections.EMPTY_LIST,"no urls");
    }
    private static Response getUrlsResponse(List<String> urls,String message){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();

        UrlsGetResponse urlsGetResponse = new UrlsGetResponse(message,urls );
        builder.entity(parser().toJson(urlsGetResponse));
        return builder.status(Response.Status.OK).build();
    }
    public static Gson parser(){
        return new Gson();
    }
}
