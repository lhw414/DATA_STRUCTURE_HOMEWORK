import java.util.*;

public class Graph {
    private HashSet<String> stationNames;
    private HashMap<String, Station> idToStation;
    private HashMap<String, List<Station>> sameName;
    private HashMap<String, List<Edge>> transferLine;
    
    public Graph() { // constructor
        this.stationNames = new HashSet<>();
        this.idToStation = new HashMap<>();
        this.sameName = new HashMap<>();
        this.transferLine = new HashMap<>();
    }

    // Function : get sameName list
    public List<Station> getSameNameList(String stationName) {
        return this.sameName.get(stationName);
    }

    // Function : add station when same name not exist
    public void addStation(Station newStation) {
        this.stationNames.add(newStation.getName());
        this.idToStation.put(newStation.getId(), newStation);
        List<Station> sameNameList = new LinkedList<>();
        sameNameList.add(newStation);
        this.sameName.put(newStation.getName(), sameNameList);
        List<Edge> transferLineList = new LinkedList<>();
        this.transferLine.put(newStation.getName(), transferLineList);
    }

    // Function : add station when same name exist
    public void addDuplicateStation(Station newStation) {
        this.idToStation.put(newStation.getId(), newStation);

        List<Station> sameNameList = this.sameName.get(newStation.getName());
        List<Edge> transferLineList = this.transferLine.get(newStation.getName());
        for(int i=0; i<sameNameList.size(); i++) {
            Station prevStation = sameNameList.get(i);
            Edge newEdge1 = new Edge(prevStation.getId(), newStation.getId(), 5);
            Edge newEdge2 = new Edge(newStation.getId(), prevStation.getId(), 5);
            prevStation.addEdge(newEdge1);
            newStation.addEdge(newEdge2);
            transferLineList.add(newEdge1);
            transferLineList.add(newEdge2);
        }
        sameNameList.add(newStation);
    }

    public boolean isSameNameStationExist(String stationName) {
        return stationNames.contains(stationName);
    }

    public void addEdge(Edge newEdge) {
        Station startStation = this.idToStation.get(newEdge.getstartStation());
        startStation.addEdge(newEdge);
    }

    public void updateTransferLine(String stationName, int transferTime) {
        List<Edge> transferLineList = this.transferLine.get(stationName);
        for(int i=0; i<transferLineList.size(); i++) {
            Edge transferEdge = transferLineList.get(i);
            transferEdge.updateTravelTime(transferTime);
        }
    }

    public Path findShortestPath(String sourceStationId, String destinationStationId) {
        Station source = idToStation.get(sourceStationId);
        Station destination = idToStation.get(destinationStationId);

        HashMap<Station, Integer> distances = new HashMap<>();
        HashMap<Station, Station> previous = new HashMap<>();
        PriorityQueue<Station> minHeap = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Station station : idToStation.values()) {
            distances.put(station, Integer.MAX_VALUE);
            previous.put(station, null);
        }

        distances.put(source, 0);
        minHeap.offer(source);

        while (!minHeap.isEmpty()) {
            Station current = minHeap.poll();

            if (current == destination) {
                break;
            }

            for (Edge edge : current.getEdges()) {
                String neighbor = edge.getendStation();
                Station neighborStation = idToStation.get(neighbor);
                int newDistance = distances.get(current) + edge.getTravelTime();

                if (newDistance < distances.get(neighborStation)) {
                    distances.put(neighborStation, newDistance);
                    previous.put(neighborStation, current);
                    minHeap.offer(neighborStation);
                }
            }
        }

        List<String> shortestPath = new LinkedList<>();
        int totalTravelTime = 0;
        Station current = destination;

        while (current != null) {
            shortestPath.add(0, current.getName());
            if (previous.get(current) != null) {
                for (Edge edge : previous.get(current).getEdges()) {
                    if (idToStation.get(edge.getendStation()) == current) {
                        totalTravelTime += edge.getTravelTime();
                        break;
                    }
                }
            }
            current = previous.get(current);
        }
        
        return new Path(shortestPath, totalTravelTime);
    }
}
