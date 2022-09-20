
package Lab01;


import java.rmi.Naming;


public class Client_Lab01 {

    
    public static void main(String[] args) {
        try{
            //The Client connects in the address below and uses the look_op to communicate with the Server
            String name="//localhost/Implementation";
            RMInterface look_op=(RMInterface) Naming.lookup(name);
            UI ui=new UI(look_op);
            ui.setVisible(true);
        }
        catch(Exception e){
            System.out.println("Implementation err:" +e);
            System.exit(1);
        } 
    }
    
}
