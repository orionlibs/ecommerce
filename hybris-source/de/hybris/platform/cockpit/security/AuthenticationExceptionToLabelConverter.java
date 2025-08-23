package de.hybris.platform.cockpit.security;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.AuthenticationException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;

public class AuthenticationExceptionToLabelConverter implements Converter<AuthenticationException, Label>
{
    private static final String WARNING_LABEL_CSS_CLASS = "loginWarningLabel";
    private static final String ERROR_LABEL_CSS_CLASS = "loginErrorLabel";
    private Map<Class, String> authenticationExceptionMessageMapping;
    private List<Class> warningExceptions;
    private String defaultLabel;


    public Label convert(AuthenticationException source) throws ConversionException
    {
        return convert(source, new Label());
    }


    protected String getDefaultLabel()
    {
        return this.defaultLabel;
    }


    public Label convert(AuthenticationException source, Label prototype) throws ConversionException
    {
        prototype.setValue(Labels.getLabel(getLabelKey(source)));
        if(isWarning(source))
        {
            UITools.modifySClass((HtmlBasedComponent)prototype, "loginWarningLabel", true);
        }
        else
        {
            UITools.modifySClass((HtmlBasedComponent)prototype, "loginErrorLabel", true);
        }
        return prototype;
    }


    private boolean isWarning(AuthenticationException source)
    {
        return (CollectionUtils.isNotEmpty(getWarningExceptions()) && getWarningExceptions().contains(source.getClass()));
    }


    private String getLabelKey(AuthenticationException source)
    {
        if(null != getAuthenticationExceptionMessageMapping() &&
                        getAuthenticationExceptionMessageMapping().containsKey(source.getClass()))
        {
            return this.authenticationExceptionMessageMapping.get(source.getClass());
        }
        return getDefaultLabel();
    }


    protected Map<Class, String> getAuthenticationExceptionMessageMapping()
    {
        return this.authenticationExceptionMessageMapping;
    }


    public void setAuthenticationExceptionMessageMapping(Map<Class<?>, String> authenticationExceptionMessageMapping)
    {
        this.authenticationExceptionMessageMapping = authenticationExceptionMessageMapping;
    }


    public void setDefaultLabel(String defaultLabel)
    {
        this.defaultLabel = defaultLabel;
    }


    protected List<Class> getWarningExceptions()
    {
        return this.warningExceptions;
    }


    public void setWarningExceptions(List<Class<?>> warningExceptions)
    {
        this.warningExceptions = warningExceptions;
    }
}
