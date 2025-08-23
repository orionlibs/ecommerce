package de.hybris.platform.audit.internal.interceptors;

import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.XMLAuditReportConfigReader;
import de.hybris.platform.audit.internal.config.validation.AuditConfigValidationException;
import de.hybris.platform.audit.internal.config.validation.AuditConfigViolation;
import de.hybris.platform.audit.internal.config.validation.AuditReportConfigValidator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.audit.AuditReportConfigModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AuditReportConfigValidateInterceptor implements ValidateInterceptor<AuditReportConfigModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditReportConfigValidateInterceptor.class);
    public static final String AUDIT_VALIDATOR_MODE_PRODUCTION = "audit.report.production.mode";
    private XMLAuditReportConfigReader xmlAuditReportConfigReader;
    private AuditReportConfigValidator auditReportConfigValidator;


    @Required
    public void setXmlAuditReportConfigReader(XMLAuditReportConfigReader xmlAuditReportConfigReader)
    {
        this.xmlAuditReportConfigReader = xmlAuditReportConfigReader;
    }


    @Required
    public void setAuditReportConfigValidator(AuditReportConfigValidator auditReportConfigValidator)
    {
        this.auditReportConfigValidator = auditReportConfigValidator;
    }


    private boolean canBeValidated(AuditReportConfigModel model)
    {
        return (model.getContent() != null && model.getCode() != null);
    }


    public void onValidate(AuditReportConfigModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(canBeValidated(model))
        {
            validate(model);
        }
    }


    private void validate(AuditReportConfigModel model) throws InterceptorException
    {
        try
        {
            AuditReportConfig auditReportCfg = this.xmlAuditReportConfigReader.fromXml(model);
            List<AuditConfigViolation> configurationViolations = this.auditReportConfigValidator.validate(auditReportCfg);
            if(!configurationViolations.isEmpty())
            {
                throw new AuditConfigValidationException(model.getCode(), configurationViolations);
            }
            if(!model.getCode().equals(auditReportCfg.getName()))
            {
                throw new AuditConfigValidationException(model.getCode(),
                                Collections.singletonList(new AuditConfigViolation("AuditReportConfigModel.code (" + model
                                                .getCode() + ") must match the given config name(" + auditReportCfg
                                                .getName() + ")!", AuditConfigViolation.ViolationLevel.ERROR)));
            }
            List<AuditConfigViolation> configurationWarnings = this.auditReportConfigValidator.validate(auditReportCfg, AuditConfigViolation.ViolationLevel.WARNING);
            if(CollectionUtils.isNotEmpty(configurationWarnings))
            {
                handleConfigurationWarnings(model, configurationWarnings);
            }
        }
        catch(AuditConfigValidationException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new InterceptorException("Failed to validate configuration xml for " + model.getCode(), ex, this);
        }
    }


    private void handleConfigurationWarnings(AuditReportConfigModel model, List<AuditConfigViolation> configurationWarnings) throws AuditConfigValidationException
    {
        if(Registry.getCurrentTenant().getConfig().getBoolean("audit.report.production.mode", true))
        {
            throw new AuditConfigValidationException(model.getCode(), configurationWarnings);
        }
        for(AuditConfigViolation violation : configurationWarnings)
        {
            LOG.info("Report {} has a problem: {}", model.getCode(), violation.getMessage());
        }
    }
}
