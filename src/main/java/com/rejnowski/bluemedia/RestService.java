package com.rejnowski.bluemedia;

import com.google.gson.Gson;
import com.rejnowski.bluemedia.db.model.WebsiteResource;
import com.rejnowski.bluemedia.model.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
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
    public Response postUrl(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        UrlPostRequest request = gson.fromJson(json, UrlPostRequest.class);

        DBDao dao = new DBDao();
        boolean isTokenCorrect = dao.isTokenCorrect(request.getToken());
        if (isTokenCorrect) {
            PageDownloader.startDownloading(request.getUrl(), request.getDescription());

            return MyResponseBuilder.urlPostRequestAcceptedResponse();
        }
        return MyResponseBuilder.urlPostRequestNotAcceptedResponse();
    }


    @POST
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    public Response login(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(json, LoginRequest.class);
        DBDao dao = new DBDao();
        DBDao.LoginResult loginResult = dao.tryLogin(request.getLogin(), request.getPassword());
        if (loginResult == DBDao.LoginResult.PASSWORD_INCORRECT) {
            return MyResponseBuilder.passwordIncorrectResponse();
        } else if (loginResult == DBDao.LoginResult.LOGIN_INCORRECT) {
            return MyResponseBuilder.loginIncorrectResponse(request.getLogin());
        } else {

            return MyResponseBuilder.succesfulLoginResponse(request.getLogin());

        }

    }

    @GET
    @Path("/website")
    @Produces("application/json")
    public Response getWebsite(
            @QueryParam("id") int id,
            @QueryParam("max") int maxSize,
            @QueryParam("url") String url) {
        List<WebsiteResource> resources = new ArrayList<>();
        DBDao dao = new DBDao();
        if(id!=0){
            Optional<WebsiteResource> resourceOpt = dao.getWebsiteResourceById(id);
            if(resourceOpt.isPresent()) {
                resources.add(resourceOpt.get());
               return MyResponseBuilder.successfulGetResourcesResponse(resources);
            }
            else return MyResponseBuilder.noResourcesGetResourcesResponse();

        }
        if (url == null) return MyResponseBuilder.noUrlArgumentResponse();
         resources = dao.getWebsiteResourcesByUrl(url);

        if (resources.size() != 0) {
            if(resources.size()>maxSize&& maxSize>1) {
                resources = resources.subList(0,maxSize);
            }
            return MyResponseBuilder.successfulGetResourcesResponse(resources);
        } else {

            return MyResponseBuilder.noResourcesGetResourcesResponse();
        }

    }

    @GET
    @Path("/websites")
    @Produces("application/json")
    public Response getUrls(@QueryParam("where_source_has") String sourceText,@QueryParam("from") long from, @QueryParam("to") long to) {

        DBDao dao = new DBDao();
        if(sourceText!=null&&!sourceText.equals("")){
            List<UrlWithId> urls = dao.getUrlsWithSourceContainingString(sourceText);
            if(urls.size()==0)return MyResponseBuilder.unSuccessfulGetUrlsResponse();
            return MyResponseBuilder.succesfulGetUrlsResponse(urls);
        }
        if (to == 0) to = System.currentTimeMillis();
        List<UrlWithId> list = dao.getAllUrls(from, to);
        Gson gson = new Gson();
        if (list.size() == 0) return MyResponseBuilder.unSuccessfulGetUrlsResponse();
        else return MyResponseBuilder.succesfulGetUrlsResponse(list);
    }


}