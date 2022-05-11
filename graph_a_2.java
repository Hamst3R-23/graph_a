package vlad;

import java.io.File;
import java.util.*;

public class graph_a_2<T> implements graph_a_1<T> {

    //using HashMap for graph
    private HashMap<T, ArrayList<adjacent_vertex<T>>> graph;

    //default constructor
    public graph_a_2() {
        graph = new HashMap<>();
    }

    //file constructor
    public graph_a_2(String fileName) throws Exception {
        Scanner scanner;
        File file = new File(fileName);

        //size - 3, because: vertex name + space + weight
        if(!file.exists())
            throw new Exception("file doesn't exist");

        if(file.length() < 3)
            throw new Exception("wrong format of file");

        scanner = new Scanner(file);

        //all the lines of the file
        List<String[]> new_file = new ArrayList<>();

        //weights of the edges
        List<ArrayList<Integer>> edges_weight = new ArrayList<>();

        //names
        List<T> names = new ArrayList<>();

        while(scanner.hasNextLine())
            new_file.add(scanner.nextLine().split("\\s"));
        scanner.close();

        for(int i = 0; i < new_file.size(); i++)
            names.add((T) new_file.get(i)[0]);

        for(int i = 0; i < new_file.size(); i++) {
            edges_weight.add(new ArrayList<>());
            for(int j = 1; j < new_file.get(i).length; j++) {
                int value = Integer.parseInt(new_file.get(i)[j]);
                edges_weight.get(i).add(value);
            }
        }

        for(ArrayList<Integer> list : edges_weight)
            if(list.size() != new_file.size())
                throw new Exception("matrix is not square!");

        //creating a graph
        graph = new HashMap<>();
        for(int i = 0; i < edges_weight.size(); i++) {
            graph.put(names.get(i), new ArrayList<>());
            for(int j = 0; j < edges_weight.get(i).size(); j++)
                if(edges_weight.get(i).get(j) != 0)
                    graph.get(names.get(i)).add(new adjacent_vertex<>(names.get(j), edges_weight.get(i).get(j)));
        }
    }


    //add vertex(
    public void add_vertex(T vertex_name) throws Exception{
        if(graph.containsKey(vertex_name) || vertex_name == null)
            throw new Exception("already have this vertex or put nothing");
        graph.put(vertex_name, new ArrayList<>());
    }

    //add oriented edge with from "start" to "end"
    public void add_edge(T start, T end, int weight) throws Exception {
        if(start == null || end == null || weight == 0)
            throw new Exception("put nothing or weight 0");
        if(!graph.containsKey(start) && !graph.containsKey(end)) {
            graph.put(start, new ArrayList<>());
            graph.put(end, new ArrayList<>());
        }
        else if(graph.containsKey(start) && !graph.containsKey(end))
            graph.put(end, new ArrayList<>());
        else if(!graph.containsKey(start) && graph.containsKey(end))
            graph.put(start, new ArrayList<>());
        else {
            for(adjacent_vertex<T> vertex : graph.get(start)){
                if(vertex.get_name() == end) {
                    vertex.set_weight(weight);
                    return;
                }
            }
        }
        graph.get(start).add(new adjacent_vertex<>(end, weight));
    }

    //adds non-oriented edge from "start" to "end"
    public void add_nonoriented_edge(T start, T end, int weight) throws Exception {
        add_edge(start, end, weight);
        add_edge(end, start, weight);
    }

    //remove vertex
    public void remove_vertex(T vertex_name) throws Exception{
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        if(!graph.containsKey(vertex_name))
            throw new Exception("vertex with this name doesn't exist");
        graph.remove(vertex_name);
        for(T key : graph.keySet()) {
            for(adjacent_vertex<T> vertex : graph.get(key)) {
                if(vertex_name == vertex.get_name()) {
                    graph.get(key).remove(vertex);
                    break;
                }
            }
        }
    }

