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

import es.udc.gii.mdb.knowledge.declarative.model.Model;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author GII
 */
public abstract class DelegatedLogTool extends EvolutionLogTool {

    @Override
    protected void doUpdate(Observable o, Object arg) {


        Model m = (Model) o;
        List<String> lines  = getLogData(m);
        String linePrint;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            linePrint = m.getIteration() + "\t" + line;
            printLine(linePrint.trim());
        }
        printLine("");
        printLine("");

    }
    
    protected abstract List<String> getLogData(Model m);
 
}
