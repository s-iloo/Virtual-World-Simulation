import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Octo_Full extends Octo{

    public Octo_Full(String id, Point position,
                     List<PImage> images, int resourceLimit,
                     int actionPeriod, int animationPeriod){
        super(id,position,images, resourceLimit, actionPeriod, animationPeriod);
    }

    public boolean moveTo(WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(super.getPosition(), (target).getPosition())) //typecasted to octofull
        {
            return true;
        }
        else
        {
            Point nextPos = super.nextPosition(world, (target).getPosition()); //typecasted to octofull

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos); //typecasted this to entity
            }
            return false;
        }
    }

    public void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {

        Entity octo = Create.createOctoNotFull(super.getId(), super.getResourceLimit(), //how will it know which createEntity to use
                super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
                super.getImages()); //made it into a static call for octonotfull createocto

        world.removeEntity(this); //type casted to entity
        scheduler.unscheduleAllEvents(this); // type casted to entity

        world.addEntity(octo);
        ((Octo_Not_Full)octo).scheduleActions(scheduler, world, imageStore); //made into "this" from octo
    }

    //WRONG
    public void executeEntity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(super.getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((Atlantis)fullTarget.get()).scheduleActions(scheduler, world, imageStore); //idrk what type this should be but i typecasted into atlantis

            //transform to unfull
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    super.createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }

    }
    @Override
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
         scheduler.scheduleEvent(this,
                 super.createActivityAction(world, imageStore),
                 super.getActionPeriod());
         scheduler.scheduleEvent(this, super.createAnimationAction(0),
                 super.getAnimationPeriod());

    }


}

