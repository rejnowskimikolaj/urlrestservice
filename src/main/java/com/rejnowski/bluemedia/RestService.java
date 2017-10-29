package com.rejnowski.bluemedia;

import com.google.gson.Gson;
import com.rejnowski.bluemedia.db.DBDao;
import com.rejnowski.bluemedia.db.WebsiteResource;
import com.rejnowski.bluemedia.model.LoginRequest;
import com.rejnowski.bluemedia.model.LoginResponse;
import com.rejnowski.bluemedia.model.UrlPostRequest;
import com.rejnowski.bluemedia.model.UrlPostRequestResponse;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/bm")
public class RestService {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage() {
        return "Hello world!";
    }

    @POST
    @Path("/send_url")
    @Produces("application/json")
    @Consumes("application/json")
    public Response postUrl( String json) {
        System.out.println(json);
        Gson gson= new Gson();
        UrlPostRequest request = gson.fromJson(json,UrlPostRequest.class);

        DBDao dao = new DBDao();
        boolean isTokenCorrect = dao.isTokenCorrect(request.getToken());
        if(isTokenCorrect) {
            PageDownloader.startDownloading(request.getUrl());

            Response.ResponseBuilder builder = new ResponseBuilderImpl();
            UrlPostRequestResponse urlPostRequestResponse = new UrlPostRequestResponse("started downloading.");
            builder.entity(gson.toJson(urlPostRequestResponse));
            return builder.status(Response.Status.OK).build();
        }
        Response.ResponseBuilder builder = new ResponseBuilderImpl();
        UrlPostRequestResponse urlPostRequestResponse = new UrlPostRequestResponse("you have to login first.");
        builder.entity(gson.toJson(urlPostRequestResponse));
        return builder.status(Response.Status.UNAUTHORIZED).build();
    }


    @POST
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    public Response login( String json) {
        System.out.println(json);
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(json, LoginRequest.class);
        String message;
        DBDao dao = new DBDao();

        DBDao.LoginResult loginResult = dao.tryLogin(request.getLogin(),request.getPassword());
        if(loginResult== DBDao.LoginResult.PASSWORD_INCORRECT){
            Response.ResponseBuilder builder = new ResponseBuilderImpl();
            LoginResponse loginResponse = new LoginResponse("password incorrect","");
            builder.entity(gson.toJson(loginResponse));
            return builder.status(Response.Status.UNAUTHORIZED).build();
        }
        else if(loginResult== DBDao.LoginResult.LOGIN_INCORRECT) {
            Response.ResponseBuilder builder = new ResponseBuilderImpl();
            LoginResponse loginResponse = new LoginResponse("user doesn't exist","");
            builder.entity(gson.toJson(loginResponse));
            return builder.status(Response.Status.UNAUTHORIZED).build();
        }
        else {

            Response.ResponseBuilder builder = new ResponseBuilderImpl();
            Optional<String> tokenOpt = dao.getToken(request.getLogin());
            LoginResponse loginResponse = new LoginResponse("logged in successfuly",tokenOpt.get());
            builder.entity(gson.toJson(loginResponse));
            return builder.status(Response.Status.OK).build();
        }

    }

    @GET
    @Path("/website")
    @Produces("application/json")
    public Response getWebsite(
    @QueryParam("url") String url){

        DBDao dao = new DBDao();
        Optional<WebsiteResource> resourceOpt = dao.getWebsiteResourceByUrl(url);
        Gson gson= new Gson();

        if(resourceOpt.isPresent()) {

            Response.ResponseBuilder builder = new ResponseBuilderImpl();
            builder.entity(gson.toJson(resourceOpt.get()));
            return builder.status(Response.Status.OK).build();
        }
        else{
            Response.ResponseBuilder builder = new ResponseBuilderImpl();
            UrlPostRequestResponse urlPostRequestResponse = new UrlPostRequestResponse("no resource with this url");
            builder.entity(gson.toJson(urlPostRequestResponse));
            return builder.status(Response.Status.NO_CONTENT).build();
        }

    }


}