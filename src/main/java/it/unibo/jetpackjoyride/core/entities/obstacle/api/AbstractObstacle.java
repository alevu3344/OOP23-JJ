package it.unibo.jetpackjoyride.core.entities.obstacle.api;

import static it.unibo.jetpackjoyride.core.entities.entity.api.Entity.EntityType.*;

import it.unibo.jetpackjoyride.core.entities.entity.api.AbstractEntity;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.movement.Movement;

public abstract class AbstractObstacle extends AbstractEntity implements Obstacle {

    public final ObstacleType obstacleType;

    public AbstractObstacle(final ObstacleType obstacleType, final Movement newMovement, final Hitbox hitbox) {
        super(OBSTACLE, newMovement, hitbox);
        this.obstacleType = obstacleType;
        this.entityStatus = EntityStatus.INACTIVE;
    }

    @Override
    public ObstacleType getObstacleType() {
        return this.obstacleType;
    }

}
