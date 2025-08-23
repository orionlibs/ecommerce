package de.hybris.platform.directpersistence.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RetryConcurrentModification
{
    Class[] exception() default {de.hybris.platform.persistence.hjmp.HJMPException.class, de.hybris.platform.directpersistence.exception.ConcurrentModificationException.class, de.hybris.platform.persistence.polyglot.uow.PolyglotPersistenceConcurrentModificationException.class};


    int retries() default -1;


    int sleepIntervalInMillis() default -1;
}
