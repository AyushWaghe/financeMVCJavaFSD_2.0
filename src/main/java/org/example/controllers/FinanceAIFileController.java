package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.client.FinanceAIFileClient;
import org.example.dto.APIResponse;
import org.example.dto.DocUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("financeAIFile")
@RequiredArgsConstructor
public class FinanceAIFileController {
    private final FinanceAIFileClient financeAIFileClient;

    @PostMapping
    public ResponseEntity<APIResponse<Void>> upload(
            @RequestParam("file") MultipartFile file) {

        financeAIFileClient.upload(file);
        APIResponse apiResponse=new APIResponse();
        apiResponse.setMessage("File uploaded successfully");
        apiResponse.setSuccess(true);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<Resource> download(
            @RequestParam("objectKey") String objectKey) {

        Resource resource = financeAIFileClient.download(objectKey);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/getFiles")
    public ResponseEntity<APIResponse<List<DocUploadResponse>>> getUserFilesData() {

        List<DocUploadResponse> response =
                financeAIFileClient.getUserFilesData();
        APIResponse apiResponse=new APIResponse();
        apiResponse.setData(response);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Fetched user files data successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<APIResponse<Void>> deleteFile(
            @RequestParam("objectKey") String objectKey) {


        financeAIFileClient.deleteFile(objectKey);
        APIResponse apiResponse=new APIResponse();
        apiResponse.setMessage("File deleted successfully");
        apiResponse.setSuccess(true);

        return ResponseEntity.ok(apiResponse);
    }
}
