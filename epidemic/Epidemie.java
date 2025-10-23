import java.util.Random;

public class Epidemie {
    private int[][] matrix;
    private int dimension;
    
    // s sain
    // v vaccined
    // p polio
    // i immune
    // d dead
    /**
     * Creates a forest of given size and density
     * 
     * @param n size of the forest
     * @param p tree density
     */
    public Epidemie(int n, double p, double vaccined_rate) throws IllegalArgumentException  {
        if (n < 10) {
            throw new IllegalArgumentException("The size of the matrix must be at least 10"); //a proper way to forbid an argument
        } else {
            this.dimension = n;
            this.matrix = new int[n][n];
        }
        for (int y=0; y<n; y++) {
            for (int x=0; x<n; x++) {
                if (Math.random() < p) {
                    this.matrix[y][x] = 1; //this line has a probability of p to be executed
                }
            }
        }
    }
    
    /**
     * Creates a forest of 10 m² given a tree density
     * 
     * @param p tree density
     */
    public Epidemie(double p) {
        this.dimension = 10;
        this.matrix = new int[10][10];
        for (int y=0; y<10; y++) {
            for (int x=0; x<10; x++) {
                if (Math.random() < p) {
                    this.matrix[y][x] = 1;
                }
            }
        }
    }
    
    /**
     * Display the forest in the terminal
     */
    public void displayArea() {
        String display = "";
        for (int[] row: this.matrix) { //foreach loop
            for (int cell: row) { //more intuitive than a for loop
                switch(cell) {
                    case 0:
                        display += ".";
                        break;
                    case 1:
                        display += "T";
                        break;
                    case 5:
                        display += "O";
                        break;
                    case -1:
                        display += "_";
                        break; //coffee break
                } 
            }
            display += "\n"; //store multiple rows
        }
        System.out.println(display); //displays the entire forest at once
    }
    
    private boolean checkCell(int value) {//generalized method to check something at a position
        for (int[] row: this.matrix) { 
            for (int cell: row) { 
                if (cell == value) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Tell if the forest is completly razed
     * 
     * @return completly razed
     */
    public boolean isRazed() {
        return !checkCell(1); // returns the negation of 'has a tree'
    }
    
    /**
     * Tell if the forest has been set on fire
     * 
     * @return the forest is on fire
     */
    public boolean isOnFire() {
        return checkCell(5);
    }
    
    /**
     * Put fire in the forest at given coordinates
     * 
     * @param i y coordinate
     * @param j x coordinate
     */
    public void putFire(int i, int j) {
        this.matrix[i][j] = 5;
    }
    
    /**
     * Put fire in the forest at a random position
     */
    public void putFire() {
        Random r = new Random(); //Creates a random number generator
        this.putFire(r.nextInt(this.dimension), r.nextInt(this.dimension)); //draw an integer from the random number generator
    }
    
    /**
     * Tell if a tree is a present at a given position
     * 
     * @param i y coordinate
     * @param j x coordinate
     * @return a tree is here
     */
    private boolean isTree(int i, int j) {
        return this.matrix[i][j]==1;
    }
    
    /**
     * Tell if a tree is on fire at a given position
     * 
     * @param i y coordinate
     * @param j x coordinate
     * @return a tree is on fire here
     */
    private boolean isOnFire(int i, int j) {
        return this.matrix[i][j]==5;
    }
    
    /**
     * Tell if the given position is next to a tree on fire
     * 
     * @param i y coordinate
     * @param j x coordinate
     * @return a tree on fire is close from here
     */
    private boolean hasNeighborOnFire(int i, int j) {
        if (i > 0 && isOnFire(i-1, j)) {//check down
            return true;
        }
        if (i < this.dimension-1 && isOnFire(i+1, j)) {//check up
            return true;
        }
        if (j > 0 && isOnFire(i, j-1)) {//check left
            return true;
        }
        if (j < this.dimension-1 && isOnFire(i, j+1)) {//check right
            return true;
        }
        return false;
    }
    
    /**
     * Predicts what will be at a given position in 1 hour
     * 
     * @param i y coordinate
     * @param j x coordinate
     * @return the prediction
     */
    private int nextState(int i, int j) {
        if (this.isOnFire(i, j)) {
            return -1;
        } else if (this.isTree(i, j) && this.hasNeighborOnFire(i, j)) {
            return 5;
        } else {
            return this.matrix[i][j];
        }
    }
    
    /**
     * Advances the forest's time by 1 hour
     */
    private void propagateFire1() {
        int[][] nextMatrix = new int[this.dimension][this.dimension]; 
        for (int y=0; y<this.dimension; y++) {
            for (int x=0; x<this.dimension; x++) {
                nextMatrix[y][x] = this.nextState(y, x); //changes to previous positions do not affect the change to the current position
            }
        }
        this.matrix = nextMatrix; //note that here all positions are updated at the same time
    }
    
    /**
     * Advances the forest's time by a number of hours
     * 
     * @param n number of hours
     */
    public void propagateFire(int n) {
        for (int i=0; i<n; i++) {
            this.propagateFire1();
            this.displayArea();
            try {
                Thread.sleep(5000); //adults need an average of 7 to 8 hours of sleep per day
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}