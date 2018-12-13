% Auto Regression filter
int : del_add;
del_add = 1;
int : del_mul;
del_mul = 2;

int : number_add;
number_add = 1;
int : number_mul;
number_mul = 1;

int : n; % number of operations
n = 28;
set of int : last;
last = {27,28};
set of int : add;
add = {9,10,11,12,13,14,19,20,25,26,27,28};
set of int : mul;
mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

array[1..n] of set of int : dependencies;
dependencies = [
{9},
{9},
{10},
{10},
{11},
{11},
{12},
{12},
{27},
{13},
{14},
{28},
{15,17},
{16,18},
{19},
{19},
{20},
{20},
{21,23},
{22,24},
{25},
{25},
{26},
{26},
{27},
{28},
{},
{},
];

% precedence constraints
% impose t_i + 1/2 ≤ t_j där j är från dependencies
array[1..n] of var 0..1000 : t;

%constraint forall(o in OBJECTS)(start[o] +duration[o] <= end);
constraint forall(i in 1..n, j in dependencies[i])(
  t[i] + if i in mul then del_mul else del_add endif <= t[j]
);

array[1..length(add)] of int : d_add = [del_add | i in 1 .. length(add)];
array[1..length(mul)] of int : d_mul = [del_mul | i in 1 .. length(mul)];

%constraint cumulative(start, duration, handlers, available_handlers);
constraint cumulative([t[i] | i in add], d_add, [1 | i in add], number_add);
constraint cumulative([t[i] | i in mul], d_mul, [1 | i in mul], number_mul);


%diff2([[t1,r1, d1, 1], [t2,r2, d2, 1], . . . , [tn,rn, dn, 1]])
constraint diff2([[t[i],1, if i in mul then del_mul else del_add endif, 1] | i in n]);

solve minimize t;

output [ "start = \(t[0])\nend = \(t[n]+if n in mul then del_mul else del_add endif)\n"];
