package io.fabric8.objects;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Namespace {
    private static Logger logger = Logger.getLogger(Namespace.class.getName());
    private KubernetesClient kc;
    public Namespace(KubernetesClient kc){
        this.kc = kc;
    }

    public void traverseNs(){
        NamespaceList aList = kc.namespaces().list();
        for (io.fabric8.kubernetes.api.model.Namespace aNamespace : aList.getItems())
            logger.log(Level.INFO, aNamespace.getMetadata().getName());
    }

}
