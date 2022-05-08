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
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.
            while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                // Create a new thread for the client.
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private int available;
        private byte[] inputBytes;
        private byte[] outputBytes;
        private int read;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();
                while (!clientSocket.isClosed()) {
                    available = inputStream.available();
                    if (available > 0) {
                        inputBytes = new byte[available];
                        read = 0;
                        while (read < available) read += inputStream.read(inputBytes, read, available - read);
                        outputBytes = "+PONG\r\n".getBytes();
                        outputStream.write(outputBytes);
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            } finally {
                try {
                    if (clientSocket != null) clientSocket.close();
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }
        }
    }
}
