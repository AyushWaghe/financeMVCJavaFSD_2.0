package org.example.controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.enums.BillStatus;
import org.example.services.BillInstanceService;
import org.example.utils.AuthenticationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/upcoming")
    public ResponseEntity<APIResponse<Page<BillInstanceResponse>>> getUpcomingBillInstances(@PageableDefault(size = 5,sort = "dueDate",direction = Sort.Direction.DESC)Pageable pageable){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        Page<BillInstanceResponse> billInstances=billInstanceService.getUpcomingBillInsances(userId,pageable);
        APIResponse<Page<BillInstanceResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstances);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill instances fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("status/user")
    public ResponseEntity<APIResponse<Page<BillInstanceResponse>>> getBillInstancesByStatus(@RequestParam("status") BillStatus billStatus,@PageableDefault(size = 10,sort = "dueDate",direction = Sort.Direction.DESC)Pageable pageable){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        Page<BillInstanceResponse> billInstances=billInstanceService.getBillInstancesByStatus(userId,billStatus,pageable);
//        System.out.println(billInstances);
        APIResponse<Page<BillInstanceResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstances);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill instances fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("overdue")
    public ResponseEntity<APIResponse<Page<BillInstanceResponse>>> getOverDueBillInstances(Pageable pageable){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        Page<BillInstanceResponse> billInstances=billInstanceService.getOverDueBillInsances(userId,pageable);
//        System.out.println(billInstances);
        APIResponse<Page<BillInstanceResponse>> apiResponse=new APIResponse<>();
        apiResponse.setData(billInstances);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill instances fetched successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

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
