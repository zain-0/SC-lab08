package graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed, weighted graph where vertices is the set of nodes,
    //   and edges is the list of directed edges with weights between nodes.
    // Representation invariant:
    //   Each edge in edges has source and target vertices in vertices set and non-negative weights.
    // Safety from rep exposure:
    //   The vertices and edges fields are private and final. No references to mutable internal state
    //   are returned to the caller.
    
    // Constructor
    public ConcreteEdgesGraph() {
        checkRep();
    }
    
    // Checks the representation invariant
    private void checkRep() {
        for (Edge edge : edges) {
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
            assert edge.getWeight() >= 0;
        }
    }
    
    @Override public boolean add(String vertex) {
        if (vertex == null) throw new IllegalArgumentException("Vertex cannot be null");
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
    }
    
    @Override public int set(String source, String target, int weight) {
        if (source == null || target == null || weight < 0) 
            throw new IllegalArgumentException("Invalid source, target, or weight");

        add(source);
        add(target);

        int previousWeight = 0;
        Edge existingEdge = null;

        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                previousWeight = edge.getWeight();
                existingEdge = edge;
                break;
            }
        }

        if (existingEdge != null) {
            edges.remove(existingEdge);
        }

        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }
        
        checkRep();
        return previousWeight;
    }
    
    @Override public boolean remove(String vertex) {
        boolean removed = vertices.remove(vertex);
        
        if (removed) {
            edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        }
        
        checkRep();
        return removed;
    }
    
    @Override public Set<String> vertices() {
        return new HashSet<>(vertices);
    }
    
    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> sourcesMap = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                sourcesMap.put(edge.getSource(), edge.getWeight());
            }
        }
        return sourcesMap;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        Map<String, Integer> targetsMap = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                targetsMap.put(edge.getTarget(), edge.getWeight());
            }
        }
        return targetsMap;
    }
    
    @Override public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\nVertices: ");
        sb.append(vertices).append("\nEdges:\n");
        for (Edge edge : edges) {
            sb.append(edge.toString()).append("\n");
        }
        return sb.toString();
    }
    
}

/**
 * Represents an immutable, directed edge in a graph.
 * 
 * <p>This class is internal to the rep of ConcreteEdgesGraph.
 */
class Edge {
    
    private final String source;
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //   Represents a directed edge from source to target with a specified weight.
    // Representation invariant:
    //   source and target are non-null, weight is non-negative.
    // Safety from rep exposure:
    //   All fields are private, final, and immutable.
    
    /**
     * Initializes an edge with a source vertex, a target vertex, and a non-negative weight.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the weight of the edge
     * @throws IllegalArgumentException if source or target is null, or weight is negative
     */
    public Edge(String source, String target, int weight) {
        if (source == null || target == null || weight < 0) 
            throw new IllegalArgumentException("Invalid source, target, or weight");
        
        this.source = source;
        this.target = target;
        this.weight = weight;
        
        checkRep();
    }
    
    // Checks the representation invariant
    private void checkRep() {
        assert source != null;
        assert target != null;
        assert weight >= 0;
    }
    
    /**
     * Returns the source vertex of the edge.
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Returns the target vertex of the edge.
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * Returns the weight of the edge.
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * Returns a string representation of the edge in the format: "source -> target (weight)".
     */
    @Override public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}
