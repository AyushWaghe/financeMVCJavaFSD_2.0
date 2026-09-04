package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_details")
public class UserDetail {

    @Id
    @Column(name = "user_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name="user_id",referencedColumnName = "user_id") //This table has the owning side. As join column annotation
    private User user;

    @Column(name="name")
    private String username;

    @Column(name="address")
    private String address;

    @Column(name="needs")
    private Integer needs;

    @Column(name="wants")
    private Integer wants;

    @Column(name="savings")
    private Integer savings;

    @Column(name = "notification_subscribed")
    private boolean notificationSubscribed=true;

    @Column(name = "reasoning_credits")
    private Integer reasoning_credits=20;

    @CreationTimestamp
    @Column(name = "reasoning_credits_reset_at")
    private LocalDateTime reasoningCreditsResetAt;
}
