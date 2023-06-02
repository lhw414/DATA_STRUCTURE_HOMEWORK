import java.io.*;
import java.util.*;

public class Subway {
    public static void main(String args[]) throws IOException {
        String input;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Map<
        while(true) {
            while(true) { // read station info
                input = br.readLine();
                if (input.isEmpty()) {
                    break; // move next read mode
                }
                String[] inputList = input.split(" ");
                String stationId = inputList[0];
                String stationName = inputList[1];
                String subwayLine = inputList[2];
                Station newStation = new Station(stationId, stationName, subwayLine);
            }
            
        }
    }


}
