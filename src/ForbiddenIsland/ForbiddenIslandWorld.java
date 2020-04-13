package ForbiddenIsland;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;

import java.awt.*;
import java.util.Queue;
import java.util.*;

// represents forbidden island world
public class ForbiddenIslandWorld extends World {
    // the size of the island   
    static final int ISLAND_SIZE = 64;
    // the number of part player needs to pick up
    static final int HOW_MANY_PART = 4;
    // the size of the random range, the lower the smaller
    static final int TERRAIN_NUM = 4;
    // to adjust the number got randomly
    static final int TERRAIN_NUM2 = 2;
    // maximum height of the cells
    static final double MOUNTAIN_HEIGHT = 32.0;
    // the speed of water rise
    static final int SPEED = 5;

    // All the cells of the game, including the ocean
    IList<Cell> board;
    // the current height of the ocean
    int waterHeight;
    // for every count ticks the water rise 1 
    int count;
    // how many parts of the tools the has player collected
    int partsPicked;
    // represent a random number
    Random rand;
    // represent a player
    Player player;
    // represent a helicopter;
    Helicopter helicopter;
    // represent a list of tools the player need to collect
    IList<Target> parts;

    // to represent the world
    ForbiddenIslandWorld() {
        this.waterHeight = 0;
        this.count = 0;
        this.board = this.toList(
                this.makeCell(this.mountainHeight()));
        this.partsPicked = 0;
        this.rand = new Random();
        this.player = this.createPlayer();
        this.helicopter = this.createHelicopter();
        this.parts = this.createParts();
    }

    // create a new player
    Player createPlayer() {
        return new Player(this.randomCell());
    }

    // create a list of targets
    IList<Target> createParts() {
        IList<Target> parts = new MtList<Target>();
        for (int index = 0; index < HOW_MANY_PART; index += 1) {
            parts = parts.add(new Target(this.randomCell()));
        }
        return parts;
    }

    // make the image of the helicopter at the highest point on the island
    Helicopter createHelicopter() {
        return new Helicopter(this.tallestCell());
    }

    // produce a random cell which is over water level
    public Cell randomCell() {
        IList<Cell> land = this.land();
        int index = this.rand.nextInt(land.size());
        Iterator<Cell> landiter = land.iterator();
        Cell c = landiter.next();
        index -= 1;
        while (index >= 0) {
            Cell cur = landiter.next();
            if (index == 0) {
                c = cur;
            }
            index -= 1;
        }
        return c;
    }

    // produce the highest cell of the island
    public Cell tallestCell() {
        IList<Cell> land = this.land();
        double acc = 0.0;
        int index = land.size();
        Iterator<Cell> landiter = land.iterator();
        Cell c = landiter.next();
        index -= 1;
        while (index > 0) {
            Cell cur = landiter.next();
            if (cur.height >= acc) {
                acc = cur.height;
                c = cur;
            }
            index -= 1;
        }
        return c;
    }

    // produce the board of the cells which are over water level
    IList<Cell> land() {
        Iterator<Cell> iter = this.board.iterator();
        IList<Cell> result = new MtList<Cell>();
        while (iter.hasNext()) {
            Cell next = iter.next();
            if (!next.isFlooded &&
                    (!next.left.isFlooded ||
                            !next.right.isFlooded ||
                            !next.bottom.isFlooded ||
                            !next.top.isFlooded)) {
                result = result.add(next);
            }
        }
        return result;
    }

    // add the given list of cell in the list of cell
    IList<Cell> toList(ArrayList<ArrayList<Cell>> cell) {
        IList<Cell> result = new MtList<Cell>();
        for (int col = 0; col < ISLAND_SIZE + 1; col += 1) {
            for (int row = 0; row < ISLAND_SIZE + 1; row += 1) {
                result = result.add(cell.get(col).get(row));
            }
        }
        return result;
    }

    // create a regular mountain
    ArrayList<ArrayList<Double>> mountainHeight() {
        ArrayList<ArrayList<Double>> result =
                new ArrayList<ArrayList<Double>>(ISLAND_SIZE + 1);
        Posn center = new Posn(ISLAND_SIZE / 2, ISLAND_SIZE / 2);
        for (int col = 0; col < ISLAND_SIZE + 1; col += 1) {
            result.add(new ArrayList<Double>());
            for (int row = 0; row < ISLAND_SIZE + 1; row += 1) {
                result.get(col).add(MOUNTAIN_HEIGHT - (Math.abs(center.x - row)
                        + Math.abs(center.y - col)));
            }
        }
        return result;
    }

    // create the height of a random mountain
    ArrayList<ArrayList<Double>> randomHeight() {
        ArrayList<ArrayList<Double>> result =
                new ArrayList<ArrayList<Double>>(ISLAND_SIZE + 1);
        double num;
        Posn center = new Posn(ISLAND_SIZE / 2, ISLAND_SIZE / 2);
        for (int col = 0; col < ISLAND_SIZE + 1; col += 1) {
            result.add(new ArrayList<Double>());
            for (int row = 0; row < ISLAND_SIZE + 1; row += 1) {
                num = MOUNTAIN_HEIGHT - (Math.abs(center.x - row)
                        + Math.abs(center.y - col));
                if (num <= 0.0) {
                    result.get(col).add(0.0);
                }
                else {
                    result.get(col).add((double)rand.nextInt(31) + 1);
                }
            }
        }
        return result;
    }

