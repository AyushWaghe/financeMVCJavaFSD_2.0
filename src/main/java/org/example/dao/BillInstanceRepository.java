package org.example.dao;

import jakarta.transaction.Transactional;
import org.example.enums.BillStatus;
import org.example.models.Bill;
import org.example.models.BillInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<BillInstance> findByBillStatusAndDueDate(
            BillStatus billStatus,
            LocalDate dueDate
    );



    Page<BillInstance> findByUserUserIdAndDueDateGreaterThanEqualAndBillStatus(
            Integer userId,
            LocalDate dueDate,
            BillStatus billStatus,
            Pageable pageable
    );

    Page<BillInstance> findByUserUserIdAndDueDateLessThanAndBillStatus(
            Integer userId,
            LocalDate dueDate,
            BillStatus billStatus,
            Pageable pageable
    );

    Page<BillInstance> findByUserUserIdAndBillStatus(
            Integer userId,
            BillStatus billStatus,
            Pageable pageable
    );


}
