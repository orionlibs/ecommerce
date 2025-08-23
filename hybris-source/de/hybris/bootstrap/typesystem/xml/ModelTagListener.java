package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import java.util.Arrays;
import java.util.Collection;

public class ModelTagListener extends AbstractTypeSystemTagListener
{
    private ModelData result;


    public ModelTagListener(AbstractTypeSystemTagListener parent, String tagname)
    {
        super(parent, tagname);
    }


    protected final Collection<TagListener> createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new ModelConstructorTagListener(this, "constructor"), (TagListener)new ModelMethodTagListener(this, "getter"), (TagListener)new ModelMethodTagListener(this, "setter")});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String parsedGenerateValue = getAttribute("generate");
        if(parsedGenerateValue != null)
        {
            (getModelData()).generate = Boolean.parseBoolean(parsedGenerateValue);
        }
        ModelData myResult = this.result;
        this.result = null;
        return myResult;
    }


    protected ModelData getModelData()
    {
        if(this.result == null)
        {
            this.result = new ModelData();
        }
        return this.result;
    }
}
