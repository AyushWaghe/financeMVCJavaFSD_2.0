package org.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","title"})
})
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cat_seq_gen")
    @SequenceGenerator(name="cat_seq_gen",sequenceName = "cat_seq",allocationSize = 30)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @Column(name="title",nullable = false)
    private String title;
}
