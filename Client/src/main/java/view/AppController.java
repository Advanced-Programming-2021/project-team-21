package view;

import model.message.Message;

import java.io.*;
import java.net.Socket;

public class AppController {
    public static Socket socket;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 7777);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public static void sendMessageToServer(Message message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object receiveMessageFromServer() {
        try {
            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
