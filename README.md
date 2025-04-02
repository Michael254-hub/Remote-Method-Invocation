##**RMI Server**
This `Server.java` file sets up the **RMI server** for the chat system. It initializes the **MessageService**, binds it to the RMI registry, and allows clients to connect and exchange messages. Let’s go through the code **line by line**.

---

# **Step-by-Step Explanation**

```java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
```
- **Imports:**
  - `LocateRegistry`: Used to create or locate the RMI registry.
  - `Registry`: Provides methods to bind and retrieve remote objects.

---

```java
public class Server {
```
- **Defines the `Server` class**, which sets up the RMI server.

---

```java
    public static void main(String[] args) {
```
- **Main method**: Entry point for starting the server.

---

```java
        try {
```
- **Begins a try-catch block** to handle exceptions that may occur when setting up the server.

---

```java
            MessageService service = new MessageServiceImpl();
```
- **Creates an instance of `MessageServiceImpl`**, which implements the `MessageService` interface.
- This object will handle client registration and message broadcasting.

---

```java
            // stub is added on ser.java to share with different computers
            // MessageService stub = (MessageService) UnicastRemoteObject.exportObject(service, 0); //0 means use any available port.
```
- This comment refers to an alternative way to **export the remote object manually**.
- The commented-out code would explicitly **export the `MessageService` object**, making it available for remote clients.

---

```java
            // Naming.rebind("rmi://localhost/MessageService", service);
```
- **Another alternative approach** (commented out) that could bind the object using `Naming.rebind()`.
- This would have made the service accessible via `"rmi://localhost/MessageService"`.

---

```java
            Registry registry = LocateRegistry.createRegistry(1099);
```
- **Creates an RMI registry on port `1099`** (default RMI port).
- The registry keeps track of remote objects and allows clients to find them.

---

```java
            registry.rebind("MessageService", service);
```
- **Binds the `MessageService` implementation** to the name `"MessageService"`.
- This makes the service available for clients to **lookup and call remotely**.

---

```java
            System.out.println("Server started.");
```
- **Indicates that the server is running and ready to accept clients.**

---

```java
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
```
- **Handles exceptions**, printing an error message if the server encounters any issues.

---

## **How This Server Works in the RMI Chat System**
1. **Starts the RMI Registry**
   - `LocateRegistry.createRegistry(1099)` creates a registry for storing remote objects.

2. **Creates the `MessageService` Implementation**
   - `MessageServiceImpl` is instantiated to manage client registration and message broadcasting.

3. **Binds the Service to the Registry**
   - `registry.rebind("MessageService", service)` allows clients to find the `MessageService`.

4. **Clients Connect and Communicate**
   - Clients use `registry.lookup("MessageService")` to retrieve the service stub.
   - They **register** themselves and **send messages** via this service.

---

## **Example Workflow**
### **1️⃣ Start the Server**
Run the server first:
```
java Server
```
Output:
```
Server started.
```

### **2️⃣ Clients Connect**
Clients use:
```java
Registry registry = LocateRegistry.getRegistry("server_ip", 1099);
MessageService service = (MessageService) registry.lookup("MessageService");
```
- They retrieve the `MessageService` and start sending messages.

### **3️⃣ Server Handles Messages**
- The server **broadcasts messages** to all registered clients.

---

## **Summary**
- **Creates and starts the RMI registry** on port `1099`.
- **Instantiates `MessageServiceImpl`**, which manages chat functionality.
- **Binds the service** to `"MessageService"` so clients can find and use it.
- **Allows multiple clients** to register and communicate.

Would you like to see the `MessageServiceImpl` implementation next? 🚀

#**Client Code**
This `Client.java` file is a Java RMI (Remote Method Invocation) client that connects to an RMI server, registers itself, and allows users to send messages that are broadcasted to other clients. Let’s go through the code line by line and explain how it interacts with the other parts of the RMI system.

---


##**RMI Client**
### **Step-by-Step Explanation**

