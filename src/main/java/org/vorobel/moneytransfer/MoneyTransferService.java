package org.vorobel.moneytransfer;

import lombok.Getter;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.data.api.Repository;
//import org.jboss.weld.environment.se.Weld;
//import org.jboss.weld.environment.se.WeldContainer;


public class MoneyTransferService {
    @Getter
    private int restPort;
    private RESTService restService;
    //private WeldContainer container;
    private CdiContainer cdiContainer;

    public MoneyTransferService() {
        restPort = ConfigurationService.getRestServicePort();
        init();
    }

    public MoneyTransferService(int restPort) {
        this.restPort = restPort;
        init();
    }

    private void init() {
        // weld = new Weld();
        //container = weld.initialize();
        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        // Starting the application-context enables use of @ApplicationScoped beans
        ContextControl contextControl = cdiContainer.getContextControl();
        contextControl.startContext(Repository.class);//Repository.class

        // You can use CDI here

        restService = RESTService.create();
    }

    public static void main(String[] args) {
        var service = new MoneyTransferService();
        service.start();
    }

    public void start() {
        restService.start(restPort);
    }

    public void stop() {
        restService.stop();
        //container.shutdown();
        cdiContainer.shutdown();
    }
}
