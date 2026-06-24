package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column(name="total_bal")
    private BigDecimal totalBal;

    @Column(name="needs")
    private Integer needs;

    @Column(name="wants")
    private Integer wants;

    @Column(name="savings")
    private Integer savings;

    @Column(name = "notification_subscribed")
    private boolean notificationSubscribed=true;
}
