package io.github.orionlibs.ecommerce.core.api.idempotency;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark REST controller methods with the @Idempotent annotation if they should use idempotency handling.
 * If the API request does not include an Idempotency-Key header, then the API method behaviour is unchanged
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent
{
}
