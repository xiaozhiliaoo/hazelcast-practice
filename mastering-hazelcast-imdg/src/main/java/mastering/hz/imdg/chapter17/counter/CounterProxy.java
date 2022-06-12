package mastering.hz.imdg.chapter17.counter;

import com.hazelcast.internal.util.ExceptionUtil;
import com.hazelcast.spi.impl.AbstractDistributedObject;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.spi.impl.operationservice.InvocationBuilder;

import java.util.concurrent.Future;

import static mastering.hz.imdg.chapter17.counter.CounterService.NAME;


/**
 * @author lili
 * @date 2022/6/12 12:59
 */
public class CounterProxy extends AbstractDistributedObject implements Counter {

    private final String name;

    @Override
    public String getServiceName() {
        return NAME;
    }

    @Override
    public int inc(int amount) {
        NodeEngine nodeEngine = getNodeEngine();
        IncOperation operation = new IncOperation(name, amount);
        int partitionId = nodeEngine.getPartitionService().getPartitionId(name);
        InvocationBuilder builder = nodeEngine.getOperationService().createInvocationBuilder(NAME, operation, partitionId);
        Future<Integer> future = builder.invoke();
        try {
            return future.get();
        } catch (Exception e) {
            ExceptionUtil.rethrow(e);
        }
        return 1;
    }

    @Override
    public String getName() {
        return name;
    }

    public CounterProxy(String name, NodeEngine nodeEngine, CounterService counterService) {
        super(nodeEngine, counterService);
        this.name = name;
    }
}
