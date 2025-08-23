package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;

class MapTypeTagListener extends AbstractTypeSystemTagListener
{
    MapTypeTagListener(MapTypesTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "maptype");
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().loadNewMapType(getCode(), getAttribute("argumenttype"),
                        getAttribute("returntype"), isAutocreate(), isGenerate());
        return null;
    }
}
