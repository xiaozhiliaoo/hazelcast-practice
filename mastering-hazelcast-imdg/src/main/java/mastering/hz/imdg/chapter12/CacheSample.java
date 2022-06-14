package mastering.hz.imdg.chapter12;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * @author lili
 * @date 2022/6/12 18:58
 */
public class CacheSample {
    public static void main(String[] args) {
        CachingProvider cp = Caching.getCachingProvider();
        CacheManager cm = cp.getCacheManager();
        CompleteConfiguration<String, String> config = new MutableConfiguration<String, String>()
                .setTypes(String.class, String.class);
        Cache<String, String> cache = cm.createCache("example", config);
        cache.put("world", "hello");
        System.out.println(cache.get("world"));
    }
}
