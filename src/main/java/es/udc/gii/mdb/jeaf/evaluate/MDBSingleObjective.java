/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.mdb.jeaf.evaluate;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.evaluate.SerialEvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GII
 */
public class MDBSingleObjective extends SerialEvaluationStrategy {

    @Override
    public void evaluate(EvolutionaryAlgorithm algorithm, Individual individual, List<ObjectiveFunction> functions,
        List<Constraint> constraints) {

        double fitnessValue;
        ObjectiveFunction objectiveFunction;
        double[] values;
        List<Double> constraintsValues = new ArrayList<Double>();

        objectiveFunction = functions.get(0);
        values = individual.getChromosomeAt(0);

        fitnessValue = objectiveFunction.evaluate(values);
        individual.setFitness(fitnessValue);

        //Si hay restricciones se evaluan en el individuo:
        if(constraints != null) {
            for (int i = 0; i < constraints.size(); i++) {
                constraintsValues.add(
                    constraints.get(i).evaluate(values));
            }
            individual.setConstraints(constraintsValues);
        }


    }

}
