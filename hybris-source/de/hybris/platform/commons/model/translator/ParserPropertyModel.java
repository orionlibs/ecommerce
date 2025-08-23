package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ParserPropertyModel extends ItemModel
{
    public static final String _TYPECODE = "ParserProperty";
    public static final String _TRANSLATORCONFIG2PARSERPROPERTIES = "TranslatorConfig2ParserProperties";
    public static final String NAME = "name";
    public static final String STARTEXP = "startExp";
    public static final String ENDEXP = "endExp";
    public static final String PARSERCLASS = "parserClass";
    public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
    public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";


    public ParserPropertyModel()
    {
    }


    public ParserPropertyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ParserPropertyModel(String _name, String _startExp)
    {
        setName(_name);
        setStartExp(_startExp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ParserPropertyModel(String _name, ItemModel _owner, String _startExp)
    {
        setName(_name);
        setOwner(_owner);
        setStartExp(_startExp);
    }


    @Accessor(qualifier = "endExp", type = Accessor.Type.GETTER)
    public String getEndExp()
    {
        return (String)getPersistenceContext().getPropertyValue("endExp");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "parserClass", type = Accessor.Type.GETTER)
    public String getParserClass()
    {
        return (String)getPersistenceContext().getPropertyValue("parserClass");
    }


    @Accessor(qualifier = "startExp", type = Accessor.Type.GETTER)
    public String getStartExp()
    {
        return (String)getPersistenceContext().getPropertyValue("startExp");
    }


    @Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
    public JaloTranslatorConfigurationModel getTranslatorConfiguration()
    {
        return (JaloTranslatorConfigurationModel)getPersistenceContext().getPropertyValue("translatorConfiguration");
    }


    @Accessor(qualifier = "endExp", type = Accessor.Type.SETTER)
    public void setEndExp(String value)
    {
        getPersistenceContext().setPropertyValue("endExp", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "parserClass", type = Accessor.Type.SETTER)
    public void setParserClass(String value)
    {
        getPersistenceContext().setPropertyValue("parserClass", value);
    }


    @Accessor(qualifier = "startExp", type = Accessor.Type.SETTER)
    public void setStartExp(String value)
    {
        getPersistenceContext().setPropertyValue("startExp", value);
    }


    @Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
    public void setTranslatorConfiguration(JaloTranslatorConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("translatorConfiguration", value);
    }
}
