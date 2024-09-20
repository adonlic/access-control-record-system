package me.adonlic.app.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import me.adonlic.app.access_control.model.AccessPermission;
import me.adonlic.uhppote.types.DoorNumber;
import me.adonlic.uhppote.types.DoorStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "doors")
public class Door {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "door_name", length = 255)
    private String doorName;

    @Column(name = "door_number")
    private Integer doorNumber;

    @ManyToOne
    @JoinColumn(name = "controller_id", nullable = false)
    private Controller controller;

    @OneToMany(mappedBy = "door", cascade = CascadeType.ALL)
    private List<AccessPermission> accessPermissions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    private DoorStatus doorStatus;

    public Door() {
    }

    public Door(Long id, String doorName, Integer doorNumber, Controller controller, List<AccessPermission> accessPermissions, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.doorName = doorName;
        this.doorNumber = doorNumber;
        this.controller = controller;
        this.accessPermissions = accessPermissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Door(String doorName, Integer doorNumber, Controller controller, List<AccessPermission> accessPermissions) {
        this.doorName = doorName;
        this.doorNumber = doorNumber;
        this.controller = controller;
        this.accessPermissions = accessPermissions;
    }

    public Door(Long id, String doorName, Integer doorNumber, Controller controller, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.doorName = doorName;
        this.doorNumber = doorNumber;
        this.controller = controller;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Door(String doorName, Integer doorNumber, Controller controller) {
        this.doorName = doorName;
        this.doorNumber = doorNumber;
        this.controller = controller;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public Integer getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(Integer doorNumber) {
        this.doorNumber = doorNumber;
    }

    @JsonIgnoreProperties("doors")
    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @JsonIgnoreProperties("door")
    public List<AccessPermission> getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(List<AccessPermission> accessPermissions) {
        this.accessPermissions = accessPermissions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DoorStatus getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(DoorStatus doorStatus) {
        this.doorStatus = doorStatus;
    }

    @Override
    public String toString() {
        return "Door{" +
                "id=" + id +
                ", doorName='" + doorName + '\'' +
                ", doorNumber=" + doorNumber +
                ", controller=" + controller +
                ", accessPermissions=" + accessPermissions +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "door_name", length = 255)
//    private String doorName;
//
//    @Column(name = "door_number")
//    private Integer doorNumber;
//
//    @Column(name = "controller_id")
//    private Integer controllerId;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", nullable = false)
//    private LocalDateTime updatedAt;
//
//    public Door() {
//
//    }
//
//    public Door(Long id, String doorName, Integer doorNumber, Integer controllerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.id = id;
//        this.doorName = doorName;
//        this.doorNumber = doorNumber;
//        this.controllerId = controllerId;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    public Door(String doorName, Integer doorNumber, Integer controllerId) {
//        this.doorName = doorName;
//        this.doorNumber = doorNumber;
//        this.controllerId = controllerId;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getDoorName() {
//        return doorName;
//    }
//
//    public void setDoorName(String doorName) {
//        this.doorName = doorName;
//    }
//
//    public Integer getDoorNumber() {
//        return doorNumber;
//    }
//
//    public void setDoorNumber(Integer doorNumber) {
//        this.doorNumber = doorNumber;
//    }
//
//    public Integer getControllerId() {
//        return controllerId;
//    }
//
//    public void setControllerId(Integer controllerId) {
//        this.controllerId = controllerId;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    @Override
//    public String toString() {
//        return "Door{" +
//                "id=" + id +
//                ", doorName='" + doorName + '\'' +
//                ", doorNumber=" + doorNumber +
//                ", controllerId=" + controllerId +
//                ", createdAt=" + createdAt +
//                ", updatedAt=" + updatedAt +
//                '}';
//    }
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private DoorNumber doorNumber;
//
//    private DoorStatus doorStatus;
//
//    public Door() {
//    }
//
//    public Door(DoorNumber doorNumber, DoorStatus doorStatus) {
//        this.doorNumber = doorNumber;
//        this.doorStatus = doorStatus;
//    }
//
//    public DoorNumber getDoorNumber() {
//        return doorNumber;
//    }
//
//    public void setDoorNumber(DoorNumber doorNumber) {
//        this.doorNumber = doorNumber;
//    }
//
//    public DoorStatus getDoorStatus() {
//        return doorStatus;
//    }
//
//    public void setDoorStatus(DoorStatus doorStatus) {
//        this.doorStatus = doorStatus;
//    }
}
