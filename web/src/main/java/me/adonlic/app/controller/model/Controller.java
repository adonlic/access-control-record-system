package me.adonlic.app.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "controllers")
public class Controller {
    public static int PORT = 60000; // UDP port

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "controller_sn", length = 100, nullable = false)
    private String controllerSN;

    @Column(name = "ip_address", length = 100, nullable = false)
    private String ipAddress;

    @Column(name = "port", nullable = false)
    private Integer port;

    @OneToMany(mappedBy = "controller", cascade = CascadeType.ALL)
    private List<Door> doors;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Controller() {
    }

    public Controller(Long id, String controllerSN, String ipAddress, Integer port, List<Door> doors, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.controllerSN = controllerSN;
        this.ipAddress = ipAddress;
        this.port = port;
        this.doors = doors;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Controller(String controllerSN, String ipAddress, Integer port, List<Door> doors) {
        this.controllerSN = controllerSN;
        this.ipAddress = ipAddress;
        this.port = port;
        this.doors = doors;
    }

    public Controller(Long id, String controllerSN, String ipAddress, Integer port, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.controllerSN = controllerSN;
        this.ipAddress = ipAddress;
        this.port = port;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Controller(String controllerSN, String ipAddress, Integer port) {
        this.controllerSN = controllerSN;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getControllerSN() {
        return controllerSN;
    }

    public void setControllerSN(String controllerSN) {
        this.controllerSN = controllerSN;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @JsonIgnoreProperties("controller")
    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
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

    @Override
    public String toString() {
        return "Controller{" +
                "id=" + id +
                ", controllerSN='" + controllerSN + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", doors=" + doors +
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
//    @Column(name = "controller_sn", length = 100, nullable = false)
//    private String controllerSN;
//
//    @Column(name = "ip_address", length = 100, nullable = false)
//    private String ipAddress;
//
//    @Column(name = "port", nullable = false)
//    private Integer port;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", nullable = false)
//    private LocalDateTime updatedAt;
//
//    @Transient
//    private List<Door> doors;
//
//    public Controller() {
//        doors = new ArrayList<>();
//    }
//
//    public Controller(Long id, String controllerSN, String ipAddress, Integer port, LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.id = id;
//        this.controllerSN = controllerSN;
//        this.ipAddress = ipAddress;
//        this.port = port;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.doors = new ArrayList<>();
//    }
//
//    public Controller(String controllerSN, String ipAddress, Integer port) {
//        this.controllerSN = controllerSN;
//        this.ipAddress = ipAddress;
//        this.port = port;
//        this.doors = new ArrayList<>();
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
//    public String getControllerSN() {
//        return controllerSN;
//    }
//
//    public void setControllerSN(String controllerSN) {
//        this.controllerSN = controllerSN;
//    }
//
//    public String getIpAddress() {
//        return ipAddress;
//    }
//
//    public void setIpAddress(String ipAddress) {
//        this.ipAddress = ipAddress;
//    }
//
//    public Integer getPort() {
//        return port;
//    }
//
//    public void setPort(Integer port) {
//        this.port = port;
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
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    public List<Door> getDoors() {
//        return doors;
//    }
//
//    public void setDoors(List<Door> doors) {
//        this.doors = doors;
//    }
}
