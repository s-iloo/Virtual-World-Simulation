import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationPeriod{

    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position,
                     List<PImage> images,
                     int actionPeriod, int animationPeriod){
        super(id, position,images,actionPeriod,animationPeriod);
    }

    public void executeEntity(WorldModel world, ImageStore filler, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                super.createActivityAction(world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this,
                super.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                super.getAnimationPeriod());
    }

}
