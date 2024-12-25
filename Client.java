
import java.io.*;
import java.net.*;

public class Client {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("192.168.29.12", 7777);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
        }
    }

    public void startReading() {
        // thread-read
        Runnable r1 = () -> {
            System.out.println("reader started");

            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("Server : " + msg);
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading the input: " + e.getMessage());
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - take user data and send to the client
        Runnable r2 = () -> {
            System.out.println("writer started...");

            try {
                while (true && !socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                    out.println(content);
                    out.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is client...");
        new Client();
    }
}
