package me.adonlic.app.access_control;

import me.adonlic.app.access_control.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
