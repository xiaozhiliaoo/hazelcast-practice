package mastering.hz.imdg.chapter17.counter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;


/**
 * @author lili
 * @date 2022/6/12 13:29
 */
public class Member {
    public static void main(String[] args) {
        HazelcastInstance[] instances = new HazelcastInstance[2];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = Hazelcast.newHazelcastInstance();
        }

        Counter[] counters = new Counter[4];
        for (int i = 0; i < counters.length; i++) {
            counters[i] = instances[0].getDistributedObject(CounterService.NAME, i + "counter");
        }

        System.out.println("Round1");
        for (Counter counter : counters) {
            System.out.println(counter.inc(1));
        }

        System.out.println("Round2");
        for (Counter counter : counters) {
            System.out.println(counter.inc(1));
        }

        System.out.println("Finished");
    }
}
