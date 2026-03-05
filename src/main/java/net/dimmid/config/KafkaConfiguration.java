package net.dimmid.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public class KafkaConfiguration {
    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    public static Properties getKafkaProperties() throws FileNotFoundException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                Config.getOrDefault("BOOTSTRAP_SERVERS", DEFAULT_KAFKA_BOOTSTRAP_SERVERS));
        Config.getProperty("SECURITY_PROTOCOL").ifPresent(
                prop -> props.put("security.protocol", prop)
        );
        Config.getProperty("SASL_MECHANISM").ifPresent(
                prop -> props.put(SaslConfigs.SASL_MECHANISM, prop)
        );
        Config.getProperty("SASL_JAAS_CONFIG").ifPresent(
                prop -> props.put(SaslConfigs.SASL_JAAS_CONFIG, prop)
        );
        Config.getProperty("CLIENT_DNS_LOOKUP").ifPresent(
                prop -> props.put(ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, prop));
        Config.getProperty("SSL_TRUSTSTORE_LOCATION").ifPresent(
                prop -> props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, prop)
        );
        Config.getProperty("SSL_TRUSTSTORE_PASSWORD").ifPresent(
                prop -> props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, prop)
        );
        Config.getProperty("SSL_KEYSTORE_LOCATION").ifPresent(
                prop -> props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, prop)
        );
        Config.getProperty("SSL_KEYSTORE_PASSWORD").ifPresent(
                prop -> props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, prop)
        );

        return props;
    }
}
