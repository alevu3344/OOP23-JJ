package it.unibo.jetpackjoyride.core.entities.powerup.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.jetpackjoyride.core.entities.powerup.api.AbstractPowerUp;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.movement.Movement;
import it.unibo.jetpackjoyride.core.movement.MovementGenerator;
import it.unibo.jetpackjoyride.core.movement.MovementGenerator.MovementChangers;
import it.unibo.jetpackjoyride.utilities.Pair;

public class MrCuddles extends AbstractPowerUp{
    private final static Integer MRCUDDLESLENGHT = 15;
    public Integer indexOfBody;
    private List<Boolean> lastFrames;

    public MrCuddles(Movement movement, Hitbox hitbox, Integer indexOfBody) {
        super(PowerUpType.MRCUDDLES, movement, hitbox);
        this.performingAction = PerformingAction.ASCENDING;
        this.indexOfBody = indexOfBody;
        this.lastFrames = new ArrayList<>();
        if(indexOfBody==0) {
            this.powerUpStatus = PowerUpStatus.ACTIVE;
        } else {
            this.powerUpStatus = PowerUpStatus.DEACTIVATED;
        }
    }

    @Override
    public void update(boolean isSpaceBarPressed) {
        if(this.lastFrames.size() == MRCUDDLESLENGHT) {
            this.lastFrames.remove(0);
        }
        this.lastFrames.add(isSpaceBarPressed);

        Double rotationAngle = -5 + (double)(1*this.lastFrames.stream().filter(p -> p.equals(true)).count());
        this.movement.setRotation(new Pair<>(rotationAngle,0.0));
        this.movement.update();

        if(lastFrames.size() > indexOfBody) {
            if(lastFrames.get(lastFrames.size()-1-indexOfBody)) {
                this.performingAction = PerformingAction.DESCENDING;
            } else {
                this.performingAction = PerformingAction.ASCENDING;
            }
        }
        

        if(this.movement.getCurrentPosition().get2()<150) {
            this.movement.setSpeed(new Pair<>(this.movement.getSpeed().get1(), 0.0));
            this.movement.setCurrentPosition(new Pair<>(this.movement.getCurrentPosition().get1(), 150.0));
        }
        if(this.movement.getCurrentPosition().get2()>650) {
            this.movement.setSpeed(new Pair<>(this.movement.getSpeed().get1(), 0.0));
            this.movement.setCurrentPosition(new Pair<>(this.movement.getCurrentPosition().get1(), 650.0));
        }
        
        switch (this.performingAction) {
            case ASCENDING:
                this.movement.setMovementChangers(List.of(MovementChangers.INITIALLYSTILL, MovementChangers.INVERSEGRAVITY, MovementChangers.BOUNDS));
                break;
            case DESCENDING:
                this.movement.setMovementChangers(List.of(MovementChangers.INITIALLYSTILL, MovementChangers.GRAVITY, MovementChangers.BOUNDS));
                break;
            default:
                break;
        }
    }   
}
