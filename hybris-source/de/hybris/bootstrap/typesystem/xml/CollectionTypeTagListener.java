package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;

class CollectionTypeTagListener extends AbstractTypeSystemTagListener
{
    CollectionTypeTagListener(CollectionTypesTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "collectiontype");
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().loadNewCollectionType(getCode(), getAttribute("elementtype"),
                        getAttribute("type"), isAutocreate(), isGenerate());
        return super.processEndElement(processor);
    }
}
