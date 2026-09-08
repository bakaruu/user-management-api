package com.bakaru.usermanagement.user.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    private String email;

    @Size(min = 8, max = 255)
    private String password;

    // TODO: Momentary fix — replace with a reusable @NullOrNotBlank custom
    // constraint annotation (own Constraint + ConstraintValidator classes).
    // @AssertTrue works but lives apart from the field it validates and
    // isn't reusable if other DTOs need the same "optional but not blank" rule.

    @AssertTrue(message = "First name cannot be blank")
    public boolean isFirstNameValid() {
        return firstName == null || !firstName.isBlank();
    }
}