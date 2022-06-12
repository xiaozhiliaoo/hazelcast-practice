package mastering.hz.imdg.chapter17.counter;


import java.util.*;

/**
 * @author lili
 * @date 2022/6/12 13:36
 */
public class Container {
    final Map<String, Integer> values = new HashMap<String, Integer>();

    void init(String objectName) {
        values.put(objectName, 0);
    }

    void destroy(String objectName) {
        values.remove(objectName);
    }

    void clear() {
        values.clear();
    }

    void applyMigrationData(Map<String, Integer> migrationData) {
        values.putAll(migrationData);
    }

    Map<String, Integer> toMigrationData() {
        return new HashMap<>(values);
    }

    public void inc(String objectId, int amount) {
        values.put(objectId, amount);
    }
}
