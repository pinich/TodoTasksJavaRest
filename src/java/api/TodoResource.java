/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author pini
 */
@Path("todo")
public class TodoResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TodoResource
     */
    public TodoResource() {
    }

    /**
     * Retrieves representation of an instance of api.PostResource
     * @return Json with all the posts
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {
        JsonObject result = new JsonObject();
        //JsonArray posts = DAL.getAllPosts();
        JsonArray posts = new JsonArray();
        result.add("data", posts);
        result.addProperty("status", 0);
        result.addProperty("msg", "Success");
        return Response.status(200).entity(result.toString()).build();
    }
    
    
    @GET
    @Path("ent/user_id/{idValue}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByUserId(@PathParam("idValue") String idValue) {
        JsonObject result = new JsonObject();
        //JsonArray posts = DAL.getPostsByUserId(idValue);
        JsonArray posts = new JsonArray();
        result.add("data", posts);
        result.addProperty("status", 0);
        result.addProperty("msg", "");
        return Response.status(200).entity(result.toString()).build();
    }
    
}
