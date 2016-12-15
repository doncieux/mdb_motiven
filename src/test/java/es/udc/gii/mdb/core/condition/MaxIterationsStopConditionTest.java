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
package es.udc.gii.mdb.core.condition;

import junit.framework.TestCase;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author pilar
 */
public class MaxIterationsStopConditionTest extends TestCase {
    
    private XMLConfiguration configuration;
    
    public MaxIterationsStopConditionTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        configuration = new XMLConfiguration(
                getClass().getResource("MaxIterationsStopConditionTest.xml"));
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of configure method, of class MaxIterationsStopCondition.
     */
    public void testConfigure() {
        System.out.println("configure");
        int expectedMaxIterations = 2000;
        MaxIterationsStopCondition instance = new MaxIterationsStopCondition();
        instance.configure(configuration);
        assertEquals(instance.getMaxIterations(), expectedMaxIterations);
    }

    /**
     * Test of evaluateCondition method, of class MaxIterationsStopCondition.
     */
    public void testEvaluateCondition() {
        System.out.println("evaluateCondition");
        //TODO
//        MDBCore core = null;
//        MaxIterationsStopCondition instance = new MaxIterationsStopCondition();
//        boolean expResult = false;
//        boolean result = instance.evaluateCondition(core);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
