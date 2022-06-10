import processing.core.PImage;

import java.util.List;

final class Background
{
   private String id;
   private List<PImage> images;
   private int imageIndex;
   private final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;
   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }
   public static boolean parseBackground(String[] properties,
                                         WorldModel world, ImageStore imageStore) {
      if (properties.length == BGND_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(world, pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }
   public static void setBackground(WorldModel world, Point pos,
                                    Background background) {
      if (world.withinBounds(pos)) {
         world.setBackgroundCell(pos, background);
      }
   }
   public PImage getCurrentImage() {
      return this.images.get(this.imageIndex);
      }
   }

