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

import es.udc.gii.mdb.memory.stm.register.Register;
import es.udc.gii.mdb.knowledge.declarative.model.Model;
import es.udc.gii.mdb.knowledge.representation.Predictable;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Grupo Integrado de Ingenier√≠a (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class MSEModelCalculation {

    protected static final double THRESHOLD = 1.0e-6;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MSEModelCalculation.class);

    private static List<Register> registers;
    private static Predictable output, real_output;

    public static double calculateMSE(Model model, int delays) {

        double fitness = 0;
        double d;
        double acum = 0;

        registers = (List<Register>) (List) model.getMemory().getContent();

        output = null;
        int j = 0;
        for (Register reg : registers) {

            output = model.calculateOutput(reg);
            real_output = reg.getOutputPerception();

            //Creamos este método para considerar aquellos ejemplos en los que
            //se aprendan series y sea necesario considerar discontinuidades:
            if (checkContinuity(reg)) {
                //Si es continuación de la serie anterior:
                j++;
            } else {
                j = 0;
            }

            //S√≥lo se calcula el error con las 10 √∫ltimas, las 10 primeras rellenan el buffer:
            if (j >= delays) {
                double diff = 0;
                for (int i = 0; i < output.getValues().size(); i++) {
//                    LOGGER.debug("OutputValue = " + output.getValues().get(i).getNormalizedValue() + ", " + "RealOutput " + real_output.getValues().get(i).getNormalizedValue());
                    d = output.getValues().get(i).getNormalizedValue() - real_output.getValues().get(i).getNormalizedValue();

                    diff += d * d;
                }

                acum += diff / output.getValues().size();
            }

        }

        // In Computational neuroscience, the RMSE is used to assess
        // how well a system learns a given model
        fitness = acum / (registers.size());

        return fitness;

    }

    /**
     * Este método comprueba si la memoria que estamos utilizando considera o no discontinudades. Si no las considera, es decir si el registro no tiene la
     * etiqueta de discontinuidad devuelve "true" considerando que es continuo. Si las considera, entonces devuelve "true" cuando el valor de la etiqueta
     * discontinuidad es igual a 0.0, false en caso de que su valor sea 1.0.
     *
     * @param reg
     * @return
     */
    private static boolean checkContinuity(Register reg) {

        boolean continuous = true;

        return continuous;

    }
}
