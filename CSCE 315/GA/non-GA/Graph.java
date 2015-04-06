public class Graph {

    public Graph(){
        // graph constructor
    }
    
    public static void calculateOutput() {
        //want to have a level class that help the calculation later
        //1 NONE 1 (meaning output line 1 is the output of a wire [no gate] from input line 
        //1 2 NONE 2
        //3 NOT 1 (meaning output line 3 is the output of a NOT gate whose input is line 1)
        //4 NOT 2
        //5 AND 1 4
        //6 AND 2 3 
        //7 OR 5 6
         // 
         //
         for(int i = 0; i < circuit_path.size();++i){
             
             
         }
         
    }
    
    //this function will take 2 inputs and using the curcuit path generate a new input
    // the input then can be passed to the next gate to be used 
    public static int expressionManager(int op, int input_a , int input_b){
        if(op == 0) {
            // then the logic is a not gate
           return notExpression(input_a);
        }
        else if (op == 1) {
          return andExpression(input_a,input_b);
        }
        else if (op == 2) {
           return orExpression(input_a,input_b);
        }
        else{
            error("Invalid Input", "expressions: ");
            return -1;
        }
        
    }
    
    public static int notExpression(int input_a) {
       return ~input_a;
    }
    
    public static int andExpression(int input_a, int input_b) {
        // the logic is an and gate
        return (input_a & input_b) // if they are the same then will result in 1
       
    }
    
    public static int orExpression(int input_a, int input_b){
         //logic is an or gate
               return input_a | input_b; // if they are the same then will result in 1
            
    }
    
    public static void graph_main(){
        System.out.println("\033[42mGraph Class\033[0m\n");
    }
    
}
