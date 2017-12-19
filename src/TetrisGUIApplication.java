import javax.swing.*;

/**
 * Tetris is the main application class where execution of the program begins.
 * It contains the main method where it creates an instance of itself.
 * This class is responsible for setting up the JFrame for the game as well
 * as arranging for a restart/new game feature.
 *
 * The BONUS features integrated into this Tetris Project are the following:
 * -Indication of when the game has been lost (This is accomplished dually by sound
 * (The GAME OVER voice) AND the red message that appears to inform the player that
 * the game is over)
 * -Each of the 7 pieces are displayed with their own unique color as dictated in
 * the TetrisShapeFactory. Furthermore, the cells retain their color once landing
 * and becoming part of the locked set of cells on the board.
 * -There is a NEW GAME (restart) option for players that resets the entire game
 * and begins with a brand new game.
 * -There are three separate levels of difficulty to chose from - easy, medium, and hard.
 * The more difficult the level, the faster the shape falls down.
 * -Finally, sounds are played during the game when a piece falls, when a piece lands,
 * when a line or tetris is cleared and when the game has been lost. s
 *
 * @author Sabirah Shuaybi
 */

public class TetrisGUIApplication {

    private JFrame mainFrame;
    private TetrisView currentView;

    public TetrisGUIApplication() {
        mainFrame = createFrame();
    }

    /**
     * Creates and renders the JFrame for the application
     *
     * @return the gui JFrame
     */
    private JFrame createFrame() {
        JFrame guiFrame = new JFrame("Tetris Application");

        //Set size
        guiFrame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);

        //Exit normally on closing the window
        guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Show/render frame
        guiFrame.setVisible(true);

        return guiFrame;
    }

    /**
     * Responsible for creating a brand new Tetris game, with new instances
     * of a TetrisModel, TetrisView and TetrisController.
     *
     * @param level: The level of difficulty user has selected
     *              Note: Selecting a different level means starting a new game
     */
    public void createNewGame(int level) {
        TetrisModel model = new TetrisModel();
        TetrisView view =  new TetrisView(model, level, this);
        TetrisController controller = new TetrisController(model, view, level);
        view.setController(controller);

        //If there is an existing view, remove it
        if (currentView != null) {
            mainFrame.remove(currentView);
        }
        mainFrame.add(view);
        mainFrame.setVisible(true);
        //reset the currentView to the renewed instance of TetrisView
        currentView = view;
    }

    public static void main (String[] args) {
        TetrisGUIApplication application = new TetrisGUIApplication();

        //The dafault level of the game will be medium unless user changes it
        application.createNewGame(Constants.MEDIUM_LEVEL);
    }
}
