package fr.univ.redis.config;

import fr.univ.redis.domain.StudentProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {

    @Bean
    RedisTemplate<String, StudentProfile> studentProfileRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, StudentProfile> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new JacksonJsonRedisSerializer<>(StudentProfile.class));
        template.afterPropertiesSet();
        return template;
    }
}

