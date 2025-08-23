package de.hybris.platform.deeplink.model.rules;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DeeplinkUrlRuleModel extends ItemModel
{
    public static final String _TYPECODE = "DeeplinkUrlRule";
    public static final String BASEURLPATTERN = "baseUrlPattern";
    public static final String DESTURLTEMPLATE = "destUrlTemplate";
    public static final String APPLICABLETYPE = "applicableType";
    public static final String USEFORWARD = "useForward";
    public static final String PRIORITY = "priority";


    public DeeplinkUrlRuleModel()
    {
    }


    public DeeplinkUrlRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DeeplinkUrlRuleModel(ComposedTypeModel _applicableType, String _baseUrlPattern, String _destUrlTemplate, Integer _priority)
    {
        setApplicableType(_applicableType);
        setBaseUrlPattern(_baseUrlPattern);
        setDestUrlTemplate(_destUrlTemplate);
        setPriority(_priority);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DeeplinkUrlRuleModel(ComposedTypeModel _applicableType, String _baseUrlPattern, String _destUrlTemplate, ItemModel _owner, Integer _priority)
    {
        setApplicableType(_applicableType);
        setBaseUrlPattern(_baseUrlPattern);
        setDestUrlTemplate(_destUrlTemplate);
        setOwner(_owner);
        setPriority(_priority);
    }


    @Accessor(qualifier = "applicableType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getApplicableType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("applicableType");
    }


    @Accessor(qualifier = "baseUrlPattern", type = Accessor.Type.GETTER)
    public String getBaseUrlPattern()
    {
        return (String)getPersistenceContext().getPropertyValue("baseUrlPattern");
    }


    @Accessor(qualifier = "destUrlTemplate", type = Accessor.Type.GETTER)
    public String getDestUrlTemplate()
    {
        return (String)getPersistenceContext().getPropertyValue("destUrlTemplate");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "useForward", type = Accessor.Type.GETTER)
    public Boolean getUseForward()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForward");
    }


    @Accessor(qualifier = "applicableType", type = Accessor.Type.SETTER)
    public void setApplicableType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("applicableType", value);
    }


    @Accessor(qualifier = "baseUrlPattern", type = Accessor.Type.SETTER)
    public void setBaseUrlPattern(String value)
    {
        getPersistenceContext().setPropertyValue("baseUrlPattern", value);
    }


    @Accessor(qualifier = "destUrlTemplate", type = Accessor.Type.SETTER)
    public void setDestUrlTemplate(String value)
    {
        getPersistenceContext().setPropertyValue("destUrlTemplate", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "useForward", type = Accessor.Type.SETTER)
    public void setUseForward(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForward", value);
    }
}
