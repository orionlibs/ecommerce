package de.hybris.platform.core.model.security;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PrincipalModel extends ItemModel
{
    public static final String _TYPECODE = "Principal";
    public static final String _CATEGORY2PRINCIPALRELATION = "Category2PrincipalRelation";
    public static final String _SYNCITEMJOB2PRINCIPAL = "SyncItemJob2Principal";
    public static final String _WORKFLOWTEMPLATE2PRINCIPALRELATION = "WorkflowTemplate2PrincipalRelation";
    public static final String _COMMENTWATCHERRELATION = "CommentWatcherRelation";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String DISPLAYNAME = "displayName";
    public static final String UID = "uid";
    public static final String ALLSEARCHRESTRICTIONS = "allSearchRestrictions";
    public static final String ALLGROUPS = "allGroups";
    public static final String GROUPS = "groups";
    public static final String SEARCHRESTRICTIONS = "searchRestrictions";
    public static final String ACCESSIBLECATEGORIES = "accessibleCategories";
    public static final String WRITABLECATALOGVERSIONS = "writableCatalogVersions";
    public static final String READABLECATALOGVERSIONS = "readableCatalogVersions";
    public static final String SYNCJOBS = "syncJobs";
    public static final String VISIBLETEMPLATES = "visibleTemplates";
    public static final String PROFILEPICTURE = "profilePicture";
    public static final String WATCHEDCOMMENTS = "watchedComments";
    public static final String BACKOFFICELOGINDISABLED = "backOfficeLoginDisabled";


    public PrincipalModel()
    {
    }


    public PrincipalModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PrincipalModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PrincipalModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "accessibleCategories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getAccessibleCategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getPropertyValue("accessibleCategories");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<PrincipalGroupModel> getAllgroups()
    {
        return getAllGroups();
    }


    @Accessor(qualifier = "allGroups", type = Accessor.Type.GETTER)
    public Set<PrincipalGroupModel> getAllGroups()
    {
        return (Set<PrincipalGroupModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allGroups");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Collection<SearchRestrictionModel> getAllsearchrestrictions()
    {
        return getAllSearchRestrictions();
    }


    @Accessor(qualifier = "allSearchRestrictions", type = Accessor.Type.GETTER)
    public Collection<SearchRestrictionModel> getAllSearchRestrictions()
    {
        return (Collection<SearchRestrictionModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allSearchRestrictions");
    }


    @Accessor(qualifier = "backOfficeLoginDisabled", type = Accessor.Type.GETTER)
    public Boolean getBackOfficeLoginDisabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("backOfficeLoginDisabled");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
    public String getDisplayName()
    {
        return getDisplayName(null);
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
    public String getDisplayName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedDynamicValue((AbstractItemModel)this, "displayName", loc);
    }


    @Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
    public Set<PrincipalGroupModel> getGroups()
    {
        return (Set<PrincipalGroupModel>)getPersistenceContext().getPropertyValue("groups");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "profilePicture", type = Accessor.Type.GETTER)
    public MediaModel getProfilePicture()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("profilePicture");
    }


    @Accessor(qualifier = "readableCatalogVersions", type = Accessor.Type.GETTER)
    public List<CatalogVersionModel> getReadableCatalogVersions()
    {
        return (List<CatalogVersionModel>)getPersistenceContext().getPropertyValue("readableCatalogVersions");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Collection<SearchRestrictionModel> getSearchrestrictions()
    {
        return getSearchRestrictions();
    }


    @Accessor(qualifier = "searchRestrictions", type = Accessor.Type.GETTER)
    public Collection<SearchRestrictionModel> getSearchRestrictions()
    {
        return (Collection<SearchRestrictionModel>)getPersistenceContext().getPropertyValue("searchRestrictions");
    }


    @Accessor(qualifier = "syncJobs", type = Accessor.Type.GETTER)
    public Collection<SyncItemJobModel> getSyncJobs()
    {
        return (Collection<SyncItemJobModel>)getPersistenceContext().getPropertyValue("syncJobs");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "visibleTemplates", type = Accessor.Type.GETTER)
    public Collection<WorkflowTemplateModel> getVisibleTemplates()
    {
        return (Collection<WorkflowTemplateModel>)getPersistenceContext().getPropertyValue("visibleTemplates");
    }


    @Accessor(qualifier = "watchedComments", type = Accessor.Type.GETTER)
    public List<CommentModel> getWatchedComments()
    {
        return (List<CommentModel>)getPersistenceContext().getPropertyValue("watchedComments");
    }


    @Accessor(qualifier = "writableCatalogVersions", type = Accessor.Type.GETTER)
    public List<CatalogVersionModel> getWritableCatalogVersions()
    {
        return (List<CatalogVersionModel>)getPersistenceContext().getPropertyValue("writableCatalogVersions");
    }


    @Accessor(qualifier = "backOfficeLoginDisabled", type = Accessor.Type.SETTER)
    public void setBackOfficeLoginDisabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("backOfficeLoginDisabled", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
    public void setGroups(Set<PrincipalGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("groups", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "profilePicture", type = Accessor.Type.SETTER)
    public void setProfilePicture(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("profilePicture", value);
    }


    @Accessor(qualifier = "readableCatalogVersions", type = Accessor.Type.SETTER)
    public void setReadableCatalogVersions(List<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("readableCatalogVersions", value);
    }


    @Accessor(qualifier = "syncJobs", type = Accessor.Type.SETTER)
    public void setSyncJobs(Collection<SyncItemJobModel> value)
    {
        getPersistenceContext().setPropertyValue("syncJobs", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }


    @Accessor(qualifier = "visibleTemplates", type = Accessor.Type.SETTER)
    public void setVisibleTemplates(Collection<WorkflowTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("visibleTemplates", value);
    }


    @Accessor(qualifier = "watchedComments", type = Accessor.Type.SETTER)
    public void setWatchedComments(List<CommentModel> value)
    {
        getPersistenceContext().setPropertyValue("watchedComments", value);
    }


    @Accessor(qualifier = "writableCatalogVersions", type = Accessor.Type.SETTER)
    public void setWritableCatalogVersions(List<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("writableCatalogVersions", value);
    }
}
