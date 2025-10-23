public class TestAutomata {
    public static void main(String args[]) {
        Automata iAC = new Automata(0.3);
        System.out.println("Is the forest in fire ? "+iAC.isOnFire());
        System.out.println("Is the forest completely rased ? "+iAC.isRazed());
        iAC.displayForest();
        
        Automata jAC = new Automata(25, 0.8);
        System.out.println("Is the forest in fire ? "+jAC.isOnFire());
        System.out.println("Is the forest completely rased ? "+jAC.isRazed());
        jAC.displayForest();
        
        Automata kAC = new Automata(20, 0.7);
        System.out.println("Is the forest in fire ? "+kAC.isOnFire());
        System.out.println("Is the forest completely rased ? "+kAC.isRazed());
        kAC.displayForest();
        
        try {
            iAC = new Automata(9, 0.3);
        } catch (IllegalArgumentException e) {
            System.out.println("Dimension < 10 threw an exception");
        }
        
        iAC = new Automata(15, 0.8);
        iAC.putFire();
        while (iAC.isOnFire()) {
            iAC.propagateFire(1);
        }
        
        
        for (double d=0.05; d<=1; d+=0.05) {
            int c = 0;
            for (int j=0; j<100; j++) {
                iAC = new Automata(25, d);
                iAC.putFire();
                iAC.propagateFire(500);
                if (iAC.isRazed()) {
                    c++;
                }
            }
            System.out.println("With density "+d+" : "+c+" forest completly razed");
        }
    }
}