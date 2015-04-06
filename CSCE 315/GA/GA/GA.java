import java.lang.*;
import java.util.*;
import java.io.*;




public class GA {
    
//-------------------------------------------------------------------------------    
                        //Data members
    
    public static ArrayList<Circuit> current_generation = new ArrayList<Circuit>();
    private static int population_size; // how many circuits in the inital population
    private static double average_fitness = 0; //total average fitness of a generation (should increase each round)
    private static double last_average_fitness = 0;
    private static int desired_fitness;
    private static int number_inputs;
    private static ArrayList<String> solution;
    
//-------------------------------------------------------------------------------   
                        //Functions   
   
       
    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    //full adder min = 14
    //triple negation min = 29
    public static void initializePopulation()  {
        Circuit new_circuit;
        current_generation.clear();
        int circuit_count = 0;
        while (circuit_count < population_size) {
                if ((new_circuit = Circuit.generateCircuit(number_inputs, solution, 14, 50)) != null) {
                    average_fitness += new_circuit.getFitnessValue();
                    current_generation.add(new_circuit);
                    ++circuit_count;
                }
        }
        Collections.sort(current_generation, new CustomComparator() );
        average_fitness /= population_size;
    }
   
   
   //add combined circuits of circuits with fitness value below average fitness value
   //take top 100 (or whatever the polulation size is)\
   //remove all circuits with fitness value twice as big as averagve fitness value
   //to get to the population size, generate random circuits 
    public static Circuit generatePopulation() { // will help with population generation
        System.out.println("AVERAGE FITNESS VALUE = " + average_fitness);
        
        int n_population_size = 0;
        double avg_fit = 0;
        int avg_index = 0;
        //returns true if it found a solution
        
        int z = 1;
        while (current_generation.get(current_generation.size() - z).getFitnessValue() >= 200 && z < 100) {
            if (current_generation.get(current_generation.size() - z).solution_lines.size() >= solution.size()) {
                return current_generation.get(current_generation.size() - z);
            }
            ++z;
        }
  
        for (int i = 0; i < current_generation.size() - 1 ; ++i) {
            if (current_generation.get(i).getFitnessValue() >= (int)average_fitness / 2) {
                    avg_index = i;
                    break;
            }
        }
        current_generation.subList(0, avg_index).clear();
       
        n_population_size = current_generation.size();
        for (int i = 0 ; i < n_population_size; ++i) {
                for(int j = i; j <  n_population_size; ++j)
                    combineCircuit(current_generation.get(i), current_generation.get(j)) ; // basically combine all possible circuits 
        }
        System.out.println(current_generation.size());
        
        if(current_generation.size() > population_size){
            Collections.sort(current_generation, new CustomComparator());
            current_generation.subList( 0, current_generation.size() - population_size ).clear();
        }
        
        int x = 1;
        while (x < 50) {
                Circuit new_circuit2;
                if ((new_circuit2 = mutateCircuit(current_generation.get(current_generation.size() - x))) != null)
                    current_generation.add(new_circuit2);
                ++x;
        }
        
        while (x < 20) {
            Circuit new_circuit;
            if ((new_circuit = Circuit.generateCircuit(number_inputs, solution, 14, 50)) != null);
                     current_generation.add(new_circuit);
        }
        
        if(current_generation.size() > population_size){
            Collections.sort(current_generation, new CustomComparator());
            current_generation.subList( 0, current_generation.size() - population_size ).clear();
        }
        
        int avg_val = 0;
        for(int i = 0 ; i < current_generation.size(); ++i) {
            avg_val += current_generation.get(i).getFitnessValue();
        }
        avg_val /= population_size;
        last_average_fitness = average_fitness;
        average_fitness = avg_val;

        return null;
    }

