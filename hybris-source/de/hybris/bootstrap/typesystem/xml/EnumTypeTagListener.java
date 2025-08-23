package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.Arrays;
import java.util.Collection;

class EnumTypeTagListener extends AbstractTypeSystemTagListener
{
    private int counter = 0;


    EnumTypeTagListener(EnumTypesTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "enumtype");
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new DefaultTagListener[] {(DefaultTagListener)new EnumValueTagListener(this), (DefaultTagListener)new ItemTypeTagListener.ItemDescriptionTagListener(this, "description"), (DefaultTagListener)new EnumModelTagListener(this)});
    }


    protected void inc()
    {
        this.counter++;
    }


    protected int getSequenceNumber()
    {
        return this.counter;
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        this.counter = 0;
        getParser().loadNewEnumType(
                        getAttribute("code"),
                        getAttribute("jaloclass"),
                        isAutocreate(),
                        isGenerate(),
                        isDynamic(),
                        (getSubTagValue("description") == null) ? null :
                                        String.valueOf(getSubTagValue("description")), (String)
                                        getSubTagValue("model"),
                        getAttribute("deprecatedSince"));
        return null;
    }


    private boolean isDynamic()
    {
        return Boolean.TRUE.toString().equals(getAttribute("dynamic"));
    }
}
