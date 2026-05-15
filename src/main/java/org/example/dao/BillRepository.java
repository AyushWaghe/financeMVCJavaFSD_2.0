package org.example.dao;

import jakarta.transaction.Transactional;
import org.example.models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    List<Bill> findByUserUserId(Integer userId);

    @Modifying
    //Here we tell spring that this is delete query something that modifies the DB since by default all queries are read queries in spring
    @Transactional
    /*
    You need @Transactional because your delete query must run inside a database transaction—otherwise Spring/JPA cannot guarantee execution or commit.
     Start a DB transaction → run this method → commit if success → rollback if failure
     */
    @Query("DELETE FROM Bill b WHERE b.id= :id")
    int deleteBillById(Integer id);
}
