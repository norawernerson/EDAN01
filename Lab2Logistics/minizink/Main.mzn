include "globals.mzn";

graph_size = 6;
start = 1;
n_dests = 1;
dest = [6];
n_edges = 7;
from = [1,1,2,2,3,4,4];
to = [2,3,3,4,5,5,6];
cost = [4,2,5,10,3,4,11];

% räkna ut alla möjliga vägar 
% minimera kostnaden



int: n;
array [1..n] of var 1..n: q;
predicate noattack(int: i, int: j, var int: qi, var int: qj) =
qi != qj /\ qi + i != qj + j /\ qi - i != qj - j;
constraint
forall (i in 1..n, j in i+1..n)
( noattack(i, j, q[i], q[j]) );
solve satisfy;
output[ show(q) ];
n = 8;
