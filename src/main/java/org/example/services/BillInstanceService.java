package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.BillInstanceRepository;
import org.example.dao.BillRepository;
import org.example.dao.UserRepository;
import org.example.dto.*;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.mapper.BillMapper;
import org.example.models.Bill;
import org.example.models.BillInstance;
import org.example.models.Transaction;
import org.example.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillInstanceService {

    private final BillInstanceRepository billInstanceRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;

    public BillInstanceResponse saveBillInstance(BillInstanceRequest billInstanceRequest) {
        User user = userRepository.findById(billInstanceRequest.getUserId())
                .orElseThrow(() -> new UserDetailNotFoundException("User with id " + billInstanceRequest.getUserId() + " not found"));

        Bill bill = null;
        if (billInstanceRequest.getBillId() != null) {
            bill = billRepository.findById(billInstanceRequest.getBillId()).orElseThrow(() -> new ResourceNotFoundException("Bill with bill id " + billInstanceRequest
                    .getBillId() + " not found"));
        }

        BillInstance billInstance = BillMapper.toBillInstanceEntity(billInstanceRequest, user,bill);
        BillInstance savedBill = billInstanceRepository.save(billInstance);
        return BillMapper.toBillInstanceResponse(savedBill);
    }

    public List<BillInstanceResponse> getBillInsances(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id " + userId + " not found"));

        List<BillInstance> billInstances=billInstanceRepository.findByUser_UserId(userId);

        return billInstances.stream().map(b->new BillInstanceResponse(
                b.getTitle(),
                b.getAmount(),
                b.getDueDate(),
                        b.getBillStatus()
        )
        ).toList();
    }

    public void deleteBillInstance(Integer billInstanceId) {
        int rows=billInstanceRepository.deleteBillInstanceById(billInstanceId);

        if(rows==0){
            throw new ResourceNotFoundException("Bill instance with id "+billInstanceId+" not found");
        }
    }

    public void updateBillInstance(BillInstanceRequest billInstanceRequest) {

        BillInstance existingBillInstance = billInstanceRepository.findById(billInstanceRequest.getBillInstanceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bill instance with id " + billInstanceRequest.getBillInstanceId() + " not found"
                ));

        userRepository.findById(billInstanceRequest.getUserId())
                .orElseThrow(() -> new UserDetailNotFoundException(
                        "User with id " + billInstanceRequest.getUserId() + " not found"
                ));

        existingBillInstance.setTitle(billInstanceRequest.getTitle());
        existingBillInstance.setAmount(billInstanceRequest.getAmount());
        existingBillInstance.setDueDate(billInstanceRequest.getDueDate());
        existingBillInstance.setBillStatus(billInstanceRequest.getBillStatus());

        billInstanceRepository.save(existingBillInstance);
    }
}
