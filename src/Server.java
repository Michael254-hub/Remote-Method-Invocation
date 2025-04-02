import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Server {
    public static void main(String[] args) {
        try {
            MessageService service = new MessageServiceImpl();
            // stub is added on ser.java to share with differet computers
            // MessageService stub = (MessageService) UnicastRemoteObject.exportObject(service, 0); //0 means use any available port.
             // Bind the service to the registry with a name (e.g., "MessageService")
            // Naming.rebind("rmi://localhost/MessageService", service);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MessageService", service);
            System.out.println("Server started.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}