    // create heights of a random terrain
    ArrayList<ArrayList<Double>> randomTerrain() {
        int halfSize = ISLAND_SIZE / 2;
        ArrayList<ArrayList<Double>> result =
                new ArrayList<ArrayList<Double>>();
        for (int col = 0; col <= ISLAND_SIZE + 1; col += 1) {
            result.add(new ArrayList<Double>());
            for (int row = 0; row <= ISLAND_SIZE + 1; row += 1) {
                result.get(col).add(row, 0.0);
            }
        }
        // set the height of the center
        result.get(halfSize).set(halfSize, MOUNTAIN_HEIGHT);

        // set the height of the center of four sides
        result.get(halfSize).set(0, 1.0);
        result.get(halfSize).set(ISLAND_SIZE, 1.0);
        result.get(0).set(halfSize, 1.0);
        result.get(ISLAND_SIZE).set(halfSize, 1.0);

        // set the heights recursively
        randomTerrainHelp(result, 0, halfSize, 0, halfSize);
        randomTerrainHelp(result, 0, halfSize, halfSize, ISLAND_SIZE);
        randomTerrainHelp(result, halfSize, ISLAND_SIZE, 0, halfSize);
        randomTerrainHelp(result, halfSize, ISLAND_SIZE, halfSize, ISLAND_SIZE);
        return result;
    }

    // the recursive part of setting randomTerrain heights
    // EFFECT: update the ForbiddenIsland.ForbiddenIslandWorld
    void randomTerrainHelp(ArrayList<ArrayList<Double>> arr,
                           int tx, int bx, int ty, int by) {
        // the side length 
        int s = bx - tx;
        if (s >= 2) {

            // the middle point
            int mx = (tx + bx) / 2;
            int my = (ty + by) / 2;

            // the cells at corner
            double tl = arr.get(tx).get(ty);
            double tr = arr.get(bx).get(ty);
            double bl = arr.get(tx).get(by);
            double br = arr.get(bx).get(by);

            // produce two random numbers
            double randNum1 = this.rand.nextInt(TERRAIN_NUM)
                    - (TERRAIN_NUM2);
            double randNum2 = this.rand.nextInt(TERRAIN_NUM)
                    - (TERRAIN_NUM2);

            // compute the center of four sides
            double t = (randNum1 * s / 2) + ((tl + tr) / 2);
            double b = (randNum1 * s / 2) + ((bl + br) / 2);
            double l = (randNum1 * s / 2) + ((tl + bl) / 2);
            double r = (randNum1 * s / 2) + ((tr + br) / 2);

            // compute the center 
            double m = (randNum2 * s / 3) + ((tl + tr + bl + br) / 4);

            // set the center of four sides
            arr.get(mx).set(ty, t);
            arr.get(mx).set(by, b);
            arr.get(tx).set(my, l);
            arr.get(bx).set(my, r);

            // set the center 
            arr.get(mx).set(my, m);

            // do the recursion
            randomTerrainHelp(arr, tx, mx, ty, my);
            randomTerrainHelp(arr, tx, mx, my, by);
            randomTerrainHelp(arr, mx, bx, ty, my);
            randomTerrainHelp(arr, mx, bx, my, by);
        }
    }

    // add the given height the list of cell
    ArrayList<ArrayList<Cell>> makeCell(ArrayList<ArrayList<Double>> height) {
        ArrayList<ArrayList<Cell>> result =
                new ArrayList<ArrayList<Cell>>(ISLAND_SIZE + 1);
        for (int col = 0; col < ISLAND_SIZE + 1; col += 1) {
            result.add(new ArrayList<Cell>());
            for (int row = 0; row < ISLAND_SIZE + 1; row += 1) {
                if (height.get(col).get(row) > 0) {
                    result.get(col).add(
                            new Cell(height.get(col).get(row).intValue(),
                                    row, col));
                }
                else {
                    result.get(col).add(new OceanCell(0, row, col));
                }
            }
        }
        this.updateSurroundCells(result);
        return result;
    }

