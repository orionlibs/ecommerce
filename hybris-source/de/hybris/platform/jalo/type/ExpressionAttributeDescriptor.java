package de.hybris.platform.jalo.type;

import bsh.EvalError;
import bsh.Interpreter;
import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.jalo.SessionContext;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.XMLOutputter;

public class ExpressionAttributeDescriptor extends AttributeDescriptor
{
    public static final String DEFAULT_VALUE_EXPRESSION = "defaultValueExpression";
    private static final Logger log = Logger.getLogger(ExpressionAttributeDescriptor.class);
    private Interpreter interpreter;


    public String getDefaultValueExpressionString()
    {
        return (String)getProperty("defaultValueExpression");
    }


    public void setDefaultValueExpressionString(String defaultValueExpression)
    {
        setProperty("defaultValueExpression", defaultValueExpression);
    }


    public Object getDefaultValueExpression()
    {
        Object ret = null;
        if(getDefaultValueExpressionString() != null)
        {
            if(this.interpreter == null)
            {
                this.interpreter = BeanShellUtils.createInterpreter();
            }
            try
            {
                ret = this.interpreter.eval(getDefaultValueExpressionString());
            }
            catch(EvalError e)
            {
                log.warn("Can not evaluate default value expression: " + getDefaultValueExpressionString());
            }
        }
        return ret;
    }


    public Object getDefaultValue(SessionContext ctx)
    {
        Object ret = getDefaultValueExpression();
        if(ret == null)
        {
            ret = super.getDefaultValue(ctx);
        }
        return ret;
    }


    protected void exportXMLDefinitionDefaultValue(XMLOutputter xout) throws IOException
    {
        String defaultvalue = getDefaultValueExpressionString();
        if(defaultvalue != null)
        {
            xout.startTag("custom-properties");
            xout.startTag("property");
            xout.attribute("name", "defaultValueExpression");
            xout.cdata(defaultvalue);
            xout.endTag();
            xout.endTag();
        }
    }
}
