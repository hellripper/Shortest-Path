package dijkstra;
/**
 *
 * @author  	PenchalaSainath
 * @project   	Implementing Dijkstra's Algorithm
 * @build     	v -1.0
 * @TimeFrame	October-November Year 2015
 * 
 * @Description  This is the source file where we
 * 					- have implemented dijkstra's algorithm
 * 				 	 using collections from java.util package
 * 					- have implemented the way to find the connection table 
 * 					- some additional features
 * 						-- minimum 8 topology matrix support
 * 						-- automatically computing and displaying the connection table
 * 
 */

import java.io.*;
import java.util.*;

// Creating a class which is a graph like structure
class Vertex implements Comparable<Vertex> {
	public final String name;		// Name of the router
	public List<Edge> adjacencies;  // List of connections to it
	public double minDistance = Double.POSITIVE_INFINITY; // initial distance will be infinity
	public Vertex previous;			// previous node visited

//	structure for router and its distances for other routers
	public Vertex(String argName) {
		name = argName; 
		adjacencies = new ArrayList<Edge>();
	}

//	to add a new route to the router
	public void addEdge(Edge e) {
		adjacencies.add(e);
	}

//	overrides the toString function which is supplied as default
//	this will return the name of the router
	public String toString() {
		return name;
	}

//	this is used to compare the minimum paths
//	between the source and the next set of destination paths
	public int compareTo(Vertex other) {
		return Double.compare(minDistance, other.minDistance);
	}

}

// a router connection structure
class Edge {
	public final Vertex target; // connecting router 
	public final double weight; // distance to connecting router

//	user supplied destination and distance
	public Edge(Vertex argTarget, double argWeight) {
		target = argTarget;
		weight = argWeight;
	}
}

public class Dijkstras {

//	calculating all the paths from source to destination
//	this is done mainly for connection table we use this same to find the 
//	optimal/shortest path 
	public static void computePaths(Vertex source) {
		source.minDistance = 0.;  // as minDistance is double so 0.
		
//		this is the list of vertexes
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>(); 
//		vertexes are queued initially it would be the source
		vertexQueue.add(source);

//		if the source provided is not initial then
		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

//			Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}

			}
		}
	}

//	to find the optimal path
	public static List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>(); //from src to target paths
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
//			adds the router to the list
			path.add(vertex);
// 		as reverse path will be found the paths will be populated in reverse order 
//		to make it correct we are reversing the path
		Collections.reverse(path);
//		path is returned
		return path;
	}

