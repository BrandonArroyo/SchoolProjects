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
    
    public Circuit(ArrayList<String> in_circuit, int in_not_gates, ArrayList<String> in_circuit_output, int in_not_gates_allowed, ArrayList<String> in_solution_outputs_wanted, ArrayList<Integer> in_solution_lines) {
        circuit = in_circuit;
        not_gates = in_not_gates;
        circuit_output = in_circuit_output;
        not_gates_allowed = in_not_gates_allowed;
        solution_outputs_wanted = in_solution_outputs_wanted;
        solution_lines = in_solution_lines;
    }
    
    public ArrayList<String> getCircuitOutput() {
        return circuit_output;
    }
    
    public ArrayList<String> getCircuit() {
        return circuit;
    }
    
    //used in powerSet(), helps in getting the power set 
    private static String intToBinary(int binary, int digits) {
        String temp = Integer.toBinaryString(binary);
        int found_digits = temp.length();
        String returner = temp;
        for (int i = found_digits; i < digits; i++) {
           returner = "0" + returner;
        }
        return returner;
    }
    
    
    public int getNotGates() {
        return not_gates;
    }
    
    //used in getPossibleGates(), gets all possible combinations for AND and OR gates
    private ArrayList<ArrayList<String>> powerSet(String[] set) {
        ArrayList<ArrayList<String>> power = new ArrayList<ArrayList<String>>();
        int elements = set.length;
        int power_elements = (int) Math.pow(2,elements);
        for (int i = 0; i < power_elements; i++) {
            String binary = intToBinary(i, elements);
            ArrayList<String> inner_set = new ArrayList<String>();
            for (int j = 0; j < binary.length(); j++) {
               if (binary.charAt(j) == '1')
                   inner_set.add(set[j]);
            }
            if (inner_set.size() > 1)
                power.add(inner_set);
        }
        return power;
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
    
    public ArrayList<Circuit> getPossibleGates() throws IOException {
        ArrayList<Circuit> possible_gates = new ArrayList<Circuit>();
        int circuit_size = circuit.size();
        //key is the output, value is a pair of the entry and an identifier for NOT or AND_OR
        Map<String, List<String>> unique_entrys = new HashMap<String, List<String>>();
        
        //NOT gates
        if (not_gates < not_gates_allowed) {
            for(int in = 1; in <= circuit_size; ++in) {
                String entry = (circuit_size + 1) + " NOT " + in;
                //test if output exist in circuit already, then test if an entry with the same output has been created yet
                //if not put into a map that other entrys test against
                if (!testEntry(entry)) {
                    String entry_output = getOutput(entry);
                    if (!unique_entrys.containsKey(entry_output)) {
                        List<String> val_set = new ArrayList<String>();
                        val_set.add(entry);
                        val_set.add("1");
                        unique_entrys.put(entry_output, val_set);
                    }
                }
            }
        }
        
        //AND and OR gates
        if (circuit_size > 1) {
            String input_lines[] = new String[circuit_size];
            for (int i = 0; i < circuit_size; ++i) {
                input_lines[i] = new Integer(i+1).toString();
            }
            
            ArrayList<ArrayList<String>> pset_input_lines = powerSet(input_lines);
            String and_or[] = {" AND", " OR"};
            for (String g : and_or) {
                for (int i = 0; i < pset_input_lines.size(); ++i) {
                    String entry = (circuit_size + 1) + g;
                    ArrayList<String> input_line = new ArrayList<String>(pset_input_lines.get(i));
                    for(int j = 0; j < input_line.size(); ++j) {
                        entry += " " + input_line.get(j);
                    }
                    //test if output exist in circuit already, then test if an entry with the same output has been created yet
                    //if not put into a map that other entrys test against
                    if (!testEntry(entry)) {
                        String entry_output = getOutput(entry);
                        if (!unique_entrys.containsKey(entry_output)) {
                            List<String> val_set = new ArrayList<String>();
                            val_set.add(entry);
                            val_set.add("2");
                            unique_entrys.put(entry_output, val_set);
                        }
                    }
                }
            }
        }
        
        
        for (Map.Entry<String, List<String>> me : unique_entrys.entrySet()) {
            ArrayList<String> new_circuit = new ArrayList<String>(circuit);
            List<String> entry_and_identifier = me.getValue();
            new_circuit.add(entry_and_identifier.get(0));
            ArrayList<String> new_circuit_output = new ArrayList<String>(circuit_output);
            new_circuit_output.add((String)me.getKey());
            if (isSolution((String)me.getKey())) {
                ArrayList<Integer> new_solution_lines = new ArrayList<Integer>(solution_lines);
                new_solution_lines.add(circuit_size + 1);
                if (entry_and_identifier.get(1) == "1")
                    possible_gates.add(new Circuit(new_circuit, not_gates + 1, new_circuit_output, not_gates_allowed, solution_outputs_wanted, new_solution_lines));
                else 
                    possible_gates.add(new Circuit(new_circuit, not_gates, new_circuit_output, not_gates_allowed, solution_outputs_wanted, new_solution_lines));
            }
            else {
                if (entry_and_identifier.get(1) == "1") 
                    possible_gates.add(new Circuit(new_circuit, not_gates + 1, new_circuit_output, not_gates_allowed, solution_outputs_wanted, solution_lines));
                else 
                    possible_gates.add(new Circuit(new_circuit, not_gates, new_circuit_output, not_gates_allowed, solution_outputs_wanted, solution_lines));
            }
        }
        return possible_gates;
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
    
    public BitSet fromString(final String s) {
        return BitSet.valueOf(new long[] { Long.parseLong(s, 2) });
    }

    public String toString(BitSet bs) {
        if (bs.isEmpty()){
            String temp = bs.toString();
            int length = temp.length();
            return new String(new char[length]).replace("\0", "0");
        }
        else
            return Long.toString(bs.toLongArray()[0], 2);
    }
    
    public String AND(String a, String b) {
        BitSet c = fromString(a);
        BitSet d = fromString(b);
        c.and(d);
        return add_zeros(a, toString(c));
    }
     
    public String OR(String a, String b) {
        BitSet c = fromString(a);
        BitSet d = fromString(b);
        c.or(d);
        return add_zeros(a, toString(c));
    }
    
    public String NOT(String a) {
        BitSet c = fromString(a);
        int length = a.length();
        c.flip(0, length);
        return add_zeros(a, toString(c));
    }
    
    public String add_zeros(String a, String b) {
        int difference = a.length() - b.length();
        return new String(new char[difference]).replace("\0", "0") + b;
    }
    
    //---------------------------------------------------------------------------------------------
    
}
