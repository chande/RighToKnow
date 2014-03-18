package com.chande.righttoknow;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee>
{
    @Override
    public int compare(Employee x, Employee y){

        // Assume neither string is null.
        if (x.getPayInteger() < y.getPayInteger())
        {
            return 1;
        }
        if (x.getPayInteger() > y.getPayInteger())
        {
            return -1;
        }
        return 0;
    }
}