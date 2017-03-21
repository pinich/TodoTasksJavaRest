package db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.*;


/**
 *
 * @author pini
 */
public class DAL {

    public static String mySqlConnectionString = "jdbc:mysql://localhost/test?useUnicode=yes&characterEncoding=UTF-8&user=root&password=";

    public static JsonArray getAllTodos() {
        String query = "Select * FROM `todo`";
        DataBase db = DataBase.getInstance();
        JsonArray rslt = db.getRSasJsonArray(query);
        return rslt;
    }

    public static String insertTodo(Todo c) {
        String output;
        String query = "INSERT INTO `todo` ( `message`, `date`, `status`) "
                + "VALUES ( '" + c.getMessage()+ "', CURRENT_TIMESTAMP, '" + c.getStatus() + "')";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Insert(query));
        c.setId(output);
        return output;
    }
    
    public static String deleteTodoById(String id) {
        String output;
        String query = "DELETE FROM `todo` WHERE `id` = '" + id + "'";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Delete(query));
        return output;
    }
    
    public static String updateTodo(Todo c) {
        String output;
        String query = "UPDATE `todo` SET "
                + "`message`= '" + c.getMessage()+ "' ,"
                + "`status`= '"+ c.getStatus() +"' ,"
                + "`other`= '"+ c.getOther()+"' "
                + "WHERE `id` = '" + c.getId() + "'";
        DataBase db = DataBase.getInstance();
        output = String.valueOf(db.Update(query));
        return output;
    }

    public static JsonObject getTodoById(String id) {
        String query = "SELECT * FROM `todo` WHERE `id` = '" + id + "'";
        DataBase db = DataBase.getInstance();
        JsonArray rsltArr = db.getRSasJsonArray(query);
        JsonObject rslt;
        if(rsltArr.size()>0){
            rslt = rsltArr.get(0).getAsJsonObject();
        }else{
            rslt = new JsonObject();
        }
        return rslt;
    }
    
    
}
