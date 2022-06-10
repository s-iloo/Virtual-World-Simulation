import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AstarPathingStrategy implements PathingStrategy{

    private PriorityQueue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            if( o1.getfCost() > o2.getfCost()){
                return 1;
            } else if (o1.getfCost() < o2.getfCost()){
                return -1;
            } else {
                if (o1.gethCost() > o2.gethCost()) {
                    return 1;
                } else if (o1.gethCost() < o2.gethCost()){
                    return -1;
                } else {
                    if (o1.getgCost() > o2.getgCost()) {
                        return 1;
                    } else if (o1.getgCost() < o2.getgCost()){
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    });

    //private LinkedList<Node> closeList = new LinkedList<>();


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        HashMap<Point, Node> openH = new HashMap<>(); //open list hashmap
        HashMap<Point, Node> closeH = new HashMap<>(); //close list hashmap


        openList.clear();


        Node startN = new Node(start, 0, start.distance(end), null);
        openList.add(startN); //for da first time around
        List<Point> pointReturn = new ArrayList<>(); //our return list

        Node currentNode = startN;

        while (!(openList.isEmpty())) { //while there's still stuff to look at
            currentNode = openList.remove(); //set the currentNode equal to first thing in da open list (and remove it from the open list)
            if (withinReach.test(currentNode.getP(), end)){ //means we reached our dest
                System.out.println("dest found");
                pointReturn.add(currentNode.getP()); //add the first node (basically the dest Node)
                Node tempNode = currentNode; //set a temp node equal to the first node (dest node)
                while(tempNode.getpNode() != null) { //while the prior node isn't null (exists)
                    pointReturn.add(tempNode.getpNode().getP()); //keep adding the prior node to the path
                    tempNode = tempNode.getpNode(); // then iterate to the priors prior node
                }
                Collections.reverse(pointReturn);
                pointReturn.remove(0);


                return pointReturn; //return that list
            }

            //openList.remove(currentNode);

            List<Point> neighbors = potentialNeighbors.apply(currentNode.getP())
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end)) //idrk what this does... check if the point doesn't equal to the beginning or the end ig?
                    .collect(Collectors.toList());

            //System.out.println(neighbors);

            for (Point p : neighbors) { //go through all the neighbor points
                if(!closeH.containsKey(p)) { //if the point isn't yet in the closed list
                    int gTemp = currentNode.getgCost() + 1; //my temp for checking if we should add to the open list to check it out (via g cost comparison)
                    if (!openH.containsKey(p)) { //check if the neigbor isn't in da open list yet
                        Node n = new Node(p, currentNode.getgCost() + 1, p.distance(end), currentNode); //if not then we can add it to both open lists
                        openList.add(n);
                        openH.put(p, n);
                    }
                    else{ // this means it is in da open list
                        if(gTemp < openH.get(p).getgCost()){ //found a better g cost so in the if block i can replace the greater
                            //g cost with the lesser g cost in both open lists
                            Node newN = new Node(p, gTemp, p.distance(end), currentNode);
                            openList.add(newN); //replace worse option with the better option
                            openList.remove(openH.get(p));
                            openH.replace(p, newN);
                        }
                    }
                }
                closeH.put(currentNode.getP(), currentNode);
                 //adding the currentNode to the closed list
                //deleted the other closed list cuz it wasn't really used

            }

        }
        return pointReturn;
    }
}


