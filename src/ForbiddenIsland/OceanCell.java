package ForbiddenIsland;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

import java.awt.*;

// represent ocean cell
public class OceanCell extends Cell {

    // constructor
    OceanCell(double height, int x, int y) {
        super(height, x, y);
        this.isFlooded = true;
    }

    // cell image
    public WorldImage cellImage(int waterHeight) {
        return new RectangleImage(10, 10, OutlineMode.SOLID, new Color(0, 0, 253));
    }
}