package de.hybris.bootstrap.xml;

public interface ObjectProcessor
{
    void process(TagListener paramTagListener, AbstractValueObject paramAbstractValueObject) throws ParseAbortException;
}
