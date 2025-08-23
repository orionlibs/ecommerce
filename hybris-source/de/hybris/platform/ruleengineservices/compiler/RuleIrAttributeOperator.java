package de.hybris.platform.ruleengineservices.compiler;

public enum RuleIrAttributeOperator
{
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    IN("in"),
    NOT_IN("not in"),
    CONTAINS("contains"),
    NOT_CONTAINS("not contains"),
    MEMBER_OF("memberOf");
    private final String value;


    RuleIrAttributeOperator(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public static RuleIrAttributeOperator fromValue(String value)
    {
        for(RuleIrAttributeOperator ev : values())
        {
            if(ev.getValue().equals(value))
            {
                return ev;
            }
        }
        throw new IllegalArgumentException("Unknown value \"" + value + "\"");
    }
}
