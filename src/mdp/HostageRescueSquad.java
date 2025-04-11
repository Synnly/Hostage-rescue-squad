package mdp;

import carte.cases.AucuneCase;
import carte.cases.Case;
import carte.Routine;
import coups.Coup;
import observable.Environnement;
import org.javatuples.Pair;
import personnages.*;

import java.util.*;

public class HostageRescueSquad implements MDP{
    private final Environnement env;
    private final Environnement envCopy;
    private Etat[] etats = null;

    private Map<Etat, Map<Pair<Coup, Direction>, Map<Etat, Double>>> transitions = new HashMap<>();
    private Map<Etat, Pair<Coup, Direction>[]> coupsEtat = new HashMap<>();

    /**
     * Contructeur du MDP représentant le jeu Hostage Rescue Squad
     * @param env L'environnement
     */
    public HostageRescueSquad(Environnement env){
        this.env = env;
        this.envCopy = env.copy();
    }

    @Override
    public Map<Etat, Pair<Coup, Direction>[]> getCoups() {
        if(!coupsEtat.isEmpty()){
            return coupsEtat;
        }

        Operateur op = env.getOperateurActif();
        Coup[] listeCoups = {op.getDeplacement(), op.getTir(), op.getEliminationSilencieuse()};
        Map<Etat, Pair<Coup, Direction>[]> coups = new HashMap<>();

        Etat[] etats = getEtats();
        Etat restoreState = new EtatNormal(envCopy);

        for(Etat e : etats){
            envCopy.setEtat(e);
            Operateur opCopy = envCopy.getOperateurActif();
            ArrayList<Pair<Coup, Direction>> listePaires = new ArrayList<>();
            listePaires.add(new Pair<>(op.getFinTour(), Direction.AUCUN));

            for(Coup c : listeCoups){
                for(Case caseValide : c.getCasesValides(envCopy, opCopy)){

                    // Ajout de la direction
                    if(opCopy.getX() == -1 || opCopy.getY() == -1){
                        listePaires.add(new Pair<>(c, Direction.AUCUN));
                    } else if (opCopy.getX() > caseValide.x) {
                        listePaires.add(new Pair<>(c, Direction.GAUCHE));
                    } else if (opCopy.getX() < caseValide.x) {
                        listePaires.add(new Pair<>(c, Direction.DROITE));
                    } else if (opCopy.getY() > caseValide.y) {
                        listePaires.add(new Pair<>(c, Direction.HAUT));
                    } else {
                        listePaires.add(new Pair<>(c, Direction.BAS));
                    }
                }

                coups.put(e, listePaires.toArray(new Pair[0]));
            }
        }

        envCopy.setEtat(restoreState);
        if(coupsEtat.isEmpty()){
            coupsEtat.putAll(coups);
        }

        return coupsEtat;
    }


