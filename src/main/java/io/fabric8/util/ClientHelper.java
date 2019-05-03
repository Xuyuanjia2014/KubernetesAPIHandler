package io.fabric8.util;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.logging.Logger;

public class ClientHelper {

    private static Logger logger = Logger.getLogger(ClientHelper.class.getName());

    public static KubernetesClient createK8sClient(String type, String ip, String port) throws NullPointerException{
        KubernetesClient client = null;
        io.fabric8.kubernetes.client.Config config = new io.fabric8.kubernetes.client.ConfigBuilder().
                withMasterUrl(type+"://"+ip+":"+port).build();
        try{
            client = new DefaultKubernetesClient(config);
        }catch (Exception e){
            logger.info("k8s集群连接错误："+type+"://"+ip+":"+port);
            NullPointerException exception = new NullPointerException(e.getMessage()+e.getClass());
            throw exception;
        }
        return client;
    }

    public static KubernetesClient getK8sTokenClient(String clusterIp,String token) {
        String master = "https://" + clusterIp + ":6443" ;
        io.fabric8.kubernetes.client.Config config = new io.fabric8.kubernetes.client.ConfigBuilder().withMasterUrl(master).build();
        config.setTrustCerts(true);
        config.setOauthToken(token);
        return new DefaultKubernetesClient(config);
    }

}
