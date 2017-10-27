package com.rejnowski.bluemedia;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rejnowski.bluemedia.model.LoginRequest;
import com.rejnowski.bluemedia.model.LoginResponse;
import com.rejnowski.bluemedia.model.UrlPostRequest;
import com.rejnowski.bluemedia.model.UrlPostRequestResponse;
import com.rejnowski.bluemedia.model.db.HibernateUtil;
import com.rejnowski.bluemedia.model.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = (User) session.get(User.class, 1);
        System.out.println(user.getLogin());

        return Response.ok("ok", MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    public Response login( String json) {
        System.out.println(json);
        Gson gson= new Gson();
        LoginRequest request = gson.fromJson(json,LoginRequest.class);
        String message;
        if(request.getLogin().equals("miki")&&request.getPassword().equals("admin"))
        {
            message = gson.toJson(new LoginResponse("token"));

        }
        else{
            return Response.status(Response.Status.UNAUTHORIZED).build();

        }
        return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }
}