import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Puzzle { //model
    private Button[][] puzzlePieces = new Button[4][4];
    private int[][] buttonStates = new int[4][4]; //keeps track of what puzzle piece is where (-1 - 15), -1 is a blank piece
    public boolean started = false;
    private String currentPuzzle = "BLANK";
    private int blankPiece = -1;
    private int randRow = -1;
    private int randCol = -1;
    public boolean puzzleSelected = false;
    public boolean timerStarted = false;

    public Puzzle() {
        for (int row = 0; row < 4; row++)
            for (int col = 0; col < 4; col++) {
                puzzlePieces[row][col] = new Button();
                buttonStates[row][col] = -1;
                Image puzzleImage = new Image(getClass().getResourceAsStream("BLANK.png"));

                puzzlePieces[row][col].setPadding(new Insets(0));
                puzzlePieces[row][col].setGraphic(new ImageView(puzzleImage));
                puzzlePieces[row][col].setPrefSize(187, 187);
            }
    }

    public void newPuzzle(String puzzleName) {
        currentPuzzle = puzzleName;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                String fullName = puzzleName + "_" + (row) + (col) + ".png";
                buttonStates[row][col] = (row * 4) + col;
                Image puzzleImage = new Image(getClass().getResourceAsStream(fullName));

                puzzlePieces[row][col].setGraphic(new ImageView(puzzleImage));
                puzzlePieces[row][col].setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        int swapRow = -1;
                        int swapCol = -1;
                        boolean clickFound = false;
                        for (int row = 0; row < 4; row++) {
                            for (int col = 0; col < 4; col++) {
                                if (event.getSource() == puzzlePieces[row][col]) {
                                    System.out.println("Pressed the button at row:" + row + " column:" + col);
                                    clickFound = true;
                                    swapRow = row;
                                    swapCol = col;
                                    break;
                                }
                            }
                            if (clickFound) {
                                break;
                            }
                        }
                        if (clickFound)
                            swap(swapRow, swapCol);

                        //print out all the states here:
                        /*for (int row = 0; row < 4; row++) {
                            for (int col = 0; col < 4; col++) {
                                System.out.print(buttonStates[row][col] + " ");
                            }
                        }*/

                        if(finished()){
                            System.out.println("Game finished");
                        }
                    }
                });
            }
        }
        //randRow and randCol generation
        randRow = (int)Math.floor(Math.random() * 4); //creates an integer between 0 and 3
        randCol = (int)Math.floor(Math.random() * 4);
        blankPiece = (4*randRow) + randCol;
        puzzlePieces[randRow][randCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
    }

    public void swap(int clickedRow, int clickedCol){
        int tempButtonState = -1;
        if(!(clickedRow>=3)){
            if(buttonStates[clickedRow+1][clickedCol] == blankPiece){
                tempButtonState = buttonStates[clickedRow][clickedCol];
                buttonStates[clickedRow][clickedCol] = buttonStates[clickedRow+1][clickedCol];
                puzzlePieces[clickedRow][clickedCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
                buttonStates[clickedRow+1][clickedCol] = tempButtonState;
                puzzlePieces[clickedRow+1][clickedCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(currentPuzzle + "_" +
                        (buttonStates[clickedRow+1][clickedCol]/4) + (buttonStates[clickedRow+1][clickedCol]%4) + ".png"))));
            }
        }
        if (!(clickedCol>=3)){
            if (buttonStates[clickedRow][clickedCol+1] == blankPiece) {
                tempButtonState = buttonStates[clickedRow][clickedCol];
                buttonStates[clickedRow][clickedCol] = buttonStates[clickedRow][clickedCol + 1];
                puzzlePieces[clickedRow][clickedCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
                buttonStates[clickedRow][clickedCol + 1] = tempButtonState;
                puzzlePieces[clickedRow][clickedCol+1].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(currentPuzzle + "_" +
                        (buttonStates[clickedRow][clickedCol+1]/4) + (buttonStates[clickedRow][clickedCol+1]%4) + ".png"))));
            }
        }
        if (!(clickedCol<=0)){
            if (buttonStates[clickedRow][clickedCol-1] == blankPiece) {
                tempButtonState = buttonStates[clickedRow][clickedCol];
                buttonStates[clickedRow][clickedCol] = buttonStates[clickedRow][clickedCol - 1];
                puzzlePieces[clickedRow][clickedCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
                buttonStates[clickedRow][clickedCol - 1] = tempButtonState;
                puzzlePieces[clickedRow][clickedCol - 1].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(currentPuzzle + "_" +
                        (buttonStates[clickedRow][clickedCol - 1]/4) + (buttonStates[clickedRow][clickedCol - 1]%4) + ".png"))));
            }
        }
        if (!(clickedRow<=0)){
            if (buttonStates[clickedRow-1][clickedCol] == blankPiece){
                tempButtonState = buttonStates[clickedRow][clickedCol];
                buttonStates[clickedRow][clickedCol] = buttonStates[clickedRow-1][clickedCol];
                puzzlePieces[clickedRow][clickedCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
                buttonStates[clickedRow-1][clickedCol] = tempButtonState;
                puzzlePieces[clickedRow-1][clickedCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(currentPuzzle + "_" +
                        (buttonStates[clickedRow-1][clickedCol]/4) + (buttonStates[clickedRow-1][clickedCol]%4) + ".png"))));
            }
        }
    }

    //Starting a game after a puzzle has been chosen, initializing for swap
    public void startGame(){
        started = true;
        timerStarted = true;
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 4; col++) {
                puzzlePieces[row][col].setDisable(false);
            }
        }
        for (int i=0; i<5000; i++) {
            int randInt1 = (int)Math.floor(Math.random() * 4);
            int randInt2 = (int)Math.floor(Math.random() * 4);
            swap(randInt1, randInt2);
        }
    }

    public void stopGame(){
        started = false;
        timerStarted = false;
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 4; col++) {
                puzzlePieces[row][col].setDisable(true);
            }
        }
    }
    
    public boolean finished() {
        boolean notComplete = false;
        int i = 0;
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 4; col++) {
                if (i != buttonStates[row][col]) {
                    return notComplete;
                }
                i++;
            }
        }
        puzzlePieces[randRow][randCol].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(
                currentPuzzle + "_" + (randRow) + (randCol) + ".png"))));
        stopGame();
        return !notComplete;
    }

    public Button[][] getPuzzlePieces() {
        return puzzlePieces;
    }

    public String getCurrentPuzzle() {
        return currentPuzzle;
    }
}