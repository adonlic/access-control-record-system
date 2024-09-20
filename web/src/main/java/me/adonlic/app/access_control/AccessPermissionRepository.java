package me.adonlic.app.access_control;


import me.adonlic.app.access_control.model.AccessPermission;
import me.adonlic.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessPermissionRepository extends JpaRepository<AccessPermission, Long> {
    public List<AccessPermission> findAccessPermissionsByCardId(Long id);
    public List<AccessPermission> findAccessPermissionsByCardCardNO(String cardNO);
    public List<AccessPermission> findAccessPermissionsByCardUserId(Long id);
    public List<AccessPermission> findAccessPermissionsByDoorId(Long id);
}
