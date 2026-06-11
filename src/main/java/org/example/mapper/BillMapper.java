package org.example.mapper;

import org.example.dto.BillInstanceRequest;
import org.example.dto.BillInstanceResponse;
import org.example.dto.BillRequest;
import org.example.dto.BillResponse;
import org.example.models.Bill;
import org.example.models.BillInstance;
import org.example.models.User;

public class BillMapper {

    public static Bill toEntity(BillRequest billRequest, User user){
        return Bill.builder()
                .title(billRequest.getTitle())
                .billRecurrence(billRequest.getBillRecurrence())
                .latestDueDate(billRequest.getDueDate())
                .amount(billRequest.getAmount())
                .user(user)
                .build();
    }

    public static BillInstance toBillInstanceEntity(BillInstanceRequest billInstanceRequest, User user, Bill bill){
        return BillInstance.builder()
                .title(billInstanceRequest.getTitle())
                .amount(billInstanceRequest.getAmount())
                .user(user)
                .billStatus(billInstanceRequest.getBillStatus())
                .dueDate(billInstanceRequest.getDueDate())
                .bill(bill)
                .build();
    }

    public static BillResponse toBillResponse(Bill bill){
        return BillResponse.builder()
                .title(bill.getTitle())
                .amount(bill.getAmount())
                .dueDate(bill.getLatestDueDate())
                .billRecurrence(bill.getBillRecurrence())
                .build();

    }

    public static BillInstanceResponse toBillInstanceResponse(BillInstance billInstance){
        return BillInstanceResponse.builder()
                .title(billInstance.getTitle())
                .amount(billInstance.getAmount())
                .dueDate(billInstance.getDueDate())
                .billStatus(billInstance.getBillStatus())
                .build();

    }
}
