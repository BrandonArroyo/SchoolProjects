import java.lang.*;
import java.util.*;
import java.io.*;


public class Circuit {
    
    private int not_gates;
    public ArrayList<String> circuit; //circuit format: line 1 stroed at index 0, 2 at 1,  etc.
    public ArrayList<String> circuit_output; // this is the output of the circuit
    private int not_gates_allowed;
    public ArrayList<Integer> solution_lines; //this will contain what lines are the solution
    public ArrayList<String> solution_outputs_wanted; //this will contain the desired truth table columns
    
    //added GA variables 
    private double fitness_value = -1;
    public ArrayList<Integer> not_gate_indices; //figure out
    
    public Circuit(ArrayList<String> in_circuit, int in_not_gates, ArrayList<String> in_circuit_output, int in_not_gates_allowed, ArrayList<String> in_solution_outputs_wanted, ArrayList<Integer> in_solution_lines, ArrayList<Integer> in_not_gate_indices) {
        circuit = in_circuit;
        not_gates = in_not_gates;
        circuit_output = in_circuit_output;
        not_gates_allowed = in_not_gates_allowed;
        solution_outputs_wanted = in_solution_outputs_wanted;
        solution_lines = in_solution_lines;
        not_gate_indices = in_not_gate_indices;
        setFitnessValue();
    }
   
    public ArrayList<String> getCircuitOutput() {
        return circuit_output;
    }
    
    public ArrayList<String> getCircuit() {
        return circuit;
    }
    
    public int getNotGates() {
        return not_gates;
    }
    
    public int getNotGatesAllowed() {
        return not_gates_allowed;
    }
    
    public double getFitnessValue() {
        return fitness_value;
    }
    
    
    //fitness fuction should be number of outputs correct(most priority) + number of unique inputs
    public void setFitnessValue() {
        Set<String> unique_outputs = new HashSet<String>(circuit_output);
        double solution_goal = solution_outputs_wanted.size();
        double number_solutions_value = solution_lines.size() * 100;
        double unique_solutions = Math.pow(2, Math.pow(2, 3));
        fitness_value = number_solutions_value + (unique_outputs.size() * ((solution_goal * 100) / unique_solutions));
        //System.out.println("solution goal " + solution_goal);
        //System.out.println("number_solutions_value " + number_solutions_value);
        //System.out.println("unique_solutions " + unique_solutions);
    }
    
    //used in getPossibleGates() to test an output against the solutions
    private boolean isSolution(String output) {
        for (String sol : solution_outputs_wanted) {
            if (output.equals(sol)) {
                return true;
            }
        }
        return false;
    }
    
    //if entrys output == any output from this circuit, return true
    public boolean testEntry(String entry) throws IOException {
        String test = getOutput(entry);
        for(int i = 0; i < circuit_output.size(); ++i) {
            if (test.equals(circuit_output.get(i))) {
                return true;
            }
        }
        return false;
    }
    
   // gets outputs of line
    public String getOutput(String entry) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(entry));
        tokenizer.nextToken();
        Double token = tokenizer.nval;
        int index = token.intValue() - 1;
        tokenizer.nextToken();
        String gate = tokenizer.sval;
        
        ArrayList<Integer> inputs = new ArrayList<Integer>();
        String calculation = "-1";
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            Double temp = tokenizer.nval;
            inputs.add(temp.intValue() - 1);
        }
        int num_of_inputs = inputs.size() - 1;
        if (gate.equals("AND")) {
            for(int i = 0; i < num_of_inputs; ++i) {
                if(i > 0) calculation = AND(calculation, circuit_output.get(inputs.get(i+1)));
                else calculation = AND(circuit_output.get(inputs.get(i)), circuit_output.get(inputs.get(i+1)));
            }
        }
        else if (gate.equals("OR")) {
            for(int i = 0; i < num_of_inputs; ++i) {
                 if (i > 0) calculation = OR(calculation, circuit_output.get(inputs.get(i+1)));
                 else calculation = OR(circuit_output.get(inputs.get(i)), circuit_output.get(inputs.get(i+1)));
            }
        }
        else if (gate.equals("NOT")) {
            calculation = NOT(circuit_output.get(inputs.get(0)));
        }
        return calculation;
    }
    
    //---------------------------------------------------------------------------------------------
    //below are functions that allow us to perform bitwise operations (&, |, ~) on strings
    
    public static BitSet fromString(final String s) {
        return BitSet.valueOf(new long[] { Long.parseLong(s, 2) });
    }

    public static String toString(BitSet bs) {
        if (bs.isEmpty()){
            String temp = bs.toString();
            int length = temp.length();
            return new String(new char[length]).replace("\0", "0");
        }
        else
            return Long.toString(bs.toLongArray()[0], 2);
    }
    
    public static String AND(String a, String b) {
        BitSet c = fromString(a);
        BitSet d = fromString(b);
        c.and(d);
        return add_zeros(a, toString(c));
    }
     
    public static String OR(String a, String b) {
        BitSet c = fromString(a);
        BitSet d = fromString(b);
        c.or(d);
        return add_zeros(a, toString(c));
    }
    
    public static String NOT(String a) {
        BitSet c = fromString(a);
        int length = a.length();
        c.flip(0, length);
        return add_zeros(a, toString(c));
    }
    
    public static String add_zeros(String a, String b) {
        int difference = a.length() - b.length();
        return new String(new char[difference]).replace("\0", "0") + b;
    }
    
    //---------------------------------------------------------------------------------------------
        
