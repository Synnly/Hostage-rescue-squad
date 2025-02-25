package testActions;
import actions.Deplacement;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;
public class TestDeplacement {

    private Environnement envMock = null;
    private Deplacement deplMock = null;
    private Tir tirMock = null;
    private Operateur op = null;
    private Case arr = null;

    private Terroriste ennemi1 = null;
    private Terroriste ennemi2 = null;



    @Before
    public void setup(){
        envMock = createMock(Environnement.class);
        tirMock = createMock(Tir.class);
        deplMock = createMock(Deplacement.class);
        arr = createMock(CaseNormale.class);
        ennemi1 = createMock(Terroriste.class);
        ennemi2 = createMock(Terroriste.class);
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
    }
    // ==================== Constructeur ====================
    @Test
    @DisplayName("Test basique du constructeur de Déplacement avec cout = 1, probasSucces = 0.9")
    public void testDeplacementAvecParametresAttendus(){
        Deplacement deplacement = new Deplacement(1,0.9);

        expect(op.getX()).andReturn(10);
        expect(op.getY()).andReturn(10);
        expect(op.getPointsAction()).andStubReturn(100);
        replay(op);


        expect(arr.getX()).andReturn(11);
        expect(arr.getY()).andReturn(10);
        expect(arr.estObjectif()).andReturn(false);
        replay(arr);



        expect(ennemi1.getX()).andReturn(3);
        expect(ennemi1.getY()).andReturn(3);

        expect(ennemi2.getX()).andReturn(5);
        expect(ennemi2.getY()).andReturn(5);

        List<Terroriste> ennemis = new ArrayList<>();
        ennemis.add(ennemi1);
        ennemis.add(ennemi2);

        expect(envMock.getEnnemis()).andReturn(ennemis);

        replay(envMock);
        deplacement.effectuer(envMock,op,arr);

        verify(envMock,op,arr);








        //assertEquals();
    }
}
