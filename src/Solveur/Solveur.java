package Solveur;

import instance.Instance;
import solution.Championnat;

public interface Solveur {

    public String getNom();
    public Championnat solve(Instance instance);
}