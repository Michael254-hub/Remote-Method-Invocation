import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MessageService extends Remote {
    void registerClient(MessageClient client) throws RemoteException;
    void unregisterClient(MessageClient client) throws RemoteException;
    void broadcastMessage(String message, MessageClient sender) throws RemoteException;
    List<MessageClient> getConnectedClients() throws RemoteException;
}

// public interface MessageClient extends Remote {
//     void receiveMessage(String message, MessageClient sender) throws RemoteException;
//     String getName() throws RemoteException;
// }