import processing.core.PImage;

import java.util.List;

public abstract class Execute extends Entity {
    private final int actionPeriod;
    public Execute(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, 0);
        this.actionPeriod = actionPeriod;
    }

    public abstract void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public Action createAnimationAction(int repeatCount){
        return new Animation((AnimationPeriod) this, repeatCount);
    };
    public Action createActivityAction(WorldModel world, ImageStore imageStore){
        return new Activity(this, world, imageStore);
    };
    public void executeActivityAction(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler){
        this.executeEntity(world, imageStore, eventScheduler);
    };

    public int getActionPeriod(){
        return this.actionPeriod;
    }
}
