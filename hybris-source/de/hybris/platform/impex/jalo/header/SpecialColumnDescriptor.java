package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.translators.NotifiedSpecialValueTranslator;
import de.hybris.platform.impex.jalo.translators.SpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpecialColumnDescriptor extends AbstractColumnDescriptor
{
    private final SpecialValueTranslator _valueTrans;
    private final Thread valueTranslatorThread;
    private final Map<Thread, SpecialValueTranslator> threadBoundValueTranslators = Collections.synchronizedMap(new HashMap<>());


    public SpecialColumnDescriptor(int position, HeaderDescriptor header, String expr) throws HeaderValidationException
    {
        super(position, header, expr);
        this._valueTrans = createValueTranslator();
        this.valueTranslatorThread = Thread.currentThread();
    }


    public SpecialValueTranslator getValueTranslator()
    {
        SpecialValueTranslator ret = this._valueTrans;
        Thread caller = Thread.currentThread();
        if(!caller.equals(this.valueTranslatorThread))
        {
            ret = this.threadBoundValueTranslators.get(caller);
            if(ret == null)
            {
                try
                {
                    this.threadBoundValueTranslators.put(caller, ret = createValueTranslator());
                }
                catch(HeaderValidationException e)
                {
                    throw new IllegalStateException("could not create thread bound special value translator due to " + e
                                    .getMessage());
                }
            }
        }
        return ret;
    }


    protected SpecialValueTranslator createValueTranslator() throws HeaderValidationException
    {
        String customTranslatorClass = getDescriptorData().getModifier("translator");
        if(customTranslatorClass != null)
        {
            SpecialValueTranslator ret = createSpecialValueTranslator(customTranslatorClass);
            ret.init(this);
            return ret;
        }
        throw new HeaderValidationException("" +
                        getValuePosition() + ": missing special value translator class in column " + getValuePosition(), 10);
    }


    private final SpecialValueTranslator createSpecialValueTranslator(String className) throws HeaderValidationException
    {
        try
        {
            Class<?> clazz = Class.forName(className);
            return (SpecialValueTranslator)clazz.newInstance();
        }
        catch(Exception e)
        {
            throw new HeaderValidationException("invalid special value translator class '" + className + "' - cannot create due to " + e, 10);
        }
    }


    public String getDefaultValueDefinition()
    {
        return getDescriptorData().getModifier("default");
    }


    protected boolean hasDefaultValueDefinition()
    {
        return (getDescriptorData().getModifier("default") != null);
    }


    protected void validate() throws HeaderValidationException
    {
        super.validate();
        getValueTranslator().validate(getDefinitionSrc());
    }


    public String performExport(Item item) throws ImpExException
    {
        return getValueTranslator().performExport(item);
    }


    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        SpecialValueTranslator translator = getValueTranslator();
        if(translator.isEmpty(cellValue))
        {
            if(hasDefaultValueDefinition())
            {
                translator.performImport(getDefaultValueDefinition(), processedItem);
            }
        }
        else
        {
            translator.performImport(cellValue, processedItem);
        }
    }


    public void performImport(String cellValue, PK processedItemPK) throws ImpExException
    {
        performImport(cellValue, JaloSession.getCurrentSession().getItem(processedItemPK));
    }


    public void notifyTranslationEnd(ValueLine line, HeaderDescriptor header, PK processedItemPK) throws ImpExException
    {
        if(getValueTranslator() instanceof NotifiedSpecialValueTranslator)
        {
            ((NotifiedSpecialValueTranslator)getValueTranslator()).notifyTranslationEnd(line, header,
                            JaloSession.getCurrentSession().getItem(processedItemPK));
        }
    }
}
