package mastering.hz.imdg.chapter17.discovery;

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.config.properties.PropertyTypeConverter;
import com.hazelcast.config.properties.SimplePropertyDefinition;

/**
 * @author lili
 * @date 2022/6/12 14:39
 */
public final class HostsDiscoveryConfiguration {
    public static final PropertyDefinition DOMAIN = new SimplePropertyDefinition("site-domain",
            PropertyTypeConverter.STRING);

    private HostsDiscoveryConfiguration(){}
}
