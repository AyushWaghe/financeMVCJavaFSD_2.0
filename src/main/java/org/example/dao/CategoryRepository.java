package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> findByUser_UserIdAndTitle(Integer userId,String title);
    //Here JPA gets hint of which fields to map based on the method name like above User id and Title
    //And note that is matches entity names which you have declared in the class file of it.

    List<Category> findByUser_UserId(Integer userId);
}
