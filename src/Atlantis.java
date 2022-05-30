import processing.core.PImage;

import java.util.List;

public class Atlantis extends AnimationPeriod{

    private final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;
    public Atlantis(String id, Point position,
                  List<PImage> images,
                  int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeEntity(WorldModel world, ImageStore imageStore,
                                        EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
        //System.out.println("executeEntity");
    }
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
         scheduler.scheduleEvent(this,
                 super.createAnimationAction(ATLANTIS_ANIMATION_REPEAT_COUNT),
                 super.getAnimationPeriod());

    }

}