    public static void combineCircuit(Circuit circuit1, Circuit circuit2) {
        int circuit1_size = circuit1.circuit.size();
        int circuit2_size = circuit2.circuit.size();
        ArrayList<String> solutions = circuit1.solution_outputs_wanted;
        int max_slice = (circuit1_size < circuit2_size) ? circuit1_size : circuit2_size; 
        int min_slice = number_inputs;
        int slice = randomInt(min_slice, max_slice);
        
        ArrayList<Integer> child1_not_indices = new ArrayList<Integer>();
        ArrayList<Integer> child2_not_indices = new ArrayList<Integer>();
        for (Integer i : circuit1.not_gate_indices) {
            if (i <= slice)
                child1_not_indices.add(i);
            else
                child2_not_indices.add(i);
        }
        for (Integer j : circuit2.not_gate_indices) {
            if (j <= slice)
                child2_not_indices.add(j);
            else
                child1_not_indices.add(j);
        }
        
        if (child1_not_indices.size() <= 2) {
            ArrayList<String> child1_circuit = new ArrayList<String>(circuit1.circuit.subList(0, slice));
            child1_circuit.addAll(circuit2.circuit.subList(slice, circuit2.circuit.size()));
            ArrayList<String> child1_circuit_output = new ArrayList<String>(circuit1.circuit_output.subList(0, slice));
            ArrayList<Integer> child1_solution_lines = new ArrayList<Integer>();
            for (Integer i : circuit1.solution_lines) {
                if (i < slice + 1)
                    child1_solution_lines.add(i);
            }
            String output = "";
            for (int j = slice; j < child1_circuit.size(); ++j) {
                output = Circuit.getOutput2(child1_circuit.get(j), child1_circuit_output);
                if (Circuit.isSolution2(output, solutions)) {
                    boolean already_solution = false;
                    for (Integer t : child1_solution_lines) {
                        if (child1_circuit_output.get(t - 1).equals(output)) 
                            already_solution = true;
                    }
                    if (!already_solution)
                        child1_solution_lines.add(j + 1);
                }
                child1_circuit_output.add(output);
            }
            current_generation.add(new Circuit(child1_circuit, child1_not_indices.size(), child1_circuit_output, circuit1.getNotGatesAllowed(), solutions, child1_solution_lines, child1_not_indices));
        }
        
        if (child2_not_indices.size() <= 2) {
            ArrayList<String> child2_circuit = new ArrayList<String>(circuit2.circuit.subList(0, slice));
            child2_circuit.addAll(circuit1.circuit.subList(slice, circuit1.circuit.size()));
            ArrayList<String> child2_circuit_output = new ArrayList<String>(circuit2.circuit_output.subList(0, slice));
            ArrayList<Integer> child2_solution_lines = new ArrayList<Integer>();
            for (Integer i : circuit2.solution_lines) {
                if (i < slice + 1)
                    child2_solution_lines.add(i);
            }
            String output = "";
            for (int j = slice; j < child2_circuit.size(); ++j) {
                output = Circuit.getOutput2(child2_circuit.get(j), child2_circuit_output);
                if (Circuit.isSolution2(output, solutions)) {
                    boolean already_solution = false;
                    for (Integer t : child2_solution_lines) {
                        if (child2_circuit_output.get(t - 1).equals(output)) 
                            already_solution = true;
                    }
                    if (!already_solution)
                        child2_solution_lines.add(j + 1);
                }
                child2_circuit_output.add(output);
            }
            current_generation.add(new Circuit(child2_circuit, child2_not_indices.size(), child2_circuit_output, circuit2.getNotGatesAllowed(), solutions, child2_solution_lines, child2_not_indices));
        }
    }
    
