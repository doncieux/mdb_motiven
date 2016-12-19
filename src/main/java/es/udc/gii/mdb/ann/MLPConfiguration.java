package es.udc.gii.mdb.ann;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author GII
 */
public class MLPConfiguration {

    private int[] topology;
    private String inputNeuronType;
    private String hiddenNeuronType;
    private String outputNeuronType;
    private String synapsisHiddenType;
    private String synapsisOutputType;
    private boolean delayed;

    public MLPConfiguration() {
    }

    public MLPConfiguration(int[] topology, String inputNeuronType, String hiddenNeuronType, String outputNeuronType, String synapsisHiddenType, String synapsisOutputType, boolean delayed) {
        this.topology = topology;
        this.inputNeuronType = inputNeuronType;
        this.hiddenNeuronType = hiddenNeuronType;
        this.outputNeuronType = outputNeuronType;
        this.synapsisHiddenType = synapsisHiddenType;
        this.synapsisOutputType = synapsisOutputType;
        this.delayed = delayed;
    }
    
    
    public int[] getTopology() {
        return topology;
    }

    public void setTopology(int[] topology) {
        this.topology = topology;
    }

    public String getInputNeuronType() {
        return inputNeuronType;
    }

    public void setInputNeuronType(String inputNeuronType) {
        this.inputNeuronType = inputNeuronType;
    }

    public String getHiddenNeuronType() {
        return hiddenNeuronType;
    }

    public void setHiddenNeuronType(String hiddenNeuronType) {
        this.hiddenNeuronType = hiddenNeuronType;
    }

    public String getOutputNeuronType() {
        return outputNeuronType;
    }

    public void setOutputNeuronType(String outputNeuronType) {
        this.outputNeuronType = outputNeuronType;
    }

    public String getSynapsisHiddenType() {
        return synapsisHiddenType;
    }

    public void setSynapsisHiddenType(String synapsisHiddenType) {
        this.synapsisHiddenType = synapsisHiddenType;
    }

    public String getSynapsisOutputType() {
        return synapsisOutputType;
    }

    public void setSynapsisOutputType(String synapsisOutputType) {
        this.synapsisOutputType = synapsisOutputType;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Arrays.hashCode(this.topology);
        hash = 37 * hash + Objects.hashCode(this.inputNeuronType);
        hash = 37 * hash + Objects.hashCode(this.hiddenNeuronType);
        hash = 37 * hash + Objects.hashCode(this.outputNeuronType);
        hash = 37 * hash + Objects.hashCode(this.synapsisHiddenType);
        hash = 37 * hash + Objects.hashCode(this.synapsisOutputType);
        hash = 37 * hash + (this.delayed ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MLPConfiguration other = (MLPConfiguration) obj;
        if (!Arrays.equals(this.topology, other.topology)) {
            return false;
        }
        if (!Objects.equals(this.inputNeuronType, other.inputNeuronType)) {
            return false;
        }
        if (!Objects.equals(this.hiddenNeuronType, other.hiddenNeuronType)) {
            return false;
        }
        if (!Objects.equals(this.outputNeuronType, other.outputNeuronType)) {
            return false;
        }
        if (!Objects.equals(this.synapsisHiddenType, other.synapsisHiddenType)) {
            return false;
        }
        if (!Objects.equals(this.synapsisOutputType, other.synapsisOutputType)) {
            return false;
        }
        if (this.delayed != other.delayed) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
