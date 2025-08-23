package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.jalo.Item;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

public class VelocityTranslator extends AbstractSpecialValueTranslator
{
    private static final Logger LOG = Logger.getLogger(VelocityTranslator.class);
    private static Logger velocityLoggger;
    private static VelocityEngine velocityEngine;
    private String templateName;
    private String expression;


    public VelocityTranslator()
    {
    }


    public VelocityTranslator(String templateName, String expression)
    {
        this.templateName = templateName;
        this.expression = expression;
    }


    public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.init(columnDescriptor);
        this.templateName = columnDescriptor.getQualifier();
        this.expression = columnDescriptor.getDescriptorData().getModifier("expr");
        if(this.expression == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "missing expression for velocity column " + columnDescriptor
                            .getValuePosition() + ":" + columnDescriptor
                            .getQualifier(), 0);
        }
    }


    public String performExport(Item item) throws ImpExException
    {
        if(item == null || !item.isAlive())
        {
            return null;
        }
        VelocityContext context = new VelocityContext();
        context.put("item", item);
        boolean closed = false;
        StringWriter writer = new StringWriter();
        try
        {
            Velocity.evaluate((Context)context, writer, this.templateName, this.expression);
            writer.close();
            closed = true;
            return writer.toString();
        }
        catch(Exception e)
        {
            throw new ImpExException(e, "Error while executing velocity template '" + this.templateName + "' [" + this.templateName + "]", 23400);
        }
        finally
        {
            if(!closed)
            {
                try
                {
                    writer.close();
                }
                catch(IOException e)
                {
                    LOG.warn(" Can not close writer");
                }
            }
        }
    }
}
