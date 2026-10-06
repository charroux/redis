package fr.univ.redis.web;

import fr.univ.redis.domain.StudentProfile;
import fr.univ.redis.service.RedisLabService;
import fr.univ.redis.service.RedisLabService.ScoreView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/redis")
public class RedisLabController {

    private final RedisLabService redis;

    public RedisLabController(RedisLabService redis) {
        this.redis = redis;
    }

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("response", redis.ping());
    }

    @PutMapping("/sessions/{token}")
    public ResponseEntity<Void> saveSession(
            @PathVariable String token,
            @RequestParam String studentId,
            @RequestParam(defaultValue = "1800") long ttlSeconds) {
        redis.saveSession(token, studentId, Duration.ofSeconds(ttlSeconds));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessions/{token}")
    public ResponseEntity<String> findSession(@PathVariable String token) {
        return ResponseEntity.of(redis.findSession(token));
    }

    @DeleteMapping("/sessions/{token}")
    public ResponseEntity<Void> deleteSession(@PathVariable String token) {
        return redis.deleteSession(token)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<Void> saveProfile(
            @PathVariable long studentId,
            @RequestBody StudentProfile profile) {
        if (studentId != profile.id()) {
            return ResponseEntity.badRequest().build();
        }
        redis.saveProfile(profile);
        return ResponseEntity.created(URI.create("/api/redis/students/" + studentId)).build();
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentProfile> findProfile(@PathVariable long studentId) {
        return ResponseEntity.of(redis.findProfile(studentId));
    }

    @PutMapping("/courses/{courseId}/participants/{studentId}")
    public Map<String, Boolean> addParticipant(
            @PathVariable String courseId,
            @PathVariable long studentId) {
        return Map.of("added", redis.addParticipant(courseId, studentId));
    }

    @GetMapping("/courses/{courseId}/participants")
    public Set<String> participants(@PathVariable String courseId) {
        return redis.participants(courseId);
    }

    @PostMapping("/courses/{courseId}/scores/{studentId}")
    public ScoreView incrementScore(
            @PathVariable String courseId,
            @PathVariable long studentId,
            @RequestParam double delta) {
        return new ScoreView(
                Long.toString(studentId),
                redis.incrementScore(courseId, studentId, delta));
    }

    @GetMapping("/courses/{courseId}/ranking")
    public List<ScoreView> ranking(@PathVariable String courseId) {
        return redis.ranking(courseId);
    }
}

