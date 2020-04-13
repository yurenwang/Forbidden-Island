package ForbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.FromFileImage;

// to represent a helicopterTarget
public class Helicopter extends Target {

    // constructor
    Helicopter(Cell cur) {
        super(cur);
    }

    // place the helicopter image on the background
    public WorldScene image(WorldScene bg) {
        bg.placeImageXY(new FromFileImage("helicopter.png"), this.x * 10 + 5, this.y * 10 + 5);
        return bg;
    }
}