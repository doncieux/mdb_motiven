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
package es.udc.gii.mdb.learning.memory;

import es.udc.gii.mdb.util.config.ConfWarning;
import es.udc.gii.mdb.util.config.Configurable;
import es.udc.gii.mdb.util.exception.MissingConfigurationParameterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 * Almacen de modelos que se usan en aprendizaje, puede ser la población del
 * evolutivo o un conjunto de redes que se entrenan con backpropagation, un 
 * listado de modelos de regresión, etc.
 * 
 * @author pilar
 */
public class LearningMemory implements Configurable {

    private static final String MEMORY_FILE_TAG = "file";
    
    private static final String CAPACITY_TAG = "capacity";
    
    private String path2file;
    
    private List<LearningMemoryElement> content;
    
    /**
     * Si la capacidad es -1 entonces es del tamaño de la población o configuración
     * del algoritmo de aprendizaje: población si es un evolutivo, 1 si es un backprop
     * normal, etc.
     */
    private int capacity = Integer.MAX_VALUE;

    public LearningMemory() {
    }
    
    public LearningMemory(String path2file, List<LearningMemoryElement> content, int capacity) {
        this.path2file = path2file;
        this.content = content;
        this.capacity = capacity;
    }
  
    @Override
    public void configure(Configuration configuration) throws MissingConfigurationParameterException {
        
        if (configuration.containsKey(MEMORY_FILE_TAG)) {
            path2file = configuration.getString(MEMORY_FILE_TAG);
            path2file = (!path2file.startsWith(File.separator) ? 
                    System.getProperty("user.dir") + File.separator + path2file :
                    path2file);
            loadContentFromFile();
        } else {
            ConfWarning w = new ConfWarning("LearningMemory." + MEMORY_FILE_TAG, "");
            w.warn();
        }
        
        if (configuration.containsKey(CAPACITY_TAG)) {
            capacity = configuration.getInt(CAPACITY_TAG);
        } else {
            ConfWarning w = new ConfWarning("LearningMemory." + CAPACITY_TAG, capacity);
            w.warn();
        }
        
    }
    
    public void updateContent(List<LearningMemoryElement> contentToBeUpdated) {
    
        if (this.content == null) {
            this.content = new ArrayList<>();
        }
        
        //Si el contenido es mayor que la capacidad de la memoria, entonces ordenamos 
        //la lista a actualizar para guardar sólo los mejores:
        if (capacity > 0 && contentToBeUpdated.size() > capacity &&
                content != null && content.size() > 0) {
            Collections.sort(content, new Comparator<LearningMemoryElement>() {

                @Override
                public int compare(LearningMemoryElement o1, LearningMemoryElement o2) {
                    if (o1.getUtility() < o2.getUtility()) {
                        return -1;
                    } else if (o1.getUtility() > o2.getUtility()) {
                        return 1;
                    }
                    
                    return 0;
                }
            });
            
        }        
        
        this.content.clear();
        for (int i = 0; i < Math.min(capacity, contentToBeUpdated.size()); i++) {
            this.content.add(contentToBeUpdated.get(i));
        }
    }
    
    public void loadContentFromFile() {
        
        BufferedReader reader;
        String[] strLine;
        LearningMemoryElement element;
        
        content = new ArrayList<>();
        
        try {
            reader = new BufferedReader(new FileReader(path2file));
            String line;
            
            while ((line = reader.readLine()) != null) {
                
                if (!line.isEmpty()) {
                
                    strLine = line.split("\t");
                    element = new LearningMemoryElement();
                    element.loadFromStringArray(strLine);   
                    content.add(element);
                }
                
            }
             
        } catch (FileNotFoundException ex) {
            //EXCEPTION
            Logger.getLogger(LearningMemory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //EXCEPTION
            Logger.getLogger(LearningMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void saveContentToFile() {
        
        PrintWriter writer;
        
        try {
            writer = new PrintWriter(path2file);
            
            for (LearningMemoryElement e : content) {
                
                for (int i = 0; i < e.getParameters().length; i++) {
                    
                    writer.print(e.getParameters()[i] + "\t");
                    
                }
                writer.print(e.getUtility());
                
                writer.println();
            }
            
            writer.flush();
        } catch (FileNotFoundException ex) {
            //EXCEPTION
            Logger.getLogger(LearningMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public List<LearningMemoryElement> getContent() {
        return content;
    }

    public int getCapacity() {
        return capacity;
    }
    
    
}
