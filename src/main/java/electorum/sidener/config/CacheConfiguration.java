package electorum.sidener.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Election.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Election.class.getName() + ".politicalParties", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Election.class.getName() + ".independentCandidates", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Election.class.getName() + ".coalitions", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Election.class.getName() + ".causals", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.PoliticalParty.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.IndependentCandidate.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Coalition.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Coalition.class.getName() + ".politicalParties", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.ElectionType.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.District.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.PollingPlace.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Vote.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Causal.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Causal.class.getName() + ".causalDescriptions", jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.CausalDescription.class.getName(), jcacheConfiguration);
            cm.createCache(electorum.sidener.domain.Election.class.getName() + ".users", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
