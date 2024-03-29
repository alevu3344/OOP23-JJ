package it.unibo.jetpackjoyride.core.map.impl;

import it.unibo.jetpackjoyride.core.map.api.MapBackgroundModel;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;

/**
 * Implementation of the MapBackground interface.
 * This class provides functionality to control the background model of the game.
 * @author yukai.zhou@studio.unibo.it
 */
public final class MapBackgroundModelImpl implements MapBackgroundModel {

    private static final int IMAGE_STEP = 2;
    private static final int TOTAL_IMAGE = 6;
    private static final int OFFSET = 2;

    /**
     * the backgroung position and size.
     */
     private double bgImageX1, bgImageX2;
     private double mapWidth;
     private double mapHeight;
     private int currentImage;

    /**
     * Constructor of the MapBackgroundModelImpl.
     */
     public MapBackgroundModelImpl() {
        final GameInfo gameInfo = GameInfo.getInstance();
        mapHeight = gameInfo.getScreenHeight();
        mapWidth = gameInfo.getScreenWidth();
        bgImageX1 = 0;
        bgImageX2 = this.mapWidth;
    }

    @Override
    public void updateBackgroundModel() {
        bgImageX1 -= GameInfo.MOVE_SPEED.get();
        bgImageX2 -= GameInfo.MOVE_SPEED.get();
    }

    @Override
    public Pair<Double, Double> getPosX() {
        return new Pair<>(this.bgImageX1, this.bgImageX2);
    }

    @Override 
    public void setPositionX(final int num) {
        if (num == 0) {
            this.bgImageX1 = bgImageX2 + mapWidth - OFFSET;
        } else {
            this.bgImageX2 = bgImageX1 + mapWidth - OFFSET;
        }
    }

    @Override
    public Pair<Double, Double> getSize() {
        return new Pair<>(this.mapWidth, this.mapHeight);
    }

    @Override
    public void reset() {
        this.bgImageX1 = 0;
        this.bgImageX2 = mapWidth;
    }

    @Override
    public void updateIndexForImage() {
        this.currentImage = (currentImage + IMAGE_STEP) % TOTAL_IMAGE;
    }

    @Override
    public int getIndexForImage() {
        return this.currentImage;
    }

    @Override
    public void setMapSize(final double width, final double height) {
        this.mapWidth = width;
        this.mapHeight = height;
    }

    @Override
    public void correctBackgroundPos(final double widthRatio, final double widthRatio1) {
        this.bgImageX1 = this.mapWidth * widthRatio;
        this.bgImageX2 = this.mapWidth * widthRatio1;
    }
}
