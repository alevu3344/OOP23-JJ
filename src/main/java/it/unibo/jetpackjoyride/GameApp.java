package it.unibo.jetpackjoyride;

import it.unibo.jetpackjoyride.menu.menus.api.GameMenu;
import it.unibo.jetpackjoyride.menu.menus.impl.StartMenu;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The GameApp class is the main class of the Jetpack Joyride game application.
 * It extends the JavaFX Application class and initializes the game by creating a start menu.
 */
public class GameApp extends Application {
    /**
     * Start method to start the Jetpack Joyride Game application.
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final GameMenu gameMenu = new StartMenu(primaryStage);
        primaryStage.setTitle("JetPack Joyride");
        gameMenu.showMenu();
        primaryStage.show();
    }

}
