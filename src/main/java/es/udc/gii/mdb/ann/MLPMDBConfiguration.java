package es.udc.gii.mdb.ann;

import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;


/**
 *
 * @author Mastropiero
 */



//TODO Cuando se pase el otro MLPConfiguration a la red de ANN se puede cambiar el nombre de este por MLPConfiguration a secas
public class MLPMDBConfiguration implements RepresentationConfiguration{
    
    
    private MLPConfiguration mLPConfiguration;

    public MLPMDBConfiguration() {
        mLPConfiguration = new MLPConfiguration();
    }

    public MLPMDBConfiguration(MLPConfiguration mLPConfiguration) {
        this.mLPConfiguration = mLPConfiguration;
    }
    
    public MLPMDBConfiguration(int[] topology, String inputNeuronType, String hiddenNeuronType, String outputNeuronType, String synapsisHiddenType, String synapsisOutputType, boolean delayed) {
        mLPConfiguration = new MLPConfiguration();
        mLPConfiguration.setTopology(topology);
        mLPConfiguration.setInputNeuronType(inputNeuronType);
        mLPConfiguration.setHiddenNeuronType(hiddenNeuronType);
        mLPConfiguration.setOutputNeuronType(outputNeuronType);
        mLPConfiguration.setSynapsisHiddenType(synapsisHiddenType);
        mLPConfiguration.setSynapsisOutputType(synapsisOutputType);
        mLPConfiguration.setDelayed(delayed);
    }

    public void setmLPConfiguration(MLPConfiguration mLPConfiguration) {
        this.mLPConfiguration = mLPConfiguration;
    }

    public MLPConfiguration getmLPConfiguration() {
        return mLPConfiguration;
    }
    
    
    
    public int[] getTopology() {
        return mLPConfiguration.getTopology();
    }

    public void setTopology(int[] topology) {
        mLPConfiguration.setTopology(topology);
    }

    public String getInputNeuronType() {
        return mLPConfiguration.getInputNeuronType();
    }

    public void setInputNeuronType(String inputNeuronType) {
        mLPConfiguration.setInputNeuronType(inputNeuronType);
    }

    public String getHiddenNeuronType() {
        return mLPConfiguration.getHiddenNeuronType();
    }

    public void setHiddenNeuronType(String hiddenNeuronType) {
        mLPConfiguration.setHiddenNeuronType(hiddenNeuronType);
    }

    public String getOutputNeuronType() {
        return mLPConfiguration.getOutputNeuronType();
    }

    public void setOutputNeuronType(String outputNeuronType) {
        mLPConfiguration.setOutputNeuronType(outputNeuronType);
    }

    public String getSynapsisHiddenType() {
        return mLPConfiguration.getSynapsisHiddenType();
    }

    public void setSynapsisHiddenType(String synapsisHiddenType) {
        mLPConfiguration.setSynapsisHiddenType(synapsisHiddenType);
    }

    public String getSynapsisOutputType() {
        return mLPConfiguration.getSynapsisOutputType();
    }

    public void setSynapsisOutputType(String synapsisOutputType) {
        mLPConfiguration.setSynapsisOutputType(synapsisOutputType);
    }

    public boolean isDelayed() {
        return mLPConfiguration.isDelayed();
    }

    public void setDelayed(boolean delayed) {
        mLPConfiguration.setDelayed(delayed);
    }

    @Override
    public int hashCode() {
        return mLPConfiguration.hashCode();
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
        return other.equals(this);
    }
    
    
    
}
