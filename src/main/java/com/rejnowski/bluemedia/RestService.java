package com.rejnowski.bluemedia;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rejnowski.bluemedia.model.UrlPostRequest;
import com.rejnowski.bluemedia.model.UrlPostRequestResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class RestService {
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String getMessage() {
//        return "Hello world!";
//    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response postUrl( String json) {
        System.out.println(json);
        Gson gson= new Gson();
        UrlPostRequest request = gson.fromJson(json,UrlPostRequest.class);
        String message;
        if(request.getToken().equals("dobry_token"))
        {
            message = gson.toJson(new UrlPostRequestResponse("ok"));

        }
        else{
            message = gson.toJson(new UrlPostRequestResponse("dupa"));

        }
        return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }
}