    @Override
    public Etat[] getEtats() {
        // Si états déjà calculés
        if (etats != null){
            return etats;
        }

        Set<Etat> listeEtats = new HashSet<>();
        int nbOps = 1;                                              // A changer quand plusieurs operateurs
        Routine routine = env.getEnnemis().get(0).getRoutine();     // A changer quand plusieurs routines
        int[] indCasesOp = new int[nbOps];
        int nbCases = env.getLargeur() * env.getHauteur();
        int nbTerr = env.getEnnemis().size();
        int maxPA = env.getOperateurActif().getMaxPointsAction();

        // Quels operateurs en vie
        for(int idCombOpsEnVie = 0; idCombOpsEnVie < Math.pow(2, nbOps); idCombOpsEnVie++) {
            for (int indCase = 0; indCase < Math.pow(nbCases, nbOps); indCase++) {

                // Positions operateurs
                for (int indOp = 0; indOp < nbOps; indOp++) {
                    if(((int) (idCombOpsEnVie / Math.pow(2, indOp))) % 2 == 0){    // Si op mort, pos = -1
                        indCasesOp[indOp] = -1;
                    }
                    else {
                        indCasesOp[indOp] = ((int) (indCase / Math.pow(nbCases, indOp))) % nbCases; //calcul qui retire bit droite et gauche ( ne garde que l'indice de l'opérateur)
                    }
                }

                // Quels terr en vie
                for (int idCombTerrVie = 0; idCombTerrVie < Math.pow(2, nbTerr); idCombTerrVie++) {

                    // Initialisation positions terroristes si en vie
                    int[] indCasesTerr = new int[nbTerr];
                    for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
                        if (((int) (idCombTerrVie / Math.pow(2, indTerr))) % 2 == 0) {    // Si terr mort, pos = -1
                            indCasesTerr[indTerr] = -1;
                        } else {
                            Case caseTerr = env.getCase(env.getEnnemis().get(indTerr).getX(), env.getEnnemis().get(indTerr).getY());
                            indCasesTerr[indTerr] = routine.indexOf(caseTerr);
                        }
                    }

                    // Positions terroristes
                    for (int indCaseRoutine = -1; indCaseRoutine < routine.taille()-1; indCaseRoutine++) {
                        for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
                            indCasesTerr[indTerr] = routine.nextIndex(indCasesTerr[indTerr]);
                        }

                        // Quels operateurs ont l'objectif
                        boolean[] aObjectif = new boolean[nbOps];
                        for (int i = 0; i < Math.pow(2, nbOps); i++) {
                            for (int j = 0; j < nbOps; j++) {
                                aObjectif[j] = ((int) (i / Math.pow(2, j))) == 1;
                            }

                            // Niveau de menace
                            for (int menace = env.getMinMenace(); menace <= env.getMaxMenace(); menace++) {

                                // Nombre de PA
                                for(int idNbPa = 0; idNbPa < Math.pow(maxPA+1, nbOps); idNbPa ++){

                                    int[] listePaOps = new int[nbOps];
                                    for (int j = 0; j < nbOps; j++) {
                                        listePaOps[j] = ((int) (idNbPa / Math.pow(maxPA, j)));
                                    }

                                    Etat e = creerEtat(indCasesOp, listePaOps, aObjectif, indCasesTerr, menace);
                                    if (etatEstValide(e)) {
                                        listeEtats.add(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Conversion
        etats = new Etat[listeEtats.size()];
        etats = listeEtats.toArray(etats);
        return etats;
    }

    @Override
    public Map<Etat, Double> transition(Etat etatDepart, Coup coup, Direction direction){
        // Si transition déjà calculée
        if(transitions.get(etatDepart) != null){
            if(transitions.get(etatDepart).get(new Pair<>(coup, direction)) != null){
                return transitions.get(etatDepart).get(new Pair<>(coup, direction));
            }
        }

        Map<Etat, Double> distribution = new HashMap<>();
        Etat restoreState = new EtatNormal(envCopy);

        // Simulation
        if(coup.probaSucces != 1 && coup.probaSucces != 0) {    // Succès ou échec non garanti
            envCopy.setEtat(etatDepart);
            distribution.put(simuler(etatDepart, coup, envCopy.getOperateurActif(), direction, true), coup.probaSucces);

            envCopy.setEtat(etatDepart);
            distribution.put(simuler(etatDepart, coup, envCopy.getOperateurActif(), direction, false), 1-coup.probaSucces);
        }
        else{   // Succès ou échec garanti
            envCopy.setEtat(etatDepart);
            distribution.put(simuler(etatDepart, coup, envCopy.getOperateurActif(), direction, coup.probaSucces == 1), coup.probaSucces);
        }

        envCopy.setEtat(restoreState);

        // Transition des ennemis
        distribution = transitionEnnemis(distribution);

        // Sauvegarde
        if(transitions.get(etatDepart) == null){
            Map<Pair<Coup, Direction>, Map<Etat, Double>> coupEtat = new HashMap<>();
            coupEtat.put(new Pair<>(coup, direction), distribution);
            transitions.put(etatDepart, coupEtat);
        } else {
            transitions.get(etatDepart).put(new Pair<>(coup, direction), distribution);
        }

        return distribution;
    }

    @Override
    public double recompense(Etat s, Coup coup, Etat sPrime){
        double recomp = 0;

        // coup
        if (coup.estDeplacement()) {
            recomp +=  valeurDeplacement;
        }
        else if (coup.estTir()) {
            recomp += Math.max(1, s.indCaseTerroristes.length- sPrime.indCaseTerroristes.length) * valeurTuerEnnemi;
        }
        else if (coup.estElimSil()) {
            recomp += Math.max(1, s.indCaseTerroristes.length- sPrime.indCaseTerroristes.length) * valeurTuerEnnemi;
        }
        else if (coup.estFinTour()) {
            recomp += valeurDeplacement;
        }

        // echec
        if(sPrime.estTerminal() && !sPrime.estReussite()){
            recomp += valeurEchec;
        }

        // changement de niveau de menace
        recomp += (s.menace - sPrime.menace) * valeurDeltaMenace;

        // recuperation de l'objectif
        for (int i = 0; i < s.aObjectif.length; i++) {
            if(sPrime.aObjectif[i] && !s.aObjectif[i]){
                recomp += valeurObjectif;
            }
        }

        // reussite de la mission
        if(sPrime.estTerminal() && sPrime.estReussite()){
            recomp += valeurReussite;
        }

        return recomp;
    }

    /**
     * Indique si l'état est valide
     * @param e L'état
     * @return Vrai si e est valide, faux sinon
     */
    public boolean etatEstValide(Etat e){
        // Terroriste et operateur sur la meme case
        Routine routine = env.getEnnemis().get(0).getRoutine();
        for(int indOp = 0; indOp < e.indCaseOperateurs.length; indOp ++) {
            for (int indTerr = 0; indTerr < e.indCaseTerroristes.length; indTerr++) {
                int indCasePlateauTerr = env.getPlateau().indexOf(routine.getCase(e.indCaseTerroristes[indTerr]));
                if(e.indCaseOperateurs[indOp] != -1 && e.indCaseOperateurs[indOp] == indCasePlateauTerr){
                    return false;
                }
            }
        }

        // Plusieurs objectifs récupérés
        // À changer si plusieurs objectifs
        boolean aObj = false;
        for(boolean opAObjectif : e.aObjectif){
            if(opAObjectif && !aObj){
                aObj = true;
            }
            else if(opAObjectif && aObj){
                return false;
            }
        }

        // Tous les ennemis morts et menace != minMenace
        int nbEnnemisMorts = env.getEnnemis().size();
        for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
            if(e.indCaseTerroristes[indTerr] != -1){
                nbEnnemisMorts --;
            }
        }
        // Si aucun ennemis n'est mort la menace ne peut pas augmenter (pour l'instant)
        if(nbEnnemisMorts == 0 && e.menace != env.getMinMenace()){
            return false;
        }
        if (nbEnnemisMorts == env.getEnnemis().size() && e.menace != env.getMinMenace()){
            return false;
        }

        // A enlever quand respawn d'ennemis
        // Niveau de menace != minMenace + nbEnnemisMorts quand tous ennemis pas morts

        if(e.menace > env.getMinMenace() + env.getEnnemis().size()){
            if(e.estNormal()){
                if ( e.equals(new EtatNormal(new int[]{11},new int[]{2},new boolean[]{false},new int[]{1, -1},5))) {
                    //System.out.println("oui");
                }
            }
            return false;
        }

        // Nombre de PA cohérent
        int maxPA = env.getOperateurActif().getMaxPointsAction();
        boolean opEnJeu = false;

        for(int nbPa : e.nbPAOperateurs){
            if (nbPa != 0 || nbPa != maxPA){
                if(opEnJeu){
                    return false;
                }
                opEnJeu = true;
            }
        }

        return true;
    }


    /**
     * Calcule l'état après le coup effectué
     * @param etatDepart L'état de départ
     * @param coup Le coup à effectuer
     * @param perso Le personnage effectuant le coup
     * @param direction La direction dans lequel le coup est effectué
     * @param succes Vrai si le coup réussi, faux sinon
     * @return L'état d'arrivée
     */
    private Etat simuler(Etat etatDepart, Coup coup, Personnage perso, Direction direction, boolean succes){
        // Copie profonde de l'environnement dans l'etat de depart
        Coup coupCopy = coup.copy();
        Etat restoreState = creerEtat(envCopy);
        envCopy.setEtat(etatDepart);
        Personnage persoCopy = envCopy.getPersonnage(perso);
        coupCopy.probaSucces = succes ? 1 : 0;

        // Simulation
        switch (direction){
            case HAUT -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY() - 1));
            case BAS -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY() + 1));
            case GAUCHE -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX() - 1, persoCopy.getY()));
            case DROITE -> coupCopy.effectuer(envCopy, persoCopy, envCopy.getCase(persoCopy.getX() + 1, persoCopy.getY()));
            case AUCUN -> coupCopy.effectuer(envCopy, persoCopy, AucuneCase.instance);
        }

        Etat etat = creerEtat(envCopy);
        envCopy.setEtat(restoreState);

        return etat;
    }

