package org.vorobel.moneytransfer;

public abstract class RESTService {
    static RESTService create() {
        return new JavalinService();
    }

    public abstract void start(int port);
    public abstract void stop();
}
