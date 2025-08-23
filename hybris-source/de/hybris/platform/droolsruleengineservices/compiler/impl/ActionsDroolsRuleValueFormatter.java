package de.hybris.platform.droolsruleengineservices.compiler.impl;

import java.util.AbstractList;
import java.util.List;
import java.util.StringJoiner;

public class ActionsDroolsRuleValueFormatter extends DefaultDroolsRuleValueFormatter
{
    public void initFormatters()
    {
        super.initFormatters();
        getFormatters().put(AbstractList.class.getName(), (context, value) -> {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            ((List)value).stream().forEach(());
            return joiner.toString();
        });
    }
}
