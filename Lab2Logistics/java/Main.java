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

    // int graph_size = 6;
    // int start = 1;
    // int n_dests = 2;
    // int[] dest = {5,6};
    // int n_edges = 7;
    // int[] from = {1,1,2, 2,3,4, 4};
    // int[] to = {2,3,3, 4,5,5, 6};
    // int[] cost = {4,2,5,10,3,4,11};

    // int graph_size = 6;
    // int start = 1;
    // int n_dests = 2;
    // int[] dest = {5,6};
    // int n_edges = 9;
    // int[] from = {1,1,1,2,2,3,3,3,4};
    // int[] to = {2,3,4,3,5,4,5,6,6};
    // int[] cost = {6,1,5,5,3,5,6,4,2};


    NetworkBuilder net = new NetworkBuilder();

    ArrayList<Node> nodeList = new ArrayList<Node>();

    Node source = net.addNode("source", 1);
    Node sink = net.addNode("sink", -1);
    for (int i = 0; i < graph_size; i++){
      nodeList.add(i, net.addNode("n"+Integer.toString(i) , 0));
    }

    IntVar[] flow = new IntVar[n_edges*2];

    for(int i = 0; i < n_edges; i++){
      flow[i] = new IntVar(store, "E"+i, 0, n_dests);
      flow[i+n_edges] = new IntVar(store, "B"+i, 0, n_dests);
      System.out.println(flow[i]+"  cost = "+cost[i]);
      net.addArc(nodeList.get(to[i]-1), nodeList.get(from[i]-1), cost[i], flow[i+n_edges]);
      net.addArc(nodeList.get(from[i]-1), nodeList.get(to[i]-1), cost[i], flow[i]);
    }
    // flow[n_edges] = new IntVar(store, "E"+n_edges, 0, 100);
    // flow[n_edges+1] = new IntVar(store, "E"+n_edges+1, 0, 100);
    net.addArc(source, nodeList.get(start-1), 0, n_dests, n_dests);

    for(int i = 0; i < n_dests; i++){
      net.addArc(nodeList.get(dest[i]-1), sink, 0, 1, 1);
    }

    IntVar cost1 = new IntVar(store, "cost", 0, 1000);
    net.setCostVariable(cost1);

    IntVar[] costArray = new IntVar[n_edges*2];
    for(int i = 0; i < costArray.length; i++){
      IntVar z = new IntVar(store, "sum", 0, 10000);
      costArray[i] = new IntVar(store, "c"+i, 0, 10000);
      store.impose(new XmulCeqZ(flow[i], cost[i%n_edges], costArray[i]));
    }

    for(int i = 0; i < costArray.length; i++){
    }

    Constraint c = new NetworkFlow(net);
    store.impose(c);
    IntVar sum = new IntVar(store, "sum", 0, 10000);
    store.impose(new SumInt(store, costArray, "==", sum));

    Search<IntVar> search = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store,flow, new IndomainMin<IntVar>());
    boolean result = search.labeling(store, select, cost1); // sista variabeln måste vara en IntVar

    if( result ){
      System.out.println("Summan: " + sum);
      // System.out.println(java.util.Arrays.asList(flow));
      // System.out.println(nodeList);
      System.out.println("Solved");

    } else{
      System.out.println("***No");
    }
  }
}
