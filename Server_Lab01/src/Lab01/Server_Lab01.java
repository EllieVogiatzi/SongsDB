
package Lab01;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server_Lab01 {

    
    public static void main(String[] args) {
        try {
            //Creates the Server and then waits for connections at //localhost/Implementation:1099
            Implementation server=new Implementation();
            Registry r=java.rmi.registry.LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/Implementation", server);
        } catch (RemoteException ex) {
            System.out.println("RemoteException");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server_Lab01.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
