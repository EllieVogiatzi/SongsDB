
package Lab01;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Implementation extends UnicastRemoteObject implements RMInterface{
    private Statement stat;

    public Implementation() throws RemoteException{
        super();
        try{
            //Setting up the database
            Class.forName("org.sqlite.JDBC");
            Connection conn=DriverManager.getConnection("jdbc:sqlite:music.db");
            stat=conn.createStatement();
            System.out.println ("Database connection established");
            //Cretes the arrays of the Database
            stat.executeUpdate("DROP table if exists Album;");
            stat.executeUpdate("DROP table if exists Song;");
            
            //Creates an array called Album with the fields description,genre,year and name which is the primary key
            stat.executeUpdate("CREATE table Album (description varchar(50), genre varchar(20),year varchar(4),name varchar(50) PRIMARY KEY);");
            
            //Creates array called Song with the fields title(used as a primary key),artist,duration and a field that keeps the primary key of the Album called "name" so it can create
            //a connection between song and album 
            stat.executeUpdate("CREATE table Song (title varchar(50) PRIMARY KEY,artist varchar(50), duration varchar(20), name int,FOREIGN KEY (name) REFERENCES Album(name));");
            
            stat.executeUpdate("INSERT INTO Album (description,genre,year,name) VALUES ('Queen Album','Rock',1973,'Queen');");
            stat.executeUpdate("INSERT INTO Album (description,genre,year,name) VALUES ('Queen Album','Rock',1974,'Queen II');");
            stat.executeUpdate("INSERT INTO Song (title,artist,duration,name) VALUES ('Keep Yourself Alive', 'Queen','3:47','Queen');");
            stat.executeUpdate("INSERT INTO Song (title,artist,duration,name) VALUES ('Seven Seas of Rhye', 'Queen','2:47','Queen II');");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("class not found");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("sql exception");
        }
    }
    //This function adds the information from the user to the array Album
    public void addAlbum(String description,String genre,String year,String name) throws RemoteException{
        try{
            //Inserts the entry to the table
            stat.executeUpdate("INSERT INTO Album (description,genre,year,name) VALUES ('"+description +"','" + genre +"','" +year +"','" +name +"')");
        }
        catch (SQLException ex) {
            System.out.println("sql exception 2");
        }

    }
    //This function adds the information from the user to the table Song
    public void addSong(String title,String artist,String duration,String albumName) throws RemoteException{
        try{
            stat.executeUpdate("INSERT INTO Song (title,artist,duration,name) VALUES ('"+title + "','" +artist +"','" +duration +"','" +albumName +"')");
            
        }
        catch (SQLException ex) {
            System.out.println("sql esception 3");
            ex.printStackTrace();
        }

    }
    //This function deletes Album according to the name that gets from the user
    public void deleteAlbum(String name) throws RemoteException{
        //Made a sychronized block so it can manage the competition between multiple users
        synchronized(this){
            try{
                //Deletes from the table Album according to the name given from the user but also the song from the table Song
                stat.executeUpdate("DELETE FROM Album WHERE name='"+name+"'");
                //System.out.println("I am in deleteAlbum");
                stat.executeUpdate("DELETE FROM Song WHERE name='"+name+"'");
                //System.out.println("I am in deleteAlbum 2");
            } catch (SQLException ex) {
                Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void deleteSong(String title) throws RemoteException{
        synchronized(this){
            try {
                //Deletes from the table Song according to the title taken by the user
                stat.executeUpdate("DELETE FROM Song WHERE title='"+title+"'");
                //System.out.println("I am in deleteSong");
            } catch (SQLException ex) {
                Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //Shows Albums and Songs
    public String showAll() throws RemoteException{
        ResultSet results=null;
        String result = "";
        String title, artist, duration,description,genre,year,name;
        
        try {
            //Keeps all the data from the database in the results variable
            results=stat.executeQuery("SELECT * FROM Song INNER JOIN Album ON Song.name=Album.name;");
            while(results.next()){
                
                //Keeps every field in a different variable so it can return results better and easier to the user
                title = results.getString("title");
                artist = results.getString("artist");
                duration = results.getString("duration");
                
                
                description=results.getString("description");
                genre=results.getString("genre");
                year=results.getString("year");
                name=results.getString("name");
                result = result+"Title: "+title + ", Artist: " + artist + ", Duration: " + duration +", Description: "+ description+", Genre: "+genre+", Year: "+year+", Album name: "+name+  "\n";
            }
            System.out.println(result);
            
        } catch (SQLException ex) {
            Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
        }
         return result;
    }
    public void updateAlbum(String description,String genre,String year,String name) throws RemoteException{
        synchronized(this){
            //The user only edits the field that wants changed
            //so it just checks every field if it empty and then it uses the executeUpdate so it can replace the coresponding field
            //Manages the name of the Album as a primary key so it can recognice which Album is the user interested in, so it cannot be changed 
            if(!description.equals("")){
                try {
                    stat.executeUpdate("UPDATE Album SET description='"+description+"' WHERE name='"+name+"' ");
                } catch (SQLException ex) {
                    Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(!genre.equals("")){
                try {
                    stat.executeUpdate("UPDATE Album SET genre='"+genre+"' WHERE name='"+name+"' ");
                } catch (SQLException ex) {
                    Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(!year.equals("")){
                try {
                    stat.executeUpdate("UPDATE Album SET year='"+year+"' WHERE name='"+name+"' ");
                } catch (SQLException ex) {
                    Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //This function allows the user to edit the artist and the duration with a similar way lik above
    public void updateSong(String title,String artist,String duration) throws RemoteException{
        synchronized(this){
            if(!artist.equals("")){
                try {
                    stat.executeUpdate("UPDATE Song SET artist='"+artist+"' WHERE title='"+title+"' ");
                } catch (SQLException ex) {
                    Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(!duration.equals("")){
                try {
                    stat.executeUpdate("UPDATE Song SET duration='"+duration+"' WHERE title='"+title+"' ");
                } catch (SQLException ex) {
                    Logger.getLogger(Implementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    } 
}
