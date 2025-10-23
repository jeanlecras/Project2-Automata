package epidemic;

import java.util.Random;

public class EpidemicAutomata {
    private int[][] matrix;
    private int dimension;
    private double density; // proportion d'individus
    private double vaccinationRate; // proportion de vaccinés
    private double mortalityRate; // probabilité de décès
    private Random rand = new Random();

    public EpidemicAutomata(int dim, double density, double vaccinationRate, double mortalityRate) {
        this.dimension = dim;
        this.density = density;
        this.vaccinationRate = vaccinationRate;
        this.mortalityRate = mortalityRate;
        this.matrix = new int[dim][dim];
        this.populate();
    }

    // initialisation de la population
    private void populate() {
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (rand.nextDouble() > this.density) {
                    this.matrix[i][j] = -1; // cellule vide
                } else {
                    if (rand.nextDouble() < this.vaccinationRate) this.matrix[i][j] = 2; // vacciné
                    else this.matrix[i][j] = 1; // sain non vacciné
                }
            }
        }

        // infection initiale aléatoire
        int x, y;
        do {
            x = rand.nextInt(this.dimension);
            y = rand.nextInt(this.dimension);
        } while (this.matrix[x][y] <= 0); // éviter vide/morts
        this.matrix[x][y] = 5; // premier malade
    }

    // affichage stylé de la population
    public void displayPopulation() {
        String display = "";
        for (int[] row : this.matrix) { // pour chaque ligne
            for (int cell : row) { // pour chaque cellule
                switch (cell) {
                    case -1:
                        display += "_";
                        break; // vide
                    case 0:
                        display += "D";
                        break; // décédé
                    case 1:
                        display += "S";
                        break; // sain non vacciné
                    case 2:
                        display += "V";
                        break; // vacciné
                    case 3:
                        display += "G";
                        break; // guéri
                    case 5:
                        display += "P";
                        break; // malade
                }
            }
            display += "\n"; // nouvelle ligne
        }
        System.out.println(display); // affiche toute la matrice d’un coup
    }

    // vérifie si un voisin est malade
    private boolean hasSickNeighbor(int i, int j) {
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // diagonales incluses
        };

        for (int[] dir : directions) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            if (ni >= 0 && nj >= 0 && ni < this.dimension && nj < this.dimension) {
                if (this.matrix[ni][nj] == 5) return true;
            }
        }
        return false;
    }

    // une étape de propagation
    public void propagateEpidemic1() {
        int[][] next = new int[this.dimension][this.dimension];

        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                int cell = this.matrix[i][j];

                switch (cell) {
                    case -1:
                    case 0:
                    case 2:
                    case 3:
                        next[i][j] = cell; // rien ne change
                        break;

                    case 1: // sain non vacciné
                        if (this.hasSickNeighbor(i, j)) next[i][j] = 5; // devient malade
                        else next[i][j] = 1; // reste sain
                        break;

                    case 5: // malade
                        if (rand.nextDouble() < this.mortalityRate) next[i][j] = 0; // meurt
                        else next[i][j] = 3; // guérit
                        break;
                }
            }
        }

        this.matrix = next; // mise à jour de la matrice
    }

    // vérifie s'il reste des malades
    public boolean epidemicActive() {
        for (int[] row : this.matrix)
            for (int cell : row)
                if (cell == 5) return true;
        return false;
    }

    // propagation complète sur plusieurs étapes
    public void propagateEpidemic(int n) {
        for (int step = 0; step < n; step++) {
            System.out.println("Step " + step + ":");
            this.displayPopulation();

            if (!this.epidemicActive()) {
                System.out.println("No more infected individuals — epidemic is over.\n");
                break;
            }

            this.propagateEpidemic1();

            try {
                Thread.sleep(500); // pour mieux voir la progression
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