```java
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
```
- These are necessary imports:
  - `LocateRegistry`: Used to locate the RMI registry on the network.
  - `Registry`: Provides methods to look up remote objects.
  - `Scanner`: Used to read user input from the console.

---

```java
public class Client {
```
- Defines the `Client` class, which acts as an RMI client.

---

```java
    public static void main(String[] args) {
```
- The `main` method serves as the entry point for the client application.

---

```java
        try {
```
- Starts a `try` block to catch and handle exceptions that may occur during RMI operations.

---

```java
            // YOU ADD SERVER_IP_ADDRESS TO MAKE THE COMPUTERS IN THE NET CONNECT TO THE SERVER
            // Registry registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            // this is to distribute the system
```
- These are comments explaining that to connect multiple clients across different computers, you need to replace `"localhost"` with the actual server’s IP address.

---

```java
            Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
```
- This line locates the RMI registry running on `"localhost"` (which means the same machine as the client).
- `Registry.REGISTRY_PORT` is the default RMI registry port (`1099`).
- If running on a distributed network, replace `"localhost"` with the **IP address** of the server.

---

```java
            MessageService service = (MessageService) registry.lookup("MessageService");
```
- Looks up the remote object named `"MessageService"` in the registry.
- The `lookup` method retrieves the stub (a proxy) for the `MessageService` interface, allowing the client to invoke remote methods.

---

```java
            Scanner scanner = new Scanner(System.in);
```
- Creates a `Scanner` object to read user input from the console.

---

```java
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
```
- Asks the user to enter their name and reads it from the console.

---

```java
            MessageClient client = new MessageClientImpl(name);
```
- Creates a new `MessageClientImpl` instance, which implements the `MessageClient` interface.
- This object represents the client and will be registered with the `MessageService` to receive messages.

---

```java
            service.registerClient(client);
```
- Calls the `registerClient(client)` method on the remote `MessageService`.
- This registers the client on the server, allowing it to receive messages from other clients.

---

```java
            System.out.println("Connected. Start typing messages.");
```
- Informs the user that the client is successfully connected and can now send messages.

---

```java
            while (true) {
                String message = scanner.nextLine();
                service.broadcastMessage(message, client);
            }
```
- Enters an infinite loop where:
  1. The user types a message.
  2. The message is read using `scanner.nextLine()`.
  3. The `broadcastMessage` method of `MessageService` is called, passing the message and the client reference.
  4. The server will then forward this message to all registered clients.

---

```java
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
```
- If an exception occurs (e.g., connection failure, RMI lookup failure), it prints an error message and the exception stack trace.

---

### **How This Client Interacts with the RMI System**
1. **Locating the RMI Registry**  
   - The client connects to the RMI registry running on `"localhost"` (or a specified IP address).

2. **Looking Up the Remote Object**  
   - The client retrieves a reference to the `MessageService` from the registry.

3. **Creating a Client Object**  
   - A `MessageClientImpl` object is created, representing the client.

4. **Registering with the Server**  
   - The client registers itself with the `MessageService` so it can receive messages.

5. **Sending Messages**  
   - The client enters a loop where it continuously reads user input and sends messages to the `MessageService`.

6. **Broadcasting Messages**  
   - The `MessageService` on the server receives the message and forwards it to all registered clients.

7. **Receiving Messages (Not in This Code, But Expected in `MessageClientImpl`)**  
   - The client must implement the `MessageClient` interface, which should have a method like `receiveMessage(String message)`.  
   - The server calls this method to send messages to all connected clients.

---

##**Remote Interface**
This `MessageClient.java` file defines a **Remote Interface** for an RMI client. It is part of the **RMI chat system**, where clients communicate via a central server. Let's go through the code line by line.

---

## **Step-by-Step Explanation**

```java
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
```
- **Imports:**
  - `Remote`: This interface is required for all RMI interfaces. Any class that implements it can be used remotely.
  - `RemoteException`: Signals communication-related exceptions in RMI calls.
  - `List`: (Unused in this snippet, might be used in another part of the program for handling multiple clients).

