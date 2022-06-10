import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Wyvern extends Move{

    private static final PathingStrategy CRAB_PATHING = new
            AstarPathingStrategy();


    public Wyvern(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod, CRAB_PATHING);
    }


    public void executeEntity(WorldModel world,
                              ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(
                super.getPosition(), Crab.class);
        long nextPeriod = super.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (this.moveTo(world, crabTarget.get(), scheduler))
            {
                //pass the right parameter
                world.removeEntityAt(crabTarget.get().getPosition());

            }
        }

        scheduler.scheduleEvent(this,
                super.createActivityAction(world, imageStore),
                nextPeriod);
    }
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                super.createActivityAction(world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent(this,
                super.createAnimationAction(0), super.getAnimationPeriod());
    }

    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
    public Point nextPosition(WorldModel world,
                              Point destPos) {

        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz,
                super.getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Fish))) {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            List<Point> points = super.getStrategy().computePath(getPosition(), destPos, p -> world.withinBounds(p) && !world.isOccupied(p) , Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
            if(points.size() != 0){
                newPos = points.get(0);
            }

            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Fish))) {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

}