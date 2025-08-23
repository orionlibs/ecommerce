package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;

class AtomicTypeTagListener extends AbstractTypeSystemTagListener
{
    AtomicTypeTagListener(AtomicTypesTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "atomictype");
        if(parent == null)
        {
            throw new IllegalArgumentException("parent cannot be null");
        }
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().loadNewAtomicType(getAttribute("class"),
                        getAttribute("extends"), isAutocreate(), isGenerate());
        return null;
    }
}
