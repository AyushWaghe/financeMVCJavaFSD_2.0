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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BillInstanceRepository billInstanceRepository;

    public BillResponse saveBill(BillRequest billRequest) {
        User user=userRepository.findById(billRequest.getUserId())
                .orElseThrow(()-> new UserDetailNotFoundException("User with id "+billRequest.getUserId()+" not found"));

        Bill bill= BillMapper.toEntity(billRequest,user);
        Bill savedBill=billRepository.save(bill);

        // Create the first bill instance
        BillInstance billInstance = new BillInstance();
        billInstance.setBill(savedBill);
        billInstance.setUser(user);
        billInstance.setTitle(savedBill.getTitle());
        billInstance.setAmount(savedBill.getAmount());
        billInstance.setDueDate(billRequest.getDueDate());
        billInstance.setBillStatus(BillStatus.PENDING);

        billInstanceRepository.save(billInstance);

        return BillMapper.toBillResponse(savedBill);
    }

    public List<BillResponse> getUserBills(Integer userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new UserDetailNotFoundException("User with id "+userId+" not found"));

        List<Bill> userBills=billRepository.findByUserUserId(userId);
        List<BillResponse> billResponses=new ArrayList<>();

//        for(Bill b:userBills){
//            billResponses.add(BillMapper.toBillResponse(b));
//        }
        return userBills.stream() //.stream converts the list to the stream pipeline
                .map(BillMapper::toBillResponse) //This transforms each element into another form
                .toList();
    }

    public void deleteBill(Integer billId) {
        int rows=billRepository.deleteBillById(billId);

        if (rows==0){
            throw new ResourceNotFoundException("Bill with "+billId+" not found");
        }
    }

    public BillResponse updateBill(BillRequest billRequest,Integer billId) {
        Bill bill=billRepository.findById(billId).orElseThrow(()->new ResourceNotFoundException("No bill found with bill id "+billId+" to update"));
        bill.setBillRecurrence(billRequest.getBillRecurrence());
        bill.setAmount(billRequest.getAmount());
        bill.setTitle(billRequest.getTitle());
        bill.setLatestDueDate(billRequest.getDueDate());
        return BillMapper.toBillResponse(billRepository.save(bill));
    }


}
