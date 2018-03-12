import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.util.Duration;


public class PuzzleGameApp extends Application {
    private Puzzle model;
    private PuzzleView view;
    public Timeline updateTimer;

    public void start(Stage primaryStage){
        model = new Puzzle();
        view = new PuzzleView(model);

        updateTimer = new Timeline(new KeyFrame(Duration.millis(1000),
                new javafx.event.EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) { handleTimer(); }
                }));
        updateTimer.setCycleCount(Timeline.INDEFINITE);

        view.getPuzzleList().setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) { handleListSelection(); }
        });

        view.getStartButton().setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { handleStartButtonPress(); }
        });

        primaryStage.setTitle("Slider Puzzle Game");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(view, 977,773));
        primaryStage.show();
    }

    //Event-handlers

    //Timer handler
    private void handleTimer() {
        view.update();
    }

    //ListView Puzzle selection
    private void handleListSelection() {
        model.puzzleSelected = true;
        String selectedPuzzle = view.getPuzzleList().getSelectionModel().getSelectedItem();
        //model.setCurrentPuzzle(selectedPuzzle);
        model.newPuzzle(selectedPuzzle);
        view.update();
    }

    //Start Button Handler
    private void handleStartButtonPress(){
        if(model.started){ //if a game has already started
            //stop the game
            model.stopGame();
            updateTimer.stop();
            view.update();
        } else if(model.puzzleSelected) { // else start a game
            model.startGame();
            updateTimer.play();
            view.update();
        }
    }

    public static void main(String[] args){launch(args);}
}
