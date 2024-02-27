package it.unibo.jetpackjoyride.core.entities.obstacle.impl;

import it.unibo.jetpackjoyride.core.entities.obstacle.api.AbstractObstacle;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.Obstacle;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.movement.Movement;
import java.util.function.Predicate;

/**
 * The {@link Laser} class defines one of the obstacles implemented
 * in the game. Since it extends {@link AbstractObstacle}, it inherits all
 * methods and behaviours of {@link Entity} and {@link Obstacle}.
 * Lasers are obstacles which stay active for a long period of time and given
 * their size, they limit a lot the movement of the player, but also take
 * some time to charge and allow for some quick repositioning as far as possible
 * from them.
 * 
 * @author gabriel.stira@studio.unibo.it
 */
public final class Laser extends AbstractObstacle {
    /**
     * Defines how long (in terms of lifetime) it will take for the laser to have
     * its status set to ACTIVE.
     * During this time, the laser will be in status CHARGING.
     */
    private static final Integer TIME_FOR_CHARGING = 100;
    /**
     * Defines how long the laser stays ACTIVE.
     * During this time, the laser will be in status ACTIVE.
     */
    private static final Integer LASER_DURATION = 150;
    /**
     * Defines how long it will take for the laser to have its status set to
     * INACTIVE.
     * During this time, the laser will be in status DEACTIVATED.
     */
    private static final Integer TIME_FOR_DECHARGING = 80;
    /**
     * Defines the X coordinate the laser is generated by default.
     */
    private static final Double SPAWNING_X_COORDINATE = 640.0;

    /**
     * Constructor used to create an instance of the class Laser.
     * 
     * @param newMovement The movement characteristics of the laser obstacle.
     * @param hitbox      The collision characteristics of the laser obstacle.
     */
    public Laser(final Movement newMovement, final Hitbox hitbox) {
        super(ObstacleType.LASER, newMovement, hitbox);
        this.setEntityStatus(EntityStatus.CHARGING);

        this.setEntityMovement(new Movement.Builder()
                .addNewPosition(SPAWNING_X_COORDINATE, this.getEntityMovement().getPosition().get2())
                .addNewSpeed(this.getEntityMovement().getSpeed())
                .addNewAcceleration(this.getEntityMovement().getAcceleration())
                .addNewRotation(this.getEntityMovement().getRotation())
                .addNewMovementChangers(this.getEntityMovement().getMovementChangers())
                .build());
    }

    /**
     * Updates the status of the laser entity based on its lifetime.
     * 
     * @param isSpaceBarPressed Is ignored by this entity.
     */
    @Override
    public void updateStatus(final boolean isSpaceBarPressed) {
        /*
         * Lasers could get deactivated before they naturally do in normal case
         * scenarios
         */
        if (this.getEntityStatus().equals(EntityStatus.DEACTIVATED)
                && this.getLifetime() < TIME_FOR_CHARGING + LASER_DURATION) {
            this.setLifetime(TIME_FOR_CHARGING + LASER_DURATION);
        }

        /* Set the status of the laser based on its lifetime */
        this.checkTimeUpdateStatus(lifetime -> lifetime.equals(TIME_FOR_CHARGING), EntityStatus.ACTIVE);
        this.checkTimeUpdateStatus(lifetime -> lifetime.equals(TIME_FOR_CHARGING + LASER_DURATION), EntityStatus.DEACTIVATED);
        this.checkTimeUpdateStatus(lifetime -> lifetime.equals(TIME_FOR_CHARGING + LASER_DURATION + TIME_FOR_DECHARGING), EntityStatus.INACTIVE);
    }

    /**
     *  Defines a method used to avoid code repetition, which changes the status of the 
     *  entity if a certain lifetime is reached.
     * 
     * @param time The required lifetime to change status.
     * @param newStatus The status which is set when reaching a certain lifetime.
     */
    private void checkTimeUpdateStatus(final Predicate<Integer> time, final EntityStatus newStatus) {
        if (time.test(this.getLifetime())) {
            this.setEntityStatus(newStatus);
        }
    }
}
