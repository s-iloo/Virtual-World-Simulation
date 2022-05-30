import processing.core.PImage;

import java.util.List;

public abstract class AnimationPeriod extends Execute {
    private final int animationPeriod;
    public AnimationPeriod(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){
        return this.animationPeriod;
    }
    public void nextImage(){
        super.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    };
}
