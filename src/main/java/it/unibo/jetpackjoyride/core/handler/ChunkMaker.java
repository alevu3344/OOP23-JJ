package it.unibo.jetpackjoyride.core.handler;

import java.util.*;

import javafx.scene.layout.Pane;

public interface ChunkMaker extends Runnable {
    List<ObstacleController> getControllers();
    void update(Pane root);
    void run();
    void start();
    void over();
} 