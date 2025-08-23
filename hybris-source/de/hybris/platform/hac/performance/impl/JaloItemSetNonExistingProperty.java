package de.hybris.platform.hac.performance.impl;

public class JaloItemSetNonExistingProperty extends AbstractJaloItemTest
{
    public void executeBlock()
    {
        this.country.setProperty("test", "bla");
    }


    public String getTestName()
    {
        return "Jalo item set not existing property";
    }
}
