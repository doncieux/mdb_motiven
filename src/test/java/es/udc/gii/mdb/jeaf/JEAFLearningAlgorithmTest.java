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
package es.udc.gii.mdb.jeaf;

import es.udc.gii.mdb.learning.memory.LearningMemory;
import es.udc.gii.mdb.learning.memory.LearningMemoryElement;
import java.io.File;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author GII
 */
public class JEAFLearningAlgorithmTest extends TestCase {

    private XMLConfiguration configuration;

    public JEAFLearningAlgorithmTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        configuration = new XMLConfiguration(
                getClass().getResource("JEAFLearningAlgorithmTest.xml"));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of configure method, of class JEAFLearningAlgorithm.
     */
    public void testConfigure() throws Exception {
//        System.out.println("configure");
//        JEAFLearningAlgorithm instance = new JEAFLearningAlgorithm();
//        instance.configure(configuration);
//        
//        LearningMemory lm = new LearningMemory(System.getProperty("user.dir") 
//                + File.separator + "coincollector/coinAngle-Memory.txt",
//                new ArrayList<LearningMemoryElement>(), 10);
//        JEAFLearningAlgorithm expectedInstance = new JEAFLearningAlgorithm("coinAngle", 1, 0,
//                System.getProperty("user.dir") + File.separator + "coincollector/EAFcoinAngle-Config.xml", 
//                2.0, lm);
//        
//        assertEquals(expectedInstance, instance);
    }

}