    /**
     * Crée l'état correspondant en fonction de si la mission est finie et si elle est une réussite ou non
     * @param env L'environnement
     * @return L'état de l'environnement
     */
    public Etat creerEtat(Environnement env){
        if(env.isMissionFinie()){
            if(env.isEchec()){
                return new EtatEchec(env);
            }
            return new EtatReussite(env);
        }
        else{
            return new EtatNormal(env);
        }
    }

    /**
     * Crée l'état correspondant.
     * @param indCaseOperateurs La liste des positions des cases où se situent les opérateurs dans la liste des cases du
     *                          plateau
     * @param nbPaOps La liste des quantités de points d'action de chaque opérateur
     * @param aObjectif La liste des booléens indiquant si le ie opérateur possède un objectif
     * @param indCaseTerroristes La liste des positions des cases où se situent les terroristes dans la liste des cases
     *                           de la routine
     * @param menace Le niveau de menace
     * @return L'état avec le type correpondant (normal, échec ou réussite)
     */
    public Etat creerEtat(int[] indCaseOperateurs, int[] nbPaOps, boolean[] aObjectif, int[] indCaseTerroristes, int menace){
        // Un des opérateurs morts ?
        for(int indCase : indCaseOperateurs){
            if(indCase == -1){
                return new EtatEchec(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
            }
        }

        // Tous les objectifs récupérés ?
        boolean tousObjRecup = true;
        for(boolean opAObj : aObjectif){
            if(!opAObj){
                tousObjRecup = false;
                break;
            }
        }

        if(tousObjRecup) {
            // ET tous les opérateurs sur la ligne du bas ?
            for (int indCase : indCaseOperateurs) {
                if (indCase < (env.getHauteur() - 1) * env.getLargeur()) {
                    return new EtatNormal(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
                }
            }
            return new EtatReussite(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
        }
        return new EtatNormal(indCaseOperateurs.clone(), nbPaOps.clone(), aObjectif.clone(), indCaseTerroristes.clone(), menace);
    }

    /**
     * Calcule les états et leur distribution après que les ennemis ont effectué leurs actions
     * @param distribution La distribution des états du tour joueur
     * @return La distribution à la fin du tour ennemi
     */
    private Map<Etat, Double> transitionEnnemis(Map<Etat, Double> distribution){
        Map<Etat, Double> distributionEnnemis = new HashMap<>();
        int nbTerrs = env.getEnnemis().size();
        Coup[] listeCoups = {env.getEnnemis().get(0).getDeplacement(), env.getEnnemis().get(0).getTir()};

        Etat restoreState = creerEtat(envCopy);
        for(Etat e : distribution.keySet()) {
            if(e.estTerminal() || Arrays.stream(e.nbPAOperateurs).sum() != 0){    // Etat terminal ou pas le tour de l'ennemi donc rien à faire
                distributionEnnemis.put(e, distribution.get(e));
                continue;
            }
            for (int idSuiteCoups = 0; idSuiteCoups < Math.pow(listeCoups.length, nbTerrs); idSuiteCoups++) {
                // Suite de coups
                List<Coup> suiteCoup = new ArrayList<>(e.menace);
                double proba = 1;
                for (int i = 0; i < e.menace; i++) {
                    Coup c = listeCoups[((int) (idSuiteCoups / Math.pow(listeCoups.length, i))) % listeCoups.length];
                    suiteCoup.add(c);
                    proba *= envCopy.getProbaCoupEnnemi(c);
                }

                // Simulation
                envCopy.setEtat(e);
                envCopy.effectuerCoupsTerroristes(suiteCoup);

                Etat etatSimu = creerEtat(envCopy);
                etatSimu.nbPAOperateurs[0] = env.getOperateurActif().getMaxPointsAction();
                distributionEnnemis.put(etatSimu, proba * distribution.get(e));
                envCopy.setEtat(restoreState);
            }
        }

        return distributionEnnemis;
    }
}
