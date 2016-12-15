package es.udc.gii.mdb.knowledge.representation;

import es.udc.gii.mdb.util.MDBRandom;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import es.udc.gii.mdb.util.exception.ModelException;
import java.util.Iterator;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class MockRepresentationApproach extends RepresentationApproach {

    @Override
    public void doDecode(Iterator parameters) throws ModelException {

    }

    @Override
    public double[] calculateOutputs(double[] inputs) throws ModelException {
        return new double[]{MDBRandom.nextDouble()};
    }

    @Override
    public void save(String fileName) throws ModelException {

    }


    @Override
    public int calculateNumberOfParameters() {
        return 0;
    }

    @Override
    public RepresentationConfiguration getRepresentationConfiguration() {
        return new RepresentationConfiguration() {
        };
    }

    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {

    }

}
