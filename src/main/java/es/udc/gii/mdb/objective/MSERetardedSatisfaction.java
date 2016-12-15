/*
 * Copyright (C) 2010 Grupo Integrado de Ingeniería
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.mdb.objective;

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.representation.Predictable;
import es.udc.gii.mdb.motivation.buffer.MotivationEpisode;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Grupo Integrado de Ingenier√≠a
 * (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class MSERetardedSatisfaction {

    protected static final double THRESHOLD = 1.0e-6;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MSERetardedSatisfaction.class);

    private static List<MotivationEpisode> motivationEpisodys;
    private static Predictable output;

    public static double calculateMSE(Model model) {
        configure();

        double fitness = 0;
        double d;
        double acum = 0;

        motivationEpisodys = (List<MotivationEpisode>) (List) model.getMemory().getContent();

        output = null;
        int counted = 0;
        for (MotivationEpisode me : motivationEpisodys) {
            double real_reward = me.getReward();
            if (real_reward > 1.0E-6) {
                output = model.calculateOutput(me.getActionPerceptionPair());
                double diff = 0;
                d = output.getValues().get(0).getNormalizedValue() - real_reward;
//                diff += d * d;
                diff += d * d * real_reward;

                acum += diff;
                counted++;
            }

        }

        // In Computational neuroscience, the RMSE is used to assess
        // how well a system learns a given model
        if (counted > 0) {
            fitness = acum / (counted);
        } else {
            fitness = 1.0;
        }

        return fitness;

    }

    public static void configure() {

    }
}
