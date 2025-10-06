package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:test.properties")
public interface TestConfig extends Config {
    @Key("base.url")
    String baseUrl();

    @Key("browser")
    String browser();

    @Key("timeout")
    long timeout();
}
