public class Edge {
    Station start;
    Station end;
    int travelTime;

    Edge(Station start, Station end, String subwayLine, int travelTime) {
        this.start = start;
        this.end = end;
        this.travelTime = travelTime;
    }
}
