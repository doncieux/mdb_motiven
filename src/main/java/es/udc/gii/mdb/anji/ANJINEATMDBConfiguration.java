package es.udc.gii.mdb.anji;

import com.anji.util.Properties;
import es.udc.gii.mdb.knowledge.representation.RepresentationConfiguration;
import java.util.Objects;


/**
 *
 * @author GII
 */



public class ANJINEATMDBConfiguration implements RepresentationConfiguration{
    
    private Properties properties;

    public ANJINEATMDBConfiguration() {
    }
    
    public ANJINEATMDBConfiguration(Properties properties) {
       this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return this.properties.equals(((ANJINEATMDBConfiguration) obj).properties);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.properties);
        return hash;
    }
    
    
    
}
