import java.util.*;

public class Path {
    private List<String> stations;
    private int totalTravelTime;

    public Path(List<String> stations, int totalTravelTime) {
        this.stations = stations;
        this.totalTravelTime = totalTravelTime;
    }

    public List<String> getStations() {
        return stations;
    }

    public int getTotalTravelTime() {
        return totalTravelTime;
    }

    public int compareTo(Path p) {
        if (this.getTotalTravelTime() > p.getTotalTravelTime()) {
            return -1;
        } else if (this.getTotalTravelTime() == p.getTotalTravelTime()) {
            return 0;
        } else {
            return 1;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.stations.size()-1; i++) {
            if(i<this.stations.size()-1 && stations.get(i).equals(stations.get(i+1))) {
                sb.append("[");
                sb.append(stations.get(i));
                sb.append("]");
                sb.append(" ");
                i++;
            } else {
                sb.append(stations.get(i));
                sb.append(" ");
            }
        }
        sb.append(stations.get(this.stations.size()-1));

        return sb.toString();
    }

}
