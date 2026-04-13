package com.thealgorithms.others;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for AStarSearch algorithm.
 */
class AStarSearchTest {

    /**
     * Simple heuristic that returns 0 (reduces A* to Dijkstra).
     */
    private static final AStarSearch.Heuristic ZERO_HEURISTIC = (node, goal) -> 0;
    private static Map<AStarSearch.Node, List<AStarSearch.Edge>> graph;
    private static AStarSearch.Node A;
    private static AStarSearch.Node B;
    private static AStarSearch.Node C;
    private static AStarSearch.Node D;

    @BeforeAll
    static void setUp() {
        graph = new HashMap<>();

        A = new AStarSearch.Node("A");
        B = new AStarSearch.Node("B");
        C = new AStarSearch.Node("C");
        D = new AStarSearch.Node("D");

        graph.put(A, Arrays.asList(
                new AStarSearch.Edge(B, 1),
                new AStarSearch.Edge(C, 4)
        ));

        graph.put(B, Arrays.asList(
                new AStarSearch.Edge(C, 2),
                new AStarSearch.Edge(D, 5)
        ));

        graph.put(C, Arrays.asList(
                new AStarSearch.Edge(D, 1)
        ));

        graph.put(D, Collections.emptyList());
    }

    @Test
    void testPathExists() {
        List<AStarSearch.Node> path =
                AStarSearch.findPath(graph, A, D, ZERO_HEURISTIC);

        // Expected shortest path: A -> B -> C -> D
        List<AStarSearch.Node> expected = Arrays.asList(A, B, C, D);

        assertEquals(expected, path);
    }

    @Test
    void testDirectPath() {
        List<AStarSearch.Node> path =
                AStarSearch.findPath(graph, A, B, ZERO_HEURISTIC);

        List<AStarSearch.Node> expected = Arrays.asList(A, B);

        assertEquals(expected, path);
    }

    @Test
    void testStartEqualsGoal() {
        List<AStarSearch.Node> path =
                AStarSearch.findPath(graph, A, A, ZERO_HEURISTIC);

        List<AStarSearch.Node> expected = Collections.singletonList(A);

        assertEquals(expected, path);
    }

    @Test
    void testNoPathExists() {
        AStarSearch.Node E = new AStarSearch.Node("E");

        List<AStarSearch.Node> path =
                AStarSearch.findPath(graph, E, A, ZERO_HEURISTIC);

        assertTrue(path.isEmpty());
    }

    @Test
    void testPathCostOptimality() {
        List<AStarSearch.Node> path =
                AStarSearch.findPath(graph, A, D, ZERO_HEURISTIC);

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
