
package Lab01;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface RMInterface extends Remote{
    //Declares the functions that the Server/Client are gonna use
    public void addAlbum(String description,String genre,String year,String name) throws RemoteException;
    public void addSong(String title,String artist,String duration,String albumName) throws RemoteException;
    public void deleteAlbum(String name) throws RemoteException;
    public void deleteSong(String title) throws RemoteException;
    public String showAll() throws RemoteException;
    public void updateAlbum(String description,String genre,String year,String name) throws RemoteException;
    public void updateSong(String title,String artist,String duration) throws RemoteException;
}