---

```java
public interface MessageClient extends Remote {
```
- **Defines an RMI Interface named `MessageClient`.**
  - Since RMI is based on interfaces, this acts as a contract for all RMI clients.
  - Extends `Remote`, meaning its methods can be invoked remotely.

---

```java
    void receiveMessage(String message, MessageClient sender) throws RemoteException;
```
- **Defines a method to receive messages.**
  - `String message`: The text of the received message.
  - `MessageClient sender`: The client that sent the message.
  - `throws RemoteException`: Every remote method must handle network-related failures.

  **How It Works in RMI:**
  - The `MessageService` on the server will call this method on all registered clients when a message is broadcasted.
  - The client should implement this method to display received messages.

---

```java
    String getName() throws RemoteException;
```
- **Defines a method to get the client’s name.**
  - Used by the server to identify the sender when forwarding messages.
  - Also required to be `throws RemoteException` since it will be called remotely.

---

## **How This Interface Interacts with the RMI System**
1. **Clients Implement This Interface**
   - Any RMI client must implement `MessageClient`.
   - This ensures they can receive messages and have a unique name.

2. **Server Calls `receiveMessage(...)` to Send Messages**
   - When a client sends a message, the `MessageService` (server) calls `receiveMessage(...)` on all registered clients.

3. **Server Calls `getName()` to Identify Clients**
   - When broadcasting, the server may call `getName()` to display the sender’s name.

---

## **Example of an Implementing Class**
Each client must implement this interface. Here’s how a class might implement it:

```java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessageClientImpl extends UnicastRemoteObject implements MessageClient {
    private String name;

    protected MessageClientImpl(String name) throws RemoteException {
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
```
### **Explanation of `MessageClientImpl`**
- It **extends `UnicastRemoteObject`**, making it a remote object.
- It **implements `MessageClient`**, meaning it provides definitions for `receiveMessage` and `getName`.
- **In `receiveMessage(...)`**, it prints messages with the sender’s name.
- **In `getName()`**, it returns the client’s name.

---

## **Summary**
- `MessageClient` is an **RMI interface** that defines methods for receiving messages and getting client names.
- The **server calls these methods on each registered client** when broadcasting messages.
- **Clients must implement this interface** to participate in the chat system.




## **Message Implementation**
This `MessageClientImpl.java` file implements the `MessageClient` interface, making it a **remote object** that can receive messages in an RMI chat system. Let’s go through the code **line by line** to understand its role.

---

```java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
```
- **Imports:**
  - `RemoteException`: Required because remote methods must handle network-related errors.
  - `UnicastRemoteObject`: This makes the object remotely accessible by exporting it to an RMI runtime.

---

```java
public class MessageClientImpl extends UnicastRemoteObject implements MessageClient {
```
- **Defines the `MessageClientImpl` class**, which:
  - **Extends `UnicastRemoteObject`**: This makes it a remote object that can be invoked over a network.
  - **Implements `MessageClient`**: This ensures it can receive messages and provide its name.

---

```java
    private String name;
```
- A **private field** to store the client’s name.

---

```java
    public MessageClientImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }
```
- **Constructor for `MessageClientImpl`**:
  - `super();` calls the `UnicastRemoteObject` constructor, which **exports the object** (makes it available for remote calls).
  - Stores the given `name` in the instance variable.

  **Why `throws RemoteException`?**
  - `UnicastRemoteObject` may throw a `RemoteException` when trying to export the object.

---

```java
    @Override
    public void receiveMessage(String message, MessageClient sender) throws RemoteException {
        System.out.println(sender.getName() + ": " + message);
    }
```
- **Handles incoming messages** from the `MessageService` (server).
- Prints the **sender’s name** followed by the message.

  **How It Works:**
  - The server calls `receiveMessage(...)` on all registered clients when broadcasting messages.
  - `sender.getName()` retrieves the sender’s name.
  - The message is displayed on the client's console.

---

