package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.ArrayList;
import java.util.List;

public class ModelConstructorTagListener extends AbstractTypeSystemTagListener
{
    private final ModelTagListener parser;


    public ModelConstructorTagListener(ModelTagListener parser, String tagname)
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
        String content = getAttribute("signature");
        if(content == null || content.isEmpty())
        {
            throw new IllegalArgumentException("Found empty <constructor signature=... tag which is not allowed! See file: " + location);
        }
        String[] tupel = content.split(",");
        List<String> qualifiers = new ArrayList<>();
        for(int index = 0; index < tupel.length; index++)
        {
            String qualifier = tupel[index].trim().toLowerCase(LocaleHelper.getPersistenceLocale());
            if(qualifier == null || qualifier.isEmpty())
            {
                throw new IllegalArgumentException("Qualifier in <constructor signature=... tag is empty! See file: " + location);
            }
            if(qualifiers.contains(qualifier))
            {
                throw new IllegalArgumentException("Found duplicate qualifier in <constructor signature=... tag! See file: " + location);
            }
            qualifiers.add(qualifier);
        }
        this.parser.getModelData().addConstructor(new ModelTagListener.ModelDataConstructor(location, qualifiers, false));
        return null;
    }
}
