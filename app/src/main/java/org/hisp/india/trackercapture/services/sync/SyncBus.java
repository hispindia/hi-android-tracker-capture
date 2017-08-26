package org.hisp.india.trackercapture.services.sync;

/**
 * Created by nhancao on 8/26/17.
 */

public class SyncBus {

    private State status;
    private String message;
    private Object object;

    public SyncBus(State status, String message) {
        this.status = status;
        this.message = message;
    }

    public SyncBus(State status, Object object) {
        this.status = status;
        this.object = object;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public enum State {
        SUCCESS,
        ERROR
    }
}
