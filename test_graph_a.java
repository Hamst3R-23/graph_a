package vlad;

import java.util.Arrays;

public class test_graph_a {
    public static void main(String[] args) throws Exception {
        try {
            System.out.println("Default operations:");
            System.out.println("Creating graph 'by hands':");
            graph_a_2<String> graph1 = new graph_a_2<>();
            graph_a_2<String> graph2;

            graph1.add_vertex("A");
            graph1.add_vertex("B");
            graph1.add_vertex("C");
            graph1.add_vertex("D");
            graph1.add_vertex("E");


            graph1.add_edge("A", "B", 1);
            graph1.add_edge("B", "E", 2);
            graph1.add_edge("C", "D", 3);
            graph1.add_nonoriented_edge("A", "D", 4);
            graph1.add_nonoriented_edge("B", "A", 5);
            System.out.println("Initial graph:");
            graph1.print();

            graph1.remove_vertex("E");
            graph1.remove_edge("C", "D");
            graph1.remove_nonoriented_edge("A", "D");
            System.out.println("After removing vertex F, edge between C and D, non-oriented edge between A and D:");
            graph1.print();

            graph2 = new graph_a_2<>("tests/test1.txt");
            System.out.println("Graph from test1.txt:");
            graph2.print();
            System.out.println();
            System.out.println();


            System.out.println("DFS:");
            graph_a_2<String> graph3;
            graph3 = new graph_a_2<>("tests/test2.txt");
            System.out.println("Graph from test2.txt:");
            graph3.print();
            System.out.println("Start - A");
            System.out.println(graph3.DFS("A"));
            System.out.println();
            System.out.println();

            System.out.println("BFS:");
            graph_a_2<String> graph4;
            graph4 = new graph_a_2<>("tests/test2.txt");
            System.out.println("Graph from test2:");
            graph4.print();
            System.out.println("Start - A");
            System.out.println(graph4.BFS("A"));
            System.out.println();
            System.out.println();

            System.out.println("Dijkstra's algorithm:");
            graph_a_2<String> graph5;
            graph5 = new graph_a_2<>("tests/test3.txt");
            System.out.println("Graph from test3.txt:");
            graph5.print();
            System.out.println("Start - A");
            System.out.println(Arrays.toString(graph5.Dijkstra("A")));
            System.out.println();
            System.out.println();

            System.out.println("Kruskal's algorithm:");
            graph_a_2<String> graph6;
            graph6 = new graph_a_2<>("tests/test4.txt");
            System.out.println("Graph from test4.txt:");
            graph6.print();
            System.out.println("Kruskal's algorithm:");
            graph_a_2<String> spanningGraph = graph6.Kruskal();
            spanningGraph.print();
            System.out.println();
            System.out.println();

            System.out.println("Prims's algorithm:");
            graph_a_2<String> graph7;
            graph7 = new graph_a_2<>("tests/test5.txt");
            System.out.println("Graph from test5:");
            graph7.print();
            System.out.println("Prim's algorithm:");
            graph_a_2<String> tmp_graph7 = graph7.Prim();
            tmp_graph7.print();
            System.out.println();
            System.out.println();

            System.out.println("Floyd-Warshall algorithm:");
            graph_a_2<String> graph8;
            graph8 = new graph_a_2<>("tests/test6.txt");
            System.out.println("Graph from test6.txt:");
            graph8.print();
            System.out.println("Floyd-Warshall algorithm");
            int[][] result = graph8.Floyd_Warshall();
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result.length; j++)
                    System.out.print(result[i][j] + " ");
                System.out.println();
            }
            System.out.println();
            System.out.println();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}