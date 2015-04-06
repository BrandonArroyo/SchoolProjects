import java.lang.*;
import java.util.*;
import java.io.*;

public class test {
    
    public static int randomInt(int min, int max) {
         Random rand = new Random();
         int randomNum = rand.nextInt((max - min) + 1) + min;
         return randomNum;
    }
    
    public static void main(String[] args) throws IOException { 
        
        
        System.out.println(Math.pow(2, Math.pow(2, 3)));

    }
}