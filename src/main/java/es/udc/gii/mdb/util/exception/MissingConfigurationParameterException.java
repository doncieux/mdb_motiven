package es.udc.gii.mdb.util.exception;

/**
 * Exception thrown when a parameter is not found in .properties configuration
 * files
 * @author Borja Santos-Diez Vazquez
 */

public class MissingConfigurationParameterException extends Exception {

    private final String parameterName;

    public MissingConfigurationParameterException(String parameterName) {
        super("Missing configuration parameter: '" + parameterName + "'");
        this.parameterName = parameterName;
    }
    
    public String getParameterName() {
        return parameterName;
    }

}
