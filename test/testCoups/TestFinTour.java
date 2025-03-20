package testCoups;

import coups.Deplacement;
import coups.FinTour;
import coups.Tir;
import carte.Case;
import carte.CaseNormale;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import personnages.Operateur;
import personnages.Terroriste;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFinTour {


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

        assertEquals(1, finTour.cout);
        assertEquals(1, finTour.probaSucces);

    }

    @Test
    @DisplayName("test de la fonction effectuer avec un Opérateur en paramètre qui remet à Zéro les points d'action")
    public void testPointsActionOperateurAvantEffectuerAQuatreApresEffectuerEstANul(){

        FinTour finTour = new FinTour(1,0.9);

        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        replay(envMock);


        Operateur operateur = new Operateur(envMock,10, 10, 4, deplMock, tirMock);
        assertEquals(operateur.getPointsAction() , 4);

        finTour.effectuer(envMock,operateur,arr);

        assertEquals(operateur.getPointsAction() , 0);

        verify(envMock);
    }

    @Test
    @DisplayName("test de la fonction effectuer avec un Terroriste en paramètre qui remet à Zéro les points d'action")
    public void testPointsActionTerroristeAvantEffectuerAQuatreApresEffectuerEstANul(){

        FinTour finTour = new FinTour(1,0.9);

        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        replay(envMock);


        Terroriste terroriste = new Terroriste(envMock,10, 10, 4, deplMock, tirMock);
        assertEquals(terroriste.getPointsAction() , 0);

        finTour.effectuer(envMock,terroriste,arr);

        assertEquals(terroriste.getPointsAction() , 0);

        verify(envMock);
    }
}



