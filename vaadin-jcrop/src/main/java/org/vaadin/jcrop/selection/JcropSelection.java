package org.vaadin.jcrop.selection;

/**
 * holds the values of the selectionChanged event<br>
 *
 * @author Marten Prieß (http://www.non-rocket-science.com)
 * @version 1.0
 */
public class JcropSelection {

    private int x, y, width, height;
    private boolean released = false;

    public JcropSelection(int x, int y, int width, int height, boolean released) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.released = released;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isReleased() {
        return released;
    }

    @Override
    public String toString() {
        return "JcropSelection{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", released=" + released +
                '}';
    }
}
