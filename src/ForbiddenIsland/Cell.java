package ForbiddenIsland;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

import java.awt.*;

// Represents a single square of the game area
public class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at 
    // the top-left corner of the screen
    int x;
    int y;
    // the four adjacent cells to this one
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    //constructor
    Cell(double height, int x, int y, boolean isFlooded, Cell left, Cell top,
         Cell right, Cell bottom) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.isFlooded = isFlooded;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    // constructor
    Cell(double height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.isFlooded = false;
        this.left = null;
        this.right = null;
        this.top = null;
        this.bottom = null;
    }

    //(R-G-B)
    public WorldImage cellImage(int waterHeight) {
        if (this.isFlooded) {
            return new RectangleImage(10, 10, OutlineMode.SOLID,
                    new Color(0, 0, (int)(253 - (waterHeight - this.height) * 5)));
        }
        else if (this.height <= waterHeight) {
            return new RectangleImage(10, 10, OutlineMode.SOLID,
                    new Color(
                            (int)Math.min(100,
                                    200 - (waterHeight - this.height) * 7),
                            70 - waterHeight * 2, 0));
        }
        else {
            return new RectangleImage(10, 10, OutlineMode.SOLID,
                    new Color(
                            (int)Math.min(253,
                                    (this.height - waterHeight) * 7),
                            (int)Math.min(253,
                                    200 + (this.height - waterHeight) * 6),
                            (int)Math.min(253,
                                    (this.height - waterHeight) * 7)));
        }
    }

    // EFFECT: update the left cell to the given left cell
    void updateLeft(Cell left) {
        this.left = left;
    }

    // EFFECT: update the right cell to the given right cell
    void updateRight(Cell right) {
        this.right = right;
    }

    // EFFECT: update the top cell to the given top cell
    void updateTop(Cell top) {
        this.top = top;
    }

    // EFFECT: update the bottom cell to the given bottom cell
    void updateBottom(Cell bottom) {
        this.bottom = bottom;
    }

    // return true if the cell is surrounded by water
    boolean waterSurround() {
        return !this.isFlooded &&
                (this.left.isFlooded ||
                        this.right.isFlooded ||
                        this.top.isFlooded ||
                        this.bottom.isFlooded);
    }

    // EFFECT: update the cell(isFlood, left, right, top and bottom)
    void flood(int waterHeight) {
        if (this.height <= waterHeight) {
            this.isFlooded = true;
            if (!this.top.isFlooded) {
                this.top.flood(waterHeight);
            }
            if (!this.bottom.isFlooded) {
                this.bottom.flood(waterHeight);
            }
            if (!this.left.isFlooded) {
                this.left.flood(waterHeight);
            }
            if (!this.right.isFlooded) {
                this.right.flood(waterHeight);
            }
        }
    }
}