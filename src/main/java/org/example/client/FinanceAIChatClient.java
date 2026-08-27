package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.dto.APIResponse;
import org.example.dto.ChatResponses;
import org.example.dto.MonthlySpendingResponse;
import org.example.exceptions.FinanceAIClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FinanceAIChatClient {
    private final RestClient restClient;

    public List<ChatResponses> askPlannerAgent(@RequestParam("query") String query, @RequestParam("thinkAndAnswer") boolean thinkAndAnswer){
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/chat")
                            .queryParam("query", query)
                            .queryParam("thinkAndAnswer",thinkAndAnswer)
                            .build())
                    .retrieve()
                    .body(
                            new ParameterizedTypeReference<
                                    List<ChatResponses>
                                    >() {}
                    );
        }catch (Exception e){
            throw new FinanceAIClientException("Error while calling askPlannerAgent request due to"+e);
        }
    }

    public List<ChatResponses> getMessages(){
        try {
            return restClient.get()
                    .uri(uriBuilder -> {
                        URI uri = uriBuilder
                                .path("/chat/getMessages")
                                .build();
                        return uri;
                    })
                    .retrieve()
                    .body(
                            new ParameterizedTypeReference<
                                    List<ChatResponses>
                                    >() {}
                    );
        }catch (Exception e){
            System.out.println(e);
            throw new FinanceAIClientException("Error while calling getMessages request due to"+e);
        }
    }

    public void deleteMessages() {
        try {
            restClient.delete()
                    .uri("/chat")
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            throw new FinanceAIClientException(
                    "Error while calling deleteMessages request due to " + e.getMessage()
            );
        }
    }

}
