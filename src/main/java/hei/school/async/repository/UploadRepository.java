package hei.school.async.repository;

import hei.school.async.repository.model.JUpload;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<JUpload, UUID> {}
