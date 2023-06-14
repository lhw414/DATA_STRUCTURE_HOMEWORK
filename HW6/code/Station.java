import java.util.*;

// Class : station class for storing staion data
public class Station {
    private String id;
    private String name;
    private String line;
    private int transferTime;
    private LinkedList<Edge> adjacentLines;

    Station(String id, String name, String line) { // constructor
        this.id = id;
        this.name = name;
        this.line = line;
        this.adjacentLines = new LinkedList<>();
    }

    // Method : get id
    public String getId() {
        return this.id;
    }
    
    // Method : get name
    public String getName() {
        return this.name;
    }
    
    // Method : get line
    public String getLine() {
        return this.line;
    }

    // Method : get transfer time
    public int getTransferTime() {
        return this.transferTime;
    }

    // Method : get edge list
    public LinkedList<Edge> getEdges() {
        return this.adjacentLines;
    }

    // Method : add edge to station
    public void addEdge(Edge newLine) {
        this.adjacentLines.add(newLine);
    }

    // Method : get adjacentline list
    public LinkedList<Edge> getAdjacentLineList() {
        return this.adjacentLines;
    }

    // Method : add transfer time
    public void addTransferTime(int transferTime) {
        this.transferTime = transferTime;
    }
}
