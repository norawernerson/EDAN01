import org.jacop.core.*;
import org.jacop.constraints.*;
import org.jacop.search.*;
import org.jacop.set.core.*;
import org.jacop.set.constraints.*;
import org.jacop.set.search.*;
import org.jacop.constraints.netflow.NetworkFlow;
import org.jacop.constraints.netflow.NetworkBuilder;
import org.jacop.constraints.netflow.simplex.*;
import java.util.*;

public class Main{
  static Main m = new Main();

  public static void main(String[] args){
    Store store = new Store();

    // int graph_size = 6;
    // int start = 0;
    // int n_dests = 1;
    // int[] dest = {5};
    // int n_edges = 8;
    // int[] from = {0,0,1,1,3,3,2,4};
    // int[] to = {1,3,2,4,2,4,5,5};
    // int[] costa = {0,0,3,2,5,6,0,0};

    int graph_size = 6;
    int start = 1;
    int n_dests = 1;
    int[] dest = {6};
    int n_edges = 7;
    int[] from = {1,1,2,2,3,4,4};
    int[] to = {2,3,3,4,5,5,6};
    int[] costa = {4,2,5,10,3,4,11};

    // int graph_size = 6;
    // int start = 1;
    // int n_dests = 2;
    // int[] dest = {5,6};
    // int n_edges = 7;
    // int[] from = {1,1,2, 2,3,4, 4};
    // int[] to = {2,3,3, 4,5,5, 6};
    // int[] cost = {4,2,5,10,3,4,11};

    IntVar[] x = new IntVar[n_edges+2];
    NetworkBuilder net = new NetworkBuilder();

    ArrayList<Node> nodeList = new ArrayList<Node>();

    // nodeList.add(net.addNode("source", 1));
    for (int i = 0; i < graph_size; i++){
      if(i == (start-1)){
        nodeList.add(net.addNode("source", 1));
      }else if(i == dest[0]-1){
        nodeList.add(net.addNode("sink", -1));
      }else{
        nodeList.add(net.addNode("N"+i, 0));
      }
    }
    // nodeList.add(net.addNode("sink", -1));

    for (int i = 0; i < n_edges; i++){
      x[i] = new IntVar(store, "E"+i, 0, 1);
    }

    for (int i = 0; i < n_edges; i++){
      net.addArc(nodeList.get(from[i]-1), nodeList.get(to[i]-1), costa[i], x[i]);
    }
    // net.addArc(nodeList.get(0), nodeList.get(start), 0, x[0]);
    // net.addArc(nodeList.get(dest[0]), nodeList.get(nodeList.size()-1), 0, x[n_edges+1]);

    IntVar cost = new IntVar(store, "cost", 0, 1000);
    net.setCostVariable(cost);
    store.impose(new NetworkFlow(net));

    Search<IntVar> search = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store,x, new IndomainMin<IntVar>());
    boolean result = search.labeling(store, select, cost); // sista variabeln måste vara en IntVar

    if( result ){
      System.out.println("Solved");

    } else{
      System.out.println("***No");
    }
  }
}
