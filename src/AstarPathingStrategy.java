import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AstarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors){
         //use 2 lists to keep track of the path
            //open and closed list

        //add starting location to open list and empty the closed list

        //while open list isn't empty
            //1. select next step (based on the f cost and h cost)

            //2. Remove the step from open list and add it to the closed list

            // for each neighbor of the step we're on..
                //calculate the path cost of reaching the neighbor

                // if cost < cost known for this location
                    // remove it from the open or closed list (means we've found a better route)

                // if the location isn't in either the open or closed list
                    // record the costs for the location and add it to the open list
                    // (means it'll be considered in the next search
                    // record how we got to this location???
        // loop will end when we find a route to the destination or we run out of steps
        // if a route is found, we back track up the records of how we reached each location to determine the path


    }
}
