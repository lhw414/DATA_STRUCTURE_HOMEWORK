import java.util.*;

// Class : graph class for calculating total travle time using dijkstra
public class Graph {
    private HashSet<String> stationNames; // store station names and check duplicate name
    private HashMap<String, Station> idToStation; // id to station name
    private HashMap<String, List<Station>> sameName; // station name to station instance
    private HashMap<String, List<Edge>> transferLine; // mapping transfer line
    
    public Graph() { // constructor
        this.stationNames = new HashSet<>();
        this.idToStation = new HashMap<>();
        this.sameName = new HashMap<>();
        this.transferLine = new HashMap<>();
    }

    // Method : get list of same name stations
    public List<Station> getSameNameList(String stationName) {
        return this.sameName.get(stationName);
    }

    // Method : add station when there's no duplicate station name
    public void addStation(Station newStation) {
        this.stationNames.add(newStation.getName()); // add station name
        this.idToStation.put(newStation.getId(), newStation); // add to mapping table
        List<Station> sameNameList = new LinkedList<>(); // init linked list for storing same name station
        sameNameList.add(newStation);
        this.sameName.put(newStation.getName(), sameNameList);
        List<Edge> transferLineList = new LinkedList<>(); // init linked list for storing transfer line
        this.transferLine.put(newStation.getName(), transferLineList);
    }

    // Method : add station when there's duplicate station name
    public void addDuplicateStation(Station newStation) {
        this.idToStation.put(newStation.getId(), newStation); // put new station into idtostation
        List<Station> sameNameList = this.sameName.get(newStation.getName()); // same name list
        List<Edge> transferLineList = this.transferLine.get(newStation.getName()); // transferline list
        for(int i=0; i<sameNameList.size(); i++) { // add transfer line to stations
            Station prevStation = sameNameList.get(i);
            Edge newEdge1 = new Edge(prevStation.getId(), newStation.getId(), 5); // transfer line
            Edge newEdge2 = new Edge(newStation.getId(), prevStation.getId(), 5); // transfer line
            // add to station and transfer line list
            prevStation.addEdge(newEdge1);
            newStation.addEdge(newEdge2);
            transferLineList.add(newEdge1);
            transferLineList.add(newEdge2);
        }
        sameNameList.add(newStation);
    }

    // Method : check same name station exist
    public boolean isSameNameStationExist(String stationName) {
        return stationNames.contains(stationName);
    }

    // Method : add new edge to station
    public void addEdge(Edge newEdge) {
        Station startStation = this.idToStation.get(newEdge.getstartStation());
        startStation.addEdge(newEdge);
    }

    // Method : update transfer line's travel time
    public void updateTransferLine(String stationName, int transferTime) {
        List<Edge> transferLineList = this.transferLine.get(stationName);
        for(int i=0; i<transferLineList.size(); i++) {
            Edge transferEdge = transferLineList.get(i);
            transferEdge.updateTravelTime(transferTime);
        }
    }

    // ! REF : get dijkstra skeleton code using chat gpt (and fix it using my classes)
    // Method : get shortest path using dijkstra
    public Path findShortestPath(String sourceStationId, String destinationStationId) {
        Station source = idToStation.get(sourceStationId); // get source station
        Station destination = idToStation.get(destinationStationId); // get destination station

        HashMap<Station, Integer> distances = new HashMap<>(); // distance hash map
        HashMap<Station, Station> previous = new HashMap<>(); // previous station hash map
        PriorityQueue<Station> minHeap = new PriorityQueue<>(Comparator.comparingInt(distances::get)); // using min heap for dijkstra

        for (Station station : idToStation.values()) { // init graph
            distances.put(station, Integer.MAX_VALUE);
            previous.put(station, null);
        }

        distances.put(source, 0);
        minHeap.offer(source);

        while (!minHeap.isEmpty()) { // while min heap not empty
            Station current = minHeap.poll();

            if (current == destination) {
                break;
            }

            for (Edge edge : current.getEdges()) { // minimize line weight with travel time
                String neighbor = edge.getendStation();
                Station neighborStation = idToStation.get(neighbor);
                int newDistance = distances.get(current) + edge.getTravelTime();

                if (newDistance < distances.get(neighborStation)) { // minimize
                    distances.put(neighborStation, newDistance);
                    previous.put(neighborStation, current);
                    minHeap.offer(neighborStation);
                }
            }
        }

        List<String> shortestPath = new LinkedList<>(); // shortest path list
        int totalTravelTime = 0;
        Station current = destination;

        while (current != null) { // add station in shorest path
            shortestPath.add(0, current.getName());
            if (previous.get(current) != null) {
                for (Edge edge : previous.get(current).getEdges()) {
                    if (idToStation.get(edge.getendStation()) == current) { // calculate total travel time
                        totalTravelTime += edge.getTravelTime();
                        break;
                    }
                }
            }
            current = previous.get(current);
        }
        // return path instance with shortest path and total travel time
        return new Path(shortestPath, totalTravelTime);
    }
    // ! REF END : get dijkstra skeleton code using chat gpt
}
