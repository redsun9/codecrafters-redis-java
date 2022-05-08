import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        //  Uncomment this block to pass the first stage
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            clientSocket = serverSocket.accept();
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            int available;
            while (true) {
                available = inputStream.available();
                if (available > 0) {
                    byte[] inputBytes = new byte[available];
                    int read = 0;
                    while (read < available) read += inputStream.read(inputBytes, read, available - read);
                    byte[] outputBytes = "+PONG\r\n".getBytes();
                    outputStream.write(outputBytes);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