//	main function
	public static void main(String args[]) throws IOException {

//		list of routers with their distances and next routers
		Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

//		input from the user
		int user_input_option = 0;
		// storing source and destination routers values as global as we need
		// this across all option
		int source = 0;
		int destination = 0;
//		object to the additional class 
//		there we read just the input file
		Additional adj = new Additional();

//		we should read the file only when user inputs the option 1
//		we use this variable to meet that requirement
		int choose_file = 0;
//		initializing the topology matrix
		int topology_matrix[][] = adj.loadFile(choose_file);

//		Displaying the menu when the user until user inputs option exit
		while (user_input_option != 5) {
			System.out.println("\n\nCS542 Link State Routing Simulator");
			System.out.println("");
			System.out.println("1. Create a Network Topology");
			System.out.println("2. Build a Connection Table");
			System.out.println("3. Shortest Path to Destination Router");
			System.out.println("4. Modify a topology");
			System.out.println("5. Exit");
			System.out.println("");
			System.out.println("Command :");

//			prompt user to enter the option
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			user_input_option = Integer.parseInt(br.readLine());

//			using a switch to toggle between the options
			switch (user_input_option) {

			case 1:
				choose_file = 1; // set to 1 as we need to read the input file
				
//				data from the file is returned to topology_matrix 
				topology_matrix = adj.loadFile(choose_file); 

//				printing the matrix
				System.out.print("Given Topology Matrix is :\n");
				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						System.out.print(topology_matrix[i][j] + " ");
					}
					System.out.println("");
				}

//				Additional Features
//				automatically calculating the connection table for all the routers
//				which are provided using a input file
				
//				creating the routers 
//				number of routers as same as the lenth of the topology matrix
				for (int x = 1; x <= topology_matrix.length; x++) {
					for (int i = 1; i <= topology_matrix.length; i++) {
						String s_vertex_data = Integer.toString(i);
//						converting to router data
						Vertex v3 = new Vertex(s_vertex_data);
//						adding the router
						vertexMap.put(s_vertex_data, v3);
					}

//					adding the distance between the router and distance between them
					for (int i = 0; i < topology_matrix.length; i++) {
						for (int j = 0; j < topology_matrix.length; j++) {
//							incrementing the router number as we are staring from 0
//							router cannot be 0
							int from = i + 1; 
							int to = j + 1;
//							reading the distance from the topology matrix
							int distance = topology_matrix[i][j];
							if (distance == 0 || distance == -1) {
//								no path exists to the same router
//								no path exists when there is no direct connection 
//								between one router and another
//								in which case user inputs as -1
//								in the topology matrix
							} else {
//								converting toString as inputs for vertex 
//								are of vertex data type and type casting is to be 
//								done using string function
								String s_from = Integer.toString(from);
								String s_to = Integer.toString(to);
								String s_distance = Integer.toString(distance);
								double d_distance = Double.parseDouble(s_distance);
//								s_from is the source router
								Vertex v3 = vertexMap.get(s_from);
								if (v3 != null) {
//									if the source router is not null
//									adding the path from source to destination
//									and calculating the weights
									v3.addEdge(new Edge(vertexMap.get(s_to), d_distance));
								}
							}
						}
					}

//					this will collect all the routers a nd their specific paths 
					Collection<Vertex> vertices_all = vertexMap.values();
					System.out.println("-------------------------------------------");
//					computing paths from a single source to all the destinations
					String ct_source_all = Integer.toString(x);
					Vertex new_ct_source_all = vertexMap.get(ct_source_all);
//					printng the connection table
					System.out.println("Router " + ct_source_all + " Connection Table");
//					computes all the paths from the source specified
					computePaths(new_ct_source_all);
					System.out.println("Destination\t\tInterface\n");
//					looping on all the routers and considering the routers as destination					
					for (Vertex v : vertices_all) {
//						finding the shortest path
						List<Vertex> path = getShortestPathTo(v);
						System.out.print(v);
						// System.out.println(path);

						if (v == new_ct_source_all) {
//							if source and destination are same
							System.out.println("\t\t\t-");
							continue;
						}
//						when the path is returned to the destination
//						looping on the path
						for (Vertex next_router : path) {
							if (next_router == new_ct_source_all) {
//								the path will same if the source and destination are same
								System.out.print(" ");
							} else {
//								if not printing just the next outer for the
//								connection table 
								System.out.println("\t\t\t" + next_router);
								break;
							}
						}
					}
				}
				break;

//			connection table to the specific router 
//		 	the specific router here will be the source router 
//			the code is same as in the above case
//			make sure you change the needful when changed above or below code
			case 2:
				System.out.println("Select a Source Router :");
				source = Integer.parseInt(br.readLine());
				if (source == 0 || source == topology_matrix.length) {
					System.out.println("Router dose'nt exists");
					break;
				}
//				backing up topology matrix as it will be modified while
//				computing the connection table
				int topology_matrix_backup_2[][] = new int[topology_matrix.length][topology_matrix.length];
				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						topology_matrix_backup_2[i][j] = topology_matrix[i][j];
					}
				}

				for (int i = 1; i <= topology_matrix.length; i++) {
					String s_vertex_data = Integer.toString(i);
					Vertex v3 = new Vertex(s_vertex_data);
					vertexMap.put(s_vertex_data, v3);
				}

				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						int from = i + 1;
						int to = j + 1;
						int distance = topology_matrix[i][j];
						if (distance == 0 || distance == -1) {

						} else {
							String s_from = Integer.toString(from);
							String s_to = Integer.toString(to);
							String s_distance = Integer.toString(distance);
							double d_distance = Double.parseDouble(s_distance);

							Vertex v3 = vertexMap.get(s_from);
							if (v3 != null) {
								v3.addEdge(new Edge(vertexMap.get(s_to), d_distance));
							}
						}
					}
				}

				Collection<Vertex> vertices = vertexMap.values();
				String ct_source = Integer.toString(source);
				Vertex new_ct_source = vertexMap.get(ct_source);
				System.out.println("Router " + ct_source + " Connection Table");
				computePaths(new_ct_source);
				System.out.println("Destination\t\tInterface\n");
				for (Vertex v : vertices) {
					List<Vertex> path = getShortestPathTo(v);
					System.out.print(v);
					// System.out.println(path);

					if (v == new_ct_source) {
						System.out.println("\t\t\t-");
						continue;
					}
					for (Vertex next_router : path) {
						if (next_router == new_ct_source) {
							System.out.print(" ");
						} else {
							System.out.println("\t\t\t" + next_router);
							break;
						}
					}
				}

				break;

