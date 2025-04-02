import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // YOU ADD SERVER_IP_ADDRESS TO MAKE THE COMPUTES IN THE NET CONNECT TO THE SERVER
            // Registry registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            // this is to distirbute the syste
            Registry registry = LocateRegistry.getRegistry("localhost",Registry.REGISTRY_PORT);
            MessageService service = (MessageService) registry.lookup("MessageService");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            MessageClient client = new MessageClientImpl(name);
            service.registerClient(client);

            System.out.println("Connected. Start typing messages.");

            while (true) {
                String message = scanner.nextLine();
                service.broadcastMessage(message, client);
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
