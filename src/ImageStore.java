import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

/*
ImageStore: to ideally keep track of the images used in our virtual world
 */

final class ImageStore
{
   private Map<String, List<PImage>> images;
   private List<PImage> defaultImages;
   private final int COLOR_MASK = 0xffffff;
   private final int KEYED_IMAGE_MIN = 5;
   private final int KEYED_RED_IDX = 2;
   private final int KEYED_GREEN_IDX = 3;
   private final int KEYED_BLUE_IDX = 4;

   private static final String OCTO_KEY = "octo";
   private static final String OBSTACLE_KEY = "obstacle";
   private static final String FISH_KEY = "fish";
   private static final String ATLANTIS_KEY = "atlantis";
   private static final String SGRASS_KEY = "seaGrass";
   private static final String BGND_KEY = "background";
   private static final int PROPERTY_KEY = 0;

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      this.defaultImages = new LinkedList<>();
      this.defaultImages.add(defaultImage);
   }
   public List<PImage> getImageList(String key)
   {
      return this.images.getOrDefault(key, this.defaultImages);
   }
   public void loadImages(Scanner in, PApplet screen) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            this.processImageLine(this.images, in.nextLine(), screen);
         } catch (NumberFormatException e) {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }

   private void processImageLine(Map<String, List<PImage>> images,
                                       String line, PApplet screen) {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2) {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1) {
            List<PImage> imgs = this.getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN) {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               this.setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   private List<PImage> getImages(Map<String, List<PImage>> images,
                                        String key) {
      List<PImage> imgs = images.get(key);
      if (imgs == null) {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   private void setAlpha(PImage img, int maskColor, int alpha) {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++) {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }
   private static boolean processLine(String line, WorldModel world,
                                     ImageStore imageStore) {
      String[] properties = line.split("\\s");
      if (properties.length > 0) {
         switch (properties[PROPERTY_KEY]) {
            case BGND_KEY:
               return Background.parseBackground(properties, world, imageStore);
            case OCTO_KEY:
               return Parse.parseOcto(properties, imageStore);
            case OBSTACLE_KEY:
               return Parse.parseObstacle(properties, imageStore);
            case FISH_KEY:
               return Parse.parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return Parse.parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return Parse.parseSgrass(properties, imageStore);
         }
      }

      return false;
   }
   public static void load(Scanner in, WorldModel world, ImageStore imageStore) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            if (!ImageStore.processLine(in.nextLine(), world, imageStore)) {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         } catch (NumberFormatException e) {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         } catch (IllegalArgumentException e) {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }
}
