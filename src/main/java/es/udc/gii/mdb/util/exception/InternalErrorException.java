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

package es.udc.gii.mdb.util.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Exception thrown when a severe error is encountered
 * 
 * @author GII
 */
public class InternalErrorException extends Exception {

    private static final String MESSAGE = "***Information about encapsulated exception***";
    
    private final Exception encapsulatedException;

    public InternalErrorException(Exception exception) {
        encapsulatedException = exception;
    }

    @Override
    public String getMessage() {
        return encapsulatedException.getMessage();
    }

    public Exception getEncapsulatedException() {
        return encapsulatedException;
    }
    
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }
    
    @Override
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        //LOGGER
        printStream.println(MESSAGE);
        encapsulatedException.printStackTrace(printStream);
    }
    
    @Override
    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        //LOGGER
        printWriter.println(MESSAGE);
        encapsulatedException.printStackTrace(printWriter);
    }

      
}
