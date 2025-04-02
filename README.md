

## 1. System Architecture

We'll design a system where:

    Server: Manages the message broadcasting and maintains a list of connected clients.
    'Clients:' Connect to the server, send messages, and receive messages from other clients.


## 2. Interfaces (Defining Remote Methods)

First, we need to define the remote interfaces that will be used for communication between the server and clients.

MessageService:

    registerClient(MessageClient client): Allows a client to register itself with the server.
    unregisterClient(MessageClient client): Allows a client to unregister itself from the server.
    broadcastMessage(String message, MessageClient sender): Broadcasts a message from one client to all other registered clients.
    getConnectedClients(): Retrieves a list of all currently connected clients.

MessageClient:

    receiveMessage(String message, MessageClient sender): Called by the server to send a message to a client.
    getName(): gets the name of the client.

Remote: This interface is a marker interface that indicates that the object is a remote object.
RemoteException: This exception must be declared in all remote methods.

## 3. Server Implementation

MessageServiceImpl: Implements the MessageService interface.

UnicastRemoteObject: This class is used to create a remote object that can be accessed by clients.

clients: A list to store registered MessageClient objects.

The class implements all the methods defined in the MessageService interface.

## 4. Client Implementation

MessageClientImpl: Implements the MessageClient interface.

name: Stores the client's name.

The class implements the methods defined in the MessageClient interface.

## 5. Server and Client Startup

Server Startup: 

Client Startup: 

## 6. Stubs and Skeletons

    Stub (Client-Side):
        The stub is a proxy object that resides on the client side.
        It implements the same remote interface as the server object.
        When a client calls a remote method, the stub marshals (serializes) the method parameters, sends them to the server, waits for the result, and unmarshals (deserializes) the result before returning it to the client.
        In java rmi, the rmic compiler creates the stub class.
    Skeleton (Server-Side):
        The skeleton resides on the server side.
        It receives the marshaled parameters from the stub, unmarshals them, calls the actual remote method on the server object, marshals the result, and sends it back to the stub.
        In modern java the skeleton is not explicitly generated, instead, the RMI runtime handles this functionality.
    RMI Compiler (rmic):
        Historically, the rmic compiler was used to generate stubs and skeletons. However, since Java 5, dynamic proxies and reflection have made explicit skeleton generation unnecessary. Stubs are still generated or dynamically created.
        When running the code, java RMI handles the generation of the stubs.
    How it Works:
        The client calls a remote method on the stub.
        The stub serializes the parameters and sends them to the server.
        The server's RMI runtime receives the request and calls the appropriate method on the server object.
        The server method executes and returns a result.
        The server's RMI runtime serializes the result and sends it back to the client.
        The stub deserializes the result and returns it to the client.



## 7. CONNECTING WITH MULTIPLE PCS


Key Considerations:

    Network Connectivity:
        All computers need to be on the same network or be reachable through the internet.
        If you're on a local network (like a home or office network), this is usually straightforward.
        If you're connecting over the internet, you'll need to consider firewalls and port forwarding.

    Server's IP Address:
        The clients need to know the IP address of the computer running the RMI server.
        If the server is on a local network, use its local IP address (e.g., 192.168.1.x).
        If the server is accessible over the internet, use its public IP address.

    Firewall Configuration:
        Firewalls on the server computer might block incoming RMI connections. You'll need to open the necessary ports:
            RMI Registry Port (1099 by default): Open port 1099 for incoming TCP connections.
            RMI Server Port (Dynamic): RMI may use dynamic ports for communication between the stubs and the server. It is best to set a static port. To do this, when extending the UnicastRemoteObject, you can set the port that the object will be exported on.
        On the client computers, firewalls might also block outgoing connections to the server. You may need to adjust those firewalls as well.

    Code Modifications:

        Client Code:
            In the Client.java file, you need to modify the line where the client gets the RMI registry reference.
            Instead of using LocateRegistry.getRegistry(Registry.REGISTRY_PORT), you'll need to specify the server's IP address:
        Java

Registry registry = LocateRegistry.getRegistry("SERVER_IP_ADDRESS", Registry.REGISTRY_PORT);

    Replace SERVER_IP_ADDRESS with the actual IP address of the computer running the server.

Server Code:

    To set a specific port for the server, change the Server.java code to the following.

Java

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            MessageServiceImpl service = new MessageServiceImpl();
            MessageService stub = (MessageService) UnicastRemoteObject.exportObject(service, 0); //0 means use any available port.
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.rebind("MessageService", stub);
            System.out.println("Server started.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

Then change the MessageServiceImpl.java to this.
Java

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

Steps to Connect:

    Run the RMI Registry: On the server computer, open a terminal or command prompt and run rmiregistry.
    Start the Server: On the server computer, compile and run the Server.java code.
    Start the Clients: On each client computer, compile and run the modified Client.java code, replacing SERVER_IP_ADDRESS with the server's IP address.
    Communicate: Clients can now send and receive messages through the server.

Important Notes:

    If you're connecting over the internet, consider using a VPN for security.
    Dynamic IP addresses can cause issues. If the server's IP address changes, clients will lose connection. You might need to use a dynamic DNS service.
    Security is important. Never expose RMI services to the open internet without proper access controls.

By following these steps, you and your friends should be able to connect and communicate using the RMI-based messaging system.

## HOW DO I GET MY PUBLIC IP IF I AM RUNNING AS THE SERVER 
run "curl ifconfig.me"
