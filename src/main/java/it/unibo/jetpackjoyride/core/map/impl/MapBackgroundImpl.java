package it.unibo.jetpackjoyride.core.map.impl;

import java.util.List;

import it.unibo.jetpackjoyride.core.map.api.MapBackground;
import it.unibo.jetpackjoyride.core.map.api.MapBackgroundModel;
import it.unibo.jetpackjoyride.core.map.api.MapBackgroundView;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Implementation of the MapBackground interface.
 * This class provides functionality to control the background of the game.
 * @author yukai.zhou@studio.unibo.it
 */
public final class MapBackgroundImpl implements MapBackground {

    private static final int MAX_SPEED = 14;
    private static final int DEFAULT_SPEED = 5;
    private static final int DURATION_SECONDS = 15;
    private static final int BGIMAGE_ONE = 0;
    private static final int BGIMAGE_TWO = 1;
    private static final int TIMES_FOR_CHANGE = 2;
    private static final double EPSILON = 0.000_001;

    private final MapBackgroundModel model;
    private final MapBackgroundView view;
    private final Timeline timeline;
    private final GameInfo gameInfo;
    private int counter;

    /**
     * Constructor of the MapBackgroundImpl.
     */
    public MapBackgroundImpl() {
        model = new MapBackgroundModelImpl();
        view = new MapBackgroungViewImpl(this.getModelData());
        gameInfo = GameInfo.getInstance();
          this.timeline = new Timeline(new KeyFrame(Duration.seconds(DURATION_SECONDS), e -> {
            if (GameInfo.MOVE_SPEED.get() == MAX_SPEED) {
                counter++;
            } else {
                gameInfo.setMoveSpeed(GameInfo.MOVE_SPEED.incrementAndGet());
                counter++;
               // System.out.println(counter);
            }
        }));
    }

    @Override
    public void setMapOnGameRoot(final Pane root) {
            view.addNodeInRoot(root);
    }

    @Override
    public void updateBackground() {
        updateBackgroundModel();
        updateBackgroundView();
    }


    @Override
    public void reset() {
        if (GameInfo.MOVE_SPEED.get() != DEFAULT_SPEED) {
            gameInfo.setMoveSpeed(DEFAULT_SPEED);
        }
        model.reset();
        timeline.playFromStart();
    }

    @Override
    public List<Pair<Double, Double>> getModelData() {
         return List.of(model.getPosX(), model.getSize());
    }

     /**
     * Method that updates the background view.
     */
    private void updateBackgroundView() {
        this.view.updateBackgroundView(this.getModelData());
    }

   /**
     * Method that updates the background model.
     */
    private void updateBackgroundModel() { 
        if (!timeline.statusProperty().get().equals(Status.RUNNING)) {
            timeline.play();
        }
        if (counter == TIMES_FOR_CHANGE) {
               model.updateIndexForImage(); 
               counter = 0;
        }
        this.model.updateBackgroundModel();
        if (isOutofMap(model.getPosX().get1())) {
            model.setPositionX(BGIMAGE_ONE);
            if (view.isChange(BGIMAGE_ONE, model.getIndexForImage())) {
                view.changeImage(BGIMAGE_ONE, model.getIndexForImage());
            }
        }
        if (isOutofMap(model.getPosX().get2())) {
            model.setPositionX(BGIMAGE_TWO);
            if (view.isChange(BGIMAGE_TWO, model.getIndexForImage())) {
                view.changeImage(BGIMAGE_TWO, model.getIndexForImage());
            }
        }
        updateSize();
    } 

    private boolean isOutofMap(final double x) {
        return x <= -model.getSize().get1();
    }

     /**
     * Updates the size of the background based on the screen size.
     * If the screen size has changed, this method adjusts the background accordingly.
     */
    private void updateSize() {
        final double newWidth = gameInfo.getScreenWidth();
        final double newHeight = gameInfo.getScreenHeight();
        if (Math.abs(newWidth - model.getSize().get1()) > EPSILON 
        || Math.abs(newHeight - model.getSize().get2()) > EPSILON) {
            final double ratioX1 = model.getPosX().get1() / model.getSize().get1();
            final double ratioX2 = model.getPosX().get2() / model.getSize().get1();

            model.setMapSize(newWidth, newHeight);
            model.correctBackgroundPos(ratioX1, ratioX2);
        }
    }
}
