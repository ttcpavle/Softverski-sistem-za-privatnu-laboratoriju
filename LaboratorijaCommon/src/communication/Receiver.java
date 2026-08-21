package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receiver {
    private final Socket socket;

    public Receiver(Socket socket) {
        this.socket = socket;
    }
    
    public Object receive() throws Exception{
        try {
            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch (IOException ex) {
            throw new Exception("Greska u prihvatanju objekta");
        }
    }
}
