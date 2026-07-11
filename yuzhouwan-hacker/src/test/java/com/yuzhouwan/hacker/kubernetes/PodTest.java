package com.yuzhouwan.hacker.kubernetes;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodListBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Pod Test
 *
 * @author Benedict Jin
 * @since 2023/2/5
 */
@EnableKubernetesMockClient
class PodTest {

    static KubernetesMockServer server;
    static KubernetesClient client;

    @Test
    public void testGetPodList() {

        Pod pod = new PodBuilder()
                .withNewMetadata()
                .withName("yuzhouwan")
                .endMetadata()
                .build();

        server.expect()
                .get()
                .withPath("/api/v1/pods")
                .andReturn(HttpURLConnection.HTTP_OK, new PodListBuilder()
                        .withItems(pod)
                        .build())
                .once();

        List<Pod> podList = client
                .pods()
                .inAnyNamespace()
                .list()
                .getItems();
        assertNotNull(podList);
        assertEquals(1, podList.size());

        server.expect()
                .get()
                .withPath("/api/v1/namespaces/default/pods")
                .andReturn(HttpURLConnection.HTTP_OK, new PodListBuilder()
                        .withItems()
                        .build())
                .once();

        podList = client
                .pods()
                .inNamespace("default")
                .list()
                .getItems();

        assertTrue(podList.isEmpty());
    }
}
