package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractWorkflowDecisionModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractWorkflowDecision";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VISUALISATIONX = "visualisationX";
    public static final String VISUALISATIONY = "visualisationY";
    public static final String QUALIFIER = "qualifier";


    public AbstractWorkflowDecisionModel()
    {
    }


    public AbstractWorkflowDecisionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractWorkflowDecisionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "visualisationX", type = Accessor.Type.GETTER)
    public Integer getVisualisationX()
    {
        return (Integer)getPersistenceContext().getPropertyValue("visualisationX");
    }


    @Accessor(qualifier = "visualisationY", type = Accessor.Type.GETTER)
    public Integer getVisualisationY()
    {
        return (Integer)getPersistenceContext().getPropertyValue("visualisationY");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }


    @Accessor(qualifier = "visualisationX", type = Accessor.Type.SETTER)
    public void setVisualisationX(Integer value)
    {
        getPersistenceContext().setPropertyValue("visualisationX", value);
    }


    @Accessor(qualifier = "visualisationY", type = Accessor.Type.SETTER)
    public void setVisualisationY(Integer value)
    {
        getPersistenceContext().setPropertyValue("visualisationY", value);
    }
}
