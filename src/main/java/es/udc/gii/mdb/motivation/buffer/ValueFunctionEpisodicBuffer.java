package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.memory.stm.Storageable;
import es.udc.gii.mdb.memory.stm.EpisodicBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class ValueFunctionEpisodicBuffer extends MotivationEpisodicMemory {

    private EpisodicBuffer<MotivationEpisode> traceCache;
    private int cacheSize;
    private boolean lastRewarded = false;
    private int minTraceSize;
    private List<MotivationEpisode> lastTrace = null;
    public ValueFunctionEpisodicBuffer() {
        super();
        traceCache = new EpisodicBuffer<>();
        lastTrace = new ArrayList<>();
    }

    public ValueFunctionEpisodicBuffer(int size) {
        super(size);
        lastTrace = new ArrayList<>();
        traceCache = new EpisodicBuffer<>();
    }

    @Override
    public List<MotivationEpisode> getLastTrace() {
        return lastTrace;
    }
    
    @Override
    public void addMotivationEpisodie(MotivationEpisode motivationEpisode) {

        //First, the episode is added to the traceCache
        if (traceCache.isFull()) {
            traceCache.removeEpisode(0);
        }
        traceCache.addEpisode(motivationEpisode);

        //Now, if the reward associated to the episode is 1.0, the trace has to be stored.
        if (motivationEpisode.getReward() == 1.0 && motivationEpisode.getType() == MotivationEpisode.Type.REAL) {

            if (traceCache.getContent().size() >= minTraceSize) {
                lastRewarded = Boolean.TRUE;
                double lastReward = getMaxRewardValue();
                motivationEpisode.setReward(lastReward);
                Iterator<MotivationEpisode> reverseIterator = traceCache.reverseIterator();
                reverseIterator.next();
                //eu assign
                while (reverseIterator.hasNext() && lastReward > getMinRewardValue()) {
                    MotivationEpisode me = reverseIterator.next();
                    lastReward = getDecayFunction().getDecayValue(me, getMaxRewardValue(), getMinRewardValue(), lastReward);
                    me.setReward(lastReward);
                }
                if (getEpisodicBuffer().getSize() + traceCache.getSize() > getEpisodicBuffer().getMaxSize()) {

                    int elementsToRemove = traceCache.getSize() - (getEpisodicBuffer().getMaxSize() - getEpisodicBuffer().getSize());
                    for (int i = 0; i < elementsToRemove; i++) {
                        getEpisodicBuffer().removeEpisode(0);
                    }
                }
                lastTrace.clear();
                lastTrace.addAll(traceCache.getContent());
                getEpisodicBuffer().getContent().addAll(traceCache.getContent());
                traceCache.getContent().clear();

            } else {
                lastRewarded = Boolean.FALSE;
                traceCache.removeAll();
            }
        } else {
            lastRewarded = Boolean.FALSE;
        }

    }

    @Override
    protected Storageable replaceWith(Storageable s) {
        MotivationEpisode motivationEpisody = (MotivationEpisode) s;

        addMotivationEpisodie(motivationEpisody);
        return null;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        cacheSize = conf.getInt("cacheSize", getTraceSize());
        traceCache.setMaxSize(cacheSize);
        this.minTraceSize = conf.getInt("minTrace", 5);
    }

    @Override
    public List<MotivationEpisode> getCurrentChain() {

        if (!lastRewarded) {
            return new ArrayList<>();
        }
        List<MotivationEpisode> currentChain;

        int initChain = getEpisodicBuffer().getSize();

        Iterator<MotivationEpisode> iterator = getEpisodicBuffer().reverseIterator();

        boolean chainNotFound = iterator.hasNext();
        double lastValue = Double.MAX_VALUE;
        while (chainNotFound) {
            MotivationEpisode me = iterator.next();
            if (lastValue > me.getReward() && me.getReward() > 1.0e-6) {
                lastValue = me.getReward();
                initChain--;

            } else {
                chainNotFound = false;
            }
            chainNotFound &= iterator.hasNext();
        }

        currentChain = getEpisodicBuffer().getContent().subList(initChain, getEpisodicBuffer().getSize());

        return currentChain;
    }

    @Override
    public boolean isLastEpisodeRewarded() {
        return lastRewarded;
    }

    public EpisodicBuffer<MotivationEpisode> getCache() {
        return traceCache;
    }


}
