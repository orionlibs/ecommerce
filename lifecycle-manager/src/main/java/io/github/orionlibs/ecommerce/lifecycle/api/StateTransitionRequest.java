package io.github.orionlibs.ecommerce.lifecycle.api;

import jakarta.validation.constraints.NotBlank;

public record StateTransitionRequest(@NotBlank String transitionName)
{
}
