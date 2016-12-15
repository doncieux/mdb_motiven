package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.memory.stm.Memory;
import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.memory.stm.EpisodicBuffer;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Mastropiero
 */
public abstract class MotivationEpisodicMemory extends Memory {

    private EpisodicBuffer<MotivationEpisode> episodicBuffer;
    private DecayFunctionInterface decayFunction;
    private double minRewardValue, maxRewardValue;
    private int traceSize;
    
    public MotivationEpisodicMemory() {
        episodicBuffer = new EpisodicBuffer<>(Integer.MAX_VALUE);
    }

    public MotivationEpisodicMemory(int size) {
        episodicBuffer = new EpisodicBuffer<>(size);
    }

    public abstract void addMotivationEpisodie(MotivationEpisode motivationEpisody);

    
    public abstract List<MotivationEpisode> getLastTrace();

    @Override
    protected Storageable spaceAvailable(Storageable s) {
        return replaceWith(s);
    }

    @Override
    public int isReplaced() {
        return 0;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf); //To change body of generated methods, choose Tools | Templates.
        int size = conf.getInt("size");
        episodicBuffer.setMaxSize(size);

        Configuration decayFunctionConf = conf.subset("decayFunction");
        try {
            decayFunction = (DecayFunctionInterface) Class.forName(decayFunctionConf.getString("class")).newInstance();
            decayFunction.configure(decayFunctionConf);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | MissingConfigurationParameterException ex) {
            Logger.getLogger(MotivationEpisodicMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        minRewardValue = conf.getDouble("minRewardValue",0);
        maxRewardValue = conf.getDouble("maxRewardValue",1);
        traceSize = conf.getInt("traceSize",(int) Math.round((maxRewardValue/decayFunction.getDecayFactor())));
    }

    @Override
    public List<Storageable> getContent() {
        return (List<Storageable>) (List) episodicBuffer.getContent();
    }

    public EpisodicBuffer<MotivationEpisode> getEpisodicBuffer() {
        return episodicBuffer;
    }

    public DecayFunctionInterface getDecayFunction() {
        return decayFunction;
    }

    public abstract List<MotivationEpisode> getCurrentChain();

    public double getMinRewardValue() {
        return minRewardValue;
    }

    public double getMaxRewardValue() {
        return maxRewardValue;
    }
    
    public abstract boolean isLastEpisodeRewarded();

    public int getTraceSize() {
        return traceSize;
    }
    
    
}
