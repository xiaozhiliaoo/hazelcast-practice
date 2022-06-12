package org.lili.hazelcast.jet;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toMap;

/**
 * @author lili
 * @date 2022/5/8 22:21
 */
public class LoadNames {
    public static void main(String[] args) {
        JetInstance instance = Jet.newJetClient();

        Map<String, String> namesMap = loadNames();
        instance.getMap("companyNames").putAll(namesMap);

        System.out.println(namesMap.size() + " names put to a map called 'companyNames'");

        instance.shutdown();
    }


    private static Map<String, String> loadNames() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                LoadNames.class.getResourceAsStream("/nasdaqlisted.txt"), UTF_8))) {
            return reader.lines()
                    .skip(1)
                    .map(line -> line.split("\\|"))
                    .collect(toMap(parts -> parts[0], parts -> parts[1]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
