package de.hybris.platform.cms2.model.contents;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class ContentSlotNameModel extends ItemModel
{
    public static final String _TYPECODE = "ContentSlotName";
    public static final String _AVAILABLESLOTSFORTEMPLATE = "AvailableSlotsForTemplate";
    public static final String NAME = "name";
    public static final String COMPTYPEGROUP = "compTypeGroup";
    public static final String TEMPLATEPOS = "templatePOS";
    public static final String TEMPLATE = "template";
    public static final String VALIDCOMPONENTTYPES = "validComponentTypes";


    public ContentSlotNameModel()
    {
    }


    public ContentSlotNameModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotNameModel(String _name, PageTemplateModel _template)
    {
        setName(_name);
        setTemplate(_template);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotNameModel(String _name, ItemModel _owner, PageTemplateModel _template)
    {
        setName(_name);
        setOwner(_owner);
        setTemplate(_template);
    }


    @Accessor(qualifier = "compTypeGroup", type = Accessor.Type.GETTER)
    public ComponentTypeGroupModel getCompTypeGroup()
    {
        return (ComponentTypeGroupModel)getPersistenceContext().getPropertyValue("compTypeGroup");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "template", type = Accessor.Type.GETTER)
    public PageTemplateModel getTemplate()
    {
        return (PageTemplateModel)getPersistenceContext().getPropertyValue("template");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    @Accessor(qualifier = "validComponentTypes", type = Accessor.Type.GETTER)
    public Set<CMSComponentTypeModel> getValidComponentTypes()
    {
        return (Set<CMSComponentTypeModel>)getPersistenceContext().getPropertyValue("validComponentTypes");
    }


    @Accessor(qualifier = "compTypeGroup", type = Accessor.Type.SETTER)
    public void setCompTypeGroup(ComponentTypeGroupModel value)
    {
        getPersistenceContext().setPropertyValue("compTypeGroup", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "template", type = Accessor.Type.SETTER)
    public void setTemplate(PageTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("template", value);
    }


    @Deprecated(since = "5.0", forRemoval = true)
    @Accessor(qualifier = "validComponentTypes", type = Accessor.Type.SETTER)
    public void setValidComponentTypes(Set<CMSComponentTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("validComponentTypes", value);
    }
}
