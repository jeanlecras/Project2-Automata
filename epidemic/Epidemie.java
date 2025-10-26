import java.util.Random;

public class Epidemia
{
    private char[][] matrix;
    private int dimension;
    private double mortality;
    
    //Letters meaning:
    //S sain (not vaccined)
    //V vaccined (sain)
    //  nobody
    //X contamined/sick (after being sain)
    //I immuned (after being contamined)
    //D dead (after being contamined

    /**
     * Constructor for objects of class Epidemie, without parameters
     */
    public Epidemia()
    {
        this.dimension = 10;
        this.matrix = new char[10][10];
        for (int y=0; y<10; y++) {
            for (char x=0; x<10; x++) {
                if (Math.random() < 0.6) {
                    if (Math.random() < 0.2) {
                        this.matrix[y][x] = 'V';
                    } else {
                        this.matrix[y][x] = 'S';
                    }
                } else {
                    this.matrix[y][x] = ' ';
                }
            }
        }
    }
    
    /**
     * Constructor for objects of class Epidemie, with parameters
     */
    public Epidemia(int dimension, double density, double vaccinated) {
        this.dimension = dimension;
        this.matrix = new char[dimension][dimension];
        for (int y=0; y<10; y++) {
            for (char x=0; x<10; x++) {
                if (Math.random() < density) {
                    if (Math.random() < vaccinated) {
                        this.matrix[y][x] = 'V';
                    } else {
                        this.matrix[y][x] = 'S';
                    }
                } else {
                    this.matrix[y][x] = ' ';
                }
            }
        }
    }
    
    /**
     * Tell if the matrix contains at least 1 of the given state
     */
    private boolean hasOne(char state) {
        for (char[] row : this.matrix) {
            for (char cell : row) {
                if (cell == state){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Initiate a disease by setting a contamined person at a given position, set the mortality of the disease
     */
    public void startDisease(double mortality, int y, int x) {//TODO throw Exception when cell yx isn't S ?
        this.mortality = mortality;
        this.matrix[y][x]='X';
    }
    
    /**
     * Iniate a disease by contaming one random individual
     */
    public void startDisease() {
        Random r = new Random();
        int randY, randX;
        do {
            randY = r.nextInt(this.dimension); 
            randX = r.nextInt(this.dimension);
        } while (this.matrix[randY][randX]!='S');
        startDisease(0.1, randY, randX);
    }
    
    /**
     * Tell if there is contamined individual next to the given position
     */
    private boolean hasIllNeighbor(int y, int x) {
        if (y>0 && this.matrix[y-1][x]=='X') {
            return true;
        }
        if (y<this.dimension-1 && this.matrix[y+1][x]=='X') {
            return true;
        }
        if (x>0 && this.matrix[y][x-1]=='X') {
            return true;
        }
        if (x<this.dimension-1 && this.matrix[y][x+1]=='X') {
            return true;
        }
        return false;
    }
    
    /**
     * Determine the next state of an individual at a give position
     */
    private char nextState(int y, int x) {
        if (this.matrix[y][x]=='S' && hasIllNeighbor(y, x)) {
            return 'X';
        } else if (this.matrix[y][x]=='X') {
            if (Math.random() < this.mortality) {
                return 'D';
            } else {
                return 'I';
            }
        } else {
            return this.matrix[y][x];
        }
    }
    
    /**
     * Display a representation of the area
     */
    public void displayEpidemia() {
        String display = "";
        for (char[] row : this.matrix) {
            for (char cell : row) {
                display += cell;
            }
            display += "\n";
        }
        System.out.println(display);
    }
    
    /**
     * pass 1 day : Set every individual to their next state
     */
    private void nextDay() {
        char[][] nextMatrix = new char[this.dimension][this.dimension];
        for (int y=0; y<this.dimension; y++) {
            for (int x=0; x<this.dimension; x++) {
                nextMatrix[y][x] = nextState(y,x);
            }
        }
        this.matrix = nextMatrix;
        displayEpidemia();
        try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
    }
    
    /**
     * pass n days
     */
    private void passDays(int n) {
        for (int i=0; i<n; i++) {
            nextDay();
        }
    }
    
    /**
     * start the disease and pass days until the disease is gone
     */
    private void simulation() {
        startDisease();
        while (hasOne('X')) {
            nextDay();
        }
    }
    
    /**
     * start the disease (and set the mortality) and pass days until the disease is gone
     */
    private void simulation(double mortality) {
        startDisease(mortality, 5, 5);
        while (hasOne('X')) {
            nextDay();
        }
    }
    
    /**
     * Tests
     */
    public static void main(String[] args) {
        Epidemia epi1 = new Epidemia();
        epi1.simulation();
    }
}
