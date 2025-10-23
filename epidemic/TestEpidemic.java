package epidemic;

public class TestEpidemic {
    public static void main(String[] args) {
        EpidemicAutomata epi = new EpidemicAutomata(
            15,   // dimension
            0.8,  // densité d’individus
            0.2,  // taux de vaccination
            0.3   // probabilité de décès
        );

        epi.propagateEpidemic(30);
    }
}
