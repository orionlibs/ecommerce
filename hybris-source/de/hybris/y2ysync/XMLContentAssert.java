package de.hybris.y2ysync;

import java.io.IOException;
import java.io.InputStream;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonType;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.diff.NodeMatcher;

public class XMLContentAssert extends AbstractAssert<XMLContentAssert, String>
{
    private boolean ignoreComments = false;
    private boolean ignoreWhitespaces = false;
    private boolean ignoreNodeOrder = false;


    private XMLContentAssert(String actual)
    {
        super(actual, XMLContentAssert.class);
    }


    public static XMLContentAssert assertThat(String actual)
    {
        return new XMLContentAssert(actual);
    }


    public XMLContentAssert ignoreComments()
    {
        this.ignoreComments = true;
        return this;
    }


    public XMLContentAssert ignoreWhitespaces()
    {
        this.ignoreWhitespaces = true;
        return this;
    }


    public XMLContentAssert ignoreNodeOrder()
    {
        this.ignoreNodeOrder = true;
        return this;
    }


    public XMLContentAssert isIdenticalToResource(String resourceName)
    {
        InputStream resourceAsStream = getClass().getResourceAsStream(resourceName);
        Assertions.assertThat(resourceAsStream).isNotNull();
        Diff diff = isIdenticalTo(Input.fromStream(resourceAsStream));
        Assertions.assertThat(diff.getDifferences())
                        .as("expected [%s] to be identical to resource [%s], but differences have been found", new Object[] {this.actual, resourceName}).isEmpty();
        return this;
    }


    public XMLContentAssert isIdenticalTo(String expectedXml)
    {
        Assertions.assertThat(expectedXml).isNotNull();
        Diff diff = isIdenticalTo(Input.fromString(expectedXml));
        Assertions.assertThat(diff.getDifferences())
                        .as("expected [%s] to be identical to [%s], but differences have been found", new Object[] {this.actual, expectedXml}).isEmpty();
        return this;
    }


    private Diff isIdenticalTo(Input.Builder input)
    {
        DiffBuilder compare = DiffBuilder.compare(input);
        compare.withTest(this.actual);
        compare.checkForIdentical();
        if(this.ignoreComments)
        {
            compare.ignoreComments();
        }
        if(this.ignoreWhitespaces)
        {
            compare.ignoreWhitespace();
        }
        if(this.ignoreNodeOrder)
        {
            compare.withNodeMatcher((NodeMatcher)new DefaultNodeMatcher(new ElementSelector[] {ElementSelectors.byNameAndText}));
            compare.withDifferenceEvaluator(DifferenceEvaluators.chain(new DifferenceEvaluator[] {DifferenceEvaluators.Default,
                            DifferenceEvaluators.downgradeDifferencesToEqual(new ComparisonType[] {ComparisonType.CHILD_NODELIST_SEQUENCE})}));
        }
        return compare.build();
    }


    @Deprecated(since = "2011", forRemoval = true)
    public XMLContentAssert hasTheSameContentAs(String expectedXml) throws SAXException, IOException, XpathException
    {
        XMLAssert.assertXpathEvaluatesTo("1", "count(/extension)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//rawItems)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//canonicalItems)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//targetItems)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("2", "count(//rawItems/item)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//rawItems/item[type=\"testContainer_Product\"])", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//rawItems/item[type=\"testContainer_Title\"])", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("2", "count(//canonicalItems/item)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//canonicalItems/item[type=\"testContainer_ProductCanonical\"])", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//canonicalItems/item[type=\"testContainer_TitleCanonical\"])", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("2", "count(//targetItems/item)", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//targetItems/item[type=\"testContainer_ProductTarget\"])", (String)this.actual);
        XMLAssert.assertXpathEvaluatesTo("1", "count(//targetItems/item[type=\"testContainer_TitleTarget\"])", (String)this.actual);
        XMLAssert.assertXpathsEqual("/extension/rawItems/item[type=\"testContainer_Product\"]", (String)this.actual, "/extension/rawItems/item[type=\"testContainer_Product\"]", expectedXml);
        XMLAssert.assertXpathsEqual("/extension/rawItems/item[type=\"testContainer_Title\"]", (String)this.actual, "/extension/rawItems/item[type=\"testContainer_Title\"]", expectedXml);
        XMLAssert.assertXpathsEqual("/extension/canonicalItems/item[type=\"testContainer_ProductCanonical\"]", (String)this.actual, "/extension/canonicalItems/item[type=\"testContainer_ProductCanonical\"]", expectedXml);
        XMLAssert.assertXpathsEqual("/extension/canonicalItems/item[type=\"testContainer_TitleCanonical\"]", (String)this.actual, "/extension/canonicalItems/item[type=\"testContainer_TitleCanonical\"]", expectedXml);
        XMLAssert.assertXpathsEqual("/extension/targetItems/item[type=\"testContainer_ProductTarget\"]", (String)this.actual, "/extension/targetItems/item[type=\"testContainer_ProductTarget\"]", expectedXml);
        XMLAssert.assertXpathsEqual("/extension/targetItems/item[type=\"testContainer_TitleTarget\"]", (String)this.actual, "/extension/targetItems/item[type=\"testContainer_TitleTarget\"]", expectedXml);
        return this;
    }
}
