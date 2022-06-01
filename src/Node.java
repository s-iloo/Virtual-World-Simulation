 public class Node {

        private Point p;
        private int gCost;
        private int hCost;
        private int fCost;
        private Node pNode;

        public Node(Point p, int gCost, int hCost, Node pNode){
            this.p = p;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.pNode = pNode;
        }

        public Point getP() {
            return p;
        }

        public Node getpNode() {
            return pNode;
        }

        public int getgCost() {
            return gCost;
        }

        public int gethCost() {
            return hCost;
        }

        public int getfCost() {
            return fCost;
        }
    }
}
