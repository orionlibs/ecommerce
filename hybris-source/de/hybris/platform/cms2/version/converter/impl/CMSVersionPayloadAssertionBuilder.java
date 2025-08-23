package de.hybris.platform.cms2.version.converter.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CMSVersionPayloadAssertionBuilder
{
    private CMSVersionPayloadAnalyzer comparator;
    private String attribute;
    private List<Consumer<CMSVersionPayloadAnalyzer.PayloadAttribute>> assertions;


    public static CMSVersionPayloadAssertionBuilder aModel()
    {
        return new CMSVersionPayloadAssertionBuilder();
    }


    public CMSVersionPayloadAssertionBuilder withComparator(CMSVersionPayloadAnalyzer comparator)
    {
        this.comparator = comparator;
        return this;
    }


    public CMSVersionPayloadAssertionBuilder withAttribute(String attribute)
    {
        this.attribute = attribute;
        return this;
    }


    public CMSVersionPayloadAssertionBuilder withAssertions(Consumer<CMSVersionPayloadAnalyzer.PayloadAttribute>... assertions)
    {
        this.assertions = (List<Consumer<CMSVersionPayloadAnalyzer.PayloadAttribute>>)Arrays.<Consumer<CMSVersionPayloadAnalyzer.PayloadAttribute>>stream(assertions).collect(Collectors.toList());
        return this;
    }


    public void check()
    {
        CMSVersionPayloadAnalyzer.PayloadAttribute payloadAttribute = this.comparator.getAttributeByName(this.attribute);
        this.assertions.forEach(assertion -> assertion.accept(payloadAttribute));
    }
}
