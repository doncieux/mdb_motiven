package es.udc.gii.mdb.knowledge.data;

/**
 *
 * @author Mastropiero
 */
public class ModelData {

    private double[] absMeanError;
    private double absError;
    private double[][] sampleError, predictedOutputs, realOutputs;

    public ModelData() {
        this.absMeanError = new double[0];
        this.absError = 0.0;
        this.sampleError = new double[0][0];
        this.predictedOutputs = new double[0][0];
        this.realOutputs = new double[0][0];
    }

    public ModelData(double[] absMeanError, double absError, double[][] sampleError, double[][] predictedOutputs, double[][] realOutputs) {
        this.absMeanError = absMeanError;
        this.absError = absError;
        this.sampleError = sampleError;
        this.predictedOutputs = predictedOutputs;
        this.realOutputs = realOutputs;
    }
    
    public double[] getAbsMeanError() {
        return absMeanError;
    }

    public void setAbsMeanError(double[] absMeanError) {
        this.absMeanError = absMeanError;
    }

    public double getAbsError() {
        return absError;
    }

    public void setAbsError(double absError) {
        this.absError = absError;
    }

    public double[][] getSampleError() {
        return sampleError;
    }

    public void setSampleError(double[][] sampleError) {
        this.sampleError = sampleError;
    }

    public double[][] getPredictedOutputs() {
        return predictedOutputs;
    }

    public void setPredictedOutputs(double[][] predictedOutputs) {
        this.predictedOutputs = predictedOutputs;
    }

    public double[][] getRealOutputs() {
        return realOutputs;
    }

    public void setRealOutputs(double[][] realOutputs) {
        this.realOutputs = realOutputs;
    }

    
    
    
}
