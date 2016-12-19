package es.udc.gii.mdb.motivation.buffer;

import es.udc.gii.mdb.memory.stm.Storageable;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class MotivationEpisodicBuffer extends MotivationEpisodicMemory {

    private boolean lastEpisode = false;

    public MotivationEpisodicBuffer() {
        super();
    }

    public MotivationEpisodicBuffer(int size) {
        super();
    }

    @Override
    public List<MotivationEpisode> getLastTrace() {
        //TODO
        return null;
    }
    
    

    @Override
    public void addMotivationEpisodie(MotivationEpisode motivationEpisody) {

        if (motivationEpisody.getReward() == 1.0 && motivationEpisody.getType() == MotivationEpisode.Type.REAL) {
            lastEpisode = Boolean.TRUE;
            double lastReward = getMaxRewardValue();
            motivationEpisody.setReward(lastReward);
            Iterator<MotivationEpisode> reverseIterator = getEpisodicBuffer().reverseIterator();
            boolean backTrack = reverseIterator.hasNext();
            while (backTrack && lastReward > getMinRewardValue()) {
                MotivationEpisode me = reverseIterator.next();

                if (me.getReward() == getMinRewardValue()) {
                    lastReward = getDecayFunction().getDecayValue(me, getMaxRewardValue(), getMinRewardValue(), lastReward);
                    me.setReward(lastReward);
                } else {
                    backTrack = Boolean.FALSE;
                }

                backTrack &= reverseIterator.hasNext();
            }
        } else {
            lastEpisode = Boolean.FALSE;
        }
        getEpisodicBuffer().addEpisode(motivationEpisody);

    }

    @Override
    protected Storageable replaceWith(Storageable s) {
        MotivationEpisode me = null;
        if (getEpisodicBuffer().isFull()) {
            me = getEpisodicBuffer().removeEpisode(0);
        }
        addMotivationEpisodie((MotivationEpisode) s);
        return me;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
    }

    @Override
    public List<MotivationEpisode> getCurrentChain() {
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
                //Sigo en mi traza (Descenso del valor)
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
        return lastEpisode;
    }

}
