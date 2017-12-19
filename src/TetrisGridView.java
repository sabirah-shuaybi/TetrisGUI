import java.util.Set;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;


/**
 * TetrisGridView sets up the visual playing board for Tetris.
 * TetrisView handles the following:
 * 1)Setting up the horizontal and vertical grid lines
 * 2)Rendering the board based on the state of each
 * location/cell on board (falling, locked or empty) and its
 * corresponding color.
 *
 * @author Sabirah Shuaybi
 */
public class TetrisGridView extends JComponent {

    private TetrisModel model;

    public TetrisGridView(TetrisModel model) {
        this.model = model;
    }

    @Override
    public void paintComponent(Graphics g) {
        createHorizontalGridLines
                (g, 0, Constants.START_LOC, Constants.START_LOC, Constants.START_LOC +
                        Constants.HORIZONTAL_LINE_LENGTH, Constants.START_LOC);
        createVerticalGridLines
                (g, 0, Constants.START_LOC, Constants.START_LOC, Constants.START_LOC,
                        Constants.START_LOC + Constants.VERTICAL_LINE_LENGTH);
        renderBoard(g);
    }

    /**
     * A recursive method that draws all the horizontal grid lines of the grid
     * @param g: graphics object
     * @param numLinesDrawn: the total number of horizontal lines on the grid
     * @param x1: x coordinate of the first line's start point
     * @param y1: y coordinate of the first line's start point
     * @param x2: x coordinate of the first line's end point
     * @param y2: y coordinate of the first line's end pint
     */
    private void createHorizontalGridLines(Graphics g, int numLinesDrawn, int x1, int y1, int x2, int y2) {
        if(numLinesDrawn > Constants.NUM_ROWS) {
            return;  //base case - all horizontal lines have been drawn
        }
        g.drawLine(x1, y1, x2, y2);
        createHorizontalGridLines(g, numLinesDrawn+1, x1, y1+Constants.CELL_SIZE, x2, y2+Constants.CELL_SIZE);
    }

    /**
     * A recursive method that draws all the vertical grid lines of the grid
     * @param g: graphics object
     * @param numLinesDrawn: the total number of vertical lines on the grid
     * @param x1: x coordinate of the first line's start point
     * @param y1: y coordinate of the first line's start point
     * @param x2: x coordinate of the first line's end point
     * @param y2: y coordinate of the first line's end pint
     */
    private void createVerticalGridLines(Graphics g, int numLinesDrawn, int x1, int y1, int x2, int y2) {
        if(numLinesDrawn > Constants.NUM_COLS) {
            return; //base case - all vertical lines have been drawn
        }
        g.drawLine(x1, y1, x2, y2);
        createVerticalGridLines(g, numLinesDrawn+1, x1+Constants.CELL_SIZE, y1, x2+Constants.CELL_SIZE, y2);
    }

    /**
     * This method is responsible for transforming a cell's logical location
     * into an absolute location on the grid view and deriving the cell's color
     * (The cell's color originates from the shape it was part of)
     * It then draws this square block into place on the grid/board with a
     * black border for definition/aesthetic purposes
     */
    private void drawCell(Graphics g, Cell cell) {
        //Compute the absolute x and y coordinates on the physical grid
        int x = (cell.getX()*Constants.CELL_SIZE) + Constants.START_LOC;
        int y = (cell.getY()*Constants.CELL_SIZE) + Constants.START_LOC;

        g.setColor(cell.getColor());

        g.fillRect(x, y, Constants.CELL_SIZE, Constants.CELL_SIZE);

        //Create a black border around cell (to better define it)
        g.setColor(Color.BLACK);
        g.drawRect(x, y, Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    /**
     * The renderBoard method paints onto the grid, all of the cells within the
     * locked set as well as the cells belonging to the currently falling shape.
     */
    private void renderBoard(Graphics g) {
        Set<Cell> lockedCells = model.getLockedCells();

        //Determine where the falling shape cells are and store these cells in a hash set
        TetrisShape fallingShape = model.getFallingShape();
        Cell shapeLocation = model.getShapeLocation();
        Set<Cell> tetrisPieceCells = TetrisUtil.computeFilledCells(fallingShape, shapeLocation);

        //Render all locked cells onto the board
        for(Cell c: lockedCells) {
            drawCell(g, c);
        }

        //Render the shape cells onto the board
        for(Cell c: tetrisPieceCells) {
            drawCell(g, c);
        }
    }
}

