import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * TetrisUtil contains utility functions that
 * help other classes/methods accomplish their task.
 *
 * The reason for this class is to house pure functions
 * that are integral to the functioning of the game.
 *
 * All of the utility functions are static, they
 * do not rely on state; they only perform a single task.
 *
 * @author Sabirah Shuaybi
 */

public class TetrisUtil {

    /**
     * Translates the matrix definition of a TetrisShape
     * (which consists of 1s and 0s) into absolute location
     * coordinates on the board. It takes a TetrisShape and
     * a location (Cell) and computes the rest of the absolute
     * grid location (Cells) relative to the position passed in
     *
     * Example: If the L shape is passed in, the method will return
     * a set of Cells that, when rendered, form an L shape on the
     * board, thus bringing that shape to 'life' on screen
     *
     * @param shape: The shape that is to be translated into
     *             absolute locations
     * @param position: The cell around which to orient the
     *                other cells that make up the TetrisShape
     *                (in terms of its absolute presence on the board)
     * @return a set/collection of the cells (absolute locations) of
     *         the shape passed in
     */
    public static Set<Cell> computeFilledCells(TetrisShape shape, Cell position) {

        Set<Cell> filledCells = new HashSet<>();

        //Get the theoretical definition of the shape (2D array representation)
        int[][] shapeMatrix = shape.getShapeMatrix();

        //Search the matrix for 1s, which signify a filled space
        for (int i = 0; i < shapeMatrix.length; i++) {
            for (int j = 0; j < shapeMatrix[i].length; j++) {
                if (shapeMatrix[j][i] == 1) {
                    //Offset all places with 1 with the coordinates of position
                    //(for proper definition of the shape on the board)
                    filledCells.add((new Cell(i + position.getX(), j + position.getY(), shape.getShapeColor())));
                }
            }
        }
        return filledCells;
    }

    /**
     * A method that checks to see if any objects in set1 are present
     * in set2. Useful for collision detection between the falling shape
     * and those that are locked on the board.
     *
     * @param set1: a set of Cell objects (ex: the falling shape cells)
     * @param set2: another set of Cell object (ex: the locked cells)
     * @return true if there is a conflict/intersection between the sets
     *         else, returns false
     */
    public static boolean intersects(Set<Cell> set1, Set<Cell> set2) {
        for (Cell c : set1) {
            if (set2.contains(c))
                return true;
        }
        return false;
    }

    /**
     * Method that halts the execution
     * thread by the number of milliseconds passed in
     *
     * @param milliseconds: the number of milliseconds
     *                    you want the thread to sleep
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (Exception e) {
            //ignore exception
        }
    }

    /**
     * Takes a 2D array and returns a new
     * 2D array that is a 90 degree rotation
     * of the one passed in.
     *
     * @param matrix: the 2D array to be rotated
     */
    public static int[][] rotate2DMatrix(int[][] matrix) {
        final int x = matrix.length;
        final int y = matrix[0].length;
        int[][] rotated = new int[x][y];

        //To rotate a matrix, you reverse the rows and columns
        //First row becomes the last column
        //Second row becomes the second last column
        //Third row becomes the first column
        for (int row = 0; row < x; row++) {
            for (int col = 0; col < y; col++) {
                rotated[col][x-1-row] = matrix[row][col];
            }
        }
        return rotated;
    }

    /**
     * Plays the sound associated with a line being cleared
     */
    public static void playLineClear() {
        TetrisUtil.playSound("line_cleared.wav");
    }

    /**
     * Plays the sound associated with the decent of a shape
     */
    public static void playFalling() {
        TetrisUtil.playSound("fallSound.wav");
    }

    /**
     * Plays the sound associated with a shape landing
     */
    public static void playLanding() {
        TetrisUtil.playSound("landingEffect.wav");
    }

    /**
     * Plays the sound associated with the game being lost
     */
    public static void playGameOver() {
        TetrisUtil.playSound("gameover.wav");
    }

    /**
     * The source of this code:
     * http://stackoverflow.com/questions/6045384/playing-mp3-and-wav-in-java
     *
     * @param soundFile; the audio file to be played
     */
    private static void playSound(String soundFile) {
        try {
            File f = new File("./" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e)
        {
            //ignore;
        }
    }
}