    public static Circuit mutateCircuit(Circuit c) {
        // this will mutate the circuit 
      int number_not_gates = c.getNotGates();
        
        ArrayList<String> input_circuit = new ArrayList<String>(c.circuit);
        ArrayList<String> input_circuit_output = new ArrayList<String>(c.circuit_output);
        ArrayList<Integer> temp_solution_lines = new ArrayList<Integer>(c.solution_lines);
        ArrayList<Integer> temp_not_gate_indices = new ArrayList<Integer>(c.not_gate_indices);
        ArrayList<String> solutions = new ArrayList<String>(c.solution_outputs_wanted);

        int depth = randomInt(c.circuit.size() + 1, c.circuit.size() + 16);
        int choice;
        int and_or1;
        int and_or2;
        String entry;
        String output;
        
        int i = c.circuit.size() + 1;
        int j  = c.circuit.size() + 1;
        while (j <= depth * 4) {
            //choose randomly and/not/or

            if (number_not_gates == 2)
                choice = randomInt(1, 2);
            else
                choice = randomInt(1, 3);
            if (choice == 3)
                choice = randomInt(1, 3);
    
    
            if (choice == 1) { //AND
                and_or1 = randomInt(1, i - 2);
                and_or2 = randomInt(and_or1 + 1, i - 1);
                entry = i + " AND " + and_or1 + " " + and_or2;
                output = Circuit.getOutput2(entry, input_circuit_output);
                if (!input_circuit_output.contains(output)) {
                    if (Circuit.isSolution2(output, solutions))
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
                output = Circuit.getOutput2(entry, input_circuit_output);
                if (!input_circuit_output.contains(output)) {
                    if (Circuit.isSolution2(output, solutions))
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
                output = Circuit.getOutput2(entry, input_circuit_output);
                if (!input_circuit_output.contains(output)) {
                    if (Circuit.isSolution2(output, solutions))
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
        if (input_circuit.size() < c.circuit.size() + 1)
            return null;
        return new Circuit(input_circuit, number_not_gates, input_circuit_output, 2, solutions, temp_solution_lines, temp_not_gate_indices);
    }
    
//------------------------------------------------------------------------------
                        //THE GENETIC ALGORITHM
    public static void geneticAlgorithm(int in_number_inputs, int in_population_size, ArrayList<String> in_solution) { // the number of inputs allows for the circuit to be defined
        number_inputs = in_number_inputs;
        population_size = in_population_size;
        solution = in_solution;
        initializePopulation();
        
        Circuit solution_circuit;
        int i = 0;
        while((solution_circuit = generatePopulation()) == null) {
            if(average_fitness == last_average_fitness ){
                initializePopulation();
            }
            System.out.println("generation: " + i); 
            ++i;
        }
        
        for (int y = 0; y < solution_circuit.circuit.size(); ++y) {
            System.out.println(solution_circuit.circuit.get(y) + " " + solution_circuit.circuit_output.get(y));
        }
        System.out.print("not gate indices are");
        for (Integer k : solution_circuit.not_gate_indices) {
            System.out.print(" " + k);
        }
        System.out.println();
        System.out.print("Solution line(s) are");
        for (Integer j : solution_circuit.solution_lines) {
            System.out.print(" " + j);
        }
        System.out.println();
        System.out.println("Fitness Value is " + solution_circuit.getFitnessValue());
        
    }
    
  public static class CustomComparator implements Comparator<Circuit> {
    @Override
        public int compare(Circuit o1, Circuit o2) {
            return Double.compare(o1.getFitnessValue(),o2.getFitnessValue());
        }
    }
// //-------------------------------------------------------------------------------
                // MAIN FUNCTION 
    public static void main(String[] args)  { 
        ArrayList<String> input_solution  = new ArrayList<String>();
        input_solution.add("00010111");
        input_solution.add("01101001");
        //input_solution.add("10101010");
        geneticAlgorithm(3, 100, input_solution);
    }
    
}
//-------------------------------------------------------------------------------
                    //NOTES BELOW AND CREDITS GIVEN
/*
NOTES FOR MYSELF:
http://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
What we need:
    A way to generate random circuits
    a way to determine how fit a circuit is
    some container for the fittest offspring 
    

selection which equates to survival of the fittest;
    key idea: give prefrence to better individuals, allowing them to pass on their genes to the next generation.
    The goodness of each individual depends on its fitness.
    Fitness may be determined by an objective function or by a subjective judgement.
crossover which represents mating between individuals;
    Prime distinguished factor of GA from other optimization techniques
    Two individuals are chosen from the population using the selection operator
    A crossover site along the bit strings is randomly chosen
    The values of the two strings are exchanged up to this point
    If S1=000000 and s2=111111 and the crossover point is 2 then S1'=110000 and s2'=001111
    The two new offspring created from this mating are put into the next generation of the population
    By recombining portions of good individuals, this process is likely to create even better individuals
mutation which introduces random modifications.
    With some low probability, a portion of the new individuals will have some of their bits flipped.
    Its purpose is to maintain diversity within the population and inhibit premature convergence.
    Mutation alone induces a random walk through the search space
    Mutation and selection (without crossover) create a parallel, noise-tolerant, hill-climbing algorithms

PSUEDOCODE:
 choose initial population -- generation of a inital node
 evaluate each individual's fitness -- test the output and measure how "close the solutions are"
 determine population's average fitness -- take an average of all the nodes created.
 repeat
         select best-ranking individuals to reproduce
         mate pairs at random
         apply crossover operator
         apply mutation operator
         evaluate each individual's fitness
         determine population's average fitness
 until terminating condition (e.g. until at least one individual has 
          the desired fitness or enough generations have passed)
*/