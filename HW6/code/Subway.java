import java.io.*;
import java.util.*;

// Class : Main class
public class Subway {
    // Method : main method
    public static void main(String args[]) throws IOException {
        String input;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8")); // encoding setting for using korean
        PrintStream out = new PrintStream(System.out, true, "UTF-8"); // encoding setting for using korean
        System.setOut(out);
        Graph graph = new Graph(); // graph init
        try {
            graph = readFile(args[0], graph); // read subway info files

            while(true) {
                input = br.readLine();
                if (input.equals("QUIT")) { // if quit, exit
                    break;
                }
                String[] inputList = input.split(" "); // split by space
                List<Station> startStationList = graph.getSameNameList(inputList[0]);
                List<Station> endStationList = graph.getSameNameList(inputList[1]);
                Path ansPath = new Path(new LinkedList<String>(), Integer.MAX_VALUE); // current answer path
                for(int i=0;i<startStationList.size();i++) { // for loop, start station of same name
                    for (int j=0;j<endStationList.size();j++) { // for loop, end station of same name
                        Path currpath = graph.findShortestPath(startStationList.get(i).getId(), endStationList.get(j).getId());
                        if (ansPath.compareTo(currpath) < 0) { // if current path's travel time is less than anspath, anspath = currpath
                            ansPath = currpath;
                        }
                    }
                }
                System.out.println(ansPath.toString()); // print path
                System.out.println(ansPath.getTotalTravelTime()); // print total travel time
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    // Method : read subway file and build graph
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
                Station newStation = new Station(inputList[0], inputList[1], inputList[2]); // make station
                if (graph.isSameNameStationExist(inputList[1])) { // if same name exist
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
                Edge newEdge = new Edge(inputList[0], inputList[1], Integer.parseInt(inputList[2])); // make edge
                graph.addEdge(newEdge);
            }
            while(true) {
                input = br.readLine();
                if (input == null || input.isEmpty()) {
                    break; // move next read mode
                }
                String[] inputList = input.split(" ");
                graph.updateTransferLine(inputList[0], Integer.parseInt(inputList[1])); // update transfer time
            }

            br.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return graph;
    }

}
