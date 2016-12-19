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
package es.udc.gii.mdb.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author GII
 */
public class ArrayIterator<T> implements Iterator<T> {

    private T[] array;
    
    private int pos = 0;
    
    public ArrayIterator(T[] array) {
        this.array = array;
    }
    
    @Override
    public boolean hasNext() {
        return pos < array.length;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return array[pos++];
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
