import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessageClientImpl extends UnicastRemoteObject implements MessageClient {

    private String name;

    public MessageClientImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public void receiveMessage(String message, MessageClient sender) throws RemoteException {
        System.out.println(sender.getName() + ": " + message);
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }
}