/**
 * This class serves only to conveniently hold all constants
 * required across the various classes.
 *
 * @author Sabirah Shuaybi
 */
public class Constants {

    //Size of the JFrame
    public static final int FRAME_WIDTH = 400;
    public static final int FRAME_HEIGHT = 800;

    //To hold information about various levels of difficulty
    public static final int EASY_LEVEL = 0;
    public static final int MEDIUM_LEVEL = 1;
    public static final int HARD_LEVEL = 2;

    //The two horizontal directions in terms of shape movement
    public static final int DIR_LEFT = -1;
    public static final int DIR_RIGHT = 1;

    //Starting position of all Tetris shapes is the top center of board
    public static final int START_POS_X = 4;
    //The y coordinate starting shapePosition for pieces will always be 0
    public static final int START_POS_Y = 0;

    //Tetris means clearing 4 lines at once
    public static final int TETRIS = 4;

    //Used to set the initial delay in Timer class
    public final static int ONE_SECOND = 1000;

    //All of the folling are Constants requried for the TetrisGridView:
    public static final int NUM_ROWS = 18;
    public static final int NUM_COLS = 10;

    //Start location for the first grid square drawn on screen
    public static final int START_LOC = 20;
    public static final int CELL_SIZE = 35;

    //Computations regarding the length of the grid lines
    public static final int HORIZONTAL_LINE_LENGTH = NUM_COLS*CELL_SIZE;
    public static final int VERTICAL_LINE_LENGTH = NUM_ROWS*CELL_SIZE;
}
