package org.example.controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.App;
import org.example.dto.APIResponse;
import org.example.dto.BillRequest;
import org.example.dto.BillResponse;
import org.example.models.Bill;
import org.example.services.BillService;
import org.example.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

    @Autowired
    private final BillService billService;

    @PostMapping()
    public ResponseEntity<APIResponse<BillResponse>> saveBill(@Valid @RequestBody BillRequest billRequest){
        BillResponse billResponse=billService.saveBill(billRequest);
        APIResponse<BillResponse> apiResponse=new APIResponse();
        apiResponse.setData(billResponse);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill saved successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<APIResponse<Page<BillResponse>>> getBills(@PageableDefault(size = 5,sort = "latestDueDate",direction = Sort.Direction.DESC)Pageable pageable){
        Integer userId= AuthenticationUtil.getCurrentUserId();
        Page<BillResponse> billResponses=billService.getUserBills(userId,pageable);
        APIResponse<Page<BillResponse>> apiResponse=new APIResponse();
        apiResponse.setData(billResponses);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("User bills fetched successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<APIResponse<Void>> deleteBill(@PathVariable("billId") Integer billId){
        billService.deleteBill(billId);
        APIResponse apiResponse=new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Bill deleted successfully");
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/{billId}")
    public ResponseEntity<APIResponse<BillResponse>> updateBill(@Valid @RequestBody BillRequest billRequest,@PathVariable("billId") Integer billId){
        BillResponse billResponse=billService.updateBill(billRequest,billId);
        APIResponse<BillResponse> apiResponse=new APIResponse<>();
        apiResponse.setMessage("Bill updated successfully");
        apiResponse.setSuccess(true);
        apiResponse.setData(billResponse);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);

    }

}