```java
    @Override
    public String getName() throws RemoteException {
        return name;
    }
```
- **Returns the client’s name.**
- The server calls this method to **identify the sender** when broadcasting messages.

---

## **How This Class Fits into the RMI Chat System**
1. **The Client Registers Itself**
   - The client creates an instance of `MessageClientImpl` and registers it with the `MessageService`.

2. **Receiving Messages**
   - The server calls `receiveMessage(...)` to deliver messages from other clients.

3. **Sending Messages**
   - The client sends messages using `MessageService.broadcastMessage(...)`.

4. **Identifying the Sender**
   - The server calls `getName()` to display the sender's name.

---

## **Example Usage**
### **Client Code Using This Implementation**
```java
MessageClient client = new MessageClientImpl("Alice");
service.registerClient(client);
```
- Registers `"Alice"` as a client.
- Alice can now **send and receive** messages.

---

## **Summary**
- **`MessageClientImpl` is a remote object** that allows a client to participate in the chat system.
- It **implements `MessageClient`** to:
  - **Receive messages** (`receiveMessage(...)`).
  - **Return its name** (`getName()`).
- It is **exported as a remote object** so the server can call its methods.


##**MessageService Implementation
This `MessageServiceImpl.java` file implements the **server-side logic** for the **RMI chat system**. It manages client registration, message broadcasting, and client unregistration. Let’s break it down **line by line**.

---

## **Step-by-Step Explanation**
```java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
```
### **Imports:**
- `RemoteException`: Handles network-related errors in RMI.
- `UnicastRemoteObject`: Allows the object to be used as a **remote object**.
- `ArrayList` and `List`: Used to **store connected clients**.

---

```java
public class MessageServiceImpl extends UnicastRemoteObject implements MessageService {
```
### **Defines `MessageServiceImpl`, which:**
- **Extends `UnicastRemoteObject`**:
  - This **exports** the object, making it available for remote calls.
- **Implements `MessageService`**:
  - This ensures it provides methods for **client registration**, **message broadcasting**, and **unregistration**.

---

```java
    private List<MessageClient> clients;
```
### **Stores all connected clients**
- Uses an `ArrayList` to maintain a **list of registered clients**.

---

```java
    public MessageServiceImpl() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }
```
### **Constructor**
- Calls `super();` to **export the object** for remote access.
- Initializes the `clients` list.

---

```java
    @Override
    public void registerClient(MessageClient client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered: " + client.getName());
    }
```
### **Registers a New Client**
- Adds the client to the `clients` list.
- Prints a confirmation message.

**How It Works in RMI:**
- A client calls `service.registerClient(client);` to **join the chat**.
- The server **stores the client** in `clients`.

---

```java
    @Override
    public void unregisterClient(MessageClient client) throws RemoteException {
        clients.remove(client);
        System.out.println("Client unregistered: " + client.getName());
    }
```
### **Unregisters a Client**
- Removes the client from `clients`.
- Prints a confirmation message.

**When This Happens:**
- If a client **disconnects or encounters an error**, the server removes it.

---

```java
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
```
### **Broadcasts a Message to All Clients**
- Iterates through all registered clients.
- Sends the message to every client **except the sender**.
- If sending fails (e.g., client disconnects), the client is **unregistered**.

**How It Works in RMI:**
1. **A client sends a message:**  
   ```java
   service.broadcastMessage("Hello, everyone!", client);
   ```
2. **The server loops through clients** and calls:  
   ```java
   client.receiveMessage("Hello, everyone!", sender);
   ```
3. **Each client displays the message**.

---

```java
    @Override
    public List<MessageClient> getConnectedClients() throws RemoteException {
        return clients;
    }
```
### **Returns the List of Connected Clients**
- Useful if the client wants to display **who’s online**.

---

## **How This Works in the RMI Chat System**
1. **Clients Register Themselves**
   - Clients call `registerClient(client);` to join the chat.

