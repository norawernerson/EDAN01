include "cumulative.mzn";
include "diffn.mzn";
include "jacop.mzn";

int : del_add;
int : del_mul;
int : number_add;
int : number_mul;
int : n; % number of operations
set of int : last;
set of int : add;
set of int : mul;
array[1..n] of set of int : dependencies;

array[1..n] of var 0..1000 : t;

constraint forall(i in 1..n, j in dependencies[i])(
  t[i] + if i in mul then del_mul else del_add endif <= t[j]
);

array[1..length(add)] of var int : t_add = [t[i] | i in 1 .. length(add)];
array[1..length(mul)] of var int : t_mul = [t[i] | i in 1 .. length(mul)];


array[1..length(add)] of int : d_add = [del_add | i in 1 .. length(add)];
array[1..length(mul)] of int : d_mul = [del_mul | i in 1 .. length(mul)];


constraint cumulative([t[i] | i in add], d_add, [1 | i in add], number_add);
constraint cumulative([t[i] | i in mul], d_mul, [1 | i in mul], number_mul);

array[1..length(mul)] of var 0..number_mul : avaliable_mul;
array[1..length(add)] of var 0..number_add : avaliable_add;
array[1..n] of var int: a_all;

array[1..n] of int : del = [if i in mul then del_mul else del_add endif | i in 1..n];


constraint diffn(t_mul, avaliable_mul, d_mul, [1 | i in 1..length(mul)]);
constraint diffn(t_add, avaliable_add, d_add, [1 | i in 1..length(add)]);

array[1..n] of var int : end_time = 
[t[i] + if i in mul then del_mul else del_add endif | i in 1 .. n];

array[1..length(last)] of var int : finish = 
[end_time[i] | i in last];

output["finish= \(finish)"];

var int : cost = max(finish[1], finish[2]);

solve :: seq_search([
    int_search(t, smallest_max, indomain_min, complete),
    int_search(avaliable_add++avaliable_mul, first_fail, indomain_min, complete)
  ])
minimize cost;


output [ "cost= \(cost) t= " ];
output [ show(t)];
output["avaliable_mul= \(avaliable_mul)\n"];
output["avaliable_add= \(avaliable_add)\n"];
