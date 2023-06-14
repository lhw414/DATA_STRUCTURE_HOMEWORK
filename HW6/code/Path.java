import java.util.*;

// Class : path class compare with other path and for printing
public class Path {
    private List<String> stations;
    private int totalTravelTime;

    public Path(List<String> stations, int totalTravelTime) { // constructor
        this.stations = stations;
        this.totalTravelTime = totalTravelTime;
    }

    // Method : get station list
    public List<String> getStations() {
        return stations;
    }

    // Method : get total travel time
    public int getTotalTravelTime() {
        return totalTravelTime;
    }

    // Method : compare with other path using total travel time
    public int compareTo(Path p) {
        if (this.getTotalTravelTime() > p.getTotalTravelTime()) {
            return -1;
        } else if (this.getTotalTravelTime() == p.getTotalTravelTime()) {
            return 0;
        } else {
            return 1;
        }
    }

    // Method : make string using stringbuilder
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.stations.size()-1; i++) {
            if(i<this.stations.size()-1 && stations.get(i).equals(stations.get(i+1))) { // if transfer edge, merge like [staion name]
                sb.append("[");
                sb.append(stations.get(i));
                sb.append("]");
                sb.append(" ");
                i++;
            } else { // else, just append station name
                sb.append(stations.get(i));
                sb.append(" ");
            }
        }
        // last station
        sb.append(stations.get(this.stations.size()-1));

        return sb.toString();
    }

}
