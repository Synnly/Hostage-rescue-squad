package testCoups;
import coups.Deplacement;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeplacement {

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
    @DisplayName("Test basique du constructeur de Déplacement avec cout = 1, probasSucces = 0.9")
    public void testDeplacementAvecParametresAttendus(){
        Deplacement deplacement = new Deplacement(1,0.9);

        expect(op.getX()).andReturn(10);
        expect(op.getY()).andReturn(10);
        expect(op.getPointsAction()).andStubReturn(100);
        op.setX(anyInt());
        expectLastCall();
        op.setY(anyInt());
        expectLastCall();
        op.removePointsAction(anyInt());
        expectLastCall();
        replay(op);


        expect(arr.getX()).andStubReturn(11);
        expect(arr.getY()).andStubReturn(10);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);



        expect(ennemi1.getX()).andReturn(3);
        expect(ennemi1.getY()).andReturn(3);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);


        List<Terroriste> ennemis = new ArrayList<>();
        ennemis.add(ennemi1);
        ennemis.add(ennemi2);

        expect(envMock.getEnnemis()).andReturn(ennemis);

        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(0.0);
        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);


        replay(envMock);
        deplacement.effectuer(envMock,op,arr);
        assertEquals(deplacement.cout , 1);
        assertEquals(deplacement.probaSucces , 0.9);

        verify(envMock,op,arr);



    }

    // ==================== effectuer ====================
    @Test
    @DisplayName("Test si un opérateur atteint sa case d'arrivée sachant qu'il a assez de points d'action et aucun obstacle")
    public void testCaseOperateurApresDeplacement(){

        expect(arr.getX()).andStubReturn(11);
        expect(arr.getY()).andStubReturn(10);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);


        expect(ennemi1.getX()).andReturn(3);
        expect(ennemi1.getY()).andReturn(3);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);


        List<Terroriste> ennemis = new ArrayList<>();
        ennemis.add(ennemi1);
        ennemis.add(ennemi2);

        expect(envMock.getEnnemis()).andStubReturn(ennemis);
        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(0.0);
        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);
        replay(envMock);

        Deplacement deplacement = new Deplacement(1,0.9);
        Operateur operateur = new Operateur(envMock,10, 10, 4, deplacement, tirMock);

        deplacement.effectuer(envMock,operateur,arr);

        assertEquals(operateur.getX() , arr.getX());
        assertEquals(operateur.getY() , arr.getY());


        verify(envMock,arr);
    }

    @Test
    @DisplayName("Test si un Ennemi atteint sa case d'arrivée sachant qu'il a assez de points d'action et aucun obstacle")
    public void testCaseEnnemiApresDeplacement(){

        expect(arr.getX()).andStubReturn(11);
        expect(arr.getY()).andStubReturn(10);
        replay(arr);


        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        expect(envMock.getOperateurActif()).andStubReturn(op);
        replay(envMock);

        Deplacement deplacement = new Deplacement(0,0.9);
        Terroriste ennemi = new Terroriste(envMock,10, 10, 4, deplacement, tirMock);

        deplacement.effectuer(envMock,ennemi,arr);

        assertEquals(ennemi.getX() , arr.getX());
        assertEquals(ennemi.getY() , arr.getY());

        verify(envMock,arr);
    }

    @Test
    @DisplayName("Test si l'opérateur a bien été débité des points d'actions nécessaires à son déplacement")
    public void testPointsActionOperateurApresDeplacement(){

        expect(arr.getX()).andStubReturn(11);
        expect(arr.getY()).andStubReturn(10);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);


        expect(ennemi1.getX()).andReturn(3);
        expect(ennemi1.getY()).andReturn(3);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);


        List<Terroriste> ennemis = new ArrayList<>();
        ennemis.add(ennemi1);
        ennemis.add(ennemi2);

        expect(envMock.getEnnemis()).andStubReturn(ennemis);
        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);

        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(0.0);
        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);
        replay(envMock);

        Deplacement deplacement = new Deplacement(1,0.9);
        Operateur operateur = new Operateur(envMock,10, 10, 4, deplacement, tirMock);

        deplacement.effectuer(envMock,operateur,arr);

        assertEquals(3 , operateur.getPointsAction());
        verify(envMock,arr);
    }
    @Test
    @DisplayName("Test point d'action null avant et après Déplacement")
    public void testPointActionAZeroApresDeplacement(){

        expect(arr.getX()).andStubReturn(11);
        expect(arr.getY()).andStubReturn(10);
        replay(arr);


        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        expect(envMock.getOperateurActif()).andStubReturn(op);
        replay(envMock);

        Deplacement deplacement = new Deplacement(0,0.9);
        Terroriste ennemi = new Terroriste(envMock,10, 10, 4, deplacement, tirMock);
        assertEquals(ennemi.getPointsAction() , 0);

        deplacement.effectuer(envMock,ennemi,arr);

        assertEquals(ennemi.getPointsAction() , 0);

        verify(envMock,arr);
    }
    @Test
    @DisplayName("Test le nombre de case valide ici :  0")
    public void testEnnemiCaseValideNull(){

        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        expect(envMock.getPlateau()).andStubReturn(new ArrayList<>());

        replay(envMock);

        Deplacement deplacement = new Deplacement(0,0.9);
        Terroriste ennemi = new Terroriste(envMock,10, 10, 4, deplacement, tirMock);

        List<Case> cases = deplacement.getCasesValides(envMock,ennemi);

        assertEquals(cases.size() , 0);

        verify(envMock);
    }


    @Test
    @DisplayName("Test le nombre de cases valides ici :  3 (5 cases : 1 ennemi présent, 1 case trop éloignée, 3 valides)")
    public void testOperateurCasesValideNonNull(){
        Environnement envMockForSize = createMock(Environnement.class);
        expect(envMockForSize.getLargeur()).andStubReturn(20);
        expect(envMockForSize.getHauteur()).andStubReturn(20);
        replay(envMockForSize);

        Case case1 = new CaseNormale(envMockForSize,11,11);
        Case case2 = new CaseNormale(envMockForSize,9,10);
        Case case3 = new CaseNormale(envMockForSize,10,11);
        Case case4 = new CaseNormale(envMockForSize,10,9);
        Case case5 = new CaseNormale(envMockForSize,14,13);


        ArrayList<Case>  cases = new ArrayList<>(Arrays.asList(case1,case2,case3,case4,case5));

        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);

        expect(envMock.getPlateau()).andStubReturn(cases);

        Deplacement deplacement = new Deplacement(1,0.9);
        Operateur operateur = new Operateur(envMockForSize,10, 10, 4, deplacement, tirMock);
        expect(envMock.getOperateurActif()).andStubReturn(operateur);



        expect(ennemi1.getX()).andStubReturn(3);
        expect(ennemi1.getY()).andStubReturn(3);
        replay(ennemi1);
        expect(ennemi2.getX()).andStubReturn(10);
        expect(ennemi2.getY()).andStubReturn(9);
        replay(ennemi2);


        List<Terroriste> ennemis = new ArrayList<>();
        ennemis.add(ennemi1);
        ennemis.add(ennemi2);

        expect(envMock.getEnnemis()).andReturn(ennemis);

        replay(envMock);



        List<Case> casesValides = deplacement.getCasesValides(envMock,operateur);

        assertEquals(casesValides.size() , 3);

        verify(envMock,envMockForSize);
    }

    // echoué
    @Test
    @DisplayName("Test si un opérateur reste sur sa case départ sachant qu'il a assez de points d'action et qu'il rate son action")
    public void testCaseOperateurApresDeplacementEchoue(){

        expect(arr.getX()).andStubReturn(10);
        expect(arr.getY()).andStubReturn(10);
        expect(arr.estObjectif()).andStubReturn(false);
        replay(arr);


        expect(ennemi1.getX()).andReturn(3);
        expect(ennemi1.getY()).andReturn(3);
        replay(ennemi1);
        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);
        replay(ennemi2);


        List<Terroriste> ennemis = new ArrayList<>();
        ennemis.add(ennemi1);
        ennemis.add(ennemi2);

        expect(envMock.getEnnemis()).andStubReturn(ennemis);
        expect(envMock.getLargeur()).andStubReturn(20);
        expect(envMock.getHauteur()).andStubReturn(20);
        List<Double> nbAlea = new ArrayList<>();
        nbAlea.add(1.0);
        expect(envMock.getNombresAleatoires(1)).andStubReturn(nbAlea);
        replay(envMock);

        Deplacement deplacement = new Deplacement(1,0.9);
        Operateur operateur = new Operateur(envMock,10, 10, 4, deplacement, tirMock);

        deplacement.effectuer(envMock,operateur,arr);

        assertEquals(operateur.getX() , arr.getX());
        assertEquals(operateur.getY() , arr.getY());


        verify(envMock,arr);
    }
}
