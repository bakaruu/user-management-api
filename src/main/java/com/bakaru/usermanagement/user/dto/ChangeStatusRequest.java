package com.bakaru.usermanagement.user.dto;

import com.bakaru.usermanagement.user.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusRequest {

    @NotNull(message = "Status is required")
    private UserStatus status;
}
