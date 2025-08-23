package de.hybris.platform.licence.internal;

import com.sap.security.core.server.likey.KeySystem;
import com.sap.security.core.server.likey.LicenseChecker;
import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;
import de.hybris.platform.licence.sap.DefaultKeySystem;
import de.hybris.platform.licence.sap.DefaultLogAndTrace;
import de.hybris.platform.licence.sap.DefaultPersistence;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

public class SAPLicenseValidator
{
    public static final int TEMP_LICENSE_DAYS = 90;
    private final LicenseChecker licenseChecker;
    private final DefaultLogAndTrace logAndTrace;


    public SAPLicenseValidator()
    {
        Persistence persistence = getPersistence();
        DefaultKeySystem defaultKeySystem = new DefaultKeySystem();
        this.logAndTrace = new DefaultLogAndTrace(Level.DEBUG);
        this.licenseChecker = new LicenseChecker(persistence, (KeySystem)defaultKeySystem, (LogAndTrace)this.logAndTrace);
    }


    protected Persistence getPersistence()
    {
        return (Persistence)new DefaultPersistence();
    }


    public ValidationResult validateLicense(String swProduct)
    {
        boolean licenseValid = this.licenseChecker.check(swProduct);
        return new ValidationResult(licenseValid, getValidationMessage());
    }


    private String getValidationMessage()
    {
        String traceMessages = this.logAndTrace.getAndClearTraceMessages();
        String errorMessages = this.logAndTrace.getAndClearErrorMessages();
        if(StringUtils.isBlank(traceMessages) && StringUtils.isBlank(errorMessages))
        {
            return null;
        }
        if(StringUtils.isNotBlank(traceMessages) && StringUtils.isBlank(errorMessages))
        {
            return traceMessages;
        }
        if(StringUtils.isBlank(traceMessages) && StringUtils.isNotBlank(errorMessages))
        {
            return errorMessages;
        }
        return traceMessages + " " + traceMessages;
    }
}
