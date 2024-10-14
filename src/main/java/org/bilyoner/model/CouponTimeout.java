package org.bilyoner.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CouponTimeout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long timeoutInMillis;

    private LocalDateTime updatedAt;

    public CouponTimeout() {
    }

    public CouponTimeout(long timeoutInMillis, LocalDateTime updatedAt) {
        this.timeoutInMillis = timeoutInMillis;
        this.updatedAt = updatedAt;
    }

    public CouponTimeout(Long id, long timeoutInMillis, LocalDateTime updatedAt) {
        this.id = id;
        this.timeoutInMillis = timeoutInMillis;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimeoutInMillis() {
        return timeoutInMillis;
    }

    public void setTimeoutInMillis(long timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
