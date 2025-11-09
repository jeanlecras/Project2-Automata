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
    public Epidemia() {
        this.dimension = 10; // default parameter
        this.matrix = new char[this.dimension][this.dimension]; // creation of a matrix with y = dimension and x = dimension
        for (int y=0; y<this.dimension; y++) { // creation of line 
            for (int x=0; x<this.dimension; x++) { // creation of column
                if (Math.random() < 0.6) { // if Math.random under 0.6 place 'S'
                    if (Math.random() < 0.2) { // same with 'V'
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
        for (int y=0; y<this.dimension; y++) {
            for (int x=0; x<this.dimension; x++) {
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
        for (char[] row : this.matrix) { // read the matrix line by line
            for (char cell : row) { // read each cell in each row 
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
    public void startDisease(double mortality, int y, int x) {
        // check if the cell is 'S'
        if (this.matrix[y][x] != 'S') {
            throw new IllegalArgumentException(
                "La cellule (" + y + ", " + x + ") n'est pas sain ('S'), impossible de démarrer la maladie ici."
            );
        }

        // if all is good, we apply the changements
        this.mortality = mortality;
        this.matrix[y][x]='X';
    }
    
    /**
     * Iniate a disease by contaming one random individual
     */
    public void startDisease() {
        Random r = new Random();
        int randY, randX;
        do { // choose random cell in matrix while the cell is 'S' if isn't 'S' we need to continues to choose another cell randomly
            randY = r.nextInt(this.dimension); 
            randX = r.nextInt(this.dimension);
        } while (this.matrix[randY][randX]!='S');
        startDisease(0.1, randY, randX); // apply the letter 'X' in the cell choose randomly 
    }
    
    /**
     * Tell if there is contamined individual next to the given position
     */
    private boolean hasIllNeighbor(int y, int x) {
        if (y>0 && this.matrix[y-1][x]=='X') { // cellule au dessus 
            return true;
        }
        if (y<this.dimension-1 && this.matrix[y+1][x]=='X') { // cellule en dessous
            return true;
        }
        if (x>0 && this.matrix[y][x-1]=='X') { // cellule a gauche
            return true;
        }
        if (x<this.dimension-1 && this.matrix[y][x+1]=='X') { // cellule a droite
            return true;
        }
        return false;
    }
    
    /**
     * 
     * public test method of hasIllNeighbor 
     * is only for testing this method 
     */

    public boolean testHasIllNeighbor(int y, int x) {
        return hasIllNeighbor(y, x);
    }

    /**
     * Determine the next state of an individual at a give position
     */
    private char nextState(int y, int x) {
        if (this.matrix[y][x]=='S' && hasIllNeighbor(y, x)) {
            return 'X';
        } else if (this.matrix[y][x]=='X') {
            if (Math.random() < this.mortality) { // under this.mortality is dead else immunited 
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
        for (char[] row : this.matrix) { // parcourt chaque ligne de la matrice
            for (char cell : row) { // parcours chaque cellule de la ligne 
                display += cell; // add cell in display
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
        int center = this.dimension / 2;
        startDisease(mortality, center, center);
        while (hasOne('X')) {
            nextDay();
        }
    }

    /**
     * j'ajoute deux methode pour pouvoir tester rapidement les methode depuis le main
     * modifier et acceder a la matrix qui est privé 
     * getter et setter
     */

    public char[][] getMatrix() {
        return this.matrix;
    }

    public void setMatrix(char[][] newMatrix) {
        this.matrix = newMatrix;
        this.dimension = newMatrix.length;
    }


    /**
     * Tests
     */
    public static void main(String[] args) {
        // test of method hasOne
        System.out.println("=== TEST hasOne ===");

        Epidemia epi1 = new Epidemia(2, 1.0, 0.0);
        System.out.println(epi1.hasOne('S'));
        System.out.println(epi1.hasOne('X'));
        

        // test of method StartDisease
        System.out.println("=== TEST StartDisease ===");
            // test 1 - on cell 'S'
        Epidemia startTest1 = new Epidemia(5, 1.0, 0.0);
        startTest1.startDisease(0.1, 2, 2);
        
        if (startTest1.matrix[2][2] == 'X') {
            System.out.println("Test 1 réussi : la cellule (2,2) est bien devenue 'X'");
        } else {
            System.out.println("Test 1 échoué : la cellule (2,2) n'est pas 'X'");
        }

            // test 2 - not on cell 'S'
        Epidemia startTest2 = new Epidemia(5, 1.0, 0.0);
        startTest2.matrix[1][1] = 'V'; // we change one case to have one invalid cell

        try{
            startTest2.startDisease(0.1, 1, 1);
            System.out.println("Test 2 échoué : aucune exception levée alors que la case n’est pas 'S'");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2 réussi : exception levée comme prévu -> " + e.getMessage());
        }

            // test 3 - startDisease() random
        Epidemia startTest3 = new Epidemia(5, 1.0, 0.0);
        startTest3.startDisease(); // random version
        System.out.println(startTest3.hasOne('X')); // true = a letter X is found, false = no one letter 'X' is found


        // test of hasIllNeighbor
        System.out.println("=== TESTS hasIllNeighbor ===");

        Epidemia testHasNeighbor = new Epidemia(3, 1.0, 0.0); // tout 'S'
            // On va placer des malades ('X') à des positions précises
        testHasNeighbor.matrix[1][1] = 'X'; // cellule centrale infectée

            // Cas 1 : cellule juste au-dessus du malade
        boolean result1 = testHasNeighbor.testHasIllNeighbor(0, 1); // au-dessus de (1,1)
        if (result1 == true) {System.out.println("Test1 (haut du malade) est bon !");} else {System.out.println("Le TEST n'est pas bon");}

            // Cas 2 : cellule à gauche du malade
        boolean result2 = testHasNeighbor.testHasIllNeighbor(1, 0); // gauche de (1,1)
        if (result2 == true) {System.out.println("Test2 (gauche du malade) est bon !");} else {System.out.println("Le TEST n'est pas bon");}

            // Cas 3 : cellule diagonale (pas voisine directe)
        boolean result3 = testHasNeighbor.testHasIllNeighbor(0, 0); // diagonale haut-gauche
        if (result3 == false) {System.out.println("Test3 (diagonale du malade, doit être faux) est bon !");} else {System.out.println("Le TEST n'est pas bon");}
        
        // test for nextState
        System.out.println("=== TEST nextState ===");

        Epidemia testNextState = new Epidemia(3, 1.0, 0.0);
        testNextState.setMatrix(new char[][] {
            {'S', 'X', 'S'},
            {'S', 'S', 'S'},
            {'S', 'S', 'S'}
        });

            // Cas 1 : cellule saine à gauche d’un malade
        char res1 = testNextState.nextState(0, 0);
        if (res1 == 'X') {System.out.println("Test 1 : S voisin de X devient X"); } else { System.out.println("Le test n'est pas bon");}
        

            // Cas 2 : cellule malade devient D ou I
        testNextState.mortality = 1.0; // mortalité totale -> doit mourir
        char res2 = testNextState.nextState(0, 1);
        if (res2 == 'D') {System.out.println("Test 2 : malade devient 'D'"); } else { System.out.println("Le test n'est pas bon");}

        testNextState.mortality = 0.0; // pas de mortalité -> doit guérir
        char res3 = testNextState.nextState(0, 1);
        if (res3 == 'I') {System.out.println("Test 3 : malade devient I"); } else { System.out.println("Le test n'est pas bon");}
        

            // Cas 3 : cellule isolée (aucun malade)
        char res4 = testNextState.nextState(2, 2);
        if (res4 == 'S') {System.out.println("Test 4 : S sans voisin reste S"); } else { System.out.println("Le test n'est pas bon");}

        
        // test of nextDay()
        System.out.println("\n=== TEST nextDay ===");

        Epidemia epi2 = new Epidemia(3, 1.0, 0.0);
        epi2.setMatrix(new char[][] {
            {'S', 'X', 'S'},
            {'S', 'S', 'S'},
            {'S', 'S', 'S'}
        });

        System.out.println("Avant nextDay :");
        epi2.displayEpidemia();

        epi2.mortality = 0.0;
        epi2.nextDay();

        System.out.println("Après nextDay :");
        epi2.displayEpidemia();

        // Vérifie que les voisins directs de 'X' sont maintenant infectés
        if (epi2.getMatrix()[0][0] == 'X' || epi2.getMatrix()[0][2] == 'X' || epi2.getMatrix()[1][1] == 'X') {
            System.out.println("Propagation correcte");
        }
        else {
            System.out.println("Aucune propagation détectée");
        }
        
        // test of passDay(n)
        System.out.println("\n=== TEST passDays ===");

        Epidemia epi3 = new Epidemia(3, 1.0, 0.0);
        epi3.setMatrix(new char[][] {
            {'S', 'X', 'S'},
            {'S', 'S', 'S'},
            {'S', 'S', 'S'}
        });

        System.out.println("Avant passDays(2) :");
        epi3.displayEpidemia();
        epi3.mortality = 0.0;

        epi3.passDays(2); // doit propager 2 jours de suite
        System.out.println("Après passDays(2) :");
        epi3.displayEpidemia();

        // test of simulation
        System.out.println("\n=== TEST simulation() ===");

        Epidemia epi4 = new Epidemia(5, 1.0, 0.0);
        epi4.mortality = 0.3;
        epi4.simulation();
        System.out.println("Simulation terminée !");

        // test of simulation(double mortality)
        System.out.println("\n=== TEST simulation(mortality) ===");

        Epidemia epi5 = new Epidemia(5, 1.0, 0.0);
        epi5.simulation(0.5);
        System.out.println("Simulation terminée avec mortalité = 0.5");


    }
}
