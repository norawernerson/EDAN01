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

    int graph_size = 6;
    int start = 1;
    int n_dests = 1;
    int[] dest = {6};
    int n_edges = 7;
    int[] from = {1,1,2,2,3,4,4};
    int[] to = {2,3,3,4,5,5,6};
    int[] cost = {4,2,5,10,3,4,11};


    // IntVar[] v = new IntVar[graph_size];
    // for( int i = 0; i < graph_size; i++){
    //   v[i] = new IntVar(store, "v"+i, 1, graph_size);
    // }
    //
    //
    // Constraint maxCost = new SumInt(cost, "==", maxCost);
    //
    // IntVar[] routCost = new IntVar[prefs.length];
    // for (int i = 0; i < routCost.length; i++){
    //   routCost[i] = new IntVar(store, 0, maxCost);
    // }

    // store.impose(new Alldiff());


    NetworkBuilder net = new NetworkBuilder();

    ArrayList<Node> nodeList = new ArrayList<Node>();




    Node source = net.addNode("source", 5);
    Node sink = net.addNode("sink", -5);
    for (int i = 1; i < graph_size + 1; i++){
      nodeList.add(i-1, net.addNode("n"+Integer.toString(i) , 0));
    }
    for(int i = 0; i < n_edges; i++){
      net.addArc(nodeList.get(from[i]-1), nodeList.get(to[i]-1), cost[i]);
    }
    net.addArc(source, nodeList.get(start-1), 0);
    net.addArc(nodeList.get(dest[0]-1), sink, 0);

    store.impose(new NetworkFlow(net));

    Search<IntVar> search = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, net,new IndomainMin<IntVar>());
    boolean result = search.labeling(store, select, net);

    if( result ){
      System.out.println("Solved");

    } else{
      System.out.println("***No");
    }
  }
}
