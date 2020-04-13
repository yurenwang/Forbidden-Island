package ForbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.CircleImage;
import javalib.worldimages.OutlineMode;

import java.awt.*;

// to represent a target
public class Target {
    Cell pos;
    int x;
    int y;

    // constructor
    Target(Cell pos) {
        this.pos = pos;
        this.x = this.pos.x;
        this.y = this.pos.y;
    }

    // world image with helicopter pieces
    public WorldScene image(WorldScene bg) {
        bg.placeImageXY(new CircleImage(6, OutlineMode.SOLID, Color.MAGENTA),
                this.x * 10 + 5, this.y * 10 + 5);
        return bg;
    }

    // return if the player picked up the given piece
    boolean isPicked(Cell player) {
        return (this.x == player.x) &&
                (this.y == player.y);
    }
}