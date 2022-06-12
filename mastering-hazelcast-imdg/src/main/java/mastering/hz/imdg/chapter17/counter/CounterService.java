package mastering.hz.imdg.chapter17.counter;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.internal.partition.MigrationAwareService;
import com.hazelcast.internal.partition.MigrationEndpoint;
import com.hazelcast.internal.partition.PartitionMigrationEvent;
import com.hazelcast.internal.partition.PartitionReplicationEvent;
import com.hazelcast.internal.services.ManagedService;
import com.hazelcast.internal.services.RemoteService;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author lili
 * @date 2022/6/12 12:17
 */
public class CounterService implements
        ManagedService,
        RemoteService,
        MigrationAwareService {

    public static final String NAME = "name";

    private NodeEngine nodeEngine;
    Container[] containers;

    @Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        System.out.println("CounterService.init");
        this.nodeEngine = nodeEngine;
        containers = new Container[nodeEngine.getPartitionService().getPartitionCount()];
        for (int i = 0; i < containers.length; i++) {
            containers[i] = new Container();
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void shutdown(boolean b) {
        System.out.println("CounterService.init");

    }

    @Override
    public DistributedObject createDistributedObject(String objectName, UUID source, boolean local) {
        int partitionId = nodeEngine.getPartitionService().getPartitionId(objectName);
        Container container = containers[partitionId];
        container.init(objectName);
        return new CounterProxy(objectName, nodeEngine, this);
    }

    @Override
    public void destroyDistributedObject(String objectName, boolean b) {
        int partitionId = nodeEngine.getPartitionService().getPartitionId(objectName);
        Container container = containers[partitionId];
        container.destroy(objectName);
    }


    @Override
    public Operation prepareReplicationOperation(PartitionReplicationEvent event) {
        Container container = containers[event.getPartitionId()];
        Map<String, Integer> data = container.toMigrationData();
        return data.isEmpty() ? null : new CounterMigrationOperation(data);
    }

    @Override
    public void beforeMigration(PartitionMigrationEvent event) {
        System.out.println("beforeMigration...");
    }

    @Override
    public void commitMigration(PartitionMigrationEvent event) {
        if (event.getMigrationEndpoint() == MigrationEndpoint.SOURCE) {
            Container container = containers[event.getPartitionId()];
            container.clear();
        }
    }

    @Override
    public void rollbackMigration(PartitionMigrationEvent event) {
        if (event.getMigrationEndpoint() == MigrationEndpoint.DESTINATION) {
            Container container = containers[event.getPartitionId()];
            container.clear();
        }
    }
}
