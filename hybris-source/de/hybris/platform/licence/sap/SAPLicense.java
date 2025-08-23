package de.hybris.platform.licence.sap;

import com.sap.security.core.server.likey.LicenseKey;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.licence.internal.SAPLicenseValidator;
import de.hybris.platform.licence.internal.ValidationResult;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SAPLicense extends Licence
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd/MMM/yyyy").withLocale(Locale.US);
    private static final long serialVersionUID = 3599708403149774157L;
    private final Properties props = new Properties();
    private final LicenseKey licenseKey;


    public SAPLicense(LicenseKey licenseKey)
    {
        this.licenseKey = licenseKey;
        this.props.put("licence.advancedsecurity", "true");
        this.props.put("licence.highperformance", "true");
        this.props.put("licence.version", "");
        this.props.put("licence.email", "");
        this.props.put("licence.name", licenseKey.getSwProduct());
        this.props.put("licence.eulaversion", "2.0");
        this.props.put("licence.id", licenseKey.getSystemId());
        this.props.put("licence.date", licenseKey.getBeginDate());
        this.props.put("licence.clustering", "true");
        this.props.put("licence.endcustomer", "");
        this.props.put("licence.expiration", licenseKey.getEndDate());
        this.props.put("licence.sap.systemId", licenseKey.getSystemId());
        this.props.put("licence.sap.instNo", licenseKey.getInstNo());
        this.props.put("licence.sap.sysNo", licenseKey.getSysNo());
        this.props.put("licence.sap.hwKey", licenseKey.getHwKey());
        this.props.put("licence.sap.type", licenseKey.getType());
    }


    public Properties getLicenceProperties()
    {
        return this.props;
    }


    public byte[] getSignature()
    {
        try
        {
            Field signature = this.licenseKey.getClass().getDeclaredField("signature");
            signature.setAccessible(true);
            return ((String)signature.get(this.licenseKey)).getBytes();
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public ValidationResult validateDemoExpiration()
    {
        return null;
    }


    public ValidationResult validate()
    {
        SAPLicenseValidator validator = new SAPLicenseValidator();
        return validator.validateLicense(this.licenseKey.getSwProduct());
    }


    public boolean isDemoOrDevelopLicence()
    {
        return !this.licenseKey.isPermanent();
    }


    protected DateTimeFormatter getDateTimeFormatter()
    {
        return FORMATTER;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("SAP License [");
        sb.append("begin date: ").append(this.licenseKey.getBeginDate()).append(", ");
        sb.append("end date: ").append(this.licenseKey.getEndDate()).append(", ");
        sb.append("hw key: ").append(this.licenseKey.getHwKey()).append(", ");
        sb.append("inst no: ").append(this.licenseKey.getInstNo()).append(", ");
        sb.append("sw product: ").append(this.licenseKey.getSwProduct()).append(", ");
        sb.append("sw product limit: ").append(this.licenseKey.getSwProductLimit()).append(", ");
        sb.append("sys no: ").append(this.licenseKey.getSysNo()).append(", ");
        sb.append("system id: ").append(this.licenseKey.getSystemId()).append(", ");
        sb.append("type: ").append(this.licenseKey.getType()).append("]");
        return sb.toString();
    }


    public LicenseKey getSource()
    {
        return this.licenseKey;
    }


    public Integer getDaysLeft()
    {
        String beginDate = this.licenseKey.getBeginDate();
        String endDate = this.licenseKey.getEndDate();
        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate))
        {
            DateTime _beginDate = DateTime.parse(beginDate, FORMATTER);
            DateTime _endDate = DateTime.parse(endDate, FORMATTER);
            return Integer.valueOf(
                            Days.daysBetween((ReadableInstant)_beginDate.withTimeAtStartOfDay(), (ReadableInstant)_endDate.withTimeAtStartOfDay()).getDays());
        }
        return null;
    }


    public int getDemoLicenseDays()
    {
        return 90;
    }
}
