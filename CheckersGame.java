import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;

public class CheckersGame extends Application
{

  Scene startScene, playScene;
  Button startButton, exitButton, quitButton, resetButton;
  Label startLabel;
  Group root;
  ArrayList<Circle> checkers;
  ArrayList<Circle> redCheckers;
  ArrayList<Circle> blackCheckers;
  List<Double> xcoords;
  double ogSceneX, ogSceneY, ogTranslateX, ogTranslateY;

  public static void main(String[] args) {launch(args);}

  public CheckersGame()
  {
    checkers = new ArrayList<>();
    redCheckers = new ArrayList<>();
    blackCheckers = new ArrayList<>();
  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {

    //Buttons
    startButton = new Button("Start");
      startButton.setOnAction(e -> primaryStage.setScene(playScene));
    exitButton = new Button("Exit");
      exitButton.setOnAction(e -> Platform.exit());
    quitButton = new Button("Quit");
      quitButton.setOnAction(e ->
      {
        primaryStage.setScene(startScene);
        resetCheckers();
      });
    resetButton = new Button("Reset");
      resetButton.setOnAction(e -> resetCheckers());

    //Labels
    startLabel = new Label("Welcome to Checkers the Video Game!");

    //Layouts
    VBox startLayout = new VBox(20);
    startLayout.getChildren().addAll(startLabel,startButton,exitButton);
    startLayout.setAlignment(Pos.CENTER);

    root = new Group();
    xcoords = Arrays.asList(0.0,75.0,150.0,225.0,300.0,375.0,450.0,525.0);
    putSquares();
    putCheckers();
    HBox playButtons = new HBox(20);
    playButtons.getChildren().addAll(quitButton,resetButton);
    BorderPane playLayout = new BorderPane(root);
    playLayout.setTop(playButtons);

    //Scenes and Stage
    startScene = new Scene(startLayout,900,700);
    playScene = new Scene(playLayout,900,700);
    moveCheckers();

    primaryStage.setScene(startScene);
    primaryStage.setTitle("Checkers");
    primaryStage.show();


  }

  private Rectangle makeSquare(double x, double y, double s, Color c)
  {
    Rectangle square = new Rectangle(s,s,c);
    square.setX(x);
    square.setY(y);
    return square;
  }

  private void putSquares()
  {
    List<Double> y1coords = Arrays.asList(0.0,150.0,300.0,450.0);
    List<Double> y2coords = Arrays.asList(75.0,225.0,375.0,525.0);

    for (int j=0; j<y1coords.size(); j++)
    {
      for (int i=0; i<xcoords.size(); i++)
      {
        if (xcoords.get(i) %2 == 0)
        {
          root.getChildren().add(makeSquare(xcoords.get(i),y1coords.get(j),75.0,Color.BLACK));
        }
        else
        {
          root.getChildren().add(makeSquare(xcoords.get(i),y1coords.get(j),75.0,Color.RED));
        }
      }
    }

    for (int j=0; j<y2coords.size(); j++)
    {
      for (int i=0; i<xcoords.size(); i++)
      {
        if (xcoords.get(i) %2 != 0)
        {
          root.getChildren().add(makeSquare(xcoords.get(i),y2coords.get(j),75.0,Color.BLACK));
        }
        else
        {
          root.getChildren().add(makeSquare(xcoords.get(i),y2coords.get(j),75.0,Color.RED));
        }
      }
    }
  }

  private Circle makeChecker(double x, double y, double r, Color c)
  {
    Circle checker = new Circle(x,y,r,c);
    checker.setStroke(Color.GOLDENROD);
    checker.setStrokeWidth(2.5);
    if (c == Color.DARKSLATEGREY)
    {
      blackCheckers.add(checker);
    }
    if (c==Color.INDIANRED)
    {
      redCheckers.add(checker);
    }
    return checker;
  }

  private void putCheckers()
  {
    for (int i=0; i<xcoords.size(); i++)
    {
      if (xcoords.get(i)%2==0)
      {
        checkers.add(makeChecker(xcoords.get(i)+37.5,37.5,20.0,Color.INDIANRED));
        checkers.add(makeChecker(xcoords.get(i)+37.5,525-37.5,20.0,Color.DARKSLATEGREY));
      }
      else
      {
        checkers.add(makeChecker(xcoords.get(i)+37.5,75+37.5,20.0,Color.INDIANRED));
        checkers.add(makeChecker(xcoords.get(i)+37.5,600-37.5,20.0,Color.DARKSLATEGREY));
      }
    }

    for (int i=0; i<checkers.size(); i++)
    {
      root.getChildren().add(checkers.get(i));
    }
  }

  private void moveCheckers()
  {
    for (Circle c:checkers)
    {
      c.setCursor(Cursor.HAND);
      c.setOnMousePressed(e ->
      {
        ogSceneX = e.getSceneX();
        ogSceneY = e.getSceneY();
        ogTranslateX = ((Circle)(e.getSource())).getTranslateX();
        ogTranslateY = ((Circle)(e.getSource())).getTranslateY();
      });
      c.setOnMouseDragged(e ->
      {
        double offsetX = e.getSceneX() - ogSceneX;
        double offsetY = e.getSceneY() - ogSceneY;
        double newTranslateX = ogTranslateX + offsetX;
        double newTranslateY = ogTranslateY + offsetY;

        ((Circle)(e.getSource())).setTranslateX(newTranslateX);
        ((Circle)(e.getSource())).setTranslateY(newTranslateY);

        if (blackCheckers.contains(c) && (e.getSceneY() > 0 &&  e.getSceneY() < 115) && (e.getSceneX() > 150 && e.getSceneX()<225 || e.getSceneX() > 300 && e.getSceneX()<375 || e.getSceneX() > 450 && e.getSceneX()<525 || e.getSceneX() > 600 && e.getSceneX()<675))
        {
          c.setStroke(Color.MEDIUMORCHID);
        }
        else if(redCheckers.contains(c) && (e.getSceneY() > 610 && e.getSceneY() < 700) && (e.getSceneX() > 225 && e.getSceneX()<300 || e.getSceneX() > 375 && e.getSceneX()<450 || e.getSceneX() > 525 && e.getSceneX()<600 || e.getSceneX() > 675 && e.getSceneX()<750))
        {
          c.setStroke(Color.MEDIUMORCHID);
        }
      });
    }
  }

  private void resetCheckers()
  {
    for (Circle c: checkers)
    {
      c.setTranslateX(0);
      c.setTranslateY(0);
      c.setStroke(Color.GOLDENROD);
    }
  }

}
