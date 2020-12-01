package org.example;

import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        long leastSignificantBits = UUID.randomUUID().getLeastSignificantBits();
        System.out.println(leastSignificantBits);
        long abs = Math.abs(leastSignificantBits);
        System.out.println(abs);
    }
}
