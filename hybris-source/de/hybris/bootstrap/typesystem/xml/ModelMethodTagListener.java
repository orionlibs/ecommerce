package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.StringValueTagListener;
import de.hybris.bootstrap.xml.TagListener;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;

public class ModelMethodTagListener extends AbstractTypeSystemTagListener
{
    private final ModelTagListener parser;


    public ModelMethodTagListener(ModelTagListener parser, String tagname)
    {
        super((AbstractTypeSystemTagListener)parser, tagname);
        if(parser == null)
        {
            throw new IllegalArgumentException("parent parser was null");
        }
        this.parser = parser;
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String location = this.parser.getParser().getCurrentXMLLocation();
        String name = getAttribute("name");
        if(name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Name attribute for model getter/setter is mandatory! See file: " + location);
        }
        ModelTagListener.ModelDataMethod method = new ModelTagListener.ModelDataMethod();
        method.name = name;
        method.deprecated = isMethodDeprecated();
        method.deprecatedSince = getAttribute("deprecatedSince");
        method.defaultAttribute = getBooleanAttribute("default", false);
        method.nullDecorator = (String)getSubTagValue("nullDecorator");
        if("getter".equals(getTagName()))
        {
            this.parser.getModelData().addGetter(method);
        }
        else
        {
            this.parser.getModelData().addSetter(method);
        }
        return null;
    }


    private boolean isMethodDeprecated()
    {
        return (getBooleanAttribute("deprecated", false) || StringUtils.isNotBlank(
                        getAttribute("deprecatedSince")));
    }


    protected Collection<TagListener> createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new StringValueTagListener((TagListener)this, "nullDecorator")});
    }
}
