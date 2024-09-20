package me.adonlic.app.access_control.model;

import jakarta.persistence.*;
import me.adonlic.app.controller.model.Door;
import me.adonlic.uhppote.types.SwipeReason;
import me.adonlic.uhppote.types.Validation;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "door_id", nullable = false)
    private Door door;

    @Column(name = "access_result", nullable = false)
    private Boolean accessResult;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Transient
    private SwipeReason swipeReason;

    @Transient
    private LocalDateTime swipeTime;

    @Transient
    private Validation validation;

    public AccessLog() {
    }

    public AccessLog(Long id, Card card, Door door, Boolean accessResult, LocalDateTime createdAt) {
        this.id = id;
        this.card = card;
        this.door = door;
        this.accessResult = accessResult;
        this.createdAt = createdAt;
    }

    public AccessLog(Card card, Door door, Boolean accessResult) {
        this.card = card;
        this.door = door;
        this.accessResult = accessResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Boolean getAccessResult() {
        return accessResult;
    }

    public void setAccessResult(Boolean accessResult) {
        this.accessResult = accessResult;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public SwipeReason getSwipeReason() {
        return swipeReason;
    }

    public void setSwipeReason(SwipeReason swipeReason) {
        this.swipeReason = swipeReason;
    }

    public LocalDateTime getSwipeTime() {
        return swipeTime;
    }

    public void setSwipeTime(LocalDateTime swipeTime) {
        this.swipeTime = swipeTime;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                "id=" + id +
                ", card=" + card +
                ", door=" + door +
                ", accessResult=" + accessResult +
                ", createdAt=" + createdAt +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
