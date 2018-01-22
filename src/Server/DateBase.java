package Server;

import Base.Questions;
import Base.Statistics;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DateBase
{
    final String DRIVER = "com.mysql.jdbc.Driver";
    final String URL = "jdbc:mysql://127.0.0.1:3306/Questionnaire";

    Connection connection;
    Statement statement;

    public DateBase()
    {
        try
        {
            Class.forName(DRIVER).newInstance();

            connection = DriverManager.getConnection(URL, "root", "");
            statement = connection.createStatement();

            CreateTables();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void CreateTables() throws SQLException
    {
        String createQuestionsTable = "CREATE TABLE IF NOT EXISTS Questions (id_question INTEGER PRIMARY KEY AUTO_INCREMENT, question varchar(255), answer_1 varchar(255), answer_2 varchar(255), answer_3 varchar(255), answer_4 varchar(255), name varchar(255));";
        String createStatisticsTable = "CREATE TABLE IF NOT EXISTS Statistics (id_statistic INTEGER PRIMARY KEY AUTO_INCREMENT, clientId varchar(20), question varchar(255), answer varchar(255), name varchar(255));";

        statement.execute(createQuestionsTable);
        statement.execute(createStatisticsTable);


        List<Questions> questions = SelectQuestions();
        if(questions.isEmpty())
        {
            InsertQuestion("Jak oceniasz obecna polityke Planu Petru?", "Bardzo dobrze", "Srednio", "Bardzo zle", "Nie mam zdania", "Planu Petru");
            InsertQuestion("Jak oceniasz obecna polityke Platformy Obywatelskiej?", "Bardzo dobrze", "Srednio", "Bardzo zle", "Nie mam zdania", "Platforma Obywatelska");
            InsertQuestion("Jak oceniasz obecna polityke Ruchu Kukiza?", "Bardzo dobrze", "Srednio", "Bardzo zle", "Nie mam zdania", "Ruchu Kukiza");
            InsertQuestion("Jak oceniasz obecna polityke Prawa i Sprawiedliwosci?", "Bardzo dobrze", "Srednio", "Bardzo zle", "Nie mam zdania", "PrawO i Sprawiedliwosc?");
        }

    }

    public void InsertQuestion(String question, String answer_1, String answer_2, String answer_3, String answer_4, String name) throws SQLException
    {
        PreparedStatement temp = connection.prepareStatement("insert into Questions values (NULL, ?, ?, ?, ?, ?, ?);");
        temp.setString(1, question);
        temp.setString(2, answer_1);
        temp.setString(3, answer_2);
        temp.setString(4, answer_3);
        temp.setString(5, answer_4);
        temp.setString(6, name);
        temp.execute();
    }

    public void InsertStatistic(String clientId, String question, String answer, String name) throws SQLException
    {
        PreparedStatement temp = connection.prepareStatement("insert into Statistics values (NULL, ?, ?, ?, ?);");
        temp.setString(1, clientId);
        temp.setString(2, question);
        temp.setString(3, answer);
        temp.setString(4, name);

        temp.execute();
    }

    public List<Questions> SelectQuestions()
    {
        List<Questions> questions = new LinkedList<>();

        try
        {
            ResultSet result = statement.executeQuery("Select * from Questions");

            while(result.next())
            {
                Questions question = new Questions();

                question.setId(result.getInt("id_question"));
                question.setQuestion(result.getString("question"));
                question.setAnswer_1(result.getString("answer_1"));
                question.setAnswer_2(result.getString("answer_2"));
                question.setAnswer_3(result.getString("answer_3"));
                question.setAnswer_4(result.getString("answer_4"));
                question.setName(result.getString("name"));

                questions.add(question);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

        return questions;
    }

    public List<Statistics> SelectStatistic()
    {
        List<Statistics> statistics = new LinkedList<>();
        try
        {
            ResultSet result = statement.executeQuery("Select * from Questions");

            while(result.next())
            {
                Statistics statistic = new Statistics();

                statistic.setId(result.getInt("id_statistic"));
                statistic.setClientId(result.getString("clientId"));
                statistic.setQuestion(result.getString("question"));
                statistic.setAnswer(result.getString("answer"));
                statistic.setName(result.getString("name"));

                statistics.add(statistic);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

        return statistics;
    }

    public void CloseConnection()
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }



}
