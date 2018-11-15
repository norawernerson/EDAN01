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