    //remove edge with start vertex "start" and end vertex "end"
    public void remove_edge(T start, T end) throws Exception{
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        if(start == null || end == null)
            throw new Exception("put nothing");
        if(!graph.containsKey(start) || !graph.containsKey(end))
            throw new Exception("error in vertices of graph!");
        for(adjacent_vertex<T> vertex : graph.get(start)) {
            if(vertex.get_name() == end) {
                graph.get(start).remove(vertex);
                return;
            }
        }
        throw new Exception("no this edge in graph");
    }

    //remove non-oriented edge with start vertex "start" and end vertex "end"
    public void remove_nonoriented_edge(T start, T end) throws Exception{
        remove_edge(start, end);
        remove_edge(end, start);
    }



    // depth-first search algorithm
    public ArrayList<T> DFS(T vertex_name) throws Exception {
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        if(!graph.containsKey(vertex_name))
            throw new Exception("no such vertex");
        ArrayList<T> list = new ArrayList<>();
        DFS(vertex_name, list);
        return list;
    }

    private void DFS(T vertex_name, ArrayList<T> list) {
        list.add(vertex_name);
        for(adjacent_vertex<T> vertex : graph.get(vertex_name)) {
            if(!list.contains(vertex.get_name()))
                DFS(vertex.get_name(), list);
        }
    }

    //breadth-first search algorithm
    public ArrayList<T> BFS(T vartex_name) throws Exception {
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        if(!graph.containsKey(vartex_name))
            throw new Exception("no such vertex");

        ArrayList<T> list = new ArrayList<>();
        ArrayDeque<T> queue = new ArrayDeque<>();
        queue.push(vartex_name);
        while (queue.size() != 0) {
            T vertex = queue.getLast();
            queue.remove(vertex);
            if (!list.contains(vertex))
                list.add(vertex);
            for (adjacent_vertex<T> v : graph.get(vertex))
                if (!list.contains(v.get_name())) {
                    queue.push(v.get_name());
                    list.add(v.get_name());
                }
        }
        return list;
    }

    // Dijkstra's algorithm
    public int[] Dijkstra(T vertex_name) throws Exception {
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        if(!graph.containsKey(vertex_name))
            throw new Exception("no such vertex");

        if(check_negative_weight_edge())
            throw new Exception("!edge with negative weight!");

        HashMap<T, Boolean> visit = new HashMap<>();
        HashMap<T, Integer> length = new HashMap<>();

        for(T key : graph.keySet()) {
            visit.put(key, false);
            length.put(key, Integer.MAX_VALUE);
        }
        length.replace(vertex_name, 0);

        while(unvisited(visit)) {
            T key = get_min_unvisited(length, visit);
            visit.replace(key, true);
            for (adjacent_vertex<T> vertex : graph.get(key)) {
                if (length.get(vertex.get_name()) > length.get(key) + vertex.get_weight())
                    length.replace(vertex.get_name(), length.get(key) + vertex.get_weight());
            }
        }
        int[] result = new int[graph.size()];
        int i = 0;
        for(T key : length.keySet()) {
            if(length.get(key) == Integer.MAX_VALUE) {
                result[i] = 0;
                continue;
            }
            result[i] = length.get(key);
            i++;
        }
        return result;
    }

    //for Dijkstra's algorithm: return name of vertex that wasn't visit with minimal weight
    private T get_min_unvisited(HashMap<T, Integer> lengths, HashMap<T, Boolean> visited) {
        T key = null;
        int min = Integer.MAX_VALUE;
        for(T nextKey : lengths.keySet()) {
            if(!visited.get(nextKey))
                if(lengths.get(nextKey) <= min) {
                    min = lengths.get(nextKey);
                    key = nextKey;
                }
        }
        return key;
    }


    //for Dijkstra's algorithm: return true until all vertices are visited
    private boolean unvisited(HashMap<T, Boolean> vertices) {
        for(T key : vertices.keySet())
            if(!vertices.get(key))
                return true;
        return false;
    }

