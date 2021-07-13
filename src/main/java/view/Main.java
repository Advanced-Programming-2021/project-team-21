package view;

import controller.DataController;
import controller.ProgramController;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static ServerController serverController = new ServerController();

    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        ProgramController.allCards = DataController.getAllCards();
        DataController.initializeEffectHolders();
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            while (true) {
                Socket socket = serverSocket.accept();
                startNewThread(serverSocket, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startNewThread(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                getInputAndProcess(dataInputStream, dataOutputStream);
                dataInputStream.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void getInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            String result = process(input);
            if (result.equals("")) break;
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }

    static String process(String command) {
        return serverController.run(command);
    }


}

