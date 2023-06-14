//Class : edge class (station line)
public class Edge {
    String startStationId;
    String endStationId;
    int weight;

    Edge(String startStationId, String endStationId, int weight) { // constructor
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.weight = weight;
    }

    // Method : get start station
    public String getstartStation() {
        return this.startStationId;
    }

    // Method : get end station
    public String getendStation() {
        return this.endStationId;
    }

    // get travel time
    public int getTravelTime() {
        return this.weight;
    }

    // update travel time
    public void updateTravelTime(int travelTime) {
        this.weight = travelTime;
    }
}
