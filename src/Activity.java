public class Activity implements Action{

    private final Execute entity;
    private final WorldModel world;
    private final ImageStore imageStore;


    public Activity(Execute entity, WorldModel world,
                  ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;

    }
    public void executeAction(EventScheduler scheduler){

        this.entity.executeActivityAction(this.world,this.imageStore,scheduler);
    }

    }

