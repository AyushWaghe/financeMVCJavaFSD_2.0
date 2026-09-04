package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.BillInstanceRepository;
import org.example.dao.BillRepository;
import org.example.dao.UserRepository;
import org.example.dto.BillRequest;
import org.example.dto.BillResponse;
import org.example.enums.BillStatus;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.mapper.BillMapper;
import org.example.models.Bill;
import org.example.models.BillInstance;
import org.example.models.User;
import org.example.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BillInstanceRepository billInstanceRepository;

    @Transactional
    public BillResponse saveBill(BillRequest billRequest) {
        Integer userId=AuthenticationUtil.getCurrentUserId();
        User user=userRepository.getReferenceById(userId);

        Bill bill= BillMapper.toEntity(billRequest,user);
        Bill savedBill=billRepository.save(bill);
        // Create the first bill instance
        BillInstance billInstance = new BillInstance();
        billInstance.setUser(user);
        billInstance.setTitle(savedBill.getTitle());
        billInstance.setAmount(savedBill.getAmount());
        billInstance.setDueDate(billRequest.getDueDate());
        billInstance.setBillStatus(BillStatus.PENDING);

        billInstanceRepository.save(billInstance);

        return BillMapper.toBillResponse(savedBill);
    }

    @Transactional(readOnly = true)
    public Page<BillResponse> getUserBills(Integer userId, Pageable pageable) {
        Page<Bill> userBills=billRepository.findByUserUserId(userId,pageable);

        return userBills.map(BillMapper::toBillResponse);
    }

    @Transactional
    public void deleteBill(Integer billId) {
        int rows=billRepository.deleteBillById(billId);

        if (rows==0){
            throw new ResourceNotFoundException("Bill with "+billId+" not found");
        }
    }

    @Transactional
    public BillResponse updateBill(BillRequest billRequest,Integer billId) {
        Bill bill=billRepository.findById(billId).orElseThrow(()->new ResourceNotFoundException("No bill found with bill id "+billId+" to update"));
        bill.setBillRecurrence(billRequest.getBillRecurrence());
        bill.setAmount(billRequest.getAmount());
        bill.setTitle(billRequest.getTitle());
        bill.setLatestDueDate(billRequest.getDueDate());

        // Create the updated bill instance
        BillInstance billInstance = new BillInstance();
        billInstance.setUser(bill.getUser());
        billInstance.setTitle(bill.getTitle());
        billInstance.setAmount(bill.getAmount());
        billInstance.setDueDate(billRequest.getDueDate());
        billInstance.setBillStatus(BillStatus.PENDING);

        billInstanceRepository.save(billInstance);


        return BillMapper.toBillResponse(billRepository.save(bill));
    }


}
