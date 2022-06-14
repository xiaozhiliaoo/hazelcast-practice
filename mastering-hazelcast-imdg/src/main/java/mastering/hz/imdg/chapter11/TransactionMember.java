package mastering.hz.imdg.chapter11;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionalMap;

/**
 * @author lili
 * @date 2022/6/12 19:27
 */
public class TransactionMember {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        TransactionContext txCxt = hz.newTransactionContext();
        txCxt.beginTransaction();
        TransactionalMap<String, String> map = txCxt.getMap("map");
        try {
            map.put("1", "1");
            map.put("2", "2");
            txCxt.commitTransaction();
        } catch (Throwable t) {
            txCxt.rollbackTransaction();
        }
        System.out.println("Finished");
    }
}
