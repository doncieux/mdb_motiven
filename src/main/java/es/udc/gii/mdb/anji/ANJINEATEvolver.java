/*
 * Copyright (C) 2004 Derek James and Philip Tucker
 * 
 * This file is part of ANJI (Another NEAT Java Implementation).
 * 
 * ANJI is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * created by Philip Tucker on Feb 16, 2003
 */
package es.udc.gii.mdb.anji;

import com.anji.integration.LogEventListener;
import com.anji.integration.PersistenceEventListener;
import com.anji.neat.NeatChromosomeUtility;
import com.anji.neat.NeatConfiguration;
import com.anji.neat.NeuronType;
import com.anji.persistence.Persistence;
import com.anji.run.Run;
import com.anji.util.Configurable;
import com.anji.util.Properties;
import com.anji.util.Reset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.jgap.event.GeneticEvent;


import org.slf4j.Logger;

/**
 * Configures and performs an ANJI evolutionary run.
 *
 * @author Pilar
 */
public class ANJINEATEvolver implements Configurable {

//private static Logger logger = Logger.getLogger( Evolver.class );
    private static Logger logger = null;
    /**
     * properties key, # generations in run
     */
    public static final String NUM_GENERATIONS_KEY = "num.generations";
    /**
     * properties key, fitness function class
     */
    public static final String FITNESS_FUNCTION_CLASS_KEY = "fitness_function";
    private static final String FITNESS_THRESHOLD_KEY = "fitness.threshold";
    private static final String RESET_KEY = "run.reset";
    /**
     * properties key, target fitness value - after reaching this run will halt
     */
    public static final String FITNESS_TARGET_KEY = "fitness.target";
    private NeatConfiguration config = null;
    private Chromosome champ = null;
    private Genotype genotype = null;
    private int numEvolutions = 0;
    private double targetFitness = 0.0d;
    private double thresholdFitness = 0.0d;
    private int maxFitness = 0;
    private Persistence db = null;
    private Properties props;

    /**
     * ctor; must call
     * <code>init()</code> before using this object
     */
    public ANJINEATEvolver() {
        super();
    }

    /**
     * Construct new evolver with given properties. See <a href="
     * {@docRoot}/params.htm" target="anji_params">Parameter Details </a> for
     * specific property settings.
     *
     * @see com.anji.util.Configurable#init(com.anji.util.Properties)
     */
    public void init(Properties props) throws Exception {
        boolean doReset = props.getBooleanProperty(RESET_KEY, false);
        if (doReset) {
            Reset resetter = new Reset(props);
            resetter.setUserInteraction(false);
            resetter.reset();

        }

        this.props = props;

        config = new NeatConfiguration(props);

        // peristence
        db = (Persistence) props.singletonObjectProperty(Persistence.PERSISTENCE_CLASS_KEY);

        numEvolutions = props.getIntProperty(NUM_GENERATIONS_KEY);
        targetFitness = props.getDoubleProperty(FITNESS_TARGET_KEY, 1.0d);
        thresholdFitness = props.getDoubleProperty(FITNESS_THRESHOLD_KEY, targetFitness);
        // run
        // TODO - hibernate
        Run run = (Run) props.singletonObjectProperty(Run.class);
        db.startRun(run.getName());
        config.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVALUATED_EVENT, run);

        // persistence
        PersistenceEventListener dbListener = new PersistenceEventListener(config, run);
        dbListener.init(props);
        config.getEventManager().addEventListener(
                GeneticEvent.GENOTYPE_START_GENETIC_OPERATORS_EVENT, dbListener);

        // fitness function
        BulkFitnessFunction fitnessFunc = (BulkFitnessFunction) props
                .singletonObjectProperty(FITNESS_FUNCTION_CLASS_KEY);
        config.setBulkFitnessFunction(fitnessFunc);
        maxFitness = fitnessFunc.getMaxFitnessValue();

        // load population, either from previous run or random
        genotype = db.loadGenotype(config);
        if (genotype == null) {
            genotype = Genotype.randomInitialGenotype(config);
        }

    }

    /**
     * Perform a single run.
     *
     * @throws Exception
     */
    public void run() throws Exception {

        // initialize result data
        champ = genotype.getFittestChromosome();

        // generations
        for (int generation = 0; (generation < numEvolutions); ++generation) {

            // next generation
            genotype.evolve();

            // result data
            champ = genotype.getFittestChromosome();

            config.setGeneration(generation);
            

        }

        // run finish
        champ = genotype.getFittestChromosome();
        config.getEventManager().fireGeneticEvent(
                new GeneticEvent(GeneticEvent.RUN_COMPLETED_EVENT, genotype));

   }

    /**
     * @return champion of last generation
     */
    public Chromosome getChamp() {
        return champ;
    }

    /**
     * Fitness of current champ, 0 ... 1
     *
     * @return maximum fitness value
     */
    public double getChampAdjustedFitness() {
        return (champ == null) ? 0d : (double) champ.getFitnessValue()
                / config.getBulkFitnessFunction().getMaxFitnessValue();
    }

    /**
     * @return target fitness value, 0 ... 1
     */
    public double getTargetFitness() {
        return targetFitness;
    }

    /**
     * @return threshold fitness value, 0 ... 1
     */
    public double getThresholdFitness() {
        return thresholdFitness;
    }

    /**
     * Added by the GII *
     */
    public Genotype getGenotype() {
        return genotype;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;
    }
}
