import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel {
   private static int numRows;
   private static int numCols;
   private static Background background[][];
   private static Entity occupancy[][];
   private static Set<Entity> entities;

   private static final int FISH_REACH = 1;


   public WorldModel(int numRows, int numCols, Background defaultBackground) {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++) {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public static Background[][] getBackground() {
      return background;
   }

   public int getNumRows() {
      return this.numRows;
   }
   public int getNumCols(){
      return this.numCols;
   }
   public Set<Entity> getEntities(){
      return this.entities;
   }


   public Optional<Entity> findNearest(Point pos, Class kind) {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities) {
         if (kind.isInstance(entity)) {    //changed this
            ofType.add(entity);
         }
      }

      return pos.nearestEntity(ofType);
   }

   public void removeEntity(Entity entity) {
      this.removeEntityAt(entity.getPosition());
   }

   public void removeEntityAt(Point pos) {
      if (this.withinBounds(pos)
              && this.getOccupancyCell(pos) != null) {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public static void addEntity(Entity entity) {
      if (withinBounds(entity.getPosition())) {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos) {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos)) {
         if(entity instanceof Execute){
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.setPosition(pos);
         }
      }
   }

   public static Optional<Point> findOpenAround(WorldModel world, Point pos) {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++) {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++) {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (world.withinBounds(newPt) &&
                    !world.isOccupied(newPt)) {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public Optional<Entity> getOccupant(Point pos) {
      if (this.isOccupied(pos)) {
         return Optional.of(this.getOccupancyCell(pos));
      } else {
         return Optional.empty();
      }
   }

   public static void tryAddEntity(Entity entity) {
      if (isOccupied(entity.getPosition())) {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }
      addEntity(entity);
   }

   public static boolean withinBounds(Point pos) {
      return pos.getY() >= 0 && pos.getY() < numRows &&
              pos.getX() >= 0 && pos.getX() < numCols;
   }

   public static boolean isOccupied(Point pos) {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   private static Entity getOccupancyCell(Point pos) {
      return occupancy[pos.getY()][pos.getX()];
   }

   public static void setOccupancyCell(Point pos,
                                       Entity entity) {
      occupancy[pos.getY()][pos.getX()] = entity;
   }

   private Background getBackgroundCell(Point pos) {

      return this.background[pos.getY()][pos.getX()];
   }




   //IDK ABOUT THIS ONE
   public void setBackgroundCell(Point pos,
                                        Background background) {
      this.background[pos.getY()][pos.getX()] = background;
   }



/*
   public boolean parseOcto(String[] properties,
                                   ImageStore imageStore) {
      if (properties.length == OCTO_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = Create.createOctoNotFull(properties[OCTO_ID],
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   public boolean parseObstacle(String[] properties,
                                       ImageStore imageStore) {
      if (properties.length == OBSTACLE_NUM_PROPERTIES) {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = Create.createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public boolean parseFish(String[] properties,
                                   ImageStore imageStore) {
      if (properties.length == FISH_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Entity entity = Create.createFish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   public boolean parseAtlantis(String[] properties,
                                       ImageStore imageStore) {
      if (properties.length == ATLANTIS_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = Create.createAtlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   public boolean parseSgrass(String[] properties,
                                     ImageStore imageStore) {
      if (properties.length == SGRASS_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         Entity entity = Create.createSgrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

 */

   public Optional<PImage> getBackgroundImage(
                                                     Point pos) {
      if (this.withinBounds(pos)) {
         return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
      } else {
         return Optional.empty();
      }
   }
}
