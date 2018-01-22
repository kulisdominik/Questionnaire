package Base;

import java.io.Serializable;

public class Statistics implements Serializable
{
    private int id;
    private String clientId;
    private String name;

    private String question;
    private String answer;

    public Statistics()
    {

    }

    public Statistics(String clientId, String question, String answer, String name)
    {
        this.clientId = clientId;
        this.question = question;
        this.answer = answer;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
