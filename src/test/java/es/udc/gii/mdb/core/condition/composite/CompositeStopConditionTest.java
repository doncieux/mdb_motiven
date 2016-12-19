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
package es.udc.gii.mdb.core.condition.composite;

import es.udc.gii.mdb.core.MDBCore;
import es.udc.gii.mdb.core.condition.MaxIterationsStopCondition;
import es.udc.gii.mdb.core.condition.SimpleStopCondition;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author GII
 */
public class CompositeStopConditionTest extends TestCase {

    private XMLConfiguration configuration;

    private CompositeStopCondition expectedResult;

    public CompositeStopConditionTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        configuration = new XMLConfiguration(
                getClass().getResource("AndCompositeStopConditionTest.xml"));

        expectedResult = new CompositeStopConditionImpl();
        expectedResult.addStopCondition(new MaxIterationsStopCondition(2000));
        expectedResult.addStopCondition(new MaxIterationsStopCondition(500));
        expectedResult.addStopCondition(new MaxIterationsStopCondition(1000));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getStopConditions method, of class CompositeStopCondition.
     */
    public void testGetStopConditions() throws Exception {
        System.out.println("getStopConditions");
        CompositeStopCondition instance = new CompositeStopConditionImpl();
        instance.configure(configuration);
        
        List<SimpleStopCondition> expResult = expectedResult.getStopConditions();
        List<SimpleStopCondition> result = instance.getStopConditions();
        
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setStopConditions method, of class CompositeStopCondition.
     */
    public void testSetStopConditions() throws Exception {
        System.out.println("setStopConditions");
        List<SimpleStopCondition> stopConditions = expectedResult.getStopConditions();
        CompositeStopCondition instance = new CompositeStopConditionImpl();
        instance.setStopConditions(stopConditions);
        
        assertEquals(expectedResult.getStopConditions(), instance.getStopConditions());
    }

    /**
     * Test of configure method, of class CompositeStopCondition.
     */
    public void testConfigure() throws Exception {
        System.out.println("configure");
        CompositeStopCondition instance = new CompositeStopConditionImpl();
        instance.configure(configuration);

        assertEquals(expectedResult, instance);
    }

    public class CompositeStopConditionImpl extends CompositeStopCondition {

        @Override
        public boolean evaluateCondition(MDBCore core) {
            return Boolean.TRUE;
        }
    }

}
