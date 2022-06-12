package mastering.hz.imdg.chapter17.counter;

import com.hazelcast.core.DistributedObject;

public interface Counter extends DistributedObject {
    int inc(int amount);
}
