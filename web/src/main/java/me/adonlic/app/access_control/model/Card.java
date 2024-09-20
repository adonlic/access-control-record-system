package me.adonlic.app.access_control.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import me.adonlic.app.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_no", length = 50, nullable = false)
    private String cardNO;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<AccessPermission> accessPermissions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Card() {
    }

    public Card(Long id, String cardNO, User user, List<AccessPermission> accessPermissions, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cardNO = cardNO;
        this.user = user;
        this.accessPermissions = accessPermissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Card(String cardNO, User user, List<AccessPermission> accessPermissions) {
        this.cardNO = cardNO;
        this.user = user;
        this.accessPermissions = accessPermissions;
    }

    public Card(Long id, String cardNO, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cardNO = cardNO;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Card(String cardNO, User user) {
        this.cardNO = cardNO;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    @JsonIgnoreProperties("cards")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnoreProperties("card")
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

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNO='" + cardNO + '\'' +
                ", user=" + user +
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
}
