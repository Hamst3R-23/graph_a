package vlad;

public interface graph_a_1<T> {
    //add vertex
    void add_vertex(T vertex_name) throws Exception;

    //remove vertex
    void remove_vertex(T vertex_name) throws Exception;

    //add oriented edge connecting "start" and "end"
    void add_edge(T start, T end, int weight) throws Exception;

    //add non-oriented edge connecting "start" and "end"
    void add_nonoriented_edge(T start, T end, int weight) throws Exception;

    //remove edge from "start" to "end"
    void remove_edge(T start, T end) throws Exception;

    //removes non-oriented edge between "start" and "end"
    void remove_nonoriented_edge(T start, T end) throws Exception;

    //display graph
    void print();
}