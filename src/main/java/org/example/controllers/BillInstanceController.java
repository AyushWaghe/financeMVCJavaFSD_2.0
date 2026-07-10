package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.enums.BillStatus;
import org.example.services.BillInstanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("bill-instance")
public class BillInstanceController {

    private final BillInstanceService billInstanceService;

    @PostMapping()
    public ResponseEntity<APIResponse<BillInstanceResponse>> saveBillInstance(@Valid @RequestBody BillInstanceRequest billInstanceRequest){
        BillInstanceResponse billInstanceResponse=billInstanceService.saveBillInstance(billInstanceRequest);
        APIResponse<BillInstanceResponse> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstanceResponse);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill saved successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<APIResponse<List<BillInstanceResponse>>> getUpcomingBillInstances(@PathVariable("userId") Integer userId){
        List<BillInstanceResponse> billInstances=billInstanceService.getUpcomingBillInsances(userId);
//        System.out.println(billInstances);
        APIResponse<List<BillInstanceResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstances);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill instances fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("status/user/{userId}")
    public ResponseEntity<APIResponse<List<BillInstanceResponse>>> getBillInstancesByStatus(@PathVariable("userId") Integer userId, @RequestParam("status") BillStatus billStatus){
        List<BillInstanceResponse> billInstances=billInstanceService.getBillInstancesByStatus(userId,billStatus);
//        System.out.println(billInstances);
        APIResponse<List<BillInstanceResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstances);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill instances fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("overdue/{userId}")
    public ResponseEntity<APIResponse<List<BillInstanceResponse>>> getOverDueBillInstances(@PathVariable("userId") Integer userId){
        List<BillInstanceResponse> billInstances=billInstanceService.getOverDueBillInsances(userId);
//        System.out.println(billInstances);
        APIResponse<List<BillInstanceResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstances);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill instances fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<APIResponse<List<BillInstanceResponse>>> getBillInstances(@PathVariable("userId") Integer userId){
//        List<BillInstanceResponse> billInstances=billInstanceService.getBillInsances(userId);
////        System.out.println(billInstances);
//        APIResponse<List<BillInstanceResponse>> apiResponse=new APIResponse<>();
//        apiResponse.setData(billInstances);
//        apiResponse.setSuccess(true);
//        apiResponse.setMessage("Bill instances fetched successfully");
//        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
//    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<APIResponse<Void>> deleteBillInstance(@PathVariable("billId") Integer billId){
        billInstanceService.deleteBillInstance(billId);
        APIResponse<Void> apiResponse=new APIResponse<>();
        apiResponse.setMessage("Bill instance deleted successfully");
        apiResponse.setSuccess(true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<APIResponse<Void>> updateBillInstace(@RequestBody BillInstanceRequest billInstanceRequest){
        billInstanceService.updateBillInstance(billInstanceRequest);
        APIResponse<Void> apiResponse=new APIResponse<>();
        apiResponse.setMessage("Bill instance updated successfully");
        apiResponse.setSuccess(true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PatchMapping("/mark-status")
    public ResponseEntity<APIResponse<Void>> updateBillInstanceStatus(@RequestBody BillInstanceStatusRequest billInstanceStatusRequest){
        billInstanceService.updateBillInstanceStatus(billInstanceStatusRequest);
        APIResponse<Void> apiResponse=new APIResponse<>();
        apiResponse.setMessage("Bill instance updated successfully");
        apiResponse.setSuccess(true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

}
