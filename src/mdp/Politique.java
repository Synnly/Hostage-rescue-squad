package mdp;

public interface Politique {
    public Action P(MDP mdp, Etat s);
}
