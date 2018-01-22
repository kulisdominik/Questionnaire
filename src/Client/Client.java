package Client;

import Base.Questions;
import Base.Statistics;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client extends Application
{
    private final static int PORT = 5400;
    private final static String ADDRESS = "localhost";
    private static Socket socket;
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;
    private static List<Questions> questions = null;
    private static int counter = 0;
    private static String[] statisctis;

    public static void main(String[] args)
    {
        statisctis = new String[3];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Questionnaire");
        ShowLoginWindow(primaryStage);
    }

    public void ShowLoginWindow(Stage primaryStage)
    {
        // Login scene
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Title
        Text title = new Text("Logowanie");
        grid.add(title, 0, 0, 1, 1);

        // User ID
        Label userIdL = new Label("User ID: ");
        grid.add(userIdL, 0, 1);
        TextField userTextField = new TextField();
        userTextField.setText("Podaj login!!!");
        grid.add(userTextField, 1, 1);

        // Button sign in
        Button button = new Button("Sign in");
        button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                statisctis[0] = userTextField.getText();

                questions = new LinkedList<>();
                try
                {
                    socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                    in = new ObjectInputStream(socket.getInputStream());
                    out = new ObjectOutputStream(socket.getOutputStream());

                    // Read question from server
                    Questions question;
                    while(true)
                    {
                        question = (Questions) in.readObject();
                        if(question != null)
                            questions.add(question);
                        else
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(userTextField.getText() != null)
                    ShowQuestionsWindow(primaryStage);

            }
        });



        HBox hBoxButton = new HBox(10);
        hBoxButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxButton.getChildren().add(button);
        grid.add(hBoxButton, 1, 4);

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void ShowQuestionsWindow(Stage primaryStage)
    {

        GridPane grid = new GridPane();
        Integer temporary = counter + 1;
        Text id = new Text(temporary.toString());
        grid.add(id, 0, 0);

        Text question = new Text(questions.get(counter).getQuestion());
        grid.add(question, 0, 1);
        statisctis[1] = questions.get(counter).getQuestion();

        final ToggleGroup groupRadioButtons = new ToggleGroup();
        RadioButton answer_1 = new RadioButton();
        answer_1.setText(questions.get(counter).getAnswer_1());
        answer_1.setUserData(questions.get(counter).getAnswer_1());
        answer_1.setToggleGroup(groupRadioButtons);
        answer_1.setSelected(true);

        RadioButton answer_2 = new RadioButton();
        answer_2.setText(questions.get(counter).getAnswer_2());
        answer_2.setUserData(questions.get(counter).getAnswer_2());
        answer_2.setToggleGroup(groupRadioButtons);

        RadioButton answer_3 = new RadioButton();
        answer_3.setText(questions.get(counter).getAnswer_3());
        answer_3.setUserData(questions.get(counter).getAnswer_3());
        answer_3.setToggleGroup(groupRadioButtons);

        RadioButton answer_4 = new RadioButton();
        answer_4.setText(questions.get(counter).getAnswer_4());
        answer_4.setUserData(questions.get(counter).getAnswer_4());
        answer_4.setToggleGroup(groupRadioButtons);

        Button button = new Button("Next question");

        button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    statisctis[2] = groupRadioButtons.getSelectedToggle().getUserData().toString();
                    out.writeObject(new Statistics(statisctis[0], statisctis[1], statisctis[2], questions.get(counter).getName()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                if(++counter < questions.size())
                    ShowQuestionsWindow(primaryStage);
                else
                {
                    try
                    {
                        out.writeObject(null);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    ShowStatisticsWindows(primaryStage);
                }

            }
        });
        VBox hBox = new VBox(answer_1, answer_2, answer_3, answer_4, button);
        hBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(hBox,0, 6);
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public void ShowStatisticsWindows(Stage primaryStage)
    {
        GridPane grid = new GridPane();
        Button button = new Button();
        button.setText("Koniec");
        VBox vBox = new VBox(button);

        button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    Platform.exit();
                    System.exit(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        grid.add(vBox, 0, 0);
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}







