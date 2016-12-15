package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.perception.ActionPerceptionPairMap;
import es.udc.gii.mdb.robot.ComponentValue;
import java.util.List;

/**
 *
 * This class represents two consecutive correlations from a limited size trace (depending on the memory configuration)
 * with the perceptions in which the correlations starts and finishes.
 * Suffix *R is related to the correlated trace where a reward is obtained, and suffix *I is related to the previous one correlated trace.
 * @author GII
 */
public class CorrelationChange {

    public enum TYPE {
        DECREASE, INCREASE, CONSTANT
    };

    private TYPE typeR; 
    private TYPE typeI;

    private MotivationEpisode perceptionR;
    private MotivationEpisode perceptionT;
    private MotivationEpisode perceptionT_1;
    private MotivationEpisode perceptionI;
    
    
    private List<MotivationEpisode> episodesR;
    private List<MotivationEpisode> episodesI;

    private int nR;
    private int nI;

    private String component;

    public CorrelationChange(String component, TYPE typeR, TYPE typeI, MotivationEpisode perceptionR, MotivationEpisode perceptionT, MotivationEpisode perceptionT_1, MotivationEpisode perceptionI, int nR, int nI, List<MotivationEpisode> episodesR, List<MotivationEpisode> episodesI) {
        this.typeR = typeR;
        this.typeI = typeI;
        this.perceptionR = perceptionR;
        this.perceptionT = perceptionT;
        this.perceptionT_1 = perceptionT_1;
        this.perceptionI = perceptionI;
        this.nR = nR;
        this.nI = nI;
        this.component = component;
        this.episodesR = episodesR;
        this.episodesI = episodesI;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(component).append("\n");
        sb.append(perceptionR).append("\n");
        sb.append(perceptionT).append("\n");
        sb.append(perceptionT_1).append("\n");
        sb.append(perceptionI).append("\n");
        sb.append(nR).append("\n");
        sb.append(nI).append("\n");
        sb.append(typeR.name()).append("\n");
        sb.append(typeI.name()).append("\n");
        sb.append("\n");
        return sb.toString();
    }

    public TYPE getTypeR() {
        return typeR;
    }

    public void setTypeR(TYPE typeR) {
        this.typeR = typeR;
    }

    public TYPE getTypeI() {
        return typeI;
    }

    public void setTypeI(TYPE typeI) {
        this.typeI = typeI;
    }

    public MotivationEpisode getPerceptionR() {
        return perceptionR;
    }

    public void setPerceptionR(MotivationEpisode perceptionR) {
        this.perceptionR = perceptionR;
    }

    public MotivationEpisode getPerceptionT() {
        return perceptionT;
    }

    public void setPerceptionT(MotivationEpisode perceptionT) {
        this.perceptionT = perceptionT;
    }

    public MotivationEpisode getPerceptionT_1() {
        return perceptionT_1;
    }

    public void setPerceptionT_1(MotivationEpisode perceptionT_1) {
        this.perceptionT_1 = perceptionT_1;
    }

    public MotivationEpisode getPerceptionI() {
        return perceptionI;
    }

    public void setPerceptionI(MotivationEpisode perceptionI) {
        this.perceptionI = perceptionI;
    }

    public int getnR() {
        return nR;
    }

    public void setnR(int nR) {
        this.nR = nR;
    }

    public int getnI() {
        return nI;
    }

    public void setnI(int nI) {
        this.nI = nI;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public ComponentValue getPerceptionRValue() {
        return perceptionR.getActionPerceptionPair().getExternalTn().get(ActionPerceptionPairMap.getInstance().getRelativeIndex(component));
    }

    public ComponentValue getPerceptionTValue() {
        if (perceptionT != null) {
            return perceptionT.getActionPerceptionPair().getExternalTn().get(ActionPerceptionPairMap.getInstance().getRelativeIndex(component));
        }
        return null;
    }

    public List<MotivationEpisode> getEpisodesI() {
        return episodesI;
    }

    public List<MotivationEpisode> getEpisodesR() {
        return episodesR;
    }
    
}
