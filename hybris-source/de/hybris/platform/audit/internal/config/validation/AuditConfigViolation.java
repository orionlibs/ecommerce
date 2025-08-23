package de.hybris.platform.audit.internal.config.validation;

import java.io.Serializable;
import java.util.Objects;

public class AuditConfigViolation implements Serializable
{
    private static final long serialVersionUID = 0L;
    private final String message;
    private final ViolationLevel level;


    public AuditConfigViolation(String message, ViolationLevel level)
    {
        this.message = message;
        this.level = level;
    }


    public ViolationLevel getLevel()
    {
        return this.level;
    }


    public String getMessage()
    {
        return this.message;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof AuditConfigViolation))
        {
            return false;
        }
        AuditConfigViolation that = (AuditConfigViolation)o;
        return (Objects.equals(this.message, that.message) && this.level == that.level);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.message, this.level});
    }
}
