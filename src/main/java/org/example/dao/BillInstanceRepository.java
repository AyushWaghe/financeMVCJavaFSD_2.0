package org.example.dao;

import jakarta.transaction.Transactional;
import org.example.models.BillInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillInstanceRepository extends JpaRepository<BillInstance,Integer> {

    List<BillInstance> findByUser_UserId(Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BillInstance b WHERE b.billInstanceId = :billInstanceId")
    int deleteBillInstanceById(Integer billInstanceId);

    @Query("""
    SELECT b
    FROM BillInstance b
    WHERE b.dueDate = :targetDate
    AND b.billStatus <> 'PAID'
""")
    List<BillInstance> findDueBills(@Param("targetDate") LocalDate targetDate);


}
