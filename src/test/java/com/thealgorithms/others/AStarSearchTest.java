package com.thealgorithms.others;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test cases for AStarSearch algorithm.
 */
class AStarSearchTest {

    /**
     * Simple heuristic that returns 0 (reduces A* to Dijkstra).
     */
    private static final AStarSearch.Heuristic ZERO_HEURISTIC = (node, goal) -> 0;
    private static Map<AStarSearch.Node, List<AStarSearch.Edge>> graph;

    private static AStarSearch.Node nodeA;
    private static AStarSearch.Node nodeB;
    private static AStarSearch.Node nodeC;
    private static AStarSearch.Node nodeD;

    @BeforeAll
    static void setUp() {
        graph = new HashMap<>();

        nodeA = new AStarSearch.Node("A");
        nodeB = new AStarSearch.Node("B");
        nodeC = new AStarSearch.Node("C");
        nodeD = new AStarSearch.Node("D");

        graph.put(nodeA, Arrays.asList(new AStarSearch.Edge(nodeB, 1), new AStarSearch.Edge(nodeC, 4)));

        graph.put(nodeB, Arrays.asList(new AStarSearch.Edge(nodeC, 2), new AStarSearch.Edge(nodeD, 5)));

        graph.put(nodeC, Arrays.asList(new AStarSearch.Edge(nodeD, 1)));

        graph.put(nodeD, Collections.emptyList());
    }

    @Test
    void testPathExists() {
        List<AStarSearch.Node> path = AStarSearch.findPath(graph, nodeA, nodeD, ZERO_HEURISTIC);

        // Expected shortest path: A -> B -> C -> D
        List<AStarSearch.Node> expected = Arrays.asList(nodeA, nodeB, nodeC, nodeD);

        assertEquals(expected, path);
    }

    @Test
    void testDirectPath() {
        List<AStarSearch.Node> path = AStarSearch.findPath(graph, nodeA, nodeB, ZERO_HEURISTIC);

        List<AStarSearch.Node> expected = Arrays.asList(nodeA, nodeB);

        assertEquals(expected, path);
    }

    @Test
    void testStartEqualsGoal() {
        List<AStarSearch.Node> path = AStarSearch.findPath(graph, nodeA, nodeA, ZERO_HEURISTIC);

        List<AStarSearch.Node> expected = Collections.singletonList(nodeA);

        assertEquals(expected, path);
    }

    @Test
    void testNoPathExists() {
        AStarSearch.Node nodeE = new AStarSearch.Node("nodeE");

        List<AStarSearch.Node> path = AStarSearch.findPath(graph, nodeE, nodeA, ZERO_HEURISTIC);

        assertTrue(path.isEmpty());
    }

    @Test
    void testPathCostOptimality() {
        List<AStarSearch.Node> path = AStarSearch.findPath(graph, nodeA, nodeD, ZERO_HEURISTIC);

        // Calculate total cost
        double cost = calculatePathCost(path);

        // Expected shortest cost = 1 (A->B) + 2 (B->C) + 1 (C->D) = 4
        assertEquals(4.0, cost);
    }

    /**
     * Utility method to calculate path cost.
     */
    private double calculatePathCost(List<AStarSearch.Node> path) {
        double total = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            AStarSearch.Node current = path.get(i);
            AStarSearch.Node next = path.get(i + 1);

            for (AStarSearch.Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                if (edge.target.equals(next)) {
                    total += edge.cost;
                    break;
                }
            }
        }

        return total;
    }
}
