package io.fabric8.demo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.JSONSchemaProps;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class CustomResourceSchemaValidation {
    private static final Logger logger = Logger.getLogger(CustomResourceSchemaValidation.class.getName());

    public static void main(String args[]) throws IOException {


        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            JSONSchemaProps schema = readSchema();

            CustomResourceDefinitionBuilder builder = new CustomResourceDefinitionBuilder()
                    .withApiVersion("apiextensions.k8s.io/v1beta1")
                    .withNewMetadata().withName("crontabs.stable.example.com").endMetadata()
                    .withNewSpec()
                    .withGroup("stable.example.com")
                    .withVersion("v1")
                    .withScope("Namespaced")
                    .withNewNames()
                    .withPlural("crontabs")
                    .withSingular("crontab")
                    .withKind("CronTab")
                    .addToShortNames("ct")
                    .endNames()
                    .withNewValidation()
                    .withNewOpenAPIV3SchemaLike(schema)
                    .endOpenAPIV3Schema()
                    .endValidation()
                    .endSpec();

            CustomResourceDefinition crd = builder.build();
            crd.getSpec().getValidation().getOpenAPIV3Schema().setDependencies(null);
            new DefaultKubernetesClient().customResourceDefinitions().create(builder.build());
            System.out.println(schema.toString());
//            CustomResourceDefinitionBuilder builder = new CustomResourceDefinitionBuilder()
//                    .withApiVersion("apiextensions.k8s.io/v1beta1")
//                    .withNewMetadata().withName("sparkclusters.radanalytics.io")
//                    .endMetadata()
//                    .withNewSpec()
//                    .withNewNames()
//                    .withKind("SparkCluster")
//                    .withPlural("sparkclusters")
//                    .endNames()
//                    .withGroup("radanalytics.io")
//                    .withVersion("v1")
//                    .withScope("Namespaced")
//                    .withNewValidation()
//                    .withNewOpenAPIV3SchemaLike(schema)
//                    .endOpenAPIV3Schema()
//                    .endValidation()
//                    .endSpec();
//
//            new DefaultKubernetesClient().customResourceDefinitions().createOrReplace(builder.build());

//            KubernetesClient client2 = new DefaultKubernetesClient();
//            CustomResourceDefinition crd2 = client.customResourceDefinitions().load(CustomResourceSchemaValidation.class.getResourceAsStream("/my-custom-resource-file.yml")).get();
//            client.customResourceDefinitions().create(crd2);



        }
    }


    public static JSONSchemaProps readSchema() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        URL in = CustomResourceSchemaValidation.class.getResource("/simple-schema.json");
//        URL in = CustomResourceSchemaValidation.class.getResource("/sparkCluster.json");
        if (null == in) {
            return null;
        }
        try {
            return mapper.readValue(in, JSONSchemaProps.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void log(String action) {
        logger.info(action);
    }
}
