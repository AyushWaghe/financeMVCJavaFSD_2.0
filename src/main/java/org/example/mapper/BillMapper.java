package org.example.mapper;

import org.example.dto.BillRequest;
import org.example.dto.BillResponse;
import org.example.models.Bill;
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

    public static BillResponse toBillResponse(Bill bill){
        return BillResponse.builder()
                .title(bill.getTitle())
                .amount(bill.getAmount())
                .dueDate(bill.getLatestDueDate())
                .billRecurrence(bill.getBillRecurrence())
                .build();

    }
}
