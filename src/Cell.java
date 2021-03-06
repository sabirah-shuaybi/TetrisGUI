import java.awt.Color;
/**
 * Cell class defines the smallest unit of a TetrisShape
 * (Ex: a TetrisShape consists of 4 cells)
 *
 * NOTE: Cell is an immutable object. (This is intentional)
 * Thus, there are no setters, only getters.
 * To change a cell object, the old one is deleted and
 * replaced by a new object with updated fields
 *
 * The @Override methods were generated by the IDE
 *
 * @author Sabirah Shuaybi
 */
public class Cell {
    private int x;
    private int y;
    private Color cellColor;

    //There are two separate constructors for varying inputs
    //(As there are times when we don't care about a cell's color, only its location)
    public Cell(int x, int y) {
        this(x, y, Color.black);
    }

    //A cell object is made up of three entities:
        //an x coordinate, a y coordinate and a color
    public Cell(int x, int y, Color cellColor) {
        this.x = x;
        this.y = y;
        this.cellColor = cellColor;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Color getColor() {
        return cellColor;
    }

    /**
     * To define an equality operator for Cell object
     * we must state that a Cell if equal to another Cell
     * if and only if its x coordinate is equal to that of
     * the other cell's x coordinate AND its y coordinate is
     * equal to that of the other cell's y coordinate.
     *
     * NOTE: A cell's color is not factored into this
     * definition of equality. Equal cells are equal do
     * to their identical x and y locations, regardless of
     * their color.
     *
     * Must override equals method in parent class, Object,
     * because otherwise, it would always return false when
     * comparing two Cells with the same x and y coordinate
     * (since references would be different)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (x != cell.x) return false;
        return y == cell.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

