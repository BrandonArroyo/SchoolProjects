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
        
        for (int i = 0; i < 10; ++i) {
            System.out.println(randomInt(1, 1));
        }
    }

}