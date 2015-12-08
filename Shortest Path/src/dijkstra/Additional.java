package dijkstra;
/**
 *
 * @author  	PenchalaSainath
 * @project   	Implementing Dijkstra's Algorithm
 * @build     	v -1.0
 * @TimeFrame	October-November Year 2015
 * 
 * @Description  This file contains additional files required such as 
 * 					- loading the input file which is additional to 
 * 					dijkstra's algorithm
 * 
 */

import java.io.*;
import java.util.*;

public class Additional {
	public int[][] loadFile(int choose_file) throws FileNotFoundException, IOException {
//		user send choose_file to be 1 when he wants to read the file
		if (choose_file == 1) {
//			when the user input is 1
			String buffer;
			List<String> rows = new ArrayList<>();  //store the rows read
			int num_of_routers; 					// number of routers
			String buffer_array[];					// store the individual distance
			String file;							// file name
//			buffered reader to read the file name
			BufferedReader br_input = new BufferedReader(new InputStreamReader(System.in));
//			prompting the user to input the file name
			System.out.println("Input original network topology matix data file:");
			file = br_input.readLine();

//			reading the file row by row
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((buffer = br.readLine()) != null) {

				rows.add(buffer);
			}

//			number of rows will be equal to the number of routers 
			num_of_routers = rows.size();
//			creating the graph variable of size which is the number of routers
			int graph[][] = new int[num_of_routers][num_of_routers];

//			looping on rows
			for (int i = 0; i < rows.size(); i++) {
//				splitting the rows at space to get the distances 
				buffer_array = rows.get(i).split(" ");
				for (int j = 0; j < rows.size(); j++) {
//					saving the distances
					graph[i][j] = Integer.parseInt(buffer_array[j]);
				}
			}
//			closing the buffered reader
			br.close();
//			returning the data read
			return graph;
		} else {
//			if user sends choose_file is not 1 then return null
			return null;
		}
	}
}