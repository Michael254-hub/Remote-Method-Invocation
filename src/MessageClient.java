import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface MessageClient extends Remote {
    void receiveMessage(String message, MessageClient sender) throws RemoteException;
    String getName() throws RemoteException;
}