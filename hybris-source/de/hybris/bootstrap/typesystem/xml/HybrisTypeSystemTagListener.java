package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import java.util.Arrays;
import java.util.Collection;

public class HybrisTypeSystemTagListener extends AbstractTypeSystemTagListener
{
    public HybrisTypeSystemTagListener(HybrisTypeSystemParser parser)
    {
        super(parser, "items");
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new AtomicTypeTagListener.AtomicTypesTagListener(this), (TagListener)new CollectionTypeTagListener.CollectionTypesTagListener(this), (TagListener)new EnumTypeTagListener.EnumTypesTagListener(this),
                        (TagListener)new MapTypeTagListener.MapTypesTagListener(this), (TagListener)new RelationTypeTagListener.RelationTypesTagListener(this), (TagListener)new ItemTypeTagListener.ItemTypesTagListener(this)});
    }


    protected final Object processStartElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().startLoadingExtension();
        return null;
    }


    protected final Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().finishedLoadingExtension();
        return null;
    }
}
