import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * This class is an encapsulation of the SWING timer. It is used to
 * perform an action at regularly defined time intervals, in this case,
 * making the shape fall (or move down) at a specified speed.
 * The speed of the shape depends on the level of difficulty.
 *
 * @author Sabirah Shuaybi
 */
public class TetrisTimer implements ActionListener {

    private TetrisController controller;
    private Timer timer;

    public TetrisTimer(TetrisController controller, int level) {
        this.controller = controller;
        timer = new Timer(getSpeed(level), this);

        //Delay the timer by one second at start of game
        timer.setInitialDelay(Constants.ONE_SECOND);
        timer.start();
    }

    /**
     * Determines the frequency of the timer based on
     * the level of difficulty passed in
     *
     * @return the speed (in milliseconds) of the timer
     * (in other words,how frequently the timer
     * will call actionPerformed
     */
    private int getSpeed(int level) {
        switch (level) {
            case Constants.HARD_LEVEL: return 500;
            case Constants.MEDIUM_LEVEL: return 1000;
            default: return 2000;
        }
    }

    /* Stops the timer (needed for restart game feature) */
    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Timer fired");
        controller.moveDown();
    }
}
