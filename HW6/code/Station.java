import java.util.*;

public class Station {
    private String id;
    private String name;
    private String line;
    private int transferTime;
    private LinkedList<Edge> adjacentLines;

    Station(String id, String name, String line) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.adjacentLines = new LinkedList<>();
    }

    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getLine() {
        return this.line;
    }

    public int getTransferTime() {
        return this.transferTime;
    }

    public LinkedList<Edge> getEdges() {
        return this.adjacentLines;
    }

    public void addEdge(Edge newLine) {
        this.adjacentLines.add(newLine);
    }

    public LinkedList<Edge> getAdjacentLineList() {
        return this.adjacentLines;
    }

    public void addTransferTime(int transferTime) {
        this.transferTime = transferTime;
    }
}
