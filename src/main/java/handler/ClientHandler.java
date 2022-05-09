package handler;

import types.RedisData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static handler.RequestProcessor.processRequest;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            RequestParser requestParser = new RequestParser(inputStream);
            while (!clientSocket.isClosed()) {
                RedisData request = requestParser.parseRequest();
                System.out.println("Request: " + request);
                RedisData response = processRequest(request);
                System.out.println("Response: " + response);
                outputStream.write(response.getFormattedValue().getBytes());
                outputStream.flush();
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
