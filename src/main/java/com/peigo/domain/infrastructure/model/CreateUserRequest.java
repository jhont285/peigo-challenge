package com.peigo.domain.infrastructure.model;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateUserRequest(@NonNull
                                @NotBlank
                                String username,

                                @Size(min = 7)
                                @NonNull
                                String password,

                                @NonNull
                                String confirmPassword) {
}