    //for Dijkstra's algorithm: check for negative weight of edge
    private boolean check_negative_weight_edge() {
        for(T key : graph.keySet())
            for(adjacent_vertex<T> vertex : graph.get(key))
                if (vertex.get_weight() < 0)
                    return true;
        return false;
    }

    //Kruskal's algorithm
    public graph_a_2<T> Kruskal() throws Exception {
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        graph_a_2<T> new_graph = new graph_a_2<>();
        ArrayList<HashSet<T>> sub_graph = new ArrayList<>();
        ArrayList<Edge<T>> edges = new ArrayList<>();
        for(T key : graph.keySet())
            for(adjacent_vertex<T> vertex : graph.get(key))
                edges.add(new Edge<>(key, vertex.get_name(), vertex.get_weight()));
        edges = sort_edges(edges);

        for(Edge<T> edge : edges) {
            if(!new_graph.graph.containsKey(edge.get_start()) && !new_graph.graph.containsKey(edge.get_end())) {
                sub_graph.add(new HashSet<>());
                sub_graph.get(sub_graph.size() - 1).add(edge.get_start());
                sub_graph.get(sub_graph.size() - 1).add(edge.get_end());

                new_graph.graph.put(edge.get_start(), new ArrayList<>());
                new_graph.graph.put(edge.get_end(), new ArrayList<>());
            }

            else if(new_graph.graph.containsKey(edge.get_start()) && !new_graph.graph.containsKey(edge.get_end())) {
                new_graph.graph.put(edge.get_end(), new ArrayList<>());
                for(HashSet<T> set : sub_graph) {
                    if(set.contains(edge.get_start())){
                        set.add(edge.get_end());
                        break;
                    }
                }
            }

            else if(!new_graph.graph.containsKey(edge.get_start()) && new_graph.graph.containsKey(edge.get_end())) {
                new_graph.graph.put(edge.get_start(), new ArrayList<>());
                for(HashSet<T> set : sub_graph) {
                    if(set.contains(edge.get_end())){
                        set.add(edge.get_start());
                        break;
                    }
                }
            }

            else {
                HashSet<T> src = null;
                HashSet<T> dst = null;
                for(HashSet<T> set : sub_graph) {
                    if(set.contains(edge.get_start()))
                        src = set;
                    if(set.contains(edge.get_end()))
                        dst = set;
                }
                if(src == dst)
                    continue;

                for(T vertex : dst)
                    src.add(vertex);
                sub_graph.remove(dst);
            }
            new_graph.graph.get(edge.get_start()).add(new adjacent_vertex<>(edge.get_end(), edge.getWeight()));
        }
        return new_graph;
    }

    //Prim's algorithm
    public graph_a_2<T> Prim() throws Exception {
        if(graph.isEmpty())
            throw new Exception("graph is empty");
        graph_a_2<T> current_graph = new graph_a_2<>();
        for(T key : graph.keySet()) {
            current_graph.graph.put(key, new ArrayList<>());
            break;
        }
        Edge<T> edge;

        while((edge = get_min_edge(current_graph)).getWeight() != Integer.MAX_VALUE) {
            if(current_graph.graph.containsKey(edge.get_start()))
                current_graph.graph.put(edge.get_end(), new ArrayList<>());
            else
                current_graph.graph.put(edge.get_start(), new ArrayList<>());
            current_graph.graph.get(edge.get_start()).add(new adjacent_vertex<>(edge.get_end(), edge.getWeight()));
        }
        return current_graph;
    }

    //for Prim's algorithm: returns minimum edge
    private Edge<T> get_min_edge(graph_a_2<T> currentGraph) {
        Edge<T> min_edge = new Edge<>(null, null, Integer.MAX_VALUE);
        for(T key : graph.keySet()) {
            if(currentGraph.graph.containsKey(key)) {
                for(adjacent_vertex<T> v1 : graph.get(key))
                    if(!currentGraph.graph.containsKey(v1.get_name()))
                        if(v1.get_weight() < min_edge.getWeight())
                            min_edge = new Edge<>(key, v1.get_name(), v1.get_weight());
            }
            else {
                for(adjacent_vertex<T> v2 : graph.get(key))
                    if(currentGraph.graph.containsKey(v2.get_name()))
                        if(v2.get_weight() < min_edge.getWeight())
                            min_edge = new Edge<>(key, v2.get_name(), v2.get_weight());
            }
        }
        return min_edge;
    }

