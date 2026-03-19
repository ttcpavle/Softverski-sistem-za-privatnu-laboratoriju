/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author totic
 */
public class Connection {
    private static Connection instance;
    private Socket socket;
    private Sender sender;
    private Receiver receiver;

    public Connection() {
        try {
            this.socket = new Socket("localhost", 9090);
            this.sender = new Sender(socket);
            this.receiver = new Receiver(socket);
        } catch (IOException ex) {
            System.getLogger(Connection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public static Connection getInstance(){
        if(instance == null){
            instance = new Connection();
        }
        return instance;
    }

    public void zatvoriKonekciju() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public Socket getSocket() {
        return socket;
    }

    public Sender getSender() {
        return sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }
       
    
    
}
