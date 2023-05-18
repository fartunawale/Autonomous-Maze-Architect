// Names: Fartun Awale
// x500s: awale013

import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        this.startRow = startRow; // this is x variable from main
        this.endRow = endRow; // this is y variable from main
        maze = new Cell[rows][cols];

        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){
                maze[i][j]= new Cell();
            }
        }
    }

//—————————————————————————————————————————————————————————————————
// makeMaze — lays the blueprint for navigating through the maze
// printMaze — creates & prints the maze blueprint
// solveMaze — solves the maze, and prints the solved maze

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int row1, int col1, int x, int y) {
        MyMaze board;
        board = new MyMaze(row1, col1, x, y); // creating new maze object

        Stack1Gen<int[]> stack = new Stack1Gen<>(); // build the maze using a stack
        stack.push(new int[]{x,0}); // initialize the stack with the starting index
        board.maze[board.startRow][0].setVisited(true); // mark this spot as visited

        while(!stack.isEmpty()) {
            int[] coordinates = stack.top(); // get the top element
            int myRow = coordinates[0];
            int myCol = coordinates[1];

            int[][] array = new int[4][2];
            int index = 0;
            // DFS Searching Algorithm
            // if conditions check: direction — visited or not — in bounds or not
            if ((0 <= myRow - 1) && !board.maze[myRow - 1][myCol].getVisited()) { // direction: up
                array[index][0] = myRow - 1;
                array[index][1] = myCol;
                index++;
            }
            if ((myRow + 1 < board.maze.length) && !board.maze[myRow + 1][myCol].getVisited()) { // direction: down
                array[index][0] = myRow + 1;
                array[index][1] = myCol;
                index++;

            }
            if ((0 <= myCol - 1) && !board.maze[myRow][myCol - 1].getVisited()) { // direction: left
                array[index][0] = myRow;
                array[index][1] = myCol - 1;
                index++;
            }
            if ((myCol + 1) < board.maze[0].length && !board.maze[myRow][myCol + 1].getVisited()) { // direction: right
                array[index][0] = myRow;
                array[index][1] = myCol + 1;
                index++;
            }
            Random t = new Random(); // choose random direction to get next neighbor
            if (index == 0) {
                stack.pop();
            } else {
                int rand = t.nextInt(0, index);
                if (array[rand][0] > myRow) {
                    stack.push(new int[]{myRow + 1, myCol}); // add neighbor's index to stack
                    board.maze[myRow + 1][myCol].setVisited(true); // marks the neighbor as visited
                    board.maze[myRow][myCol].setBottom(false); // removes the bottom between current cell & neighbor cell
                } else if (array[rand][0] < myRow) {
                    stack.push(new int[]{myRow - 1, myCol});
                    board.maze[myRow - 1][myCol].setVisited(true);
                    board.maze[myRow - 1][myCol].setBottom(false); // removes the bottom between current cell & neighbor cell
                } else if (array[rand][1] > myCol) {
                    stack.push(new int[]{myRow, myCol + 1});
                    board.maze[myRow][myCol + 1].setVisited(true);
                    board.maze[myRow][myCol].setRight(false); // removes the wall between current cell & neighbor cell
                } else {
                    stack.push(new int[]{myRow, myCol - 1});
                    board.maze[myRow][myCol - 1].setVisited(true);
                    board.maze[myRow][myCol - 1].setRight(false); // removes the wall between current cell & neighbor cell
                }
            }
        }
        for (int i=0; i<board.maze.length; i++){
            for (int j=0; j<board.maze[0].length; j++){
                board.maze[i][j].setVisited(false);
            }
        }
        return board;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        String s = "|"; // Top
        for (int i=0; i<maze[0].length; i++){
            s += "---" + "|"; // Rest of the top row
        }
        s += "\n";
        for (int i=0; i<maze.length; i++){
            if (i == this.startRow){
                s += " ";
            } else{
                s += "|";
            }
            for (int j=0; j<maze[0].length; j++){ // middle
                if (maze[i][j].getVisited()){
                    s += " * ";
                } else{
                    s += "   ";
                }
                if (maze[i][j].getRight() && !(i == this.endRow && j == maze[0].length-1)){
                    s += "|";
                } else{
                    s += " ";
                }
            }
            s += "\n";
            s += "|";
            for (int k=0; k<maze[0].length; k++){ // bottom
                if (maze[i][k].getBottom()){
                    s += "---" + "|";
                } else{
                    s += "   " + "|";
                }
            }
            s += "\n";
        }
        System.out.println(s);
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        // BFS Searching Algorithm
        Q1Gen<int[]> queue = new Q1Gen<>(); // use queue to solve maze
        int[] arr2 = new int[]{this.startRow, 0}; // creating first index // entrance
        queue.add(arr2); // adding it to queue

        while (queue.length() != 0){
            int[] hold = queue.remove(); // remove from queue into hold array
            // if spot is in bounds & not visited
            maze[hold[0]] [hold[1]].setVisited(true);
            if (maze[hold[0]] [hold[1]] == maze[this.endRow][maze[0].length-1]){ // index 0 - row && index 1 - col
                break; // exit found, end maze search
            }
            // up direction
            if ((0 <= hold[0]-1) && !(maze[hold[0]-1] [hold[1]].getVisited()) && !(maze[hold[0]-1] [hold[1]].getBottom())){
                queue.add(new int[]{hold[0]-1, hold[1]});
            }
            // down direction
            if ((hold[0]+1 < maze.length) && (!maze[hold[0]+1] [hold[1]].getVisited()) && !(maze[hold[0]] [hold[1]].getBottom())){
                queue.add(new int[]{hold[0]+1, hold[1]});
            }
            // left direction
            if ((0 <= hold[1]-1) && (!maze[hold[0]] [hold[1]-1].getVisited()) && (!maze[hold[0]] [hold[1]-1].getRight())){
                queue.add(new int[]{hold[0], hold[1]-1});
            }
            // right direction
            if ((hold[1]+1 < maze[0].length) && (!maze[hold[0]] [hold[1]+1].getVisited()) && (!maze[hold[0]] [hold[1]].getRight())){
                queue.add(new int[]{hold[0], hold[1]+1});
            }
        }
        printMaze();
    }

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.println("\n____________________________________________");
        System.out.println("|------------------------------------------|");
        System.out.println("|           Welcome to My Maze!            |");
        System.out.println("|__________________________________________|");
        System.out.println("\nTo create your first maze, enter a number between 5-20 for the rows: ");
        //System.out.println("enter a number between 5-20 for rows: ");
        int row1 = s.nextInt();
        System.out.println("Enter another number between 5-20 for the columns: ");
        int col1 = s.nextInt();

        Random r = new Random(); // randomly choose starRow (x) and endRow (y)
        int x, y;

        if (!((5 <= row1) && ( row1 <= 20) && (5 <= col1) && (col1 <= 20))) { // if user's input are out of bounds
            System.out.println("\nOOPS! \nOne or both of your boundaries entered are incorrect, your maze will now have to be 5 x 5.\n");
            row1 = 5;
            col1 = 5;
            x = r.nextInt(0, 5); // random rows
            y = r.nextInt(0, 5); // ransom cols
        } else{
            x = r.nextInt(0, row1); // random rows
            y = r.nextInt(0, row1); // ransom cols
        }
        System.out.println("\n___________________________________________");
        System.out.println("|         Entrance location — row " + x + "       |");
        System.out.println("|         Exit location — row " + y + "           |");
        System.out.println("|_________________________________________|\n");

        // MAKING THE MAZE
        MyMaze m = makeMaze(row1,col1, x, y);

        // PRINTING THE MAZE
        System.out.println("MAZE BEFORE: \n");
        m.printMaze();
        System.out.println("MAZE AFTER: \n");

        // SOLVING THE MAZE
        m.solveMaze();
        System.out.println("I hope you enjoy your maze!");
    }
}
