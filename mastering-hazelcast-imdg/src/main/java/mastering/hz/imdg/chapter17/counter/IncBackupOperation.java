package mastering.hz.imdg.chapter17.counter;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.impl.operationservice.BackupOperation;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.io.IOException;

/**
 * @author lili
 * @date 2022/6/12 14:02
 */
public class IncBackupOperation extends Operation implements BackupOperation {

    private String objectId;

    private int amount;

    public IncBackupOperation() {
    }

    public IncBackupOperation(String objectId, int amount) {
        this.amount = amount;
        this.objectId = objectId;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeUTF(objectId);
        out.writeInt(amount);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        objectId = in.readUTF();
        amount = in.readInt();
    }

    @Override
    public void run() throws Exception {
        System.out.println("Executing backup" + objectId + ".inc() on:" + getNodeEngine().getThisAddress());
        CounterService service = getService();
        Container c = service.containers[getPartitionId()];
        c.inc(objectId, amount);
    }
}
