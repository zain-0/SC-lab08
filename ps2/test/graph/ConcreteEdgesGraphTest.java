package graph;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Map;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   - test string output format with multiple vertices and edges
    
    @Test public void testInitialVerticesEmpty() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected no vertices in a new graph", graph.vertices().isEmpty());
    }

    @Test public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected vertex to be added", graph.add("A"));
        assertTrue("Expected vertex 'A' to exist", graph.vertices().contains("A"));
    }
    
    @Test public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertFalse("Expected no duplicate vertex added", graph.add("A"));
    }

    @Test public void testSetEdge() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        int previousWeight = graph.set("A", "B", 5);
        assertEquals("Expected initial weight to be 0", 0, previousWeight);
        assertEquals("Expected updated weight", 5, (int) graph.targets("A").get("B"));
    }

    @Test public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        assertTrue("Expected vertex 'A' to be removed", graph.remove("A"));
        assertFalse("Expected vertex 'A' to not exist", graph.vertices().contains("A"));
        assertTrue("Expected edge from 'A' to 'B' to be removed", graph.sources("B").isEmpty());
    }

    @Test public void testSourcesAndTargets() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.set("A", "B", 5);
        graph.set("C", "B", 3);
        
        Map<String, Integer> sources = graph.sources("B");
        Map<String, Integer> targets = graph.targets("A");
        
        assertEquals("Expected source 'A' with weight 5", (Integer) 5, sources.get("A"));
        assertEquals("Expected source 'C' with weight 3", (Integer) 3, sources.get("C"));
        assertEquals("Expected target 'B' with weight 5", (Integer) 5, targets.get("B"));
    }
}
