import processing.core.PImage;

import java.util.List;

public abstract class Move extends AnimationPeriod {

    private PathingStrategy strategy = new SingleStepPathingStrategy();
    public Move(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy strategy){
        super(id, position, images, actionPeriod, animationPeriod);
        this.strategy = strategy;
    }


    public PathingStrategy getStrategy() {
        return strategy;
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
    public abstract Point nextPosition(WorldModel world, Point destPos);

}

