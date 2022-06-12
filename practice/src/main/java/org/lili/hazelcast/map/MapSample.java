package org.lili.hazelcast.map;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * @author lili
 * @date 2022/5/8 1:26
 */
public class MapSample {
    public static void main(String[] args) {
        // Start the Hazelcast Client and connect to an already running Hazelcast Cluster on 127.0.0.1
        HazelcastInstance hz = HazelcastClient.newHazelcastClient();
        // Get the Distributed Map from Cluster.
        IMap map = hz.getMap("my-distributed-map");
        //Standard Put and Get.
        map.put("key", "value");
        map.get("key");
        //Concurrent Map methods, optimistic updating
        map.putIfAbsent("somekey", "somevalue");
        map.replace("key", "value", "newvalue");
        // Shutdown this Hazelcast client
        //hz.shutdown();
    }
}
