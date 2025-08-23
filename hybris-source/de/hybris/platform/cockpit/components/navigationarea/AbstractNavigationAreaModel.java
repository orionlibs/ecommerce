package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.collection.impl.ObjectCollectionImpl;
import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIDynamicQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryUserRightsService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public abstract class AbstractNavigationAreaModel extends AbstractSectionPanelModel
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractNavigationAreaModel.class);
    private EnumerationService enumerationService;
    private AbstractUINavigationArea area = null;
    private QueryTypeSectionModel selectedQueryTypeView;
    protected List<QueryTypeSectionModel> queryTypeViews = new ArrayList<>();
    private final List<UISavedQuery> savedQueries = new ArrayList<>();
    private final List<UIDynamicQuery> dynamicQueries = new ArrayList<>();
    private final List<UICollectionQuery> collections = new ArrayList<>();
    private final List<UICollectionQuery> specialCollections = new ArrayList<>();
    private final List<String> specialCollectionTypes = new ArrayList<>();


    public AbstractNavigationAreaModel()
    {
        setDefaultQueryType();
        setAvailableQueryType();
    }


    public AbstractNavigationAreaModel(AbstractUINavigationArea area)
    {
        this.area = area;
    }


    protected void setDefaultQueryType()
    {
        this.selectedQueryTypeView = (QueryTypeSectionModel)new NotSharedQuerySectionModel();
    }


    protected void setAvailableQueryType()
    {
        this.queryTypeViews.add(new NotSharedQuerySectionModel());
        this.queryTypeViews.add(new SharedQuerySectionModel());
    }


    public void setNavigationArea(AbstractUINavigationArea area)
    {
        if(area != null && area.getSectionModel() != this)
        {
            area.setSectionModel((SectionPanelModel)this);
        }
        this.area = area;
    }


    public AbstractUINavigationArea getNavigationArea()
    {
        return this.area;
    }


    public QueryTypeSectionModel getSelectedQueryType()
    {
        return this.selectedQueryTypeView;
    }


    public void setSelectedQueryType(QueryTypeSectionModel selectedQueryTypeView)
    {
        this.selectedQueryTypeView = selectedQueryTypeView;
    }


    public List<QueryTypeSectionModel> getAvailableViewModes()
    {
        return this.queryTypeViews;
    }


    public void hideSection(Section section)
    {
        if(section.isVisible())
        {
            ((NavigationPanelSection)section).setVisible(false);
            fireSectionHide(section);
        }
    }


    public void showSection(Section section)
    {
        if(!section.isVisible())
        {
            ((NavigationPanelSection)section).setVisible(true);
            fireSectionShow(section);
        }
    }


    protected void refreshCollections()
    {
        PK toBeRenamedPK = null;
        for(UICollectionQuery cq : this.collections)
        {
            if(cq.isInitial())
            {
                toBeRenamedPK = cq.getObjectCollection().getPK();
                break;
            }
        }
        this.collections.clear();
        this.collections.addAll(getCollectionsFromJalo());
        if(toBeRenamedPK != null)
        {
            for(UICollectionQuery cq : this.collections)
            {
                if(cq.getObjectCollection().getPK().equals(toBeRenamedPK))
                {
                    cq.setInitial(true);
                }
            }
        }
    }


    protected void refreshSavedQueries()
    {
        this.savedQueries.clear();
        if(this.selectedQueryTypeView.getQueryTypeId().equals("notshared"))
        {
            this.savedQueries.addAll(getNotSharedQueriesFromJalo());
        }
        else
        {
            this.savedQueries.addAll(getSharedQueriesFromJalo());
        }
    }


    protected List<UICollectionQuery> getCollectionsFromJalo()
    {
        List<UICollectionQuery> ret = new ArrayList<>();
        List<ObjectCollection> objectCollectionList = getNavigationArea().getObjectCollectionService().getCollectionsForUser(
                        UISessionUtils.getCurrentSession().getUser());
        for(ObjectCollection objectCollection : objectCollectionList)
        {
            ret.add(new UICollectionQuery(objectCollection));
        }
        return ret;
    }


    protected List<UISavedQuery> getNotSharedQueriesFromJalo()
    {
        List<UISavedQuery> ret = new ArrayList<>();
        Collection<CockpitSavedQueryModel> savedQueryModelCollection = getSavedQueryService().getNotSharedQueries(null,
                        UISessionUtils.getCurrentSession().getUser());
        for(CockpitSavedQueryModel savedQuery : savedQueryModelCollection)
        {
            ret.add(new UISavedQuery(savedQuery));
        }
        return ret;
    }


    protected List<UISavedQuery> getSharedQueriesFromJalo()
    {
        List<UISavedQuery> ret = new ArrayList<>();
        Collection<CockpitSavedQueryModel> savedQueryModelCollection = getSavedQueryService().getSharedQueries(null,
                        UISessionUtils.getCurrentSession().getUser());
        for(CockpitSavedQueryModel savedQuery : savedQueryModelCollection)
        {
            ret.add(new UISavedQuery(savedQuery));
        }
        return ret;
    }


    protected List<UIDynamicQuery> getDynamicQueryFromJalo()
    {
        List<UIDynamicQuery> ret = new ArrayList<>();
        List<DynamicQuery> allDynamicQuery = getNavigationArea().getDynamicQueryService().getAllDynamicQuery();
        for(DynamicQuery dynamicQuery : allDynamicQuery)
        {
            ret.add(new UIDynamicQuery(dynamicQuery));
        }
        return ret;
    }


    protected void refreshDynamicQueries()
    {
        this.dynamicQueries.clear();
        this.dynamicQueries.addAll(getDynamicQueryFromJalo());
    }


    protected void refreshSpecialCollections()
    {
        List<UICollectionQuery> colls = (this.specialCollections == null) ? Collections.EMPTY_LIST : new ArrayList<>(this.specialCollections);
        for(UICollectionQuery cq : colls)
        {
            getNavigationArea().getObjectCollectionService().renameCollection(cq.getObjectCollection(),
                            Labels.getLabel("specialcollection." + cq.getObjectCollection().getQualifier() + ".name"));
        }
        this.specialCollections.clear();
        this.specialCollections.addAll(getSpecialCollectionsFromJalo());
    }


    protected List<UICollectionQuery> getSpecialCollectionsFromJalo()
    {
        List<UICollectionQuery> ret = new ArrayList<>();
        List<ObjectCollection> objectCollectionList = getNavigationArea().getObjectCollectionService().getSpecialCollectionsForUser(
                        UISessionUtils.getCurrentSession().getUser());
        for(ObjectCollection objectCollection : objectCollectionList)
        {
            ret.add(new UICollectionQuery(objectCollection));
        }
        return ret;
    }


    public void addSavedQuery(String label, CockpitSavedQueryModel query)
    {
        this.savedQueries.add(new UISavedQuery(query));
        update();
    }


    public UICollectionQuery addNewCollection()
    {
        CockpitObjectCollectionModel collectionModel = (CockpitObjectCollectionModel)getModelService().create(CockpitObjectCollectionModel.class);
        collectionModel.setUser(UISessionUtils.getCurrentSession().getUser());
        getModelService().save(collectionModel);
        ObjectCollectionImpl objectCollectionImpl = new ObjectCollectionImpl((CockpitObjectAbstractCollectionModel)collectionModel);
        getNavigationArea().getObjectCollectionService().renameCollection((ObjectCollection)objectCollectionImpl, Labels.getLabel("collection.unnamed"));
        UICollectionQuery ret = new UICollectionQuery((ObjectCollection)objectCollectionImpl);
        this.collections.add(ret);
        ret.setInitial(true);
        update();
        return ret;
    }


    @Deprecated
    protected void addNewSpecialCollectionsStandard(String qualifier, EnumerationValue collectionType)
    {
        ObjectCollection objectCollection = getNavigationArea().getObjectCollectionService().createSpecialCollection(qualifier,
                        UISessionUtils.getCurrentSession().getUser(), collectionType);
        getNavigationArea().getObjectCollectionService().renameCollection(objectCollection,
                        Labels.getLabel("specialcollection." + qualifier + ".name"));
        UICollectionQuery ret = new UICollectionQuery(objectCollection);
        this.specialCollections.add(ret);
    }


    protected void addNewSpecialCollection(String qualifier, HybrisEnumValue collectionType)
    {
        ObjectCollection collection = getNavigationArea().getObjectCollectionService().createSpecialCollection(qualifier,
                        UISessionUtils.getCurrentSession().getUser(), collectionType);
        getNavigationArea().getObjectCollectionService().renameCollection(collection,
                        Labels.getLabel("specialcollection." + qualifier + ".name"));
        UICollectionQuery ret = new UICollectionQuery(collection);
        this.specialCollections.add(ret);
    }


    public void removeQuery(UIQuery query)
    {
        if(this.collections.contains(query))
        {
            this.collections.remove(query);
        }
        else if(this.savedQueries.contains(query))
        {
            this.savedQueries.remove(query);
        }
    }


    public List<UISavedQuery> getSavedQueries()
    {
        refreshSavedQueries();
        return new ArrayList<>(this.savedQueries);
    }


    public List<UIDynamicQuery> getDynamicQueries()
    {
        return new ArrayList<>(this.dynamicQueries);
    }


    public List<UICollectionQuery> getCollections()
    {
        refreshCollections();
        return new ArrayList<>(this.collections);
    }


    public List<UICollectionQuery> getSpecialCollections()
    {
        refreshSpecialCollections();
        return new ArrayList<>(this.specialCollections);
    }


    protected void addAllSpecialCollectionsStandard()
    {
        List<String> collectionTypes = getSpecialCollectionTypes();
        for(String collCode : collectionTypes)
        {
            boolean exists = false;
            for(UICollectionQuery collQuery : this.specialCollections)
            {
                if(collCode.equals(collQuery.getObjectCollection().getQualifier()))
                {
                    exists = true;
                    break;
                }
            }
            if(!exists)
            {
                try
                {
                    addNewSpecialCollection(collCode,
                                    getEnumerationService().getEnumerationValue(GeneratedCockpitConstants.TC.COCKPITSPECIALCOLLECTIONTYPE, collCode));
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Dynamically created special collection '" + collCode + "' added.");
                    }
                }
                catch(Exception e)
                {
                    LOG.warn("An error occurred while creating special collection with code '" + collCode + "'. Entry skipped!", e);
                }
            }
        }
    }


    public UICollectionQuery getCollectionQuery(ObjectCollection coll)
    {
        UICollectionQuery ret = null;
        for(UICollectionQuery cq : getCollections())
        {
            if(cq.getObjectCollection().equals(coll))
            {
                ret = cq;
                break;
            }
        }
        return ret;
    }


    public abstract void initialize();


    public abstract void update();


    public List<String> getSpecialCollectionTypes()
    {
        return Collections.unmodifiableList(this.specialCollectionTypes);
    }


    public void setSpecialCollectionTypes(List<String> collTypes)
    {
        this.specialCollectionTypes.clear();
        if(collTypes != null && !collTypes.isEmpty())
        {
            this.specialCollectionTypes.addAll(collTypes);
        }
    }


    private EnumerationService getEnumerationService()
    {
        if(this.enumerationService == null)
        {
            this.enumerationService = (EnumerationService)SpringUtil.getBean("enumerationService");
        }
        return this.enumerationService;
    }


    private ModelService getModelService()
    {
        return UISessionUtils.getCurrentSession().getModelService();
    }


    private SavedQueryUserRightsService getSavedQueryService()
    {
        SavedQueryService savedQueryService = getNavigationArea().getSavedQueryService();
        if(savedQueryService instanceof SavedQueryUserRightsService)
        {
            return (SavedQueryUserRightsService)savedQueryService;
        }
        return (SavedQueryUserRightsService)new Object(this, savedQueryService);
    }
}
