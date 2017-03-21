package db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pinchasc
 */
public class DataBase {
    
    private Statement stmt = null;
    private Connection conn = null;
    
    private static volatile DataBase instance =null;
    
    private DataBase() {
        super();
    }
    
    public static DataBase getInstance(){
        if(instance == null){
            instance = new DataBase();
        }
        return instance;
    }
    
    private void connect() {
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();            //MySql connection
            conn = DriverManager.getConnection(DAL.mySqlConnectionString);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return resultSet of the statement.
     *
     * @param sqlQuery
     * @return JsonArray of resultSet
     */
    public JsonArray getRSasJsonArray(String sqlQuery)  {
        connect();
        JsonArray jsonArray = new JsonArray();
        try {
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                int total_rows = resultSet.getMetaData().getColumnCount();
                JsonObject obj = new JsonObject();
                for (int i = 0; i < total_rows; i++) {
                    String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);
                    // if value in DB is null, then we set it to default value
                    if (columnValue == null) {
                        columnValue = "null";
                    }
                    if (obj.has(columnName)) {
                        columnName += "1";
                    }
                    obj.addProperty(columnName, columnValue.toString());
                }
                jsonArray.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally{
            closeConn();
        }
        return jsonArray;
    }

    /**
     * Return resultSet of the statement.
     *
     * @param sqlQuery
     * @return
     */
    public ArrayList<String[]> getRS(String sqlQuery) {
        connect();
        ArrayList<String[]> arr = new ArrayList<String[]>();
        int rowSize = 0;
        try {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            rowSize = (rs.getMetaData()).getColumnCount();
            while (rs.next()) {                  //move to the first row
                String row[] = new String[rowSize];
                for (int i = 0; i < rowSize; i++) {
                    row[i] = rs.getString(i + 1);
                }
                arr.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        closeConn();
        return arr;
    }

    /**
     * Update Database return updated row count
     *
     * @param sqlQuery
     */
    public int Update(String sqlQuery) {
        connect();
        int updatedCount=-1;
        try {
            stmt.executeUpdate(sqlQuery);
            updatedCount = stmt.getUpdateCount();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeConn();
            return updatedCount;
        }
    }
    
    public int Delete(String sqlQuery) {
        return Update(sqlQuery);
    }

        /**
     * Update Database return updated row count
     *
     * @param sqlQuery
     */
    public int Insert(String sqlQuery) {
        connect();
        int insertID=-1;
        try {
            stmt.executeUpdate(sqlQuery,Statement.RETURN_GENERATED_KEYS);
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                insertID=rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeConn();
            return insertID;
        }
    }
    
    /**
     * Get the current statement of the object
     *
     * @return
     */
    public Statement getStmt() {
        return stmt;
    }

    /**
     * Get connection
     *
     * @return
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * Close the connection and the statement
     */
    public void closeConn() {
        try {
            stmt.close();
        } catch (SQLException ex) {
        }
        try {
            conn.close();
        } catch (SQLException ex) {
        }
    }


}  //end class
