package view;

import controller.DataController;
import controller.ProgramController;
import model.message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static ServerController serverController = ServerController.getInstance();

    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        DataController.initializeEffectHolders();
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            Responses.logToConsole(Responses.SERVER_IS_ON);
            while (true) {
                Socket socket = serverSocket.accept();
                startNewThread(serverSocket, socket);
            }
        } catch (IOException e) {
            Responses.logToConsole(Responses.SERVER_INITIALIZATION_FAILED + e.getMessage());
        }
    }

    private static void startNewThread(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            try {
                Responses.logToConsole(Responses.NEW_CLIENT_CONNECTED);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                getInputAndProcess(objectInputStream, objectOutputStream);
                objectInputStream.close();
                socket.close();
                serverSocket.close();
                Responses.logToConsole(Responses.CLIENT_DISCONNECTED);
            } catch (EOFException eofException) {
                Responses.logToConsole(Responses.CLIENT_DISCONNECTED);
            } catch (Exception exception){
                exception.printStackTrace();
            }
        }).start();
    }

    private static void getInputAndProcess(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        while (true) {
            Message message = (Message) objectInputStream.readObject();
            Responses.logToConsole(Responses.NEW_MESSAGE_FROM_CLIENT + message);
            Object result = process(message);
            Responses.logToConsole(Responses.MESSAGE_SENT + result);
            if (result.equals("")) break;
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        }
    }

    static Object process(Message message) {
        return serverController.run(message);
    }
}

