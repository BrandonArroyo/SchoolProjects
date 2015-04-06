import java.lang.*;
import java.util.*;
import java.io.*;

public class test_get_output {
    
    private static BitSet fromString(final String s) {
        return BitSet.valueOf(new long[] { Long.parseLong(s, 2) });
    }
    
    private static String toString(BitSet bs) {
        return Long.toString(bs.toLongArray()[0], 2);
    }
    
    private static String AND(String a, String b) {
        BitSet c = fromString(a);
        BitSet d = fromString(b);
        c.and(d);
        return toString(c);
    }
    
    private static String OR(String a, String b) {
        BitSet c = fromString(a);
        BitSet d = fromString(b);
        c.or(d);
        return toString(c);
    }
    
    public String get_output(String entry) throws IOException {
        
        int index;
        String gate;
        String calculation = "-1";
        ArrayList<String> inputs = new ArrayList<String>();
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(entry));
        tokenizer.nextToken();
        Double token = tokenizer.nval;
        index = token.intValue() - 1;
        System.out.println("Index: " + index);
        tokenizer.nextToken();
        gate = tokenizer.sval;
        System.out.println("Gate: " + gate);
        Integer t = 0;
        
        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            Double temp = tokenizer.nval;
            System.out.println("last number: " + temp.intValue());
            inputs.add(String.valueOf(temp.intValue() - 1));
        }
        
        int num_of_inputs = inputs.size();
        
        if (gate.equals("AND")) {
            for(int i = 0; i < num_of_inputs; ++i) {
                if(i > 0) calculation = AND(calculation, circuit_output.get(inputs.get(i+1)));
                else calculation = AND(circuit_output.get(inputs.get(i)), circuit_output.get(inputs.get(i+1)));
            }
        }
        
        else if (gate.equals("OR")) {
            for(int i = 0; i < num_of_inputs; ++i) {
                 if(i > 0) calculation = OR(calculation, fromString(circuit_output.get(inputs.get(i+1))));
                 else calculation = OR(circuit_output.get(inputs.get(i)), circuit_output.get(inputs.get(i+1)));
            }
        }
        
        else if (gate.equals("NOT")) {
            System.out.println(circuit_output.get(inputs.get(0)));
            
            t = fromString(circuit_output.get(inputs.get(0)));
        }
        
        System.out.println("\ncalculation: " + t + "\n");
        return calculation;
    }

    public static void main(String[] args) throws IOException { 
        //System.out.println(get_output("1 AND 1 2"));
    }
}

