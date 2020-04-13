package ForbiddenIsland;

import javalib.impworld.WorldScene;
import javalib.worldimages.FromFileImage;

// to represent a player
public class Player {
    Cell pos;
    int x;
    int y;

    // constructor
    Player(Cell pos) {
        this.pos = pos;
        this.x = this.pos.x;
        this.y = this.pos.y;
    }

    // EFFECT: update the player coordinate(x, y which cell the player stays) 
    void updateCell(Cell newCell) {
        this.pos = newCell;
        this.x = this.pos.x;
        this.y = this.pos.y;
    }

    // player image
    public WorldScene playerImage(WorldScene bg) {
        bg.placeImageXY(new FromFileImage("pilot-icon.png"), this.x * 10 + 5,
                this.y * 10 + 5);
        return bg;
    }
}