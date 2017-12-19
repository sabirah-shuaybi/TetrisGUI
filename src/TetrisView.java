import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * This class constitutes as the "VIEW" of the MVC design. And as such,
 * is responsible for building and formatting the swing components required for
 * Tetris (labels for score, new game button, and level combo box). The view also
 * creates an instance of the GridView and centers it within the panel.
 *
 * In order to handle events associated with the JButton and JComboBox, TetrisView
 * implements the ActionListner interface.
 *
 * NOTE: Key Bindings are used INSEAD of the KeyListener interface for key input because
 * I was having trouble implementing the KeyListener on this JPanel class.
 *
 * @author Sabirah Shuaybi
 */
public class TetrisView extends JPanel implements ActionListener {

    private TetrisGUIApplication application;
    private TetrisGridView gridView;
    private TetrisController controller;
    private TetrisModel model;

    //The swing components needed for game display
    private JButton newGame;
    private JComboBox pickLevel;
    private JLabel linesCleared;
    private JLabel tetrisCleared;
    private JLabel gameOverMsg;

    private String[] LEVEL_ITEMS = {"Easy", "Medium", "Hard"};

    public TetrisView(TetrisModel model, int level, TetrisGUIApplication application) {
        super(new BorderLayout());
        gridView = new TetrisGridView(model);

        //TetrisView needs a reference to the application class to be able to
        //call its createNewGame method
        this.application = application;
        this.model = model;

        buildPanel();
        setSelectedLevel(pickLevel, level);
        this.repaint();

        //Render the board within the panel
        gridView.repaint();
        setupKeyBinding();
    }

    /**
     * Maps user key strokes to all the valid actions in Tetris
     * //http://stackoverflow.com/questions/16530775/keylistener-not-working-for-jpanel
     */
    private void setupKeyBinding() {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inMap = getInputMap(condition);
        ActionMap actMap = getActionMap();

        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
        actMap.put("Down", new DownAction());
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
        actMap.put("Left", new LeftAction());
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
        actMap.put("Right", new RightAction());
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "RotateCW");
        actMap.put("RotateCW", new RotateCWAction());
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "RotateCCW");
        actMap.put("RotateCCW", new RotateCCWAction());
    }

    /**
     * Builds up and formats the full JPanel with all the nested panels
     * associated with score, grid and playing.
     */
    private void buildPanel() {
        //Score panel holds the two scoring aspects to Tetris
        JPanel scorePanel = new JPanel(new GridLayout(4, 1));

        //For spacing purposes
        scorePanel.add(Box.createRigidArea(new Dimension(0, 5)));

        linesCleared = new JLabel("Lines Cleared: 0");
        linesCleared.setHorizontalAlignment(JLabel.CENTER);
        scorePanel.add(linesCleared);

        tetrisCleared = new JLabel("Tetris Cleared: 0");
        tetrisCleared.setHorizontalAlignment(JLabel.CENTER);
        scorePanel.add(tetrisCleared);

        gameOverMsg = new JLabel("");
        gameOverMsg.setHorizontalAlignment(JLabel.CENTER);
        scorePanel.add(gameOverMsg);

        //Board panel holds the central Tetris grid
            //(with the grid lines, locked cells and falling shape)
        JPanel boardPanel = new JPanel(new BorderLayout());
        boardPanel.add(gridView, BorderLayout.CENTER);

        //Play panel holds the new game button and level combo box
        JPanel playPanel = new JPanel(new GridLayout(2, 2));
        newGame = new JButton("New Game");
        newGame.addActionListener(this);

        //Create menu for different LEVEL_ITEMS
        pickLevel = new JComboBox(LEVEL_ITEMS);
        pickLevel.addActionListener(this);
        playPanel.add(newGame);
        playPanel.add(pickLevel);

        //Add spacing after
        //to prevent it from being displayed on the very bottom edge of window
        playPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        add(scorePanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(playPanel, BorderLayout.SOUTH);

    }

    /**
     * Gets the currently selected level from the combo box
     * @param pickLevel: the JComboBox
     * @param level: level of difficulty
     */
    private void setSelectedLevel(JComboBox pickLevel, int level) {
        switch(level) {
            case Constants.HARD_LEVEL: pickLevel.setSelectedItem("Hard"); break;
            case Constants.MEDIUM_LEVEL: pickLevel.setSelectedItem("Medium"); break;
            default: pickLevel.setSelectedItem("Easy");
        }
    }

    public void setController(TetrisController controller){
        this.controller = controller;
    }

    /** Updates the number of lines cleared by user */
    public void displayLinesCleared() {
        linesCleared.setText("Lines Cleared: " + model.getNumLinesCleared());
    }

    /**
     * Updates the number of Tetrises cleared by user
     * A Tetris occurs when four lines are simultaneously cleared
     */
    public void displayTetrisCleared() {
        tetrisCleared.setText("Tetris Cleared: " + model.getNumTetrisCleared());
    }

    public void displayGameOverMessage() {
        gameOverMsg.setText("GAME OVER!");
        gameOverMsg.setForeground(Color.red);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (controller == null)
            return;
        //Reset to a new game if the new game button is clicked OR a new level is selected
        //NOTE: If player selects a new game, the level s/he was previously playing on will be preserved
        if(e.getSource() == newGame || e.getSource() == pickLevel) {
            //Reset the game each time a particular level is selected by invoking newGame(),
            //This will allow user to switch LEVEL_ITEMS mid-game

            //Must stop timer for a fresh timer
            controller.stopTimer();

            if(pickLevel.getSelectedItem().toString().equals("Easy")) {
                application.createNewGame(Constants.EASY_LEVEL);
            }

            else if(pickLevel.getSelectedItem().toString().equals("Medium")) {
                application.createNewGame(Constants.MEDIUM_LEVEL);
            }

            else if(pickLevel.getSelectedItem().toString().equals("Hard")) {
                application.createNewGame(Constants.HARD_LEVEL);
            }
        }
    }

    /**
     * The following classes specify a specific action to be
     * handled. To accomodate that action, a method from controller
     * is called accordingly (ex: controller.moveDown in response
     * to a DownAction)
     *
     * The source of this code has been provided above
     */

    private class DownAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            controller.moveDown();
            repaint();
        }
    }

    private class LeftAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            controller.moveLeft();
            repaint();
        }
    }

    private class RightAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            controller.moveRight();
            repaint();
        }
    }

    private class RotateCWAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            controller.rotateCW();
            repaint();
        }
    }

    private class RotateCCWAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            controller.rotateCCW();
            repaint();
        }
    }


}
