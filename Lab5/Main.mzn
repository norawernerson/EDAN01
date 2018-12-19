include "globals.mzn";

int : n;
int : n_commercial;
int : n_residential;
array[0..n] of int : point_distribution;


array[1..n,1..n] of var 1..n: x;


% Våran matris som ska "fyllas" med 1 och 0, 
array[1..n, 1..n] of var 0..1 : block;

% Residential = 1
% Commercial = 0

array[1..n] of var int : row;
array[1..n] of var int : col;

%counts cost for rows
constraint forall(i in 1..n)(
  row[i] = sum(j in 1..n)(block[i,j])
);

%counts cost for columns
constraint forall(i in 1..n)(
  col[i] = sum(j in 1..n)(block[j,i])
);


%remove symetric sloutions
constraint lex2(block);


array[1..n] of var int : cost_row = [ point_distribution[row[i]]| i in 1..n];
array[1..n] of var int : cost_col = [ point_distribution[col[i]]| i in 1..n];


var int : c_row = sum(j in 1..n)(cost_row[j]);
var int : c_col = sum(j in 1..n)(cost_col[j]);

%max #residentials 1s
var int: nbr_res = sum(row);
constraint nbr_res = n_residential; 


var int : c = c_row + c_col;


solve maximize c;


output["block= \(block)\n"];
output["score= \(c)\n"];

