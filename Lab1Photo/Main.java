 import org.jacop.core.*;
import org.jacop.constraints.*;
import org.jacop.search.*;
import org.jacop.set.core.*;
import org.jacop.set.constraints.*;
import org.jacop.set.search.*;
//import 1.java;

public class Main{
  static Main m = new Main();

  public static void main(String[] args){
    Store store = new Store();// define store
    // define finite domain variables
    // int size = 9;
    // int n_prefs = 17;
    // int[][] prefs = {{1,3}, {1,5}, {1,8},
    //    {2,5}, {2,9}, {3,4}, {3,5}, {4,1},
    //    {4,5}, {5,6}, {5,1}, {6,1}, {6,9},
    //    {7,3}, {7,8}, {8,9}, {8,7}};

     int size = 11;
     int n_prefs = 20;
     int[][] prefs = {{1,3}, {1,5}, {2,5},
        {2,8}, {2,9}, {3,4}, {3,5}, {4,1},
        {4,5}, {4,6}, {5,1}, {6,1}, {6,9},
        {7,3}, {7,5}, {8,9}, {8,7}, {8,10},
        {9, 11}, {10, 11}};

    // int size = 15;
    //    int n_prefs = 20;
    //    int[][] prefs = {{1,3}, {1,5}, {2,5},
    //       {2,8}, {2,9}, {3,4}, {3,5}, {4,1},
    //       {4,15}, {4,13}, {5,1}, {6,10}, {6,9},
    //       {7,3}, {7,5}, {8,9}, {8,7}, {8,14},
    //       {9, 13}, {10, 11}};

    //domain
    IntVar[] v = new IntVar[size];
    for( int i = 0; i < size; i++){
      v[i] = new IntVar(store, "v"+i, 1, size);
    }
    Constraint ctr = new Alldifferent(v);
    store.impose(ctr);

    //another domain??
    IntVar[] cost = new IntVar[prefs.length];
    for (int i = 0; i < cost.length; i++){
      cost[i] = new IntVar(store, 0, 1);
    }



    IntVar[] dist = new IntVar[n_prefs];
    for(int i = 0; i < n_prefs; i++){
      IntVar c = new IntVar(store, "c", 1, size);
      store.impose(new Distance(v[prefs[i][0]-1], v[prefs[i][1]-1], c));
      dist[i] = c;
      store.impose(new Reified(new XgtC(c, 1), cost[i]));

    }

    IntVar summan = new IntVar(store, "sum", 1, n_prefs);
    Constraint ctr1 = new Max(dist, summan);
    store.impose(ctr1);



    IntVar c = new IntVar(store, "cost", 0, n_prefs);
    store.impose(new SumInt(store, cost, "==", c));


    Search<IntVar> search = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,new IndomainMin<IntVar>());
    boolean result = search.labeling(store, select, summan);

    if( result ){
      System.out.println("number of prefs fulfilled: " + (n_prefs - c.value()));
      System.out.println("Solution: " + v[0]+", "+v[1] +", "+v[2] +", "+v[3]);
    } else{
      System.out.println("***No");
    }

  }
}
