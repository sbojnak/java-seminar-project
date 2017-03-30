package cz.muni.fi.pv168.secretagency.commons;

/**
 * Created by Jakub Bartolomej Kosuth on 29.3.2017.
 */
public class ServiceFailureException extends RuntimeException{

    public ServiceFailureException(String msg) {
        super(msg);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
