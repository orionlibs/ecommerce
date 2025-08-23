package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class WorkflowTemplateModel extends JobModel
{
    public static final String _TYPECODE = "WorkflowTemplate";
    public static final String _WORKFLOWTEMPLATEFORCATALOGVERSION = "WorkflowTemplateForCatalogVersion";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ACTIVATIONSCRIPT = "activationScript";
    public static final String ACTIONS = "actions";
    public static final String VISIBLEFORPRINCIPALS = "visibleForPrincipals";
    public static final String CATALOGVERSIONS = "catalogVersions";


    public WorkflowTemplateModel()
    {
    }


    public WorkflowTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowTemplateModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowTemplateModel(String _code, Integer _nodeID, UserModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner((ItemModel)_owner);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public List<WorkflowActionTemplateModel> getActions()
    {
        return (List<WorkflowActionTemplateModel>)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "activationScript", type = Accessor.Type.GETTER)
    public String getActivationScript()
    {
        return (String)getPersistenceContext().getPropertyValue("activationScript");
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
    public Collection<CatalogVersionModel> getCatalogVersions()
    {
        return (Collection<CatalogVersionModel>)getPersistenceContext().getPropertyValue("catalogVersions");
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


    @Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
    public UserModel getOwner()
    {
        return (UserModel)super.getOwner();
    }


    @Accessor(qualifier = "visibleForPrincipals", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getVisibleForPrincipals()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getPropertyValue("visibleForPrincipals");
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(List<WorkflowActionTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "activationScript", type = Accessor.Type.SETTER)
    public void setActivationScript(String value)
    {
        getPersistenceContext().setPropertyValue("activationScript", value);
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
    public void setCatalogVersions(Collection<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogVersions", value);
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


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        if(value == null || value instanceof UserModel)
        {
            super.setOwner(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.user.UserModel");
        }
    }


    @Accessor(qualifier = "visibleForPrincipals", type = Accessor.Type.SETTER)
    public void setVisibleForPrincipals(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("visibleForPrincipals", value);
    }
}
