package de.hybris.platform.ruleengineservices.configuration;

public enum Switch
{
    CONSUMPTION("ruleengineservices.consumption.enabled"),
    GENERATOR_WEBSITEGROUP("promotionengineservices.generator.websitegroup.enabled");
    private final String value;


    Switch(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public static Switch fromValue(String value)
    {
        for(Switch ev : values())
        {
            if(ev.getValue().equals(value))
            {
                return ev;
            }
        }
        throw new IllegalArgumentException("Unknown value \"" + value + "\"");
    }
}