//------------------------------------------------------------------------------------
/*
                    Genetic algorithm stuff below here

*/

    public static int randomInt(int min, int max) {
         Random rand = new Random();
         int randomNum = rand.nextInt((max - min) + 1) + min;
         return randomNum;
    }
    
    // gets outputs of line
    public static String getOutput2(String entry, ArrayList<String> outputs) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(entry));
        tokenizer.nextToken();
        Double token = tokenizer.nval;
        int index = token.intValue() - 1;
        tokenizer.nextToken();
        String gate = tokenizer.sval;
        
        ArrayList<Integer> inputs = new ArrayList<Integer>();
        String calculation = "-1";
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            Double temp = tokenizer.nval;
            inputs.add(temp.intValue() - 1);
        }
        int num_of_inputs = inputs.size() - 1;
        if (gate.equals("AND")) {
            for(int i = 0; i < num_of_inputs; ++i) {
                if(i > 0) calculation = AND(calculation, outputs.get(inputs.get(i+1)));
                else calculation = AND(outputs.get(inputs.get(i)), outputs.get(inputs.get(i+1)));
            }
        }
        else if (gate.equals("OR")) {
            for(int i = 0; i < num_of_inputs; ++i) {
                 if (i > 0) calculation = OR(calculation, outputs.get(inputs.get(i+1)));
                 else calculation = OR(outputs.get(inputs.get(i)), outputs.get(inputs.get(i+1)));
            }
        }
        else if (gate.equals("NOT")) {
            calculation = NOT(outputs.get(inputs.get(0)));
        }
        return calculation;
    }
    
    public static boolean isSolution2(String output, ArrayList<String> solutions) {
        for (String sol : solutions) {
            if (output.equals(sol)) {
                return true;
            }
        }
        return false;
    }

    //wont work for 1 input
    public static Circuit generateCircuit(int number_inputs, ArrayList<String> solutions, int min_depth, int max_depth) throws IOException {

        int number_not_gates = 0;
        //int number_and_gates = 0;
        //int number_or_gates = 0;
        
        ArrayList<String> input_circuit = new ArrayList<String>();
        ArrayList<String> input_circuit_output = new ArrayList<String>();
        ArrayList<Integer> temp_solution_lines = new ArrayList<Integer>();
        ArrayList<Integer> temp_not_gate_indices = new ArrayList<Integer>();

        int depth = randomInt(min_depth, max_depth);
        int choice;
        int and_or1;
        int and_or2;
        String entry;
        String output;
        
        int input_size = (int) Math.pow(2, number_inputs);
        for (int i = 1; i <= number_inputs; ++i) {
            input_circuit.add(i + " NONE " + i); //adds the inital part of circuit same for all circuits
            int k = (int) Math.pow(2, number_inputs - i);
            output = "";
            for (int j = 0; j < (input_size / (k * 2)); ++j) {
                for (int x = 0; x < k; ++x)
                    output += '0';
                for (int y = 0; y < k; ++y)
                    output += '1';
            }
            input_circuit_output.add(output);
        }
        
        int i = number_inputs + 1;
        int j  = number_inputs + 1;
        while (j <= depth * 4) {
            //choose randomly and/not/or

            if (number_not_gates == 2)
                choice = randomInt(1, 2);
            else {
                choice = randomInt(1, 3);
                if (choice == 3) {
                    choice = randomInt(1, 3);
                        if (choice == 3)
                            choice = randomInt(1, 3);
                }
            }

            if (choice == 1) { //AND
                and_or1 = randomInt(1, i - 2);
                and_or2 = randomInt(and_or1 + 1, i - 1);
                entry = i + " AND " + and_or1 + " " + and_or2;
                output = getOutput2(entry, input_circuit_output);
                if (!input_circuit_output.contains(output)) {
                    if (isSolution2(output, solutions))
                        temp_solution_lines.add(i);
                    input_circuit.add(entry);
                    input_circuit_output.add(output);
                    ++i;
                    j += 4;
                    //++number_and_gates;
                }
            }
            else if (choice == 2) { //OR
                and_or1 = randomInt(1, i - 2);
                and_or2 = randomInt(and_or1 + 1, i - 1);
                entry = i + " OR " + and_or1 + " " + and_or2;
                output = getOutput2(entry, input_circuit_output);
                if (!input_circuit_output.contains(output)) {
                    if (isSolution2(output, solutions))
                        temp_solution_lines.add(i);
                    input_circuit.add(entry);
                    input_circuit_output.add(output);
                    ++i;
                    j += 4;
                    //++number_and_gates;
                }
            }
            else { //NOT
                entry = i + " NOT " + randomInt(1, i - 1);
                output = getOutput2(entry, input_circuit_output);
                if (!input_circuit_output.contains(output)) {
                    if (isSolution2(output, solutions))
                        temp_solution_lines.add(i);
                    input_circuit.add(entry);
                    input_circuit_output.add(output);
                    temp_not_gate_indices.add(i);
                    ++number_not_gates;
                    ++i;
                    j += 4;
                    //++number_and_gates;
                }
            }
            ++j;
        }
        if (input_circuit.size() < min_depth)
            return null;
        return new Circuit(input_circuit, number_not_gates, input_circuit_output, 2, solutions, temp_solution_lines, temp_not_gate_indices);
    }

}
