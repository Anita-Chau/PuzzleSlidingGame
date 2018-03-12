import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PuzzleView extends Pane {
    private Puzzle model;// = new Puzzle();
    private Image puzzleImage;

    //user interface components needed by the controller
    private ListView<String> puzzleList;
    private Button startButton;
    private Label thumbnailTile;
    private TextField timeField;
    private int seconds=0;
    private int minutes=0;


    //public methods to allow access to window components
    public ListView<String> getPuzzleList() { return puzzleList; }
    public Button getStartButton() { return startButton; }

    public PuzzleView(Puzzle m){
        model = m;

        //Pane aPane = new Pane();
        BorderPane outerPane = new BorderPane();
        outerPane.setStyle("-fx-background-color: white; -fx-border-color: gray;" +
                "-fx-padding: 4 4;");

        //Pane that contains the puzzle tiles
        GridPane puzzlePane = new GridPane();
        puzzlePane.setPadding(new Insets(10,0,10,10));
        puzzlePane.setHgap(1);
        puzzlePane.setVgap(1);


        puzzleImage = new Image(getClass().getResourceAsStream("Lego_Thumbnail.png"));

        for (int row=1; row<=4; row ++)
            for (int col=1; col<=4; col++){
                puzzlePane.add(model.getPuzzlePieces()[row - 1][col - 1], col, row);
            }

        //inner pane that contains ListView and button
        BorderPane buttonListPane = new BorderPane();
        buttonListPane.setPadding(new Insets(10,0,10,0));
        String[] puzzles = {"Pets", "Scenery", "Lego", "Numbers"};
        puzzleList = new ListView<String>();
        //puzzleList.setPadding(new Insets(10));
        puzzleList.setItems(FXCollections.observableArrayList(puzzles));
        puzzleList.setPrefSize(187,137);
        //puzzleList.setPadding(new Insets(0,0,10,0));
        buttonListPane.setTop(puzzleList);

        Pane empty = new Pane();
        empty.setPrefSize(187,10);
        buttonListPane.setCenter(empty);

        startButton = new Button("Start");
        startButton.setPrefSize(187,30);
        //startButton.setPadding(new Insets(10,0,0,0));
        startButton.setStyle("-fx-font: 14 arial; -fx-base:rgb(20,75,0); " +
                "-fx-text-fill: rgb(255,255,255);");
        buttonListPane.setBottom(startButton);

        //Pane that contains the thumbnail, texts, and button
        GridPane infoPane = new GridPane();
        infoPane.setPadding(new Insets(10,10,0,10));

        Pane timePane = new Pane();
        timePane.setPrefWidth(137);
        Label tLabel = new Label("Time:    ");
        tLabel.setPrefWidth(50);
        tLabel.relocate(0,0);
        timeField = new TextField("0:00");
        timeField.setEditable(false);
        timeField.setPrefWidth(137);
        timeField.relocate(50,0);
        timePane.getChildren().addAll(tLabel,timeField);

        thumbnailTile = new Label();
        thumbnailTile.setPadding(new Insets(0));
        thumbnailTile.setGraphic(new ImageView(puzzleImage));
        thumbnailTile.setPrefSize(187,187);

        infoPane.add(thumbnailTile,1,1);
        infoPane.add(buttonListPane,1,2);
        infoPane.add(timePane, 1,3);

        //Putting whole window together
        outerPane.setCenter(puzzlePane);
        outerPane.setRight(infoPane);
        getChildren().add(outerPane);

        update();
    }

    public void update() { //called whenever the model changes
        String puzzleName = model.getCurrentPuzzle();
        if (!(puzzleName.equals("BLANK"))){
            puzzleImage = new Image(getClass().getResourceAsStream(puzzleName + "_Thumbnail.png"));
            thumbnailTile.setGraphic(new ImageView(puzzleImage));
        }
        if(model.started) {
            puzzleList.setDisable(true);
            thumbnailTile.setDisable(true);
            startButton.setText("Stop");
            startButton.setStyle("-fx-font: 14 arial; -fx-base:rgb(200,30,30); " +
                    "-fx-text-fill: rgb(255,255,255);");
            if(model.finished()){
                seconds = 0;
                minutes = 0;
                puzzleList.setDisable(false);
                thumbnailTile.setDisable(false);
                startButton.setText("Start");
                startButton.setStyle("-fx-font: 14 arial; -fx-base:rgb(20,75,0); " +
                        "-fx-text-fill: rgb(255,255,255);");
            }
        } else {
            seconds = 0;
            minutes = 0;
            puzzleList.setDisable(false);
            thumbnailTile.setDisable(false);
            startButton.setText("Start");
            startButton.setStyle("-fx-font: 14 arial; -fx-base:rgb(20,75,0); " +
                    "-fx-text-fill: rgb(255,255,255);");
        }
        if(model.timerStarted){
            if(seconds<59) {
                seconds++;
            } else {
                seconds = 0;
                minutes++;
            }
            timeField.setText(minutes + ":" + String.format("%02d", seconds));
        }
    }
}
