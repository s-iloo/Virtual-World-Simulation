public class Animation implements Action {
    private final AnimationPeriod entity;

    private final int repeatCount;

    public Animation(AnimationPeriod entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler){
        this.executeAnimationAction(scheduler);
    }
    private void executeAnimationAction(EventScheduler scheduler)
    {
        (this.entity).nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    (this.entity).createAnimationAction(
                            Math.max(this.repeatCount - 1, 0)),
                    ((AnimationPeriod)(this.entity)).getAnimationPeriod()); //i changed this from this.entity to AnimationPeriod which is my interface
        }
    }

}
