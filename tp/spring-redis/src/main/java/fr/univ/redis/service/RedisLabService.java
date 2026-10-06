package fr.univ.redis.service;

import fr.univ.redis.domain.StudentProfile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class RedisLabService {

    private static final String SESSION_PREFIX = "session:";
    private static final String STUDENT_PREFIX = "student:";

    private final StringRedisTemplate strings;
    private final RedisTemplate<String, StudentProfile> profiles;

    public RedisLabService(
            StringRedisTemplate strings,
            @Qualifier("studentProfileRedisTemplate")
            RedisTemplate<String, StudentProfile> profiles) {
        this.strings = strings;
        this.profiles = profiles;
    }

    public String ping() {
        return Objects.requireNonNull(strings.execute(
                (RedisCallback<String>) RedisConnection::ping));
    }

    public void saveSession(String token, String studentId, Duration ttl) {
        strings.opsForValue().set(sessionKey(token), studentId, ttl);
    }

    public Optional<String> findSession(String token) {
        return Optional.ofNullable(strings.opsForValue().get(sessionKey(token)));
    }

    public boolean deleteSession(String token) {
        return Boolean.TRUE.equals(strings.delete(sessionKey(token)));
    }

    public void saveProfile(StudentProfile profile) {
        profiles.opsForValue().set(studentKey(profile.id()), profile);
    }

    public Optional<StudentProfile> findProfile(long studentId) {
        return Optional.ofNullable(profiles.opsForValue().get(studentKey(studentId)));
    }

    public boolean addParticipant(String courseId, long studentId) {
        Long added = strings.opsForSet().add(participantsKey(courseId), Long.toString(studentId));
        return added != null && added > 0;
    }

    public Set<String> participants(String courseId) {
        Set<String> members = strings.opsForSet().members(participantsKey(courseId));
        return members == null ? Set.of() : members;
    }

    public double incrementScore(String courseId, long studentId, double delta) {
        Double score = strings.opsForZSet()
                .incrementScore(rankingKey(courseId), Long.toString(studentId), delta);
        return Objects.requireNonNull(score);
    }

    public List<ScoreView> ranking(String courseId) {
        Set<ZSetOperations.TypedTuple<String>> tuples = strings.opsForZSet()
                .reverseRangeWithScores(rankingKey(courseId), 0, -1);

        if (tuples == null) {
            return List.of();
        }

        return tuples.stream()
                .map(tuple -> new ScoreView(
                        Objects.requireNonNull(tuple.getValue()),
                        Objects.requireNonNull(tuple.getScore())))
                .sorted(Comparator.comparingDouble(ScoreView::score).reversed())
                .toList();
    }

    private static String sessionKey(String token) {
        return SESSION_PREFIX + token;
    }

    private static String studentKey(long studentId) {
        return STUDENT_PREFIX + studentId;
    }

    private static String participantsKey(String courseId) {
        return "course:" + courseId + ":participants";
    }

    private static String rankingKey(String courseId) {
        return "course:" + courseId + ":ranking";
    }

    public record ScoreView(String studentId, double score) {
    }
}

