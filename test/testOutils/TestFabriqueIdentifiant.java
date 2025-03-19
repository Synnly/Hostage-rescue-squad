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
        FabriqueIdentifiant.getInstance().resetId();
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 0);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 1);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 2);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 3);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 4);

    }

    @Test
    @DisplayName("Test de la remise à zéro des identifiant")
    public void testDuResetDesIdentifiants() {
        FabriqueIdentifiant.getInstance().resetId();
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 0);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 1);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 2);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 3);
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 4);
        FabriqueIdentifiant.getInstance().resetId();
        assertEquals(FabriqueIdentifiant.getInstance().getId(), 0);



    }

}

