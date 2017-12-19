import java.util.Random;
import java.awt.Color;

/**
 * TetrisShapeFactory constructs all the 7 shapes present in Tetris
 * Each shape is represented by its own 2D array (shapeMatrix) using 1s and 0s.
 * The class also assigns a unique color to each of the 7 shapes.
 *
 * TetrisShapeFactory contains a method that randomly picks out a shape
 * from the shapes array (which holds one of each shape) and returns it.
 *
 * Thus, this class enables the creation as well as the randomization of
 * the pieces in the game.
 *
 * @author Sabirah Shuaybi
 */
public class TetrisShapeFactory {

    //A collection of all the different TetrisShapes
    private static TetrisShape[] shapes = {createI(), createO(), createT(),
            createL(), createJ(), createS(), createZ()};

    /**
     * Each of the 7 methods below is responsible for defining
     * one of the 7 TetrisShapes. ShapeMatrix arrays are represented
     * by integers where 1 is filled and 0 is empty. Each TetrisShape
     * is also affiliated with it's unique color.
     *
     * Note: These TetrisShapes are formed not by absolute locations but by
     * a 2D array of integers to define their specific shape; these definitions
     * have to be translated into concrete values to be rendered on the board.
     * (This translation/computation is done by the computeFilledCells method
     * in the TetrisUtil class)
     *
     * @return The TetrisShape created
     */
    private static TetrisShape createI() {
        int[][] shapeMatrix = new int[4][4];
        shapeMatrix[0][0] = 1;
        shapeMatrix[0][1] = 1;
        shapeMatrix[0][2] = 1;
        shapeMatrix[0][3] = 1;

        Color IColor = new Color(141, 31, 147);
        return new TetrisShape(shapeMatrix, IColor);
    }
    private static TetrisShape createO() {
        int[][] shapeMatrix = new int [2][2];
        shapeMatrix[0][0] = 1;
        shapeMatrix[0][1] = 1;
        shapeMatrix[1][0] = 1;
        shapeMatrix[1][1] = 1;

        Color OColor = new Color(27, 166, 29);
        return new TetrisShape(shapeMatrix, OColor);
    }

    private static TetrisShape createT() {
        int[][] shapeMatrix = new int [3][3];
        shapeMatrix[0][1] = 1;
        shapeMatrix[1][0] = 1;
        shapeMatrix[1][1] = 1;
        shapeMatrix[1][2] = 1;

        Color TColor = new Color(21, 142, 180);
        return new TetrisShape(shapeMatrix, TColor);
    }

    private static TetrisShape createL() {
        int[][] shapeMatrix = new int [3][3];
        shapeMatrix[0][0] = 1;
        shapeMatrix[1][0] = 1;
        shapeMatrix[2][0] = 1;
        shapeMatrix[2][1] = 1;

        Color LColor = new Color(202, 100, 18);
        return new TetrisShape(shapeMatrix, LColor);
    }
    private static TetrisShape createJ() {
        int[][] shapeMatrix = new int [3][3];
        shapeMatrix[0][1] = 1;
        shapeMatrix[1][1] = 1;
        shapeMatrix[2][0] = 1;
        shapeMatrix[2][1] = 1;

        Color JColor = new Color(202, 223, 24);
        return new TetrisShape(shapeMatrix, JColor);
    }

    private static TetrisShape createZ() {
        int[][] shapeMatrix = new int [3][3];
        shapeMatrix[0][0] = 1;
        shapeMatrix[0][1] = 1;
        shapeMatrix[1][1] = 1;
        shapeMatrix[1][2] = 1;

        Color ZColor = new Color(197, 10, 118);
        return new TetrisShape(shapeMatrix, ZColor);
    }

    private static TetrisShape createS() {
        int[][] shapeMatrix = new int [3][3];
        shapeMatrix[0][1] = 1;
        shapeMatrix[0][2] = 1;
        shapeMatrix[1][0] = 1;
        shapeMatrix[1][1] = 1;

        Color SColor = new Color(198, 21, 13);
        return new TetrisShape(shapeMatrix, SColor);
    }
    /**
     * Randomly picks out and returns one of the 7
     * TetrisShapes
     *
     * @return a randomly selected TetrisShape
     */
    public static TetrisShape getRandom() {
        //Generate a random integer r between 0 and 6
        Random r = new Random();
        int randomIndex = r.nextInt(7);

        return shapes[randomIndex];
    }
}

