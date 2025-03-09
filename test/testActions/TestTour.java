package testActions;

import actions.Deplacement;
import actions.FinTour;
import actions.Tir;
import carte.Case;
import carte.CaseNormale;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import personnages.Operateur;
import personnages.Terroriste;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTour {


    private Environnement envMock = null;
    private Deplacement deplMock = null;
    private Tir tirMock = null;
    private Operateur op = null;
    private Case arr = null;

    private Terroriste ennemi1 = null;
    private Terroriste ennemi2 = null;
    private Terroriste ennemi = null;




    @Before
    public void setup(){
        envMock = createMock(Environnement.class);
        tirMock = createMock(Tir.class);
        deplMock = createMock(Deplacement.class);
        arr = createMock(CaseNormale.class);
        ennemi1 = createMock(Terroriste.class);
        ennemi2 = createMock(Terroriste.class);
        ennemi = createMock(Terroriste.class);
        op = createMock(Operateur.class);


    }

    @After
    public void teardown(){
        envMock = null;
        tirMock = null;
        deplMock = null;
        op = null;
        arr = null;
        ennemi1 = null;
        ennemi2 = null;
        ennemi = null;
    }

    @Test
    @DisplayName("Test Simple du constructeur FinTour (ignoredInt,igonredDouble) = (1,0.9)")
    public void testFinTourAvecParametresAttendus(){
        FinTour finTour = new FinTour(1,0.9);

        assertEquals(finTour.cout , 1);
        assertEquals(finTour.probaSucces , 0.9);

    }
}
