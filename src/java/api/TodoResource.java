/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import db.DAL;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Todo;

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
        JsonArray posts = DAL.getAllTodos();
        result.add("data", posts);
        result.addProperty("status", 0);
        result.addProperty("msg", "Success");
        return Response.status(200).entity(result.toString()).build();
    }
    
    
    @GET
    @Path("/{idValue}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoById(@PathParam("idValue") String idValue) {
        JsonObject result = new JsonObject();
        JsonObject todos = DAL.getTodoById(idValue);
        result.add("data", todos);
        result.addProperty("status", 0);
        result.addProperty("msg", "");
        return Response.status(200).entity(result.toString()).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String content) {
        JsonObject o = (JsonObject) new JsonParser().parse(content);
        JsonObject result = new JsonObject();
        
        JsonObject tmp = DAL.getTodoById(o.get("id").getAsString());
        
        Todo u = new Todo(tmp.get("id").getAsString(),o.get("message").getAsString(),o.get("status").getAsInt(),tmp.get("date").getAsString(),o.get("other").getAsString());
        
        String res = DAL.updateTodo(u);
        if (!res.equals("-1")) {
            result.addProperty("data", res);
            result.addProperty("status", 0);
            result.addProperty("msg", "");
        } else {
            result.addProperty("msg", "Unable to update Todo");
            result.addProperty("status", -1);
        }
        return Response.status(200).entity(result.toString()).build();
    }
    
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String content) {
        JsonObject o = (JsonObject) new JsonParser().parse(content);
        JsonObject result = new JsonObject();
        
        Todo u = new Todo("-1",o.get("message").getAsString(),o.get("status").getAsInt(),"",o.get("other").getAsString());
        
        String res = DAL.insertTodo(u);
        if (!res.equals("-1")) {
            result.addProperty("data", res);
            result.addProperty("status", 0);
            result.addProperty("msg", "");
        } else {
            result.addProperty("msg", "Unable to Insert Todo");
            result.addProperty("status", -1);
        }
        return Response.status(200).entity(result.toString()).build();
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String content) {
        JsonObject o = (JsonObject) new JsonParser().parse(content);
        JsonObject result = new JsonObject();
        
        JsonObject tmp = DAL.getTodoById(o.get("id").getAsString());
        String res;
        if(tmp.has("id")){          //check if found
            res = DAL.deleteTodoById(tmp.get("id").getAsString());
        }else{
            res="-1";
        }
        if (!res.equals("-1")) {
            result.addProperty("data", res);
            result.addProperty("status", 0);
            result.addProperty("msg", "");
        } else {
            result.addProperty("msg", "Unable to Delete Todo");
            result.addProperty("status", -1);
        }
        return Response.status(200).entity(result.toString()).build();
    }
}
