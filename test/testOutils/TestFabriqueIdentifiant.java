package testOutils;


import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import outils.FabriqueIdentifiant;
import static org.junit.Assert.*;


public class TestFabriqueIdentifiant {



/*Reset est appelé en début de fonction pour ne pas générer d'erreurs lors de
l'exécution séquentielle des tests

 */
    @Test
    @DisplayName("Test de sur les valeurs de fabrique identifiant")
    public void testDeLincrementationDesIdentifiants() {
        FabriqueIdentifiant.resetIdPersonnage();
        assertEquals(0, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(1, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(2, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(3, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(4, FabriqueIdentifiant.nextIdPersonnage());

    }

    @Test
    @DisplayName("Test de la remise à zéro des identifiant")
    public void testDuResetDesIdentifiants() {
        FabriqueIdentifiant.resetIdPersonnage();
        assertEquals(0, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(1, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(2, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(3, FabriqueIdentifiant.nextIdPersonnage());
        assertEquals(4, FabriqueIdentifiant.nextIdPersonnage());
        FabriqueIdentifiant.resetIdPersonnage();
        assertEquals(0, FabriqueIdentifiant.nextIdPersonnage());



    }

}

