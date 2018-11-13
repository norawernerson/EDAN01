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

    //atother domain
    IntVar[] cost = new IntVar[prefs.length];
    for (int i = 0; i < cost.length; i++){
      cost[i] = new IntVar(store, 0, 1);
    }

    for(int i = 1; i < n_prefs-1 ; i++){
      PrimitiveConstraint c1 = new XeqY(v[prefs[i][0]-1], new IntVar(store, v[prefs[i][1]-1].value()+1, v[prefs[i][1]-1].value()+1));
      PrimitiveConstraint c2 = new XeqY(v[prefs[i][0]-1], new IntVar(store, v[prefs[i][1]-1].value()-1, v[prefs[i][1]-1].value()-1));
      PrimitiveConstraint[] c3 = {c1, c2};
      store.impose(new IfThenElse( new Or(c3), new XeqC(cost[i], 0), new XeqC(cost[i], 1)));
      // if((v[prfes[i][0]].value() == v[prfes[i][1]]+1)) || (v[prfes[i][0]].value() == v[prfes[i][1]]-1))){
      //   XeqC(cost[i], 0);
      // }else{
      //   XeqC(cost[i], 1);
      // }
    }
    PrimitiveConstraint c1 = new XeqY(v[prefs[0][0]-1],new IntVar(store, v[prefs[0][1]-1].value()+1, v[prefs[0][1]-1].value()+1));
    store.impose(new IfThenElse( c1, new XeqC(cost[0], 0), new XeqC(cost[0], 1)));
    PrimitiveConstraint c2 = new XeqY(v[prefs[size-1][0]-1],new IntVar(store, v[prefs[size-1][1]-1].value()-1, v[prefs[size-1][1]-1].value()-1));
    store.impose(new IfThenElse( c2, new XeqC(cost[size-1], 0), new XeqC(cost[size-1], 1)));

    IntVar c = new IntVar(store, "cost", 0, n_prefs);
    store.impose(new SumInt(store, cost, "==", c));

    Search<IntVar> search = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,new IndomainMin<IntVar>());
    boolean result = search.labeling(store, select, c);
    if( result ){
      System.out.println("number of prefs fulfilled: " + (n_prefs-c.value()));
      System.out.println("Solution: " + v[0]+", "+v[1] +", "+v[2] +", "+v[3]);
    } else{
      System.out.println("***No");
    }

  }
}
