import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
//test
/*
yo yo yo

VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
        extends PApplet
{
   private final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private Set<Point> corruptedPoints = new HashSet<>();
   private int corruptedCount= 0;

   private final String IMAGE_LIST_FILE_NAME = "imagelist";
   private final String DEFAULT_IMAGE_NAME = "background_default";
   private final int DEFAULT_IMAGE_COLOR = 0x808080;

   private final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
              createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
              createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      WorldView.drawViewport(view);
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;

         }
         view.shiftView(dx, dy);
      }
   }

   public void mousePressed()   {


      Background background = new Background("lava", imageStore.getImageList("lava"));


      Point pressed = mouseToPoint(mouseX, mouseY);
      Point p1 = new Point(pressed.getX()-1, pressed.getY());
      Point p2 = new Point(pressed.getX()-1, pressed.getY()+1);
      Point p3 = new Point(pressed.getX(), pressed.getY()+1);
      Point p4 = new Point(pressed.getX(), pressed.getY()-1);
      Point p5 = new Point(pressed.getX()+1, pressed.getY()-1);
      Point p6 = new Point(pressed.getX()+1, pressed.getY());

      List<Point> points = new ArrayList<>();
      points.add(pressed);
      points.add(p1);
      points.add(p2);
      points.add(p3);
      points.add(p4);
      points.add(p5);
      points.add(p6);



      List<Point> valid = points.stream().filter(p-> WorldModel.withinBounds(p)).filter(p->!(corruptedPoints.contains(p)))
              .collect(Collectors.toList());

      valid.forEach(p-> corruptedCount++);
      valid.forEach(p-> corruptedPoints.add(p));

      System.out.println(corruptedCount);

      if(corruptedCount < 600){
         valid.forEach(p-> Background.setBackground(world, p, background));

         for(Point p : valid){
            if(world.isOccupied(p)){
               Optional<Entity> occupant = world.getOccupant(p);
               System.out.println(occupant.get().getClass());
               if(occupant.get().getClass().equals(Octo_Not_Full.class) || occupant.get().getClass().equals(Octo_Full.class)){
                  Octo_Corrupted corrupted = new Octo_Corrupted("octo", p, imageStore.getImageList("octo_corrupted"), 5, 6 );
                  world.removeEntity(occupant.get());
                  world.addEntity(corrupted);
                  corrupted.scheduleActions(scheduler, world, imageStore);
               }

            }
         }

         if(world.isOccupied(pressed)){
            world.removeEntityAt(pressed);
         }
         Wyvern w = new Wyvern("wyvern", pressed, imageStore.getImageList("wyvern"), 5, 6);
         world.addEntity(w);
         w.scheduleActions(scheduler, world, imageStore);
      } else {
         System.out.println("Corrupted limit reached.");
      }
   }

   private Point mouseToPoint(int x, int y)   {
      return new Point((mouseX/TILE_WIDTH) + view.getViewport().getCol(), (mouseY/TILE_HEIGHT) + view.getViewport().getRow());
   }

   private Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, ImageStore imageStore,
                           PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void loadWorld(WorldModel world, String filename,
                          ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         ImageStore.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void scheduleActions(WorldModel world,
                                EventScheduler scheduler, ImageStore imageStore) {
      for (Entity entity : world.getEntities()) {
         //Only start actions for entities that include action (not those with just animations)
         if (!(entity instanceof Obstacle)) { //obstacles have no actionperiod
            if (((Execute) entity).getActionPeriod() > 0) //typecasted entitty to execute
               ((Execute) entity).scheduleActions(scheduler, world, imageStore);

         }
      }
   }

   private static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