// 			Computing the path to the specific destination
//			source is taken if the step 2 of the user input
//			destination is asked in this case	
			case 3:
//				if source router is not given
				if (source == 0) {
//					System.out.println("Choose a Source Router :");
//					source = Integer.parseInt(br.readLine());
//					if (source == 0 || source == topology_matrix.length) {
//						System.out.println("Router dose'nt exists");
//						break;
//					}
					System.out.println("Choose source router from option 2");
					break;
				}
//				prompting the user for destination
				// if (destination == 0) {
				System.out.println("Choose a Destination Router :");
				destination = Integer.parseInt(br.readLine());
				if (destination == 0 || destination > topology_matrix.length) {
					System.out.println("Router dosent exists");
					break;
				}
				// }

				for (int i = 1; i <= topology_matrix.length; i++) {
					String s_vertex_data = Integer.toString(i);
					Vertex v3 = new Vertex(s_vertex_data);
					vertexMap.put(s_vertex_data, v3);
				}

				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						int from = i + 1;
						int to = j + 1;
						int distance = topology_matrix[i][j];
						if (distance == 0 || distance == -1) {

						} else {
							String s_from = Integer.toString(from);
							String s_to = Integer.toString(to);
							String s_distance = Integer.toString(distance);
							double d_distance = Double.parseDouble(s_distance);

							Vertex v3 = vertexMap.get(s_from);
							if (v3 != null) {
								v3.addEdge(new Edge(vertexMap.get(s_to), d_distance));
							}
						}
					}
				}

				// Collection<Vertex> vertices_3 = vertexMap.values();
				String s_source = Integer.toString(source);
				Vertex new_source = vertexMap.get(s_source);
				String s_destination = Integer.toString(destination);
				Vertex new_destination = vertexMap.get(s_destination);

				computePaths(new_source);
				System.out.println(
						"The shortest path from router " + new_source + " to router " + new_destination + " is ");
				List<Vertex> path = getShortestPathTo(new_destination);

				for (Vertex next_router : path) {
					if (next_router == new_source) {
						System.out.print(next_router);
					} else {
						System.out.print(" -> " + next_router);
					}

				}
				// System.out.print(path);
				System.out.println("");
				System.out.println("The total cost is " + new_destination.minDistance);

				break;

//		    when a router is down we have to remove the router from the network
			case 4:
//				checking for the topology not be be initial
				if (topology_matrix == null) {
					System.out.println("Topology not yet loaded");
					break;
				}
//				prompting the user to input which router to be removed
				System.out.println("Select a router to be removed:");
				int remove_router = Integer.parseInt(br.readLine());

//				if source is initial prompting user to enter
				if (source == 0) {
					System.out.println("Choose a Source Router :");
					source = Integer.parseInt(br.readLine());
					if (source == 0 || source == topology_matrix.length) {
						System.out.println("Router dosent exists");
						break;
					}
				}

//				if destination is initial prompting user to enter
				if (destination == 0) {
					System.out.println("Choose a Destination Router :");
					destination = Integer.parseInt(br.readLine());
					if (destination == 0 || destination > topology_matrix.length) {
						System.out.println("Router dosent exists");
						break;
					}
				}

				for (int i = 0; i < topology_matrix.length; i++) {
					if (i == (remove_router - 1)) {
						topology_matrix[remove_router - 1][i] = 0;
					} else {
						topology_matrix[remove_router - 1][i] = -1;
						topology_matrix[i][remove_router - 1] = -1;
					}
				}