    //Floyd-Warshall algorithm
    public int[][] Floyd_Warshall() throws Exception {
        if(graph.isEmpty())
            throw new Exception("graph is empty ");
        int[][] a_m = adjecency_matrix();
        for(int k = 0; k < a_m.length; k++) {
            for (int i = 0; i < a_m.length; i++)
                for (int j = 0; j < a_m.length; j++)
                    if ((a_m[i][k] + a_m[k][j]) < a_m[i][j])
                        a_m[i][j] = a_m[i][k] + a_m[k][j];

            for (int i = 0; i < a_m.length; i++)
                if(a_m[i][i] < 0)
                    throw new Exception("negative weight cycle");
        }
        for(int k = 0; k < a_m.length; k++)
            for(int j = 0; j < a_m.length; j++)
                if(a_m[k][j] == Integer.MAX_VALUE / 2)
                    a_m[k][j] = 0;
        return a_m;
    }

    //sort edges in non-decreasing order
    private ArrayList<Edge<T>> sort_edges(ArrayList<Edge<T>> edges) {
        ArrayList<Edge<T>> sorted_edges = new ArrayList<>();
        while(!edges.isEmpty())
            sorted_edges.add(get_min_edge(edges));
        return sorted_edges;
    }

    //return edge with minimum weight
    private Edge<T> get_min_edge(ArrayList<Edge<T>> edges) {
        Edge<T> min_edge = new Edge<>(null, null, Integer.MAX_VALUE);
        for(Edge<T> e : edges)
            if(e.getWeight() < min_edge.getWeight())
                min_edge = e;
        edges.remove(min_edge);
        return min_edge;
    }

    //returns adjacency matrix
    private int[][] adjecency_matrix() {
        int[][] adjacency_matrix = new int[graph.size()][graph.size()];
        ArrayList<T> vertices = new ArrayList<>();
        for(T key : graph.keySet())
            vertices.add(key);

        int i = 0;
        for(T k1 : vertices) {
            int k2 = 0;
            for(int j = 0; j < graph.size(); j++) {
                adjacency_matrix[i][j] = Integer.MAX_VALUE / 2; //for infinity for the Floyd-Warshall
                if(k2 < graph.get(k1).size() && graph.get(k1).get(k2).get_name() == vertices.get(j)) {
                    adjacency_matrix[i][j] = graph.get(k1).get(k2).get_weight();
                    k2++;
                }
            }
            i++;
        }
        for(i = 0; i < adjacency_matrix.length; i++)
            adjacency_matrix[i][i] = 0;
        return adjacency_matrix;
    }

    //get weight
    public int get_weight() {
        int weight = 0;
        for(T key : graph.keySet())
            for(adjacent_vertex<T> vertex : graph.get(key))
                weight += vertex.get_weight();
        return weight;
    }

    //print graph
    public void print() {
        for(T key : graph.keySet()) {
            System.out.print(key + ": ");
            for(adjacent_vertex<T> vertex : graph.get(key))
                System.out.print(key + " -> " + vertex.get_name() + "(" + vertex.get_weight() + "); ");
            System.out.println();
        }
        System.out.println();
    }
}

//class of edge
class Edge<T> {
    private T start;
    private T end;
    private int weight;

    public Edge(T start, T end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public T get_start() {
        return start;
    }

    public T get_end() {
        return end;
    }

    public int getWeight() {
        return weight;
    }
}

//Class of adjacent vertex
class adjacent_vertex<T> {
    private T name;

    private int weight;

    public adjacent_vertex(T name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public T get_name() {
        return name;
    }

    public int get_weight() {
        return weight;
    }

    public void set_weight(int weight) {
        this.weight =  weight;
    }
}