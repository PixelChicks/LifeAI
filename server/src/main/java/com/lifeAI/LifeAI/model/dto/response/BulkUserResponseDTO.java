package com.lifeAI.LifeAI.model.dto.response;

import com.lifeAI.LifeAI.model.dto.request.UserResponseDTO;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUserResponseDTO {
    private int successCount;
    private int failureCount;
    private List<UserResponseDTO> createdUsers;
    private List<String> errors;
}