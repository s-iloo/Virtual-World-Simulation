import processing.core.PImage;

import java.util.List;

public abstract class Octo extends Move{
    private final int resourceLimit;
    //private static final PathingStrategy octoPathing = new AstarPathingStrategy();
    private static final PathingStrategy octoPathing = new SingleStepPathingStrategy();
    public Octo(String id, Point position,
                         List<PImage> images, int resourceLimit,
                         int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod, octoPathing);
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit(){
        return this.resourceLimit;
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    public Point nextPosition(WorldModel world,
                                  Point destPos) {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz,
                this.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());

            List<Point> points = getStrategy().computePath(getPosition(), destPos,
                    p -> world.withinBounds(p) && !world.isOccupied(p), Point::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
            //newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
            if(points.size() != 0){
                newPos = points.get(0);
            }
            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getPosition();
            }
        }
        return newPos;
    }
}
