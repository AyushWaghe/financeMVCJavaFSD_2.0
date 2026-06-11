package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.App;
import org.example.dto.APIResponse;
import org.example.dto.BillRequest;
import org.example.dto.BillResponse;
import org.example.models.Bill;
import org.example.services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/bill/user/{userId}")
    public ResponseEntity<APIResponse<List<BillResponse>>> getBills(@PathVariable("userId") Integer userId){
        List<BillResponse> billResponses=billService.getUserBills(userId);
        APIResponse<List<BillResponse>> apiResponse=new APIResponse();
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
