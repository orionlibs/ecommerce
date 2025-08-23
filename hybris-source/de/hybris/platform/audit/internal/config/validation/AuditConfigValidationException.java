package de.hybris.platform.audit.internal.config.validation;

import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import java.util.ArrayList;
import java.util.List;

public class AuditConfigValidationException extends InterceptorException
{
    private final ArrayList<AuditConfigViolation> violations;


    public AuditConfigValidationException(String reportCode, List<AuditConfigViolation> violations)
    {
        super(renderErrorMessage(reportCode, violations));
        this.violations = new ArrayList<>(violations);
    }


    public List<AuditConfigViolation> getViolations()
    {
        return this.violations;
    }


    private static String renderErrorMessage(String code, List<AuditConfigViolation> violations)
    {
        StringBuilder sb = (new StringBuilder("Audit report ")).append(code).append(" configuration is invalid. ");
        for(AuditConfigViolation violation : violations)
        {
            sb.append(" ").append(violation.getMessage()).append(" ");
        }
        return sb.toString();
    }
}