    // the on key event
    // EFFECT: update the on key event to the given string
    public void onKeyEvent(String k) {
        if (k.equals("m")) {
            this.count = 0;
            this.waterHeight = 0;
            this.board = this.toList(
                    this.makeCell(this.mountainHeight()));
            this.player = this.createPlayer();
            this.helicopter = this.createHelicopter();
            this.partsPicked = 0;
            this.parts = createParts();
        }
        if (k.equals("r")) {
            this.count = 0;
            this.waterHeight = 0;
            this.board = this.toList(
                    this.makeCell(this.randomHeight()));
            this.player = this.createPlayer();
            this.helicopter = this.createHelicopter();
            this.partsPicked = 0;
            this.parts = createParts();
        }
        if (k.equals("t")) {
            this.count = 0;
            this.waterHeight = 0;
            this.board = this.toList(
                    this.makeCell(this.randomTerrain()));
            this.player = this.createPlayer();
            this.helicopter = this.createHelicopter();
            this.partsPicked = 0;
            this.parts = createParts();
        }
        if (k.equals("left") && !this.player.pos.left.isFlooded) {
            this.player.updateCell(this.player.pos.left);
            this.parts = this.parts.pickUp(this.player.pos);
            this.partsPicked = HOW_MANY_PART - this.parts.size();
        }
        if (k.equals("up") && !this.player.pos.top.isFlooded) {
            this.player.updateCell(this.player.pos.top);
            this.parts = this.parts.pickUp(this.player.pos);
            this.partsPicked = HOW_MANY_PART - this.parts.size();
        }
        if (k.equals("down") && !this.player.pos.bottom.isFlooded) {
            this.player.updateCell(this.player.pos.bottom);
            this.parts = this.parts.pickUp(this.player.pos);
            this.partsPicked = HOW_MANY_PART - this.parts.size();
        }
        if (k.equals("right") && !this.player.pos.right.isFlooded) {
            this.player.updateCell(this.player.pos.right);
            this.parts = this.parts.pickUp(this.player.pos);
            this.partsPicked = HOW_MANY_PART - this.parts.size();
        }
        else {
            return;
        }
    }

    // EFFECT: water rise 1, every tick
    public void onTick() {
        this.count += 1;
        if (this.count == SPEED) {
            this.count = 0;
            this.waterHeight += 1;
        }
        this.flooding();
    }

    // return a list of cell if the next cell is surround by water
    public IList<Cell> coast() {
        IList<Cell> mtCoast = new MtList<Cell>();
        Iterator<Cell> iter = this.board.iterator();

        while (iter.hasNext()) {
            Cell next = iter.next();
            if (next.waterSurround()) {
                mtCoast = mtCoast.add(next);
            }
        }
        return mtCoast;
    }

    // EFFECT: update the next cell to the next coast cell
    public void flooding() {
        Iterator<Cell> iter = this.coast().iterator();

        while (iter.hasNext()) {
            Cell next = iter.next();
            next.flood(this.waterHeight);
        }
    }

    // EFFECT: update the surround four cells
    public void updateSurroundCells(ArrayList<ArrayList<Cell>> cells) {
        for (int index = 0; index < ISLAND_SIZE + 1; index += 1) {
            for (int index2 = 0; index2 < ISLAND_SIZE + 1; index2 += 1) {
                Cell curCell = cells.get(index).get(index2);

                if (index2 < ISLAND_SIZE) {
                    curCell.updateRight(cells.get(index).get(index2 + 1));
                }
                else {
                    curCell.updateRight(curCell);
                }
                if (index2 > 0) {
                    curCell.updateLeft(cells.get(index).get(index2 - 1));
                }
                else {
                    curCell.updateLeft(curCell);
                }
                if (index > 0) {
                    curCell.updateTop(cells.get(index - 1).get(index2));
                }
                else {
                    curCell.updateTop(curCell);
                }
                if (index < ISLAND_SIZE) {
                    curCell.updateBottom(cells.get(index + 1).get(index2));
                }
                else {
                    curCell.updateBottom(curCell);
                }
            }
        }
    }

    // background with player and helicopter pieces??
    public WorldScene makeImage(WorldScene bg) {
        bg = this.player.playerImage(this.targetImage(this.boardImage(bg)));
        return bg;
    }

    // add the helicopter pieces image on the background
    public WorldScene targetImage(WorldScene bg) {
        Iterator<Target> iter = this.parts.iterator();
        while (iter.hasNext()) {
            bg = iter.next().image(bg);
        }
        bg = this.helicopter.image(bg);
        return bg;
    }

    // create board image
    public WorldScene boardImage(WorldScene bg) {
        Iterator<Cell> iter = this.board.iterator();
        while (iter.hasNext()) {
            Cell next = iter.next();
            bg.placeImageXY(next.cellImage(this.waterHeight), next.x * 10 + 5, next.y * 10 + 5);
        }
        return bg;
    }

    // background 
    public WorldScene makeScene() {
        WorldScene bg = new WorldScene(650, 650);
        return this.makeImage(bg);
    }

    // represents the end of the world
    public WorldEnd worldEnds() {
        if (this.player.pos == this.helicopter.pos
                && this.player.pos == this.helicopter.pos
                && !this.parts.isCons()) {
            WorldScene w = new WorldScene(650, 650);
            w = this.makeImage(w);
            w.placeImageXY(new TextImage("Yeah!", 70, Color.RED), 320, 300);
            return new WorldEnd(true, w);
        }
        if (this.player.pos.isFlooded) {
            WorldScene w = new WorldScene(650, 650);
            w = this.makeImage(w);
            w.placeImageXY(new TextImage("Game Over!", 70, Color.RED), 320, 300);
            return new WorldEnd(true, w);
        }
        else {
            return new WorldEnd(false, this.makeImage(new WorldScene(650, 650)));
        }
    }
}