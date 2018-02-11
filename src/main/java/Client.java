import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static final Logger Log = LoggerFactory.getLogger("Stockfish Client");

    public static void main(String[] args) throws IOException {
        Server server = new Server(4444);

        try (
                Socket serverSocket = new Socket("localhost", 4444);
                PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("end"))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            Log.error("Host not found");
            System.exit(1);
        } catch (IOException e) {
            Log.error("Stockfish Server handshake failed");
            System.exit(1);
        }
    }
}