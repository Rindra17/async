package hei.school.async.repository.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "upload")
public class JUpload {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String fileName;

  private String email;

  private Instant createdAt;
}
