package it.unibo.jetpackjoyride.map;

import it.unibo.jetpackjoyride.core.map.impl.MapBackgroundImpl;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Test class for MapBackgroundImpl.
 * @author yukai.zhou@studio.unibo.it
 */
final class MapBackgroundImplTest extends ApplicationTest {
    private static final int EXPECTED_DEFALUT_MOVE_SPEED = 5;
    private static final int POSITION = 0;
    private static final int SIZE = 1;

    private static final int WIDTH_FOR_TEST = 1600;
    private static final int HEIGHT_FOR_TEST = 800;
    private static final int WIDTH_DEFALUT = 800;
    private static final int HEIGHT_DEFALUT = 600;
    private MapBackgroundImpl mapBackground;


    @Override
    public void start(final Stage stage) {
        mapBackground = new MapBackgroundImpl();
        final Pane root = new Pane();
        stage.setScene(new Scene(root, WIDTH_DEFALUT, HEIGHT_DEFALUT));
        stage.show();
    }

    /**
     * Test whether the background is resizable with GameInfo.
     */
    @Test
    void isRisizebleWithGameInfo() {
        interact(() -> {
            final GameInfo gameInfo = GameInfo.getInstance();
            final Pair<Double, Double> size = mapBackground.getModelData().get(SIZE);
            mapBackground.updateBackground();
            assertNotNull(size, "The size should not be null");
            assertEquals(size, new Pair<>(gameInfo.getScreenWidth(), gameInfo.getScreenHeight()));

            gameInfo.updateInfo(WIDTH_FOR_TEST, HEIGHT_FOR_TEST);
            mapBackground.updateBackground();
            final Pair<Double, Double> size1 = mapBackground.getModelData().get(SIZE);
            assertEquals(WIDTH_FOR_TEST, size1.get1());
            assertEquals(HEIGHT_FOR_TEST, size1.get2());

        });
    }

    /**
     * Test updating background.
     */
    @Test
    void testUpdateBackground() {
        interact(() -> {
            double x1 = mapBackground.getModelData().get(POSITION).get1();
            double x2 = mapBackground.getModelData().get(POSITION).get2();
            mapBackground.updateBackground();
            assertEquals(x1 - GameInfo.MOVE_SPEED.get(), mapBackground.getModelData().get(POSITION).get1());
            assertEquals(x2 - GameInfo.MOVE_SPEED.get(), mapBackground.getModelData().get(POSITION).get2());

            x1 = mapBackground.getModelData().get(POSITION).get1();
            x2 = mapBackground.getModelData().get(POSITION).get2();
            assertTrue(x1 < x2 ? x1 + mapBackground.getModelData().get(SIZE).get1() >= x2 
                        : x2 + mapBackground.getModelData().get(SIZE).get1() >= x1);
        });
    }

    /**
     * Test resetting.
     */
    @Test
    void testReset() {
        interact(() -> {
            GameInfo.MOVE_SPEED.set(10);
            assertEquals(10, GameInfo.MOVE_SPEED.get());
            mapBackground.reset();
            assertEquals(EXPECTED_DEFALUT_MOVE_SPEED, GameInfo.MOVE_SPEED.get());
        });
    }

    /**
     * Test getting position X.
     */
    @Test
    void testGetPosX() {
        final Pair<Double, Double> posX = mapBackground.getModelData().get(POSITION);
        assertNotNull(posX, "The position X should not be null");
    }

    /**
     * Test getting size.
     */
    @Test
    void testGetSize() {
        final GameInfo gameInfo = GameInfo.getInstance();
        final Pair<Double, Double> size = mapBackground.getModelData().get(SIZE);
        mapBackground.updateBackground();
        assertNotNull(size, "The size should not be null");
        assertEquals(size, new Pair<>(gameInfo.getScreenWidth(), gameInfo.getScreenHeight())); 
    }
}
