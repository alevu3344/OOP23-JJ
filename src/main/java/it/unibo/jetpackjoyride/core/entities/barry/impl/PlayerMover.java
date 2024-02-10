package it.unibo.jetpackjoyride.core.entities.barry.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

import javax.sound.midi.Soundbank;

import it.unibo.jetpackjoyride.core.entities.barry.api.Barry;
import it.unibo.jetpackjoyride.core.entities.barry.api.Barry.BarryStatus;
import it.unibo.jetpackjoyride.core.hitbox.impl.PlayerHitbox;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The PlayerMover class is responsible for managing the movement of the player character (Barry).
 * It handles updating the player's model and view based on user input and game logic.
 */
public class PlayerMover {
    private GameInfo gameInfo = GameInfo.getInstance();
    private Barry model;
    private BarryView view;
    private Map<BarryStatus, List<Image>> statusMap = new HashMap<>();
   

    private final Map<BarryStatus, Integer> framesPerAnimation = new HashMap<>() {
        {
            put(BarryStatus.WALKING, 4);
            put(BarryStatus.BURNED, 4);
            put(BarryStatus.ZAPPED, 4);
            put(BarryStatus.FALLING, 2);
            put(BarryStatus.PROPELLING, 2);
            put(BarryStatus.HEAD_DRAGGING, 2);
        }
    };

    /**
     * Constructs a new PlayerMover instance.
     */
    public PlayerMover() {
        this.model = new BarryImpl();
        this.buildMap();

        this.view = new BarryView(this.getSpritesForStatus());
    }

    /**
     * Builds the status map containing lists of images for each BarryStatus.
     */
    private void buildMap() {
        for (var entry : framesPerAnimation.entrySet()) {
            List<Image> images = new ArrayList<>();
            for (int i = 0; i < entry.getValue(); i++) {
                String imagePath = getClass().getClassLoader()
                        .getResource("sprites/entities/player/barry" + entry.getKey().toString() + (i + 1) + ".png")
                        .toExternalForm();

                images.addAll(Collections.nCopies(7, new Image(imagePath)));
            }
            this.statusMap.put(entry.getKey(), new ArrayList<>(images));
        }
    }

    /**
     * Retrieves the list of sprites for the current BarryStatus.
     * 
     * @return The list of sprites for the current BarryStatus.
     */
    private List<Image> getSpritesForStatus() {
        return this.statusMap.get(this.model.getBarryStatus());
    }

    /**
     * Moves the player character based on the given input.
     * 
     * @param pressed Indicates whether the movement input is pressed.
     */
    public void move(boolean pressed) {
       
        this.model.move(pressed);
       
    }

    /**
     * Updates the view of the player character.
     * 
     * @param root The root pane to which the player character's view will be added.
     */
    public void updateView(Pane root) {
        this.view.update(model);
        this.view.setCurrentImages(this.getSpritesForStatus(), this.model.getBarryStatus());
        if (!root.getChildren().contains((Node)this.view.getImageView())) {
            root.getChildren().add((Node)this.view.getImageView());
    
        }
        if(this.model.hasShield()){
            if (!root.getChildren().contains((Node)this.view.getShieldImageView())) {
                root.getChildren().add((Node)this.view.getShieldImageView());
            }
        }
    }

    /**
     * Retrieves the model of the player character.
     * 
     * @return The model of the player character.
     */
    public Barry getBarryModel() {
        return this.model;
    }

    /**
     * Retrieves the hitbox of the player character.
     * 
     * @return The hitbox of the player character.
     */
    public PlayerHitbox getHitbox() {
        return this.model.getHitbox();
    }
}
