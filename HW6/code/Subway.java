import java.io.*;
import java.util.*;

public class Subway {
    public static void main(String args[]) throws IOException {
        String input;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Graph graph = new Graph();
        Boolean flag = true;
        try {
            while(true) {
                while(flag) { // read station info
                    input = br.readLine();
                    if (input.equals("QUIT")) {
                        flag = false;
                        break;
                    }
                    if (input.isEmpty()) {
                        break; // move next read mode
                    }
                    String[] inputList = input.split(" ");
                    Station newStation = new Station(inputList[0], inputList[1], inputList[2]);
                    if (graph.isSameNameStationExist(inputList[1])) {
                        graph.addDuplicateStation(newStation);
                    } else {
                        graph.addStation(newStation);
                    }
                }
                while(flag) {
                    input = br.readLine();
                    if (input.equals("QUIT")) {
                        flag = false;
                        break;
                    }
                    if (input.isEmpty()) {
                        break; // move next read mode
                    }
                    String[] inputList = input.split(" ");
                    Edge newEdge = new Edge(inputList[0], inputList[1], Integer.parseInt(inputList[2]));
                    graph.addEdge(newEdge);
                }
                while(flag) {
                    input = br.readLine();
                    if (input.equals("QUIT")) {
                        flag = false;
                        break;
                    }
                    if (input.isEmpty()) {
                        break; // move next read mode
                    }
                    String[] inputList = input.split(" ");
                    graph.updateTransferLine(inputList[0], Integer.parseInt(inputList[1]));
                }
                while(flag) {
                    input = br.readLine();
                    if (input.equals("QUIT")) {
                        break;
                    }
                    String[] inputList = input.split(" ");
                    List<Station> startStationList = graph.getSameNameList(inputList[0]);
                    List<Station> endStationList = graph.getSameNameList(inputList[1]);
                    Path ansPath = new Path(new LinkedList<String>(), Integer.MAX_VALUE);
                    for(int i=0;i<startStationList.size();i++) {
                        for (int j=0;j<endStationList.size();j++) {
                            Path currpath = graph.findShortestPath(startStationList.get(i).getId(), endStationList.get(j).getId());
                            if (ansPath.compareTo(currpath) < 0) {
                                ansPath = currpath;
                            }
                        }
                    }
                    System.out.println(ansPath.toString());
                    System.out.println(ansPath.getTotalTravelTime());
                }
                break;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }


}
