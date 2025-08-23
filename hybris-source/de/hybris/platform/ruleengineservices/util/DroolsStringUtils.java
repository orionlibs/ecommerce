package de.hybris.platform.ruleengineservices.util;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.lang.model.SourceVersion;

public class DroolsStringUtils
{
    private static final String SPACE_REGREX = "\\s+";
    private static final Pattern SPACE_REGREX_PATTERN = Pattern.compile("\\s+");
    private static final String[] UNICODE_ESCAPE_PREFIXES = new String[] {"\\u0000", "\\u000", "\\u00", "\\u0", "\\u"};
    private ConfigurationService configurationService;
    private boolean useOutputEncoding = true;
    private boolean useValidation = true;


    public String encodeMvelStringLiteral(Object input)
    {
        if(input == null)
        {
            return "";
        }
        if(!this.useOutputEncoding)
        {
            return input.toString();
        }
        return escapeString(input.toString());
    }


    public String validateFQCN(String fqcn)
    {
        if(this.useValidation && !SourceVersion.isName(fqcn))
        {
            throw new IllegalStateException("Not a valid Java FQCN: " + fqcn);
        }
        return fqcn;
    }


    public String validateSpringBeanName(String beanName)
    {
        boolean flag = (SourceVersion.isName(beanName) && (isStartWithLowerCase(beanName) || isStartWith2UpperCase(beanName)));
        if(this.useValidation && !flag)
        {
            throw new IllegalStateException("Not a valid spring bean name: " + beanName);
        }
        return beanName;
    }


    private boolean isStartWith2UpperCase(String beanName)
    {
        return (beanName.length() > 1 && Character.isUpperCase(beanName.charAt(1)) && Character.isUpperCase(beanName.charAt(0)));
    }


    private boolean isStartWithLowerCase(String beanName)
    {
        return (beanName.length() >= 1 && Character.isLowerCase(beanName.charAt(0)));
    }


    public String validateIndentation(String indentation)
    {
        if(this.useValidation && !SPACE_REGREX_PATTERN.matcher(indentation).matches())
        {
            throw new IllegalStateException("Indentation must not contain non-white space characters: '" + indentation + "'");
        }
        return indentation;
    }


    public String validateVariableName(String variableName)
    {
        if(this.useValidation && (!SourceVersion.isIdentifier(variableName) || SourceVersion.isKeyword(variableName)))
        {
            throw new IllegalStateException("Not a valid variable name for Java: " + variableName);
        }
        return variableName;
    }


    public String escapeString(String str)
    {
        StringBuilder sb = new StringBuilder();
        if(str == null)
        {
            return sb.toString();
        }
        int sz = str.length();
        for(int i = 0; i < sz; i++)
        {
            char ch = str.charAt(i);
            if(ch > 'ÿ')
            {
                if(ch == '€')
                {
                    sb.append(ch);
                }
                else
                {
                    sb.append(toUnicodeEscape(ch));
                }
            }
            else if(ch < ' ')
            {
                switch(ch)
                {
                    case '\b':
                        sb.append('\\');
                        sb.append('b');
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case '\t':
                        sb.append('\\');
                        sb.append('t');
                        break;
                    case '\f':
                        sb.append('\\');
                        sb.append('f');
                        break;
                    case '\r':
                        sb.append('\\');
                        sb.append('r');
                        break;
                    default:
                        sb.append(toUnicodeEscape(ch));
                        break;
                }
            }
            else
            {
                switch(ch)
                {
                    case '"':
                    case '\'':
                        sb.append(toUnicodeEscape(ch));
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    default:
                        sb.append(ch);
                        break;
                }
            }
        }
        return sb.toString();
    }


    private String toUnicodeEscape(char ch)
    {
        String hex = Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
        return UNICODE_ESCAPE_PREFIXES[hex.length()] + UNICODE_ESCAPE_PREFIXES[hex.length()];
    }


    protected boolean isUseOutputEncoding()
    {
        return this.useOutputEncoding;
    }


    public void setUseOutputEncoding(boolean useOutputEncoding)
    {
        this.useOutputEncoding = useOutputEncoding;
    }


    protected boolean isUseValidation()
    {
        return this.useValidation;
    }


    public void setUseValidation(boolean useValidation)
    {
        this.useValidation = useValidation;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
