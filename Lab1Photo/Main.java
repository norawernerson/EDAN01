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
    int size = 9;
    // define finite domain variables
    int n_prefs = 17;
    int[][] prefs = {{1,3}, {1,5}, {1,8},
       {2,5}, {2,9}, {3,4}, {3,5}, {4,1},
       {4,5}, {5,6}, {5,1}, {6,1}, {6,9},
       {7,3}, {7,8}, {8,9}, {8,7}};
    //domain
    IntVar[] v = new IntVar[size];
    for( int i = 0; i < size; i++){
      v[i] = new IntVar(store, "v"+i, 1, size);
    }
    Constraint ctr = new Alldifferent(v);
    store.impose(ctr);


    // define constraints
    store.impose(new XeqY(v[0],  );
    store.impose(new XneqY(v[0], v[2]) );
    store.impose(new XneqY(v[1], v[2]) );
    store.impose(new XneqY(v[1], v[3]) );
    store.impose(new XneqY(v[2], v[3]) );
    // search for a solution and print results
    Search<IntVar> search = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,new IndomainMin<IntVar>());
    boolean result = search.labeling(store, select);
    if( result )
      System.out.println("Solution: " + v[0]+", "+v[1] +", "+v[2] +", "+v[3]);
    else
      System.out.println("***No");
  }
}
