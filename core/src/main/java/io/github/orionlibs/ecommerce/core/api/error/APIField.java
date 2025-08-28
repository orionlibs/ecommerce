package io.github.orionlibs.ecommerce.core.api.error;

public record APIField(String field,
                       String message,
                       Object rejectedValue)
{
}
