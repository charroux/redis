package fr.univ.redis.domain;

public record StudentProfile(
        long id,
        String name,
        String email,
        String city,
        long version) {
}

