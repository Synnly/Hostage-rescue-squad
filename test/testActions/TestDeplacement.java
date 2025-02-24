package testActions;
import actions.Deplacement;
import actions.Tir;
import observable.Environnement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import personnages.Operateur;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;
public class TestDeplacement {

    private Environnement envMock = null;
    private Deplacement deplMock = null;
    private Tir tirMock = null;
    private Operateur op = null;

    @Before
    public void setup(){
        envMock = createMock(Environnement.class);
        tirMock = createMock(Tir.class);
        deplMock = createMock(Deplacement.class);
    }

    @After
    public void teardown(){
        envMock = null;
        tirMock = null;
        deplMock = null;
        op = null;
    }
    @Test
    @DisplayName("C")
}
