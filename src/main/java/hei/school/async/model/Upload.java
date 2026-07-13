package hei.school.async.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Upload(UUID id, String fileName, String email, Instant createdAt) {}
