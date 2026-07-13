package hei.school.async.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hei.school.async.repository.model.JUpload;

@Repository
public interface UploadRepository extends JpaRepository<JUpload, UUID> {
}
