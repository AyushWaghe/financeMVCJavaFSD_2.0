package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dao.BillInstanceRepository;
import org.example.dao.BillRepository;
import org.example.dao.UserRepository;
import org.example.dto.*;
import org.example.enums.BillStatus;
import org.example.event.BillReminderEvent;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.UserDetailNotFoundException;
import org.example.mapper.BillMapper;
import org.example.models.Bill;
import org.example.models.BillInstance;
import org.example.models.Transaction;
import org.example.models.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillInstanceService {

    private final BillInstanceRepository billInstanceRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final KafkaTemplate<String,BillReminderEvent> kafkaTemplate;

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

    public List<BillInstanceResponse> getUpcomingBillInsances(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id " + userId + " not found"));

        List<BillInstance> billInstances=billInstanceRepository.findByUserUserIdAndDueDateGreaterThanEqualAndBillStatus(userId,LocalDate.now(), BillStatus.PENDING);

        return billInstances.stream().map(b->new BillInstanceResponse(
                b.getBillInstanceId(),
                b.getTitle(),
                b.getAmount(),
                b.getDueDate(), b.getBillStatus()
        )
        ).toList();
    }

    public List<BillInstanceResponse> getOverDueBillInsances(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id " + userId + " not found"));

        List<BillInstance> billInstances=billInstanceRepository.findByUserUserIdAndDueDateLessThanAndBillStatus(userId,LocalDate.now(), BillStatus.PENDING);

        return billInstances.stream().map(b->new BillInstanceResponse(
                        b.getBillInstanceId(),
                        b.getTitle(),
                        b.getAmount(),
                        b.getDueDate(), b.getBillStatus()
                )
        ).toList();
    }

    public List<BillInstanceResponse> getBillInstancesByStatus(Integer userId,BillStatus status) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailNotFoundException("User with id " + userId + " not found"));

        List<BillInstance> billInstances=billInstanceRepository.findByUserUserIdAndBillStatus(userId, status);

        return billInstances.stream().map(b->new BillInstanceResponse(
                        b.getBillInstanceId(),
                        b.getTitle(),
                        b.getAmount(),
                        b.getDueDate(), b.getBillStatus()
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

    public void updateBillInstanceStatus(BillInstanceStatusRequest billInstanceStatusRequest) {

        BillInstance existingBillInstance = billInstanceRepository.findById(billInstanceStatusRequest.getBillInstanceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bill instance with id " + billInstanceStatusRequest.getBillInstanceId() + " not found"
                ));

        existingBillInstance.setBillStatus(billInstanceStatusRequest.getBillStatus());

        billInstanceRepository.save(existingBillInstance);
    }

    public void processDueBills() {

        LocalDate targetDate=LocalDate.now().plusDays(3);
        List<BillInstance> dueBills=billInstanceRepository.findDueBills(targetDate);

        for (BillInstance bill : dueBills) {
            BillReminderEvent event = new BillReminderEvent(
                    bill.getBillInstanceId(),
                    bill.getTitle(),
                    bill.getAmount(),
                    bill.getDueDate(),
                    bill.getBillStatus()
            );

            System.out.println(dueBills);

            kafkaTemplate.send("bill-reminder-topic", bill.getUser().getUserId().toString(), event);
        }

    }
}
