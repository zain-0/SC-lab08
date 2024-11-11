package graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed graph where each vertex in vertices has a list of
    //   its outgoing edges to other vertices and their respective weights.
    // Representation invariant:
    //   Each vertex is unique within vertices, and each edge has a non-negative weight.
    // Safety from rep exposure:
    //   vertices is private, final, and no direct references to mutable objects are exposed.
    
    // Constructor
    public ConcreteVerticesGraph() {
        checkRep();
    }
    
    // Checks the representation invariant
    private void checkRep() {
        for (Vertex vertex : vertices) {
            assert vertex != null;
            assert vertices.stream().filter(v -> v.getName().equals(vertex.getName())).count() == 1;
            for (int weight : vertex.getEdges().values()) {
                assert weight >= 0;
            }
        }
    }
    
    @Override public boolean add(String vertexName) {
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals(vertexName)) {
                return false;
            }
        }
        vertices.add(new Vertex(vertexName));
        checkRep();
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        if (source == null || target == null || weight < 0) 
            throw new IllegalArgumentException("Invalid source, target, or weight");

        add(source);
        add(target);
        
        Vertex sourceVertex = findVertex(source);
        int previousWeight = sourceVertex.setEdge(target, weight);
        
        if (weight == 0 && previousWeight > 0) {
            sourceVertex.removeEdge(target);
        }
        
        checkRep();
        return previousWeight;
    }
    
    @Override public boolean remove(String vertexName) {
        Vertex vertexToRemove = findVertex(vertexName);
        if (vertexToRemove == null) return false;
        
        vertices.remove(vertexToRemove);
        for (Vertex vertex : vertices) {
            vertex.removeEdge(vertexName);
        }
        
        checkRep();
        return true;
    }
    
    @Override public Set<String> vertices() {
        Set<String> vertexNames = new HashSet<>();
        for (Vertex vertex : vertices) {
            vertexNames.add(vertex.getName());
        }
        return Collections.unmodifiableSet(vertexNames);
    }
    
    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex vertex : vertices) {
            Integer weight = vertex.getEdges().get(target);
            if (weight != null) {
                sources.put(vertex.getName(), weight);
            }
        }
        return sources;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        Vertex sourceVertex = findVertex(source);
        if (sourceVertex == null) return new HashMap<>();
        return sourceVertex.getEdges();
    }
    
    @Override public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\n");
        for (Vertex vertex : vertices) {
            sb.append(vertex.toString()).append("\n");
        }
        return sb.toString();
    }
    
    private Vertex findVertex(String name) {
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals(name)) {
                return vertex;
            }
        }
        return null;
    }
}

/**
 * Represents a mutable vertex with outgoing edges in a directed graph.
 */
class Vertex {
    
    private final String name;
    private final Map<String, Integer> edges = new HashMap<>();
    
    // Abstraction function:
    //   Represents a vertex with a name and a map of outgoing edges, where
    //   each entry maps a target vertex to a weight.
    // Representation invariant:
    //   name is non-null, and each weight in edges is non-negative.
    // Safety from rep exposure:
    //   All fields are private and immutable. The edges map is safely exposed
    //   as an unmodifiable map when accessed.
    
    /**
     * Initializes a vertex with the specified name.
     * 
     * @param name the name of the vertex
     * @throws IllegalArgumentException if name is null
     */
    public Vertex(String name) {
        if (name == null) throw new IllegalArgumentException("Vertex name cannot be null");
        this.name = name;
        checkRep();
    }
    
    // Checks the representation invariant
    private void checkRep() {
        assert name != null;
        for (int weight : edges.values()) {
            assert weight >= 0;
        }
    }
    
    /**
     * Returns the name of this vertex.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns an unmodifiable map of edges from this vertex.
     */
    public Map<String, Integer> getEdges() {
        return Collections.unmodifiableMap(edges);
    }
    
    /**
     * Sets an edge from this vertex to a target vertex with the specified weight.
     * 
     * @param target the target vertex
     * @param weight the weight of the edge
     * @return the previous weight of the edge, or 0 if no edge existed
     */
    public int setEdge(String target, int weight) {
        if (target == null || weight < 0) throw new IllegalArgumentException("Invalid target or weight");
        
        int previousWeight = edges.getOrDefault(target, 0);
        if (weight > 0) {
            edges.put(target, weight);
        } else {
            edges.remove(target);
        }
        
        checkRep();
        return previousWeight;
    }
    
    /**
     * Removes the edge from this vertex to the specified target vertex.
     */
    public void removeEdge(String target) {
        edges.remove(target);
        checkRep();
    }
    
    @Override public String toString() {
        StringBuilder sb = new StringBuilder(name + " -> ");
        sb.append(edges);
        return sb.toString();
    }
}
