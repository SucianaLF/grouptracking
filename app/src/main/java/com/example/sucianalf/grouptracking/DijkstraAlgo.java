package com.example.sucianalf.grouptracking;

        import java.util.PriorityQueue;

        import java.util.List;
        import java.util.ArrayList;
        import java.util.Collections;


public class DijkstraAlgo{
    public static void computePaths(Node source){
        source.shortestDistance=0;
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(source);

        while(!queue.isEmpty()){
            Node u = queue.poll();
            for(Edge e: u.adjacencies){
                Node v = e.target;
                double weight = e.weight;
                double distanceFromU = u.shortestDistance+weight;
                if(distanceFromU<v.shortestDistance){
                    queue.remove(v);
                    v.shortestDistance = distanceFromU;
                    v.parent = u;
                    queue.add(v);
                }
            }
        }
    }

    public static List<Node> getShortestPathTo(Node target){
        List<Node> path = new ArrayList<Node>();
        for(Node node = target; node!=null; node = node.parent){
            path.add(node);
        }

        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args){
//        String[] arr = new String[10];
//        Node[] allNodes = new Node[5000];
//
//        for (int i = 0; i < 10; i++) {
//            arr[i] = "n"+i;
//            allNodes[i] = new Node("Arad"+i);
//
//            System.out.println(allNodes[i]);
//        }
//
//        allNodes[0].adjacencies = new Edge[]{
//                new Edge(allNodes[1],75),
//        };
//        allNodes[1].adjacencies = new Edge[]{new Edge(allNodes[2],60)};
//        allNodes[2].adjacencies = new Edge[]{new Edge(allNodes[3],60)};
//        allNodes[3].adjacencies = new Edge[]{new Edge(allNodes[4],50)};
//        allNodes[4].adjacencies = new Edge[]{new Edge(allNodes[5],50)};
//        allNodes[5].adjacencies = new Edge[]{new Edge(allNodes[5],0)};
//
////	    Node[] nodes = allNodes;
//
//        computePaths(allNodes[0]);
//
//        //print shortest paths
//
////		for(Node n: nodes){
////			System.out.println("Distance to " +
////				n + ": " + n.shortestDistance);
////    		List<Node> path = getShortestPathTo(n);
////    		System.out.println("Path: " + path);
////		}
//
//        List<Node> path = getShortestPathTo(allNodes[5]);
//        System.out.println("Path: " + path);
//
    }


}


//define Node
class Node implements Comparable<Node>{

    public final String value;
    public Edge[] adjacencies;
    public double shortestDistance = Double.POSITIVE_INFINITY;
    public Node parent;

    public Node(String val){
        value = val;
    }

    public String toString(){
        return value;
    }

    public int compareTo(Node other){
        return Double.compare(shortestDistance, other.shortestDistance);
    }

}

//define Edge
class Edge{
    public final Node target;
    public final double weight;
    public Edge(Node targetNode, double weightVal){
        target = targetNode;
        weight = weightVal;
    }
}
