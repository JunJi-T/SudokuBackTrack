
package sudokubacktrack;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
/**
 *
 * @author JunJi
 */
public class SudokuBackTrack {

    //Horizontal(Row) first then vertical(Column)
    public static char[][][] Grid;
    ArrayDeque<String> queue = new ArrayDeque<String>();
    
    //reads a new sudoku layout and set it up in grid
    public void Setup(String filePath){
        
        Scanner input;
        Reader reader = null;
        
        try{
            
            reader = new FileReader(filePath);
        }catch(IOException i){
            
            System.out.println(i.getMessage());
        }
        
        input = new Scanner(reader);       
        
        Grid = new char[9][9][10];   
        
        for(int h = 0; h <= 8; h++){
            
            String line = input.next();
            for(int v = 0; v <= 8; v++){
                
                Grid[h][v][0] = line.charAt(v);
            }
        }
        
        //initialise the domains
        for(int x = 0; x <= 8; x++){
            for(int y = 0; y <= 8; y++){
                for(int z = 1; z <= 9; z++){
                    Grid[x][y][z] = String.valueOf(z).charAt(0);
                }
            }
        }
        
        //populate queue with empty grid locations
        for(int x = 0; x <= 8; x++){
            for(int y = 0; y <= 8; y++){
                if(Grid[x][y][0] == '-')
                    queue.add(String.valueOf(x)+String.valueOf(y));
            }
        }
    }
    
    //print a fancy string diagram of the sudoku.
    public String GridToString(){
               
        String state = "+-----------------------+" + "\n";
        
        for(int h = 0; h <= 8; h++){
                        
            state += "|"; 
            
            for(int v = 0; v <= 8; v++){
                
                state += " " + Grid[h][v][0];
                if(v == 2 || v == 5 ){
                    
                    state += " |";
                }
                
                if(v == 8){
                    
                    state += " |\n";
                }
            }
            if(h == 2 || h == 5){
                state += "|-------+-------+-------|" + "\n";
            }
        }
        state += state = "+-----------------------+" + "\n";
        
        return state;
    }
      
    //check for any conflict in the row, column or box of this grid location(h,v)
    public boolean chkConflict(int h, int v){

        if(Grid[h][v][0] != '-'){
            
        //check its horizontal(Row)
            for(int i = 0; i <= 8; i++){
                if(i != v){
                    if(Grid[h][i][0] == Grid[h][v][0]){
                        return true;
                    }
                }
            }
            //check its vertical(column)
            for(int i = 0; i <= 8; i++){
                if(i != h){
                    if(Grid[i][v][0] == Grid[h][v][0]){
                        return true;
                    }
                }
            }
            //check its 3x3 box
            int boxH = ((int)Math.floor((double)h/3))*3;
            int boxV = ((int)Math.floor((double)v/3))*3;      
            
            for(int x = boxH; x <= boxH+2; x++){
                for(int y = boxV; y <= boxV+2; y++){
                    if(x != h || y != v){
                        if(Grid[x][y][0] == Grid [h][v][0]){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    //check if board if full(solved)
    public boolean isGridFull(){
        
        for(int x = 0; x <= 8; x++){
            for(int y = 0; y <= 8; y++){
                if(Grid[x][y][0] == '-')
                    return false;
            }
        }
        return true;
    }
    
    //Backtracking algorithm
    public boolean backTrack(){
        
        boolean SUCCESS = true;
        boolean FAILED = false;
        
        if(isGridFull()){
            //board full, solution found
            return SUCCESS;       
        }else{    
            String nextEmptyGrid = queue.pop();
            int GridH = Character.getNumericValue(nextEmptyGrid.charAt(0));
            int GridV = Character.getNumericValue(nextEmptyGrid.charAt(1));
            
            //for all values in the var domain
            int i = 0;
            for(i = 0; i <= 8; i++){
                char value = Grid[GridH][GridV][i+1];
                if(value != '-'){
                    Grid[GridH][GridV][0] = value;
                    if(chkConflict(GridH, GridV)){
                        Grid[GridH][GridV][0] = '-'; 
                    }else{
                        if(backTrack() == SUCCESS){
                            return SUCCESS;
                        }else{
                            Grid[GridH][GridV][0] = '-';                          
                        }
                    }   
                }               
            }
            queue.push(nextEmptyGrid);
            return FAILED;
        }        
    }
    
    public static void main(String[] args) {
        
        SudokuBackTrack SBT = new SudokuBackTrack();
        SBT.Setup(args[0]);    
        
        //initial Grid
        System.out.println(SBT.GridToString());
        
        
        long startTime = System.nanoTime();
        
        SBT.backTrack();
        
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double miliseconds = (double)elapsedTime / 1000000;
        
        //Completed Grid
        System.out.println(SBT.GridToString());
        
        System.out.println("Time Elapsed: " + miliseconds + " miliseconds"); 
    }
    
}
