package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server
{
    public static void main(String args[])
    {
        final int PORT = 5400;
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(PORT);
            ExecutorService exec = Executors.newCachedThreadPool();

            while(true)
            {
                Socket socket = serverSocket.accept();
                exec.submit(new ServerCall(socket));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
