package com.rejnowski.bluemedia.rest_communication;

import com.google.gson.Gson;
import com.rejnowski.bluemedia.db.DBDao;
import com.rejnowski.bluemedia.db.model.WebsiteResource;
import com.rejnowski.bluemedia.rest_communication.model.*;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MyResponseBuilder {

    public static Response loginIncorrectResponse(String login){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        LoginResponse loginResponse = new LoginResponse("user "+login+" doesn't exist","");
        builder.entity(parser().toJson(loginResponse));
        return builder.status(Response.Status.UNAUTHORIZED).build();
    }
    public static Response passwordIncorrectResponse(){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        LoginResponse loginResponse = new LoginResponse("incorrect password","");
        builder.entity(parser().toJson(loginResponse));
        return builder.status(Response.Status.UNAUTHORIZED).build();
    }
    public static Response succesfulLoginResponse(String login){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        DBDao dao = new DBDao();
        Optional<String> tokenOpt = dao.getToken(login);
        LoginResponse loginResponse = new LoginResponse("hello, "+login+"!",tokenOpt.get());
        builder.entity(parser().toJson(loginResponse));
        return builder.status(Response.Status.OK).build();
    }

    public static Response urlPostRequestAcceptedResponse(){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        UrlPostRequestResponse urlPostRequestResponse = new UrlPostRequestResponse("request accepted");
        builder.entity(parser().toJson(urlPostRequestResponse));
        return builder.status(Response.Status.OK).build();
    }

    public static Response urlPostRequestNotAcceptedResponse(){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        UrlPostRequestResponse urlPostRequestResponse = new UrlPostRequestResponse("you have to login first.");
        builder.entity(parser().toJson(urlPostRequestResponse));
        return builder.status(Response.Status.UNAUTHORIZED).build();
    }

    public static Response noUrlArgumentResponse(){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        WebsiteGetResponse websiteGetResponse = new WebsiteGetResponse("no url argument",null);
        builder.entity(parser().toJson(websiteGetResponse));
        return builder.status(Response.Status.OK).build();
    }

    public static Response successfulGetResourcesResponse(List<WebsiteResource> websiteResources){
      return   getResourcesResponse(websiteResources,"success");
    }

    private static Response getResourcesResponse(List<WebsiteResource> websiteResources,String message){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();

        WebsiteGetResponse websiteGetResponse = new WebsiteGetResponse("success",websiteResources );
        builder.entity(parser().toJson(websiteGetResponse));
        return builder.status(Response.Status.OK).build();
    }

    public static Response noResourcesGetResourcesResponse(){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();

        WebsiteGetResponse websiteGetResponse = new WebsiteGetResponse("no resources",Collections.EMPTY_LIST );
        builder.entity(parser().toJson(websiteGetResponse));
        return builder.status(Response.Status.OK).build();


    }


    public static Response succesfulGetUrlsResponse(List<UrlWithId> urls){

      return getUrlsResponse(urls,"success");
    }

    public static Response unSuccessfulGetUrlsResponse(){

        return getUrlsResponse(Collections.EMPTY_LIST,"no urls");
    }
    private static Response getUrlsResponse(List<UrlWithId> urls, String message){
        Response.ResponseBuilder builder = new ResponseBuilderImpl();

        UrlsGetResponse urlsGetResponse = new UrlsGetResponse(message,urls );
        builder.entity(parser().toJson(urlsGetResponse));
        return builder.status(Response.Status.OK).build();
    }
    public static Gson parser(){
        return new Gson();
    }
}
