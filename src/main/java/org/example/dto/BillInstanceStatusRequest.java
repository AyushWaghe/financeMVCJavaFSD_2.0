package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.BillStatus;

@Data
public class BillInstanceStatusRequest {

    @NotNull(message = "Bill instance id for status update cannot be null")
    Integer billInstanceId;

    @NotNull(message = "Bill instance status for status update cannot be null")
    BillStatus billStatus;
}
