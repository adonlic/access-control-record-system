package me.adonlic.app.access_control.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import me.adonlic.app.controller.model.Door;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_permissions")
public class AccessPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "door_id", nullable = false)
    private Door door;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "is_allowed", nullable = false)
    private Boolean isAllowed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AccessPermission() {
    }

    public AccessPermission(Long id, Door door, Card card, Boolean isAllowed, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.door = door;
        this.card = card;
        this.isAllowed = isAllowed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AccessPermission(Door door, Card card, Boolean isAllowed) {
        this.door = door;
        this.card = card;
        this.isAllowed = isAllowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnoreProperties("accessPermissions")
    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    @JsonIgnoreProperties("accessPermissions")
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Boolean getAllowed() {
        return isAllowed;
    }

    public void setAllowed(Boolean allowed) {
        isAllowed = allowed;
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
        return "AccessPermission{" +
                "id=" + id +
                ", door=" + door +
                ", card=" + card +
                ", isAllowed=" + isAllowed +
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
}
