import processing.core.PImage;

import java.util.List;

public class Create {
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
    public static Octo_Full createOctoFull(String id, int resourceLimit,
                                 Point position, int actionPeriod, int animationPeriod,
                                 List<PImage> images)
    {
        return new Octo_Full(id, position, images,
                resourceLimit, actionPeriod, animationPeriod); // new Octo_full and casted it to entity and got rid of entity kind
    }
    public static Octo_Not_Full createOctoNotFull(String id, int resourceLimit,
                                    Point position, int actionPeriod, int animationPeriod,
                                    List<PImage> images)
    {
        return new Octo_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod); //i changed this to be type casted to entity interface but it is a new object of octonotfull class (also got rid of
        //the entity param
    }
    public static Crab createCrab(String id, Point position,
                                     int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(id, position, images, actionPeriod, animationPeriod);
    }
    public static Atlantis createAtlantis(String id, Point position,
                                        List<PImage> images)
    {
        return new Atlantis(id, position, images,
                 0, 0);
    }
    public static Fish createFish(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Fish(id, position, images,
                actionPeriod);
    }

    public static Sgrass createSgrass(String id, Point position, int actionPeriod,
                                      List<PImage> images)
    {
        return new Sgrass(id, position, images, actionPeriod);
    }
    public static Obstacle createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle(id, position, images, 0);
    }






}
