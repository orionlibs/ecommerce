package io.github.orionlibs.ecommerce.lifecycle.api;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record StateTransitionRequest(@NotBlank String transitionName,
                                     @NotBlank String actor,
                                     String reason,
                                     Map<String, Object> payload)
{
}
