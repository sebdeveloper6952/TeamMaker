package com.example.sebdeveloper6952.teammaker;

/**
 * Created by sevic69 on 10/2/2016.
 */

public class CMember
{
    public String name;
    public double weight;

    public CMember(String name, double weight)
    {
        this.name = name;
        this.weight = weight;
    }

    @Override
    public String toString()
    {
        return this.name + " " + this.weight;
    }
}
