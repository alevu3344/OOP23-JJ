package it.unibo.jetpackjoyride.core.entities.coin.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import it.unibo.jetpackjoyride.core.entities.coin.api.Coin;
import it.unibo.jetpackjoyride.core.entities.coin.api.CoinShapeFactory;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.hitbox.impl.HitboxImpl;
import it.unibo.jetpackjoyride.core.statistical.impl.GameStats;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import java.util.Optional;

/**
 * The class is responsible for generating coins during gameplay.
 * @author yukai.zhou@studio.unibo.it
 */
public final class CoinGenerator {

    private static final int POSITION = 0;
    private static final int MAX_REUSABLE_COINS = 50;
    private static final double PROBABILITY_BASE = 0.3;
    private static final double PROBABILITY_RATE = 0.15;
    private static final int COIN_WIDTH = 30;
    private static final int COIN_HEIGHT = 30;

    private final Canvas canvas;
    private final Timeline timeline;
    private final List<Coin> coinList = new ArrayList<>();
    private final List<Coin> reusableCoin = new ArrayList<>();
    private final CoinShapeFactory coinShapeFactory;
    private final GameInfo gameInfo;
    private final int initialSpeed = GameInfo.MOVE_SPEED.get();
    private final Random random = new Random();

    private Optional<Hitbox> playeHitbox;

     /**
     * Constructor of the CoinGenerator .
     *
     * @param playeHitbox    the hitbox use to check collision
     */
    public CoinGenerator(final Optional<Hitbox> playeHitbox) {
        this.gameInfo = GameInfo.getInstance();
        this.playeHitbox = playeHitbox;
        this.canvas = new Canvas(gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
        this.coinShapeFactory = new CoinShapeFactoryImpl();
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> generateCoin()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the generation of coins.
     */
    public void startGenerate() {
        timeline.play();
    }

    /**
     * Stops the generation of coins.
     */
    public void stopGenerate() {
        timeline.stop();
    }

    /**
     * Cleans up the generated coins and Canvas.
     */
    public void clean() {
        this.coinList.clear();
        this.canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Sets actual the hitbox of the player.
     *
     * @param playerHitbox the hitbox of the player
     */
    public void setPlayerHitbox(final Optional<Hitbox> playerHitbox) {
         this.playeHitbox = playerHitbox;
    }

    /**
     * A method to add the canvas containing the coins into a Group.
     *
     * @param group The group which containing all elements
     */
    public void addCoinsView(final Group group) {
          group.getChildren().add(this.canvas);
    }

    /**
     * The method use to generating coins.
     */
    private void generateCoin() {
        if (generateOrNot()) {
            final List<Pair<Double, Double>> shapes = coinShapeFactory.regularShapes();
            for (final Pair<Double, Double> position : shapes) {
                Coin coin;
                if (!reusableCoin.isEmpty()) {
                    coin = reusableCoin.remove(0);
                    coin.setPosition(position);

                } else {
                    coin = new CoinImpl(position,
                            new HitboxImpl(position,
                            new Pair<>(Double.valueOf(COIN_WIDTH), Double.valueOf(COIN_HEIGHT)), 0.0));
                }
                coinList.add(coin);
            }
        }
    }

    private boolean generateOrNot() {
        final double probabilityInfluenBySpeed = 
        PROBABILITY_BASE + (GameInfo.MOVE_SPEED.get() - initialSpeed) * PROBABILITY_RATE;
        return random.nextDouble() < probabilityInfluenBySpeed;
    }
     /**
     * Update the coins model and View on the canvas.
     */
    public void updateCoin() {
        updatPosition();
        renderCoin();
    }

    /**
     * Renders the coins on the canvas.
     */
    private void renderCoin() {
        if (isScreenSizeChange()) {
            canvas.setHeight(gameInfo.getScreenHeight());
            canvas.setWidth(gameInfo.getScreenWidth());
            canvas.getGraphicsContext2D().clearRect(0, 0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
        }
        for (final Coin coin : coinList) {
            coin.render(canvas.getGraphicsContext2D());
        }
    }
/**
* Updates the position of the coins.
*/
    private void updatPosition() {
        if (this.playeHitbox.isPresent()) {
            checkCollision();
        }

        while (reusableCoin.size() > MAX_REUSABLE_COINS) {
            reusableCoin.remove(reusableCoin.size() - 1);
        }

        final Iterator<Coin> iterator = coinList.iterator();
        while (iterator.hasNext()) {
            final Coin coin = iterator.next();
            coin.updateModel();
            if (isOutofMap(coin.getModelData().get(POSITION).get1())) {
                reusableCoin.add(coin);
                coin.setCollectedState(false);
                iterator.remove();
            }
        }
    }

    /**
     * Checks if the screen size has changed.
     *
     * @return true if the screen size has changed, false otherwise
     */
    private boolean isScreenSizeChange() {
        return canvas.getWidth() != gameInfo.getScreenWidth() || canvas.getHeight() != gameInfo.getScreenHeight();
    }

    /**
     * Checks for collisions between coins and the player and 
     * only coins that cross half of the screen will be checked. 
     * If a collision occurs, updates the game statistics accordingly.
     */
    private void checkCollision() {
        final List<Coin> sortedList = coinList.stream()
                .filter(p -> p.getModelData().get(POSITION).get1() < gameInfo.getScreenWidth() / 2)
                .sorted(Comparator.comparingDouble(p -> p.getModelData().get(POSITION).get1()))
                .collect(Collectors.toList());

        for (final Coin coin : sortedList) {
             if (playeHitbox.isPresent()) {
                GameStats.updateCoins(coin.checkCollision(playeHitbox.get()));
             }
        }
    }

    /**
     * Checks if a coin is out of the visible area of the screen.
     *
     * @param x the x-coordinate of the coin
     * @return true if the coin is out of the visible area, false otherwise
     */
    private boolean isOutofMap(final double x) {
        return x < -gameInfo.getScreenWidth();
    }
}
