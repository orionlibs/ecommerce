package io.github.orionlibs.ecommerce.core.api.error;

import java.time.OffsetDateTime;
import java.util.List;

public record APIError(
                OffsetDateTime timestamp,
                int status,
                String message,
                List<APIField> fieldErrors
)
{
}