2. **Messages Are Sent and Broadcasted**
   - A client calls `broadcastMessage("Hello!", sender);`
   - The server **forwards** the message to all clients **except the sender**.

3. **Clients Receive Messages**
   - The client’s `receiveMessage(...)` method is invoked.

4. **Clients Can Leave**
   - The server removes them with `unregisterClient(client);`

---

## **Example Workflow**
### **1️⃣ Start the Server**
```java
java Server
```
Output:
```
Server started.
```

### **2️⃣ Client Joins**
```java
service.registerClient(client);
```
Output:
```
Client registered: Alice
```

### **3️⃣ Client Sends a Message**
```java
service.broadcastMessage("Hello!", client);
```
- The server **forwards** it to all clients.

### **4️⃣ Other Clients Receive the Message**
```java
Alice: Hello!
```

### **5️⃣ Client Disconnects**
```java
service.unregisterClient(client);
```
Output:
```
Client unregistered: Alice
```

---

## **Summary**
- **Handles client registration, messaging, and unregistration**.
- **Broadcasts messages to all clients** except the sender.
- **Removes disconnected clients** to keep the list updated.
- **Forms the core of the chat system**.

The `MessageService` interface defines the **core functionalities** of the RMI chat system's **server-side service**. Let’s break it down **line by line** to understand its role.

---



##**Message Service Remote**
## **Step-by-Step Explanation**
```java
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
```
### **Imports:**
- `Remote`:  
  - This **marks the interface as a remote service**, making it accessible over the network.
- `RemoteException`:  
  - **All remote methods must declare `throws RemoteException`** to handle network errors.
- `List`:  
  - Used to store and return a **list of connected clients**.

---

```java
public interface MessageService extends Remote {
```
### **Defines the `MessageService` Interface**
- Extends `Remote`, making it an **RMI interface**.
- It is implemented by `MessageServiceImpl` (the actual server).

---

### **1️⃣ `registerClient(MessageClient client)`**
```java
    void registerClient(MessageClient client) throws RemoteException;
```
- **Registers a new client** in the chat system.
- Called by clients when they **join the server**.
- The server stores the client in a list.

---

### **2️⃣ `unregisterClient(MessageClient client)`**
```java
    void unregisterClient(MessageClient client) throws RemoteException;
```
- **Removes a client** from the chat system.
- Called when:
  - A client **disconnects**.
  - A client **fails to respond**.
- Prevents sending messages to **disconnected clients**.

---

### **3️⃣ `broadcastMessage(String message, MessageClient sender)`**
```java
    void broadcastMessage(String message, MessageClient sender) throws RemoteException;
```
- **Sends a message to all registered clients**, except the sender.
- The server iterates through all clients and calls:
  ```java
  client.receiveMessage(message, sender);
  ```
- If a client is unreachable, it is **unregistered**.

---

### **4️⃣ `getConnectedClients()`**
```java
    List<MessageClient> getConnectedClients() throws RemoteException;
```
- **Returns a list of currently connected clients**.
- Useful for showing **who is online** in the chat.

---

## **How This Interface Works in the RMI Chat System**
1. **Clients connect to the server**  
   ```java
   service.registerClient(client);
   ```
2. **Clients send messages**  
   ```java
   service.broadcastMessage("Hello, world!", client);
   ```
3. **The server delivers messages** to all clients.
4. **Clients can disconnect**  
   ```java
   service.unregisterClient(client);
   ```
5. **Clients can check who’s online**  
   ```java
   List<MessageClient> clients = service.getConnectedClients();
   ```

---

## **The Commented `MessageClient` Interface**
```java
// public interface MessageClient extends Remote {
//     void receiveMessage(String message, MessageClient sender) throws RemoteException;
//     String getName() throws RemoteException;
// }
```
- This **defines client-side functionality**.
- **Allows clients to receive messages** and return their name.

---

## **Summary**
- `MessageService` provides **server-side chat functionalities**.
- Handles **client registration, message broadcasting, and disconnection**.
- Used by the `MessageServiceImpl` class.

