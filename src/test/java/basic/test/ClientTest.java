package basic.test;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.objects.Namespace;
import io.fabric8.util.ClientHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    private String ip ="*.*.*.*";
    private String type = "https";
    private String port = "6443";
    private String token = "very important";

    private KubernetesClient kc ;
    private Namespace ns;
    @Before
    public void init(){
        kc = ClientHelper.getK8sTokenClient(ip,token);
        ns = new Namespace(kc);
    }

    @Test
    public void getAllNamespaces(){
        ns.traverseNs();
    }

    @After
    public void destory(){
        kc.close();
        ns = null;
    }
}
