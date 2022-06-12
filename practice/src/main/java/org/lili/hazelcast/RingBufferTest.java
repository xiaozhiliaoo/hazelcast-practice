package org.lili.hazelcast;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.ringbuffer.Ringbuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author lili
 * @date 2022/6/4 16:58
 */
public class RingBufferTest {

    @Test
    @DisplayName("capacity=5")
    void t1() {
        Config config = new ClasspathXmlConfig("rb.xml");
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        Ringbuffer<String> rb = hz.getRingbuffer("rb");

        rb.add("1");
        rb.add("2");
        rb.add("3");
        rb.add("4");
        rb.add("5");
        rb.add("6");
        rb.add("7");
        rb.add("8");


        assertThat(rb.headSequence()).isEqualTo(3L);
        assertThat(rb.tailSequence()).isEqualTo(7L);

    }


    @Test
    @DisplayName("capacity=默认")
    void t2() {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Ringbuffer<String> rb = hz.getRingbuffer("rb");

        rb.add("1");
        rb.add("2");
        rb.add("3");
        rb.add("4");
        rb.add("5");
        rb.add("6");
        rb.add("7");
        rb.add("8");

        assertThat(rb.headSequence()).isEqualTo(0L);
        assertThat(rb.tailSequence()).isEqualTo(7L);
    }
}
