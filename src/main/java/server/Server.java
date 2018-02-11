package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final Logger Log = LoggerFactory.getLogger("Stockfish Server");

    public Server(int port) throws IOException {
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String input, output;

            Protocol protocol = new Protocol();
            output = protocol.processInput(null);
            out.println(output);

            while ((input = in.readLine()) != null) {
                output = protocol.processInput(input);
                out.println(output);

                if (output.equalsIgnoreCase("end"))
                    break;
            }
        } catch (IOException e) {
            Log.error("Exception caught when trying to listen on port " + port + " or listening for a connection:\n" + e.getMessage());
        }
    }
}