package de.hybris.platform.ruleengineservices.impex.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.DefaultImportProcessor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.Item;
import java.util.Collections;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionalImportProcessor extends DefaultImportProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(ConditionalImportProcessor.class);
    private Predicate<ValueLine> importProcessorCondition;


    public void init(ImpExImportReader reader)
    {
        super.init(reader);
        try
        {
            this.importProcessorCondition = getImportProcessorCondition(getReader().getCurrentHeader());
        }
        catch(ImpExException exc)
        {
            LOG.error("Cannot use conditional import processor", (Throwable)exc);
            throw new IllegalStateException("Cannot use conditional import processor - provided condition class is invalid", exc);
        }
    }


    public Item processItemData_Impl(ValueLine valueLine) throws ImpExException
    {
        try
        {
            adjustSessionSettings();
            if(getImportProcessorCondition().test(valueLine))
            {
                return super.processItemData_Impl(valueLine);
            }
            valueLine.resolve((Item)null, Collections.emptyList());
        }
        finally
        {
            restoreSessionSettings();
        }
        return null;
    }


    protected Predicate<ValueLine> getImportProcessorCondition(HeaderDescriptor header) throws ImpExException
    {
        String condition = header.getDescriptorData().getModifier("condition");
        try
        {
            Class<Predicate<ValueLine>> clazz = (Class)Class.forName(condition);
            return clazz.newInstance();
        }
        catch(Exception exc)
        {
            throw new ImpExException(exc, "Can not instantiate Processor Condition [" + condition + "] because: " + exc
                            .getMessage(), 0);
        }
    }


    protected Predicate<ValueLine> getImportProcessorCondition()
    {
        return this.importProcessorCondition;
    }
}
