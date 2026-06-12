package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq_gen")
    @SequenceGenerator(
            name = "user_seq_gen",
            sequenceName = "user_seq",
            allocationSize = 25
    )
    @Column(name="user_id")
    private Integer userId;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="user_email")
    private String useremail;

    @CreationTimestamp //Automatically sets the value when the value is being generated. That is when the user is being registered
    @Column(name="created_at")
    private LocalDateTime createdAt;


    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL) //This is the inverse side (non-owning side)
    //Now user can access the user details fields like for example user address field. This is possible since one to one mapping is eager
    private UserDetail userDetails;

//    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
//    private List<Transaction> transactions;

    public User(String password, String useremail, UserDetail userDetails) {
        this.password = password;
        this.useremail = useremail;
        this.userDetails = userDetails;
    }
}

