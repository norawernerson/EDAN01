n = 5;
n_commercial = 13;
n_residential = 12;

point_distribution = array1d(0..n, [-5, -4, -3, 3, 4, 5]);

int : n;
int : n_commercial;
int : n_residential;
array[0..n] of int : point_distribution;

array[1..n,1..n] of var 1..n: x;

array[1..n, 1..n] of var int : block;
block = array2d(1..n, 1..n, [x[i,j] | i in 1..n, j in 1..n]);

int : cost = 0;

array[1..n] of var int : cost_array1;
array[1..n] of var int : cost_array2;

%counts cost for rows
constraint forall(i in 1..n)(
  cost_array1[i] = sum(j in 1..n)(block[i,j])
);

%counts cost for columns
constraint forall(i in 1..n)(
  cost_array2[i] = sum(j in 1..n)(block[j,i])
);

array[1..n] of var int : cost_a = [ point_distribution[cost_array1[i]]| i in 1..n];
array[1..n] of var int : cost_b = [ point_distribution[cost_array2[i]]| i in 1..n];

%max #n_commercial 1s
constraint sum(j in 1..n)(cost_array1[j]) == 13;

var int : c = sum(j in 1..n)(cost_a[j]) + sum(j in 1..n)(cost_b[j]);

solve maximize c;
% solve :: seq_search([
%     int_search(t, smallest_max, indomain_min, complete),
%     int_search(avaliable_add++avaliable_mul, first_fail, indomain_min, complete)
%   ])
% minimize cost;


% output [ "cost= \(cost) t= " ];
% output [ show(t)];
% output["avaliable_mul= \(avaliable_mul)\n"];
% output["avaliable_add= \(avaliable_add)\n"];
