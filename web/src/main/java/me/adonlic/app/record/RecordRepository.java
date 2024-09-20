package me.adonlic.app.record;

import me.adonlic.app.access_control.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<AccessLog, Long> {
}
