import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * TetrisController serves as the controller for Tetris.
 *
 * The controller contains the central loop that runs as the game is in progress,
 * and keeps evaluating user input until game is lost or user presses quit.
 *
 * It contains instances of TetrisLogic and TetrisView, in order to communicate between them.
 * It receives input from the user, delegates that input to the corresponding method (moveDown(),
 * drop(), moveHorizontal(), rotateCW() etc).
 * (ex: an input of ACTION_DOWN will result in moveDown() being invoked)
 *
 * This class also handles the functionality of deciding when to clear a row and
 * when a Tetris has been scored.
 *
 * @author Sabirah Shuaybi
 */

public class TetrisController {

    private TetrisView view;
    private TetrisModel model;
    private TetrisTimer timer;

    private boolean gameOverFlag = false;


    public TetrisController(TetrisModel model, TetrisView view, int level) {
        this.model = model;
        this.view = view;
        //Pass in the currently selected level to timer
        timer = new TetrisTimer(this, level);
    }

    /**
     * Evaluates whether the move that user wants to execute is valid.
     * This involves creating a hypothetical next position (the position
     * the shape would occupy if its a valid move) and evaluating the validity
     * of that hypothetical move.
     *
     * @param nextLocation; the next, hypothetical location that the shape might occupy.
     *                      Note; Only need one cell because the other cells will be
     *                      computer relative to the cell passed in
     * @return true if desired move is valid, else false
     */
    private boolean isValidMove(Cell nextLocation) {

        //Get all the cells that occupy the hypothetical move
        Set<Cell> nextPotentialCells = TetrisUtil.computeFilledCells
                (model.getFallingShape(), nextLocation);

        //Evaluation of validity involves checking for collision with locked cells
        if (TetrisUtil.intersects(nextPotentialCells, model.getLockedCells())) {
            return false;
        }

        //Evaluation of validity ALSO involves checking that the next move will not
        //go out of bounds of the board
        for (Cell c : nextPotentialCells) {
            if (c.getX() < 0 || c.getX() > (Constants.NUM_COLS - 1) ||
                    c.getY() > (Constants.NUM_ROWS - 1)) {
                return false;
            }
        }
        //Else, the desired move is valid
        return true;
    }

    /**
     * Handles the moveDown of the shape and returns a boolean
     * value that communicates how successful the attempt to
     * moveDown was (true, if successful moveDown, false if invalid moveDown)
     *
     * @return true if shape was able to move down (valid move),
     * false if shape was unable to move down (invalid move)
     */
    public void moveDown() {
        //If game is over, exit to prevent any further vertical movement
        if (gameOverFlag)
            return;
        else {
            //Or, check game status, and if game over, proceed with the end game events
            if (isGameOver()) {
                gameOverFlag = true;
                endGame();
                view.repaint();
                return;
            }
        }
        //If execution reaches this point, it means that game is still in progress
        Cell shapeLocation = model.getShapeLocation();
        Cell nextLocation = new Cell(shapeLocation.getX(), shapeLocation.getY() + 1);

        if (isValidMove(nextLocation)) {
            //Produce a short sound effect to indicate the fall of the shape
            TetrisUtil.playFalling();

            //move is valid so update shape's location to the new location
            //no longer a hypothetical location
            model.setShapeLocation(nextLocation);
        } else {
            Set<Cell> shapeCells = TetrisUtil.computeFilledCells
                    (model.getFallingShape(), shapeLocation);
            //Shape has landed, so add shape's cells to locked cells
            model.addToLockedCells(shapeCells);
            TetrisUtil.playLanding();

            //Create the next shape
            model.createNewShape();

            //Check row status: are any rows complete? and if so, how many?
            int numRowsCleared = clearRows();

            //If 4 rows have been cleared, update TetrisCleared score
            if (numRowsCleared == Constants.TETRIS) {

                //Line clear and Tetris clear sound effects are the same
                TetrisUtil.playLineClear();
                model.incrementTetrisCleared();
                view.displayTetrisCleared();
            }
        }
        view.repaint();
    }

    /**
     * A public moveLeft method that can be called from outside the controller
     * (that is, in response to a key event)
     */
    public void moveLeft() {
        moveHorizontal(Constants.DIR_LEFT);
    }

    /**
     * A public moveRight method that can be called from outside
     * (that is, in response to a key event)
     */
    public void moveRight() {
        moveHorizontal(Constants.DIR_RIGHT);
    }

    /**
     * Moves shape either 1 unit to the left or
     * 1 unit to the right depending on direction
     *
     * @param direction: left or right
     */
    private void moveHorizontal(int direction) {
        Cell shapeLocation = model.getShapeLocation();

        //Since DIR_LEFT = -1 and DIR_RIGHT = 1, can add these directly to shape's location to
        //achieve the desired horizontal move
        Cell nextLocation = new Cell(shapeLocation.getX() + direction, shapeLocation.getY());
        if (isValidMove(nextLocation)) {
            model.setShapeLocation(nextLocation);
        }
    }

    /**
     * A public rotate clock-wise method that can be called from outside
     * the controller (that is, in response to a key event)
     */
    public void rotateCW() {
        model.rotateCW();
        //If the rotation was not in fact a valid move (ex: out of bounds), undo rotation
        if (!isValidMove(model.getShapeLocation())) {
            //To undo a CW rotation, you would rotate the shape CCW
            model.rotateCCW();
        }
    }

    /**
     * A public rotate counter clock-wise method that can be called from outside
     * the controller (that is, in response to a key event)
     */
    public void rotateCCW() {
        model.rotateCCW();
        //If the rotation was not in fact a valid move (ex: out of bounds), undo rotation
        if (!isValidMove(model.getShapeLocation())) {
            //To undo a CCW rotation, you would rotate the shape CW
            model.rotateCW();
        }
    }

