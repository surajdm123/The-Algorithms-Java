package com.thealgorithms.others;

import java.util.*;

/**
 * A* (A-Star) Search Algorithm implementation for finding the shortest path
 * between two nodes in a graph.
 *
 * <p>This algorithm uses a heuristic to guide its search, making it more efficient
 * than Dijkstra’s algorithm in many cases.
 *
 * <p>f(n) = g(n) + h(n)
 * where:
 * - g(n): cost from start node to current node
 * - h(n): estimated cost from current node to goal (heuristic)
 *
 * <p>Time Complexity:
 * - Worst case: O(E log V)
 *
 * <p>Space Complexity:
 * - O(V)
 *
 * <p>Use Cases:
 * - Pathfinding (maps, games)
 * - AI planning
 *
 * @author Suraj Devatha
 */
public class AStarSearch {

    /**
     * Finds shortest path using A*.
     *
     * @param graph     adjacency list
     * @param start     start node
     * @param goal      goal node
     * @param heuristic heuristic function
     * @return list of nodes representing shortest path
     */
    public static List<Node> findPath(
            Map<Node, List<Edge>> graph,
            Node start,
            Node goal,
            Heuristic heuristic
    ) {

        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();
        Map<Node, Node> cameFrom = new HashMap<>();

        PriorityQueue<Node> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(fScore::get)
        );

        gScore.put(start, 0.0);
        fScore.put(start, heuristic.estimate(start, goal));

        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                Node neighbor = edge.target;
                double tentativeG = gScore.get(current) + edge.cost;

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + heuristic.estimate(neighbor, goal));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    /**
     * Reconstructs path from goal to start.
     */
    private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        List<Node> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Heuristic interface (can plug different heuristics).
     */
    public interface Heuristic {
        double estimate(Node current, Node goal);
    }

    /**
     * Node class representing a vertex in the graph.
     */
    static class Node {
        String id;

        Node(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    /**
     * Edge class representing weighted connections.
     */
    static class Edge {
        Node target;
        double cost;

        Edge(Node target, double cost) {
            this.target = target;
            this.cost = cost;
        }
    }
}
