package com.rejnowski.bluemedia;

import com.google.gson.Gson;
import com.rejnowski.bluemedia.db.DBDao;
import com.rejnowski.bluemedia.model.LoginRequest;
import com.rejnowski.bluemedia.model.LoginResponse;
import com.rejnowski.bluemedia.model.UrlPostRequest;
import com.rejnowski.bluemedia.model.UrlPostRequestResponse;
import com.rejnowski.bluemedia.db.HibernateUtil;
import com.rejnowski.bluemedia.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

    @GET
    @Path("/get")
//    @Produces("application/json")
//    @Consumes("application/json")
    public Response simpleGet( ) {


        return Response.ok("ok", MediaType.APPLICATION_JSON).build();
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
        Optional<User> userOpt = dao.getUser(request.getLogin());
        if (!userOpt.isPresent()) return Response.status(Response.Status.UNAUTHORIZED).build();
        else {
            User dbUser = userOpt.get();
            if (dbUser.getLogin().equals(request.getLogin()) && dbUser.getPassword().equals(request.getPassword())) {
                LoginResponse response = new LoginResponse("tokenik");

                message =gson.toJson(response);
                return Response.ok(message, MediaType.APPLICATION_JSON).build();

            } else return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }
}