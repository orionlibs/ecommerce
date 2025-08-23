package de.hybris.bootstrap.xml;

public class DummyTagListener extends DefaultTagListener
{
    public Object processEndElement(ObjectProcessor processor)
    {
        return null;
    }


    public String getTagName()
    {
        return "DUMMY-TAG";
    }
}
