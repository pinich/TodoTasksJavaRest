/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import model.*;


/**
 *
 * @author pini
 */
public class DAL {

    public static String mySqlConnectionString = "jdbc:mysql://pinich.ddns.net/dbPhoto?user=admin&password=admin1234";
//    public static String mySqlConnectionString = "jdbc:mysql://localhost/dbPhoto?user=root&password=1";

    public static String test() {
        String query = "SELECT * FROM `test`";
        DataBase db = DataBase.getInstance();
        JsonArray result = db.getRSasJsonArray(query);
        return "Output from Test DAL: " + result.size();
    }

    
    /**
     * Insert new comment into the DataBase, and change the ID of the object
     *
     * @param c - Comment
     * @return New ID
     */
    public static String insertComment(Comment c) {
        String output;
        String query = "INSERT INTO `comment` ( `body`, `date`, `user_id`, `post_id`) "
                + "VALUES ( '" + c.getBody() + "', CURRENT_TIMESTAMP, '" + c.getUser_id() + "', '" + c.getPost_id() + "')";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Insert(query));
        c.setId(output);
        return output;
    }

    /**
     * Gets Post ID and returns Hashmap of all the comments by this post ID, The
     * key is the Comment ID
     *
     * @param id The Post ID
     * @return HashMap of all the comments by PostID
     */
    public static JsonArray getCommentsByPostId(String id) {
        String query = "Select comm.* , `user`.`username` "
                + "FROM (SELECT * FROM `comment` WHERE `post_id` = '" + id + "') comm "
                + "LEFT JOIN `user` "
                + "ON comm.`user_id` = `user`.`id`";
        DataBase db = DataBase.getInstance();
        JsonArray rslt = db.getRSasJsonArray(query);
        return rslt;
    }



    //<editor-fold defaultstate="collapsed" desc="User DAL">

    /**
     * Get user by a given user id
     *
     * @param id
     * @return User object (with a given id), of null if user with given id was
     * not found
     */
    public static User getUserById(String id) {
        User output = null;
        String query = "Select * FROM `user` WHERE `id` = '" + id + "'";
        DataBase db = DataBase.getInstance();
        JsonArray rslt = db.getRSasJsonArray(query);
        if (rslt.size() > 0) {
            JsonObject o = (JsonObject) rslt.get(0);
            output = new User(o.get("id").getAsString(), o.get("username").getAsString(), o.get("pass").getAsString(), o.get("email").getAsString(), o.get("reg_date").getAsString(), o.get("active").getAsString(), o.get("firstName").getAsString(), o.get("lastName").getAsString(), o.get("birthDay").getAsString(), o.get("picture").getAsString());
        }
        return output;
    }

    /**
     * Insert given user into database
     *
     * @param u of type User
     * @return number of inserted rows, or -1 if there was a problem
     */
    public static String insertUser(User u) {
        String output;
        String query = "INSERT INTO `user` ( `username`, `pass`, `email`,`firstName`,`lastName`,`birthDay`,`picture`) "
                + "VALUES ( '" + u.getUserName() + "', '" + u.getPass() + "', '" + u.getEmail() + "','" + u.getFirstName() + "','" + u.getLastName() + "','" + u.getBirthDay() + "','" + u.getPicture() + "')";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Insert(query));
        u.setId(output);
        return output;
    }


    /**
     * Get all users from database as Json array
     *
     * @return Json array that contains all the users in database
     */
    public static JsonArray getAllUsers() {
        String query = "Select * FROM `user`";
        DataBase db = DataBase.getInstance();
        JsonArray rslt = db.getRSasJsonArray(query);
        return rslt;
    }


//</editor-fold>
    /**
     * Update user password in database from given User object (by id)
     *
     * @param u
     * @return number of updated rows in database, or -1 if fails
     */
    public static String updatePassword(User u) {
        String output;
        String query = "UPDATE `user` SET "
                + "`pass`= '" + u.getPass() + "' "
                + "WHERE `id` = '" + u.getId() + "'";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Update(query));
        return output;
    }



    public static String updateProfilePic(User u) {
        String output;
        String query = "UPDATE `user` SET "
                + "`picture`= '" + u.getPicture() + "' "
                + "WHERE `id` = '" + u.getId() + "'";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Update(query));
        return output;
    }

//</editor-fold>
    /**
     * User userPost by username
     *
     * @param username
     * @return User object, or null if there was no match
     */
    public static User userPost(String username) {
        User output = null;
        String query = "Select * FROM `user` WHERE `username`='" + username + "'";
        DataBase db = DataBase.getInstance();
        JsonArray rslt = db.getRSasJsonArray(query);
        if (rslt.size() > 0) {
            JsonObject o = (JsonObject) rslt.get(0);
            output = new User(o.get("id").getAsString(), o.get("username").getAsString(), o.get("pass").getAsString(), o.get("email").getAsString(), o.get("reg_date").getAsString(), o.get("active").getAsString(), o.get("firstName").getAsString(), o.get("lastName").getAsString(), o.get("birthDay").getAsString(), o.get("picture").getAsString());
        }
        return output;
    }
}
