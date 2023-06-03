import java.io.*;
import java.util.*;

public class Subway {
    public static void main(String args[]) throws IOException {
        String input;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        System.setOut(out);
        Graph graph = new Graph();
        try {
            graph = readFile(args[0], graph);

            while(true) {
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
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static Graph readFile(String filepath, Graph graph) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
        String input;
        try{
            while(true) { // read station info
                input = br.readLine();
                if (input == null || input.isEmpty()) {
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
            while(true) {
                input = br.readLine();
                if (input == null || input.isEmpty()) {
                    break; // move next read mode
                }
                String[] inputList = input.split(" ");
                Edge newEdge = new Edge(inputList[0], inputList[1], Integer.parseInt(inputList[2]));
                graph.addEdge(newEdge);
            }
            while(true) {
                input = br.readLine();
                if (input == null || input.isEmpty()) {
                    break; // move next read mode
                }
                String[] inputList = input.split(" ");
                graph.updateTransferLine(inputList[0], Integer.parseInt(inputList[1]));
            }

            br.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return graph;
    }

}
