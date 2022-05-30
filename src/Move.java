import processing.core.PImage;

import java.util.List;

public abstract class Move extends AnimationPeriod {


    public Move(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
    public abstract Point nextPosition(WorldModel world, Point destPos);

}