    /**
     * Method evaluates whether a row contains
     * any clocked cells and returns true is so.
     * Note: Method returns true even if a row
     * has only one locked cell
     * <p>
     * Useful for skipping over rows that are empty
     * (Since you don't need to check the rows that
     * are completely empty when determining if any
     * rows need to be cleared)
     *
     * @param row: the row
     * @return true if a row has a locked cell,
     * false if a row is completely empty
     */
    private boolean doesRowHaveAnyLockedCells(int row) {
        for (int column = 0; column < Constants.NUM_COLS; column++) {
            Cell cell = new Cell(column, row);
            if (model.getLockedCells().contains(cell))
                return true;
        }
        return false;
    }

    /**
     * Loops across a row and determines if the
     * entire row has been filled (contains
     * locked cells)
     *
     * @param row: row to check for completion
     * @return true is a row is completed/full, else false
     */
    private boolean isRowFilled(int row) {
        for (int column = 0; column < Constants.NUM_COLS; column++) {
            Cell cell = new Cell(column, row);

            //Return false as soon as an empty cell is encountered
            if (!model.getLockedCells().contains(cell))
                return false;
        }
        return true;
    }

    /**
     * Runs across the entire board, if any row has even one
     * lockedCell, it checks to see if that row has been
     * completed, if so, calls to remove row and update score
     *
     * @return integer representing how many lines were cleared
     * simultaneously --> useful for determining a Tetris
     */
    private int clearRows() {
        int numRowsCleared = 0;
        for (int row = (Constants.NUM_ROWS - 1); row >= 0; row--) {

            //If you come across an empty row, can just exit method
            if (!doesRowHaveAnyLockedCells(row)) {
                return numRowsCleared;
            }
            if (isRowFilled(row)) {
                removeRow(row);
                model.incrementLinesCleared();
                view.displayLinesCleared();
                //Adding 1 to clearRows() each time keeps track
                //of any lines cleared simultaneously
                numRowsCleared = clearRows() + 1;
            }
        }
        return numRowsCleared;
    }


    /**
     * Deletes a row of cells.
     * <p>
     * Note: This method is only called
     * when a row has been filled
     *
     * @param row: the row of cells to be removed
     */
    private void removeRow(int row) {
        for (int column = 0; column < Constants.NUM_COLS; column++) {
            Cell cell = new Cell(column, row);
            //Remove the row of cells from lockedCell set
            //as they are no longer part of the board
            model.getLockedCells().remove(cell);
        }

        /* Objective: Once a line has been removed, all the cells
        above must be shifted down. However, since Cell is an
        immutable object, in order to 'change' the state of
        the cells above, the cells above are be removed,
        and new cells are created with the updated y coordinate */

        Iterator<Cell> i = model.getLockedCells().iterator();
        Set<Cell> removedCells = new HashSet<>();
        while (i.hasNext()) {
            Cell c = i.next();
            if (c.getY() < row) {
                i.remove();
                removedCells.add(c);
            }
        }
        for (Cell c : removedCells) {
            Color cellColor = c.getColor();
            //Add the removed cells (which are technically new
            //Cell objects) back to lockedCells but with updated
            //y coordinates (shifted down)
            model.getLockedCells().add((new Cell(c.getX(), c.getY() + 1, cellColor)));
        }
        TetrisUtil.playLineClear();
    }

    /**
     * Method that checks if the shape just created already intersects
     * with any of the lockedCells on the board (before falling down at all)
     * <p>
     * If this is the case, it means game is over.
     *
     * @return true if game is over, or false if game is not over
     */
    private boolean isGameOver() {
        Set<Cell> shapeCells = TetrisUtil.computeFilledCells
                (model.getFallingShape(), model.getShapeLocation());

        if (TetrisUtil.intersects(shapeCells, model.getLockedCells())) {

            //Add current shape to locked cells because it cannot move down at all
            model.addToLockedCells(shapeCells);

            //To show user that shape is locked now because it
            //landed even before it could moveDown -> game over
            view.repaint();
            return true;
        } else
            return false;
    }

    /**
     * Handles the events associated with game over
     * Stops the timer
     * Sends message to user informing them that
     * they have lost
     * Plays the game over sound effect
     */
    private void endGame() {
        stopTimer();
        view.displayGameOverMessage();
        TetrisUtil.playGameOver();
    }

    /* Stops the timer */
    public void stopTimer() {
        timer.stop();
    }

    /**
     * Used for testing/debugging aspects of the game.
     * Sets up a pre-filled grid to make it easier to test for
     * things like deleting a line, updating score and detecting game over.
     * <p>
     * Note: This method is no longer being invoked
     */
    private void testMethod() {
        model.getLockedCells().add(new Cell(0, 17));
        model.getLockedCells().add(new Cell(1, 17));
        model.getLockedCells().add(new Cell(2, 17));
        model.getLockedCells().add(new Cell(3, 17));
        model.getLockedCells().add(new Cell(4, 17));
        model.getLockedCells().add(new Cell(5, 17));
        model.getLockedCells().add(new Cell(6, 17));
        model.getLockedCells().add(new Cell(7, 17));
        model.getLockedCells().add(new Cell(8, 17));

        model.getLockedCells().add(new Cell(0, 16));
        model.getLockedCells().add(new Cell(1, 16));
        model.getLockedCells().add(new Cell(2, 16));
        model.getLockedCells().add(new Cell(3, 16));
        model.getLockedCells().add(new Cell(4, 16));
        model.getLockedCells().add(new Cell(5, 16));
        model.getLockedCells().add(new Cell(6, 16));
        model.getLockedCells().add(new Cell(7, 16));
        model.getLockedCells().add(new Cell(8, 16));
    }
}

