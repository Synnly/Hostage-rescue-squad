package outils;

public class FabriqueIdentifiant {
    private static FabriqueIdentifiant instance =  new FabriqueIdentifiant();
    private int identifiant = 0;
    public FabriqueIdentifiant getInstance(){
        return instance;
    }
    public int getId(){
        return identifiant++;
    }
}