//				calculating the connection table
//				baking up the data because we are changing the data
//				while calculating the connection table in Additional.java
				int topology_matrix_backup_4[][] = new int[topology_matrix.length][topology_matrix.length];
				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						topology_matrix_backup_4[i][j] = topology_matrix[i][j];
					}
				}

//				displaying the modified topology
				System.out.println("-------------------------------------------");
				System.out.println("Modified Topology after removing router" + remove_router);
				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						System.out.print(topology_matrix[i][j] + " ");
					}
					System.out.println("");
				}

				// int connection_table_4[] =
				// adj.Connection_Table(topology_matrix_backup_4, source);
				// System.out.println("ROUTER\t\t\tINTERFACE\n");
				// for (int i = 0; i < connection_table_4.length; i++) {
				// if (source == i)
				// System.out.println((i + 1) + "\t\t\t" + 0 + "\n");
				// else if (connection_table_4[i] == 0)
				// System.out.println((i + 1) + "\t\t\t" + (i + 1) + "\n");
				// else
				// System.out.println((i + 1) + "\t\t\t" +
				// (connection_table_4[i] + 1) + "\n");
				// }

//				same as calculating minimum distance
				for (int i = 1; i <= topology_matrix.length; i++) {
					String s_vertex_data = Integer.toString(i);
					Vertex v3 = new Vertex(s_vertex_data);
					vertexMap.put(s_vertex_data, v3);
				}

				for (int i = 0; i < topology_matrix.length; i++) {
					for (int j = 0; j < topology_matrix.length; j++) {
						int from = i + 1;
						int to = j + 1;
						int distance = topology_matrix[i][j];
						if (distance == 0 || distance == -1) {

						} else {
							String s_from = Integer.toString(from);
							String s_to = Integer.toString(to);
							String s_distance = Integer.toString(distance);
							double d_distance = Double.parseDouble(s_distance);

							Vertex v3 = vertexMap.get(s_from);
							if (v3 != null) {
								v3.addEdge(new Edge(vertexMap.get(s_to), d_distance));
							}
						}
					}
				}

				Collection<Vertex> vertices_modified = vertexMap.values();
				String s_source_4 = Integer.toString(source);
				Vertex new_source_4 = vertexMap.get(s_source_4);
				String s_destination_4 = Integer.toString(destination);
				Vertex new_destination_4 = vertexMap.get(s_destination_4);

				computePaths(new_source_4);
//				computing connection table
				System.out.println("-------------------------------------------");
				System.out.println("Router " + new_source_4 + " Connection Table");
				System.out.println("Destination\t\tInterface\n");
				for (Vertex v : vertices_modified) {
					List<Vertex> path_modified = getShortestPathTo(v);
					System.out.print(v);
					// System.out.println(path);

					if (v == new_source_4) {
						System.out.println("\t\t\t-");
						continue;
					}
					if (path_modified.size() == 1) {
						System.out.println("\t\t\t-");
						continue;
					}
					for (Vertex next_router : path_modified) {
						if (next_router == new_source_4) {
							System.out.print(" ");
						} else {
							System.out.println("\t\t\t" + next_router);
							break;
						}
					}
				}

//				computing shortest path
				System.out.println("-------------------------------------------");
				System.out.println(
						"The shortest path from router " + new_source_4 + " to router " + new_destination_4 + " is ");
				List<Vertex> path_4 = getShortestPathTo(new_destination_4);

				for (Vertex next_router : path_4) {
					if (next_router == new_source_4) {
						System.out.print(next_router);
					} else {
						System.out.print(" -> " + next_router);
					}

				}
				// System.out.print(path);
				System.out.println("");
				System.out.println("The total cost is " + new_destination_4.minDistance);

				break;

			case 5:
				user_input_option = 5;
				System.out.println("Exit CS542 project. Good Bye!");
				break;
			}
		}
	}
}