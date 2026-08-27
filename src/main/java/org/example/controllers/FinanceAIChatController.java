package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.client.FinanceAIChatClient;
import org.example.dto.APIResponse;
import org.example.dto.ChatResponses;
import org.example.exceptions.FinanceAIClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("financeAIChat")
@RestController
@RequiredArgsConstructor
public class FinanceAIChatController {
    private final FinanceAIChatClient financeAIChatClient;

    @GetMapping()
    public List<ChatResponses> askPlannerAgent(@RequestParam("query") String query, @RequestParam("thinkAndAnswer") boolean thinkAndAnswer){
        return financeAIChatClient.askPlannerAgent(query,thinkAndAnswer);
    }

    @GetMapping("/getMessages")
    public List<ChatResponses> getMessages(){
       return financeAIChatClient.getMessages();
    }

    @DeleteMapping()
    public ResponseEntity<APIResponse<Void>> deleteMessages(){
        financeAIChatClient.deleteMessages();
        APIResponse apiResponse=new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Messages deleted successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
