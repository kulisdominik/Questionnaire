package Server;

import Base.Questions;
import Base.Statistics;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class ServerCall implements Runnable
{
    private Socket socket;
    private DateBase dateBase;
    private boolean isRunning = true;

    ServerCall(Socket socket)
    {
        this.dateBase = new DateBase();
        this.socket = socket;

        new Thread().start();
    }

    @Override
    public void run()
    {
        ObjectOutputStream  out = null;
        ObjectInputStream  in = null;
        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            List<Questions> questions = dateBase.SelectQuestions();
            for(Questions question : questions)
            {
                out.writeObject(question);
            }
            out.writeObject(null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        while (isRunning)
        {
            try
            {
                Statistics statistics = (Statistics) in.readObject();
                if(statistics == null)
                {
                    isRunning = false;
                    break;
                }

                dateBase.InsertStatistic(statistics.getClientId(), statistics.getQuestion(), statistics.getAnswer(), statistics.getName());
                            }
            catch (Exception e)
            {
                isRunning = false;
                System.out.println("Jesli widzisz ten komunikat to programista przewidzial ta blad :)");
                e.printStackTrace();
            }
        }

        List<Statistics> statistics = dateBase.SelectStatistic();
        for(Statistics statistic : statistics)
            try
            {
                out.writeObject(statistic);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        try
        {
            out.close();
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
