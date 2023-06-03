public class Edge {
    String startStationId;
    String endStationId;
    int weight;

    Edge(String startStationId, String endStationId, int weight) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.weight = weight;
    }

    public String getstartStation() {
        return this.startStationId;
    }

    public String getendStation() {
        return this.endStationId;
    }

    public int getTravelTime() {
        return this.weight;
    }

    public void updateTravelTime(int travelTime) {
        this.weight = travelTime;
    }
}
