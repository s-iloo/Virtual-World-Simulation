import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Fish extends Execute{
    private final Random rand = new Random();
    private final String CRAB_KEY = "crab";
    private final String CRAB_ID_SUFFIX = " -- crab";
    private final int CRAB_PERIOD_SCALE = 4;
    private final int CRAB_ANIMATION_MIN = 50;
    private final int CRAB_ANIMATION_MAX = 150;

    public Fish(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public void executeEntity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = super.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = Create.createCrab(super.getId() + CRAB_ID_SUFFIX,
                pos, super.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        this.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

    }
}
