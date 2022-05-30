import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Sgrass extends Execute{
    private final Random rand = new Random();
    private final String FISH_KEY = "fish";
    private final String FISH_ID_PREFIX = "fish -- ";
    private final int FISH_CORRUPT_MIN = 20000;
    private final int FISH_CORRUPT_MAX = 30000;

    public Sgrass(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }
    public void executeEntity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = WorldModel.findOpenAround(world, super.getPosition());

        if (openPt.isPresent())
        {
            Fish fish = Create.createFish(FISH_ID_PREFIX + super.getId(),
                    openPt.get(), FISH_CORRUPT_MIN +
                            this.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                super.createActivityAction(world, imageStore),
                super.getActionPeriod());
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                super.createActivityAction(world, imageStore),
                super.getActionPeriod());
    }








}
