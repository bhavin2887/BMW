package com.coreiot.javaprogram;

public class MyClass {
    public static void main (String[] args){

        final double first=15.55;//value extract by unmodifiable format method
        final double second=15.56;//value extract by unmodifiable format method
        final double firstDifference= first+second; //I receive 44899.6



        final float first1=15.55f;//value extract by unmodifiable format method
        final float second1=15.56f;//value extract by unmodifiable format method
        final float firstDifference1= first1+second1; //I receive 44899.6

        System.out.println(firstDifference);
        System.out.println(firstDifference1);

    }
}
