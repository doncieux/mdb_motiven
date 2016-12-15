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
package es.udc.gii.mdb.memory.stm;

import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MDBRuntimeException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * This class supports a memory system with a reemplacement criteria based on
 * atention mechanism.
 *
 * @author Grupo Integrado de Ingeniería
 * (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 *
 */
public class EpisodicBuffer<E> implements Configurable {

    private List<E> buffer;
    private int maxSize;

    public EpisodicBuffer(int maxSize) {
        this.buffer = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public EpisodicBuffer() {
        buffer = new ArrayList<>();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getSize() {
        return buffer.size();
    }

    public List<E> getContent() {
        return buffer;
    }

    public boolean isFull() {
        return getSize() >= maxSize;
    }

    public void addEpisode(E episode) {
        if (isFull()) {
            //TODO Exception
            throw new MDBRuntimeException();
        }
        buffer.add(episode);
    }

    public boolean removeEpisode(E episode) {
        return buffer.remove(episode);
    }

    public E removeEpisode(int index) {
        return buffer.remove(index);
    }

    @Override
    public void configure(Configuration configuration) {
        //TODO
        maxSize = 20;
    }

    public Iterator<E> iterator() {
        return buffer.iterator();
    }

    public Iterator<E> reverseIterator() {
        Iterator<E> iterator = new Iterator<E>() {

            private int currentIndex = buffer.size() - 1;

            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public E next() {
                return buffer.get(currentIndex--);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return iterator;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void removeAll() {
        this.buffer.clear();
    }
    
}
