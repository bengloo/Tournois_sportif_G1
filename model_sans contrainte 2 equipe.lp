\ENCODING=ISO-8859-1
\Problem name: ilog.cplex

Minimize
 obj1:
Subject To
 CI1e0j0: x0_1_0 + x1_0_0  = 1
 CI1e1j0: x0_1_0 + x1_0_0  = 1
 CI1e0j1: x0_1_1 + x1_0_1  = 1
 CI1e1j1: x0_1_1 + x1_0_1  = 1
 CI2d0e0: x0_0_0 + x0_0_1  = 0
 CI2d0e1: x0_1_0 + x0_1_1  = 1
 CI2d1e0: x1_0_0 + x1_0_1  = 1
 CI2d1e1: x1_1_0 + x1_1_1  = 0
 CI3d0e1: x0_1_0 + x1_0_0  = 1
 CI3d1e0: x0_1_0 + x1_0_0  = 1
Bounds
 0 <= x0_1_0 <= 1
 0 <= x1_0_0 <= 1
 0 <= x0_1_1 <= 1
 0 <= x1_0_1 <= 1
 0 <= x0_0_0 <= 1
 0 <= x0_0_1 <= 1
 0 <= x1_1_0 <= 1
 0 <= x1_1_1 <= 1
Generals
 x0_1_0  x1_0_0  x0_1_1  x1_0_1  x0_0_0  x0_0_1  x1_1_0  x1_1_1 
End
