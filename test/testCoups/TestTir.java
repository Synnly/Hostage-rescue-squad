package testCoups;

import coups.Deplacement;
import coups.Tir;
import carte.Case;
import carte.CaseNormale;
import observable.Environnement;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTir {

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
    // ==================== Constructeur ====================
    @Test
    @DisplayName("Test Tir sur un ennemi et élimination de celui-ci")
    public void testTirSurUnEnnemiPresent(){
        expect(arr.getX()).andStubReturn(10);
        expect(arr.getY()).andStubReturn(5);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);

        expect(op.getX()).andStubReturn(10);
        expect(op.getY()).andStubReturn(1);
        expect(op.getPointsAction()).andStubReturn(100);
        op.setX(anyInt());
        expectLastCall();
        op.setY(anyInt());
        expectLastCall();
        op.removePointsAction(anyInt());
        expectLastCall();
        replay(op);

        Environnement envMockPourCase = createMock(Environnement.class);
        expect(envMockPourCase.getLargeur()).andStubReturn(20);
        expect(envMockPourCase.getHauteur()).andStubReturn(20);
        replay(envMockPourCase);

        Case case1 = createMock(CaseNormale.class);
        expect(case1.getX()).andStubReturn(10);
        expect(case1.getY()).andStubReturn(1);
        expect(case1.estObjectif()).andStubReturn(false);
        expect(case1.peutVoir()).andStubReturn(true);


        Case case2 = createMock(CaseNormale.class);
        expect(case2.getX()).andStubReturn(10);
        expect(case2.getY()).andStubReturn(2);
        expect(case2.estObjectif()).andStubReturn(false);
        expect(case2.peutVoir()).andStubReturn(true);

        Case case3 = createMock(CaseNormale.class);
        expect(case3.getX()).andStubReturn(10);
        expect(case3.getY()).andStubReturn(3);
        expect(case3.estObjectif()).andStubReturn(false);
        expect(case3.peutVoir()).andStubReturn(true);

        Case case4 = createMock(CaseNormale.class);
        expect(case4.getX()).andStubReturn(10);
        expect(case4.getY()).andStubReturn(4);
        expect(case4.estObjectif()).andStubReturn(false);
        expect(case4.peutVoir()).andStubReturn(true);

        Case case5 = createMock(CaseNormale.class);
        expect(case5.getX()).andStubReturn(10);
        expect(case5.getY()).andStubReturn(5);
        expect(case5.estObjectif()).andStubReturn(false);
        expect(case5.peutVoir()).andStubReturn(true);

        replay(case1, case2, case3, case4, case5);



        expect(ennemi1.getX()).andReturn(10);
        expect(ennemi1.getY()).andReturn(5);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);




        ArrayList<Terroriste> ennemis = new ArrayList<>(Arrays.asList(ennemi1, ennemi2));
        expect(envMock.getEnnemis()).andStubReturn(ennemis);

        expect(envMock.getEnnemis()).andStubReturn(ennemis);
        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);

        expect(envMock.getCase(10,1)).andStubReturn(case1);
        expect(envMock.getCase(10,2)).andStubReturn(case2);
        expect(envMock.getCase(10,3)).andStubReturn(case3);
        expect(envMock.getCase(10,4)).andStubReturn(case4);
        expect(envMock.getCase(10,5)).andStubReturn(case5);

        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(1.0);
        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);
        expect(envMock.aEnnemisSurCase(EasyMock.anyObject())).andStubReturn(true);
        envMock.tuerEnnemis(EasyMock.anyObject());
        expectLastCall().andAnswer(() ->{ennemis.remove(ennemi1); return null;});

        replay(envMock);

        Deplacement deplacement = new Deplacement(1,0.9);
        Tir tir = new Tir(1,1);
        tir.effectuer(envMock,op,arr);
        assertEquals(1 , ennemis.size());
        verify(envMock,arr);
    }

    @Test
    @DisplayName("Test Tir sur une case sans rencontrer d'ennemis en chemin")
    public void testTirAvecAucunEnnemiPresentDansLaLigneDeTir(){
        expect(arr.getX()).andStubReturn(10);
        expect(arr.getY()).andStubReturn(5);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);

        expect(op.getX()).andStubReturn(10);
        expect(op.getY()).andStubReturn(1);
        expect(op.getPointsAction()).andStubReturn(100);
        op.setX(anyInt());
        expectLastCall();
        op.setY(anyInt());
        expectLastCall();
        op.removePointsAction(anyInt());
        expectLastCall();
        replay(op);

        Environnement envMockPourCase = createMock(Environnement.class);
        expect(envMockPourCase.getLargeur()).andStubReturn(20);
        expect(envMockPourCase.getHauteur()).andStubReturn(20);
        replay(envMockPourCase);

        Case case1 = createMock(CaseNormale.class);
        expect(case1.getX()).andStubReturn(10);
        expect(case1.getY()).andStubReturn(1);
        expect(case1.estObjectif()).andStubReturn(false);
        expect(case1.peutVoir()).andStubReturn(true);


        Case case2 = createMock(CaseNormale.class);
        expect(case2.getX()).andStubReturn(10);
        expect(case2.getY()).andStubReturn(2);
        expect(case2.estObjectif()).andStubReturn(false);
        expect(case2.peutVoir()).andStubReturn(true);

        Case case3 = createMock(CaseNormale.class);
        expect(case3.getX()).andStubReturn(10);
        expect(case3.getY()).andStubReturn(3);
        expect(case3.estObjectif()).andStubReturn(false);
        expect(case3.peutVoir()).andStubReturn(true);

        Case case4 = createMock(CaseNormale.class);
        expect(case4.getX()).andStubReturn(10);
        expect(case4.getY()).andStubReturn(4);
        expect(case4.estObjectif()).andStubReturn(false);
        expect(case4.peutVoir()).andStubReturn(true);

        Case case5 = createMock(CaseNormale.class);
        expect(case5.getX()).andStubReturn(10);
        expect(case5.getY()).andStubReturn(5);
        expect(case5.estObjectif()).andStubReturn(false);
        expect(case5.peutVoir()).andStubReturn(true);

        replay(case1, case2, case3, case4, case5);



        expect(ennemi1.getX()).andReturn(5);
        expect(ennemi1.getY()).andReturn(5);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);




        ArrayList<Terroriste> ennemis = new ArrayList<>(Arrays.asList(ennemi1, ennemi2));
        expect(envMock.getEnnemis()).andStubReturn(ennemis);

        expect(envMock.getEnnemis()).andStubReturn(ennemis);
        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);

        expect(envMock.getCase(10,1)).andStubReturn(case1);
        expect(envMock.getCase(10,2)).andStubReturn(case2);
        expect(envMock.getCase(10,3)).andStubReturn(case3);
        expect(envMock.getCase(10,4)).andStubReturn(case4);
        expect(envMock.getCase(10,5)).andStubReturn(case5);
        expect(envMock.aEnnemisSurCase(EasyMock.anyObject())).andStubReturn(false);
        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(1.0);
        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);
        replay(envMock);

        Deplacement deplacement = new Deplacement(1,0.9);
        Tir tir = new Tir(1,1);
        tir.effectuer(envMock,op,arr);
        assertEquals(2 , ennemis.size());
        verify(envMock,arr);
    }

    // échoué
    @Test
    @DisplayName("Test Tir échoué sur un ennemi")
    public void testTirSurUnEnnemiPresentEchoue() {
        expect(arr.getX()).andStubReturn(10);
        expect(arr.getY()).andStubReturn(5);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);

        expect(op.getX()).andStubReturn(10);
        expect(op.getY()).andStubReturn(1);
        expect(op.getPointsAction()).andStubReturn(100);
        op.setX(anyInt());
        expectLastCall();
        op.setY(anyInt());
        expectLastCall();
        op.removePointsAction(anyInt());
        expectLastCall();
        replay(op);

        Environnement envMockPourCase = createMock(Environnement.class);
        expect(envMockPourCase.getLargeur()).andStubReturn(20);
        expect(envMockPourCase.getHauteur()).andStubReturn(20);
        replay(envMockPourCase);

        Case case1 = createMock(CaseNormale.class);
        expect(case1.getX()).andStubReturn(10);
        expect(case1.getY()).andStubReturn(1);
        expect(case1.estObjectif()).andStubReturn(false);
        expect(case1.peutVoir()).andStubReturn(true);

        Case case2 = createMock(CaseNormale.class);
        expect(case2.getX()).andStubReturn(10);
        expect(case2.getY()).andStubReturn(2);
        expect(case2.estObjectif()).andStubReturn(false);
        expect(case2.peutVoir()).andStubReturn(true);

        Case case3 = createMock(CaseNormale.class);
        expect(case3.getX()).andStubReturn(10);
        expect(case3.getY()).andStubReturn(3);
        expect(case3.estObjectif()).andStubReturn(false);
        expect(case3.peutVoir()).andStubReturn(true);

        Case case4 = createMock(CaseNormale.class);
        expect(case4.getX()).andStubReturn(10);
        expect(case4.getY()).andStubReturn(4);
        expect(case4.estObjectif()).andStubReturn(false);
        expect(case4.peutVoir()).andStubReturn(true);

        Case case5 = createMock(CaseNormale.class);
        expect(case5.getX()).andStubReturn(10);
        expect(case5.getY()).andStubReturn(5);
        expect(case5.estObjectif()).andStubReturn(false);
        expect(case5.peutVoir()).andStubReturn(true);

        replay(case1, case2, case3, case4, case5);

        expect(ennemi1.getX()).andReturn(10);
        expect(ennemi1.getY()).andReturn(5);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);

        ArrayList<Terroriste> ennemis = new ArrayList<>(Arrays.asList(ennemi1, ennemi2));
        expect(envMock.getEnnemis()).andStubReturn(ennemis);
        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);

        expect(envMock.getCase(10,1)).andStubReturn(case1);
        expect(envMock.getCase(10,2)).andStubReturn(case2);
        expect(envMock.getCase(10,3)).andStubReturn(case3);
        expect(envMock.getCase(10,4)).andStubReturn(case4);
        expect(envMock.getCase(10,5)).andStubReturn(case5);

        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(1.0);

        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);

        expect(envMock.aEnnemisSurCase(EasyMock.anyObject())).andStubReturn(true);

        replay(envMock);

        Tir tir = new Tir(1, 0.9);
        tir.effectuer(envMock, op, arr);
        assertEquals(2 , ennemis.size());

        verify(envMock, arr);
    }

}
