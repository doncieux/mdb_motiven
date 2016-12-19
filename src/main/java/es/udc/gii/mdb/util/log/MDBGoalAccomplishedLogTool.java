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
package es.udc.gii.mdb.util.log;

import es.udc.gii.mdb.core.MDBCore;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 *
 * This {@link LogTool} can be used to follow the learning process of the
 * architecture. It stores the iterations when the task is accomplished. This
 * way we can appreciate how as time goes by, the iterations written in the log
 * file get closer.
 *
 * @author GII
 */
public class MDBGoalAccomplishedLogTool extends MDBLogTool {

    protected static final double THRESHOLD = 1.0e-6;

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
    }

    /**
     *
     * When writing a new line, it writes the iteration of the main loop when
     * the task has been accomplished.
     *
     * @param o
     * @param arg
     */
    @Override
    public void doUpdate(Observable o, Object arg) {
        MDBCore core = (MDBCore) o;

        String s = "";
        List<Double> rewards = new ArrayList<>();
        rewards.add(Double.valueOf(core.getIterations()));
        rewards.addAll(core.getRewards());
       
        for (Double d : rewards) {
            s += d + "\t";

        }

        printLine(s);

    }

}
