package it.unibo.jetpackjoyride.core.handler.entity;

import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp.PickUpType;
import it.unibo.jetpackjoyride.core.entities.powerup.api.PowerUp.PowerUpType;
import it.unibo.jetpackjoyride.core.handler.obstacle.ObstacleHandler;
import it.unibo.jetpackjoyride.core.handler.pickup.PickUpHandler;
import it.unibo.jetpackjoyride.core.handler.powerup.PowerUpHandler;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController.Items;

import java.util.*;
import javafx.scene.Group;

public class EntityHandler {
    private ObstacleHandler obstacleHandler;
    private PowerUpHandler powerUpHandler;
    private PickUpHandler pickUpHandler;

    private Set<Items> unlockedPowerUps;

    private Event eventHappening;
    private boolean isUsingPowerUp;
    private Integer counter;

    public enum Event {
        BARRYHIT, POWERUPHIT, NONE, POWERUPSPAWNED, PICKUPPICKEDUP
    }


    public void initialize(final Set<Items> unlockedPowerUps) {
        this.obstacleHandler = new ObstacleHandler();
        this.powerUpHandler = new PowerUpHandler();
        this.pickUpHandler = new PickUpHandler();

        this.unlockedPowerUps = unlockedPowerUps;

        this.obstacleHandler.initialize();
        this.isUsingPowerUp = false;
        this.counter = 0;
    }

    public Event update(final Group entityGroup, final Hitbox playerHitbox, final boolean isSpaceBarPressed) {
        this.eventHappening = Event.NONE;

        if(!this.isUsingPowerUp && this.counter % 500 == 0) {//Every 500m spawns a pickUp if Barry is not using a powerUp
            this.spawnPickUp(PickUpType.VEHICLE);
        }

        if(this.obstacleHandler.update(entityGroup, isUsingPowerUp ? this.powerUpHandler.getAllPowerUps().get(0).getEntityModel().getHitbox() : playerHitbox)) {
            this.eventHappening = isUsingPowerUp ? Event.POWERUPHIT : Event.BARRYHIT;
            if(this.isUsingPowerUp) {
                this.powerUpHandler.destroyAllPowerUps();
                this.isUsingPowerUp = false;
            }
        }

        this.powerUpHandler.update(entityGroup, isSpaceBarPressed);

        if(this.pickUpHandler.update(entityGroup, playerHitbox)) {
            this.isUsingPowerUp = true;
            this.eventHappening = Event.PICKUPPICKEDUP;
            PickUp pickUpPickedUp = this.pickUpHandler.getAllPickUps().get(0).getEntityModel();
            switch (pickUpPickedUp.getPickUpType()) {
                case VEHICLE:
                    if(!this.spawnPowerUp(this.powerUpSpawnerHelper(this.unlockedPowerUps))) {
                        this.isUsingPowerUp = false;
                    }
                    break;
                default:
                    break;
            }
        }

        this.counter++;
        return this.eventHappening;
    }

    private Optional<PowerUpType> powerUpSpawnerHelper(final Set<Items> unlockedItems) {
        if(unlockedItems.isEmpty()) {
            return Optional.empty();
        }

        final Random random = new Random(unlockedItems.size());
        Integer i=0;
        for(var powerUpType : unlockedItems) {
            if(i.equals(random.nextInt())) {
                return powerUpType.getCorresponding();
            }
        }

        return Optional.empty();
    }

    public void spawnPickUp(final PickUpType pickUpType) {
        this.pickUpHandler.spawnPickUp(pickUpType);
    }

    public boolean spawnPowerUp(final Optional<PowerUpType> powerUpType) {
        if(powerUpType.isPresent()) {
            this.powerUpHandler.spawnPowerUp(powerUpType.get());
            return true;
        }
        return false;
    }

    public void stop() {
        this.obstacleHandler.over();
    }

    public void start(){
        this.obstacleHandler.start();
    }

    public void reset(){
        this.obstacleHandler.deactivateAllObstacles();
    }

}
