import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Octo_Not_Full extends Octo{

    private int resourceCount;


    public Octo_Not_Full(String id, Point position,
                     List<PImage> images, int resourceLimit, int resourceCount,
                     int actionPeriod, int animationPeriod){
        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;

    }

    public boolean moveTo(WorldModel world,
                                  Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(super.getPosition(), (target).getPosition())) //here i changed the type of target to OctoNotFull
        {
            this.resourceCount += 1;
            world.removeEntity(target); //remove entity is supposed to take type
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = super.nextPosition(world, (target).getPosition()); //here i changed the type of target to OctoNotFull

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos); // i need to change what is passed in???
            }
            return false;
        }
    }

    public boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= super.getResourceLimit())
        {
            Entity octo = Create.createOctoFull(super.getId(), super.getResourceLimit(), //i made createOcto(Full) into static and called it here
                    super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
                    super.getImages());

            world.removeEntity(this); //typecasted this to Entity
            scheduler.unscheduleAllEvents(this); //also typecasted this to entity

            world.addEntity(octo);
            ((Octo_Full)octo).scheduleActions(scheduler, world, imageStore); //idk about this cuz i could replace with this
            //^EDIT i typecasted octo to Execute

            return true;
        }

        return false;
    }


    public void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(super.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    super.createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, super.createActivityAction(world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, super.createAnimationAction(0), super.getAnimationPeriod());
    }

}
