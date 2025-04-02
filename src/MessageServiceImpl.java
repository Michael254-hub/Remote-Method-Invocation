import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MessageServiceImpl extends UnicastRemoteObject implements MessageService {

    private List<MessageClient> clients;

    public MessageServiceImpl() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public void registerClient(MessageClient client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered: " + client.getName());
    }

    @Override
    public void unregisterClient(MessageClient client) throws RemoteException {
        clients.remove(client);
        System.out.println("Client unregistered: " + client.getName());
    }

    @Override
    public void broadcastMessage(String message, MessageClient sender) throws RemoteException {
        for (MessageClient client : clients) {
            if (!client.equals(sender)) {
                try {
                    client.receiveMessage(message, sender);
                } catch (RemoteException e) {
                    System.err.println("Error sending message to client: " + e.getMessage());
                    unregisterClient(client);
                }
            }
        }
    }

    @Override
    public List<MessageClient> getConnectedClients() throws RemoteException {
        return clients;
    }
}