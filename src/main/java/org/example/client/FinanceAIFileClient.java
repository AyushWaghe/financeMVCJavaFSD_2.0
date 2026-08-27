package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.dto.APIResponse;
import org.example.dto.ChatResponses;
import org.example.dto.DocUploadResponse;
import org.example.exceptions.FinanceAIClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FinanceAIFileClient{
        private final RestClient restClient;

    public void upload(@RequestParam("file") MultipartFile file) {

        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();

            builder.part("file", file.getResource());

            restClient.post()
                    .uri("/file")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(builder.build())
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            throw new FinanceAIClientException(
                    "Error while calling upload request due to " + e.getMessage()
            );
        }
    }

    public Resource download(String objectKey) {

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/file")
                            .queryParam("objectKey", objectKey)
                            .build())
                    .retrieve()
                    .body(Resource.class);

        } catch (Exception e) {
            throw new FinanceAIClientException(
                    "Error while calling download request due to " + e.getMessage()
            );
        }
    }

    public List<DocUploadResponse> getUserFilesData() {

        try {
            return restClient.get()
                    .uri("/file/getFiles")
                    .retrieve()
                    .body(new ParameterizedTypeReference<
                            List<DocUploadResponse>
                            >() {});

        } catch (Exception e) {
            throw new FinanceAIClientException(
                    "Error while calling getUserFiles request due to " + e.getMessage()
            );
        }
    }

    public APIResponse<Void> deleteFile(String objectKey) {

        try {
            return restClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/file")
                            .queryParam("objectKey", objectKey)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<APIResponse<Void>>() {});

        } catch (Exception e) {
            throw new FinanceAIClientException(
                    "Error while calling deleteFile request due to " + e.getMessage()
            );
        }
    }
}
