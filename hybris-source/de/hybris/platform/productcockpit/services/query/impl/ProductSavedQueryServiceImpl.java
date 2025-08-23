package de.hybris.platform.productcockpit.services.query.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.CockpitConfigurationService;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.CockpitSavedParameterValue;
import de.hybris.platform.cockpit.jalo.CockpitSavedQuery;
import de.hybris.platform.cockpit.model.CockpitSavedParameterValueModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.CockpitSavedSortCriterionModel;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.UIRole;
import de.hybris.platform.cockpit.services.config.impl.ListViewConfigurationPersistingStrategy;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.services.query.impl.SavedQueryServiceImpl;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ProductSavedQueryServiceImpl extends SavedQueryServiceImpl
{
    private static final Logger log = LoggerFactory.getLogger(ProductSavedQueryServiceImpl.class);
    protected CockpitConfigurationService cockpitConfigurationService;
    protected UserService userService;
    protected MediaService mediaService;
    protected LoginService loginService;
    protected UIConfigurationService uiConfigurationService;
    protected ListViewConfigurationPersistingStrategy listViewConfigurationPersistingStrategy;


    @Required
    public void setListViewConfigurationPersistingStrategy(ListViewConfigurationPersistingStrategy listViewConfigurationPersistingStrategy)
    {
        this.listViewConfigurationPersistingStrategy = listViewConfigurationPersistingStrategy;
    }


    @Required
    public void setCockpitConfigurationService(CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setLoginService(LoginService loginService)
    {
        this.loginService = loginService;
    }


    @Required
    public void setUiConfigurationService(UIConfigurationService uiConfigurationService)
    {
        this.uiConfigurationService = uiConfigurationService;
    }


    private List<ObjectType> getTypePath(ObjectTemplate template)
    {
        List<ObjectType> path = new ArrayList<>();
        path.add(template);
        if(!template.isDefaultTemplate())
        {
            path.add(template.getBaseType());
        }
        BaseType baseType = template.getBaseType();
        List<ObjectType> supertypes = TypeTools.getAllSupertypes((ObjectType)baseType);
        Collections.reverse(supertypes);
        path.addAll(supertypes);
        return path;
    }


    private CockpitUIComponentConfigurationModel getComponentConfiguration(String roleName, String templateCode, String code)
    {
        UserGroupModel userGroupModel;
        PrincipalModel principalModel = null;
        if(!StringUtils.isEmpty(roleName))
        {
            try
            {
                UserModel userModel = this.userService.getUserForUID(roleName);
            }
            catch(UnknownIdentifierException uie)
            {
                try
                {
                    userGroupModel = this.userService.getUserGroupForUID(roleName);
                }
                catch(UnknownIdentifierException uie2)
                {
                    log.error("No principal found for uid: " + roleName);
                }
            }
        }
        try
        {
            return this.cockpitConfigurationService.getComponentConfiguration((PrincipalModel)userGroupModel, templateCode, code);
        }
        catch(UnknownIdentifierException uie)
        {
            return null;
        }
    }


    protected void saveListViewConfiguration(String objectTemplateCode, CockpitSavedQueryModel cockpitSavedQuery)
    {
        DefaultSearchBrowserModel defaultSearchBrowser = (DefaultSearchBrowserModel)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        ListViewConfiguration configuration = null;
        if(defaultSearchBrowser != null && defaultSearchBrowser.getTableModel() != null)
        {
            ObjectTemplate template = this.typeService.getObjectTemplate(objectTemplateCode);
            String code = "listViewContentBrowser_" + cockpitSavedQuery.getCode();
            DefaultColumnModel defaultColumnModel = (DefaultColumnModel)defaultSearchBrowser.getTableModel().getColumnComponentModel();
            configuration = defaultColumnModel.getConfiguration();
            UserModel currentUser = this.userService.getCurrentUser();
            this.listViewConfigurationPersistingStrategy.persistComponentConfiguration((UIComponentConfiguration)configuration, currentUser, template, code);
            CockpitUIComponentConfigurationModel componentConfiguration = this.cockpitConfigurationService.getComponentConfiguration((PrincipalModel)currentUser, objectTemplateCode, code);
            componentConfiguration.setPrincipal(null);
            this.modelService.save(componentConfiguration);
        }
        else
        {
            UISavedQuery selectedSavedQuery = UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getSelectedSavedQuery();
            CockpitUIComponentConfigurationModel currentListViewConfiguration = null;
            String componentConfigurationCodeWeAreLookingFor = (selectedSavedQuery != null) ? ("listViewContentBrowser_" + selectedSavedQuery.getSavedQuery().getCode()) : "listViewContentBrowser";
            UIRole role = this.uiConfigurationService.getSessionRole();
            String roleName = (role != null) ? role.getName() : null;
            ObjectTemplate objectTemplate = this.typeService.getObjectTemplate(objectTemplateCode);
            for(ObjectType type : getTypePath(objectTemplate))
            {
                String typeCode = type.getCode();
                if(this.loginService.getCurrentSessionSettings().getUser() != null)
                {
                    String userID = this.loginService.getCurrentSessionSettings().getUser().getUid();
                    currentListViewConfiguration = getComponentConfiguration(userID, typeCode, componentConfigurationCodeWeAreLookingFor);
                }
                if(currentListViewConfiguration == null)
                {
                    currentListViewConfiguration = getComponentConfiguration(roleName, typeCode, componentConfigurationCodeWeAreLookingFor);
                }
                if(currentListViewConfiguration == null)
                {
                    currentListViewConfiguration = getComponentConfiguration(this.uiConfigurationService.getFallbackRole().getName(), typeCode, componentConfigurationCodeWeAreLookingFor);
                }
                if(currentListViewConfiguration == null)
                {
                    currentListViewConfiguration = getComponentConfiguration(null, typeCode, componentConfigurationCodeWeAreLookingFor);
                }
                if(currentListViewConfiguration != null)
                {
                    break;
                }
            }
            if(currentListViewConfiguration == null)
            {
                return;
            }
            CockpitUIComponentConfigurationModel clonedListViewConfiguration = (CockpitUIComponentConfigurationModel)this.modelService.clone(currentListViewConfiguration);
            clonedListViewConfiguration.setCode("listViewContentBrowser_" + cockpitSavedQuery.getCode());
            clonedListViewConfiguration.setPrincipal(null);
            MediaModel clonedMediaModel = (MediaModel)this.modelService.clone(currentListViewConfiguration.getMedia());
            clonedMediaModel.setCode(clonedMediaModel.getCode() + "_" + clonedMediaModel.getCode());
            this.modelService.save(clonedMediaModel);
            this.mediaService.duplicateData(currentListViewConfiguration.getMedia(), clonedMediaModel);
            this.modelService.save(clonedMediaModel);
            clonedListViewConfiguration.setMedia(clonedMediaModel);
            this.modelService.save(clonedListViewConfiguration);
            this.modelService.save(cockpitSavedQuery);
        }
    }


    protected void addSpecialParameters(CockpitSavedQuery cockpitSavedQuery, Query query)
    {
        String catVerStr = getPKsString((Collection)query
                        .getContextParameter("selectedCatalogVersions"));
        String categoriesStr = getPKsString((Collection)query
                        .getContextParameter("selectedCategories"));
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put("rawValue", catVerStr);
        attributes.put("parameterQualifier", "selectedCatalogVersions");
        CockpitSavedParameterValue pv_cata = CockpitManager.getInstance().createCockpitSavedParameterValue(attributes);
        attributes.clear();
        attributes.put("rawValue", categoriesStr);
        attributes.put("parameterQualifier", "selectedCategories");
        CockpitSavedParameterValue pv_catv = CockpitManager.getInstance().createCockpitSavedParameterValue(attributes);
        cockpitSavedQuery.addToCockpitSavedParameterValues(pv_cata);
        cockpitSavedQuery.addToCockpitSavedParameterValues(pv_catv);
    }


    public CockpitSavedQueryModel createSavedQuery(String label, Query query, UserModel user)
    {
        CockpitSavedQueryModel csq = (CockpitSavedQueryModel)this.modelService.create(CockpitSavedQueryModel.class);
        csq.setLabel(label);
        csq.setUser(user);
        csq.setSimpleText(query.getSimpleText());
        if(query.getSelectedTypes().isEmpty())
        {
            throw new IllegalArgumentException("Query has no type set");
        }
        if(query.getSelectedTypes().size() > 1)
        {
            log.warn("Query has ambigious types. Only one will be saved.");
        }
        Iterator<SearchType> iterator = query.getSelectedTypes().iterator();
        if(iterator.hasNext())
        {
            csq.setSelectedTypeCode(((SearchType)iterator.next()).getCode());
        }
        if(query.getContextParameter("objectTemplate") != null)
        {
            try
            {
                csq.setSelectedTemplateCode(((ObjectTemplate)query
                                .getContextParameter("objectTemplate")).getCode());
            }
            catch(ClassCastException cce)
            {
                log.warn("Could not save selected template.", cce);
            }
        }
        setDefaultViewMode(csq);
        this.modelService.save(csq);
        addCategoryItemSelectionParameters(csq, query);
        for(Map.Entry<PropertyDescriptor, Boolean> criterionEntry : (Iterable<Map.Entry<PropertyDescriptor, Boolean>>)query.getSortCriteria().entrySet())
        {
            CockpitSavedSortCriterionModel cssc = (CockpitSavedSortCriterionModel)this.modelService.create(CockpitSavedSortCriterionModel.class);
            cssc.setCriterionQualifier(((PropertyDescriptor)criterionEntry.getKey()).getQualifier());
            cssc.setAsc(criterionEntry.getValue());
            cssc.setCockpitSavedQuery(csq);
            this.modelService.save(cssc);
        }
        for(SearchParameterValue param : query.getParameterValues())
        {
            CockpitSavedParameterValueModel cspv = (CockpitSavedParameterValueModel)this.modelService.create(CockpitSavedParameterValueModel.class);
            cspv.setParameterQualifier(param.getParameterDescriptor().getQualifier());
            cspv.setOperatorQualifier(param.getOperator().getQualifier());
            cspv.setRawValue(wrapCollectionValues(getDecodedValues(param.getValue())));
            cspv.setCockpitSavedQuery(csq);
            this.modelService.save(cspv);
        }
        for(List<SearchParameterValue> orParameters : (Iterable<List<SearchParameterValue>>)query.getParameterOrValues())
        {
            if(orParameters.isEmpty())
            {
                continue;
            }
            CockpitSavedParameterValueModel cspv = (CockpitSavedParameterValueModel)this.modelService.create(CockpitSavedParameterValueModel.class);
            cspv.setParameterQualifier(((SearchParameterValue)orParameters.get(0)).getParameterDescriptor().getQualifier());
            cspv.setReference(Boolean.TRUE);
            cspv.setOperatorQualifier(((SearchParameterValue)orParameters.get(0)).getOperator().getQualifier());
            StringBuilder commaSeparatedPK = new StringBuilder();
            for(SearchParameterValue paramValue : orParameters)
            {
                String pkStr = tryGetPkString(paramValue.getValue());
                if(pkStr != null)
                {
                    commaSeparatedPK.append("," + pkStr);
                }
            }
            cspv.setRawValue((commaSeparatedPK.length() != 0) ? commaSeparatedPK.substring(1) : null);
            cspv.setCockpitSavedQuery(csq);
            this.modelService.save(cspv);
        }
        saveListViewConfiguration(((SearchType)query.getSelectedTypes().iterator().next()).getCode(), csq);
        return csq;
    }


    private void setDefaultViewMode(CockpitSavedQueryModel cockpitSavedQueryModel)
    {
        BrowserModel browserModel = null;
        if(UISessionUtils.getCurrentSession() != null && UISessionUtils.getCurrentSession().getCurrentPerspective() != null &&
                        UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea() != null)
        {
            browserModel = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        }
        AdvancedBrowserModel advancedBrowserModel = null;
        if(browserModel instanceof AdvancedBrowserModel)
        {
            advancedBrowserModel = (AdvancedBrowserModel)browserModel;
        }
        String viewMode = (advancedBrowserModel != null) ? advancedBrowserModel.getViewMode() : null;
        if("GRID".equals(viewMode) || "LIST".equals(viewMode))
        {
            cockpitSavedQueryModel.setDefaultViewMode(viewMode);
        }
    }


    private String tryGetPkString(Object object)
    {
        String pkStr = null;
        Object value = (object instanceof TypedObject) ? (value = ((TypedObject)object).getObject()) : object;
        if(value instanceof ItemModel)
        {
            pkStr = ((ItemModel)value).getPk().toString();
        }
        else if(value instanceof Item)
        {
            pkStr = ((Item)value).getPK().toString();
        }
        return pkStr;
    }


    @Deprecated
    protected void addCategoryItemSelectionParameters(CockpitSavedQuery cockpitSavedQuery, Query query)
    {
        String catVerStr = getPKsString((Collection)query
                        .getContextParameter("selectedCatalogVersions"));
        String categoriesStr = getPKsString((Collection)query
                        .getContextParameter("selectedCategories"));
        Map<Object, Object> attributes = new HashMap<>();
        attributes.put("rawValue", catVerStr);
        attributes.put("parameterQualifier", "selectedCatalogVersions");
        CockpitSavedParameterValue pv_cata = CockpitManager.getInstance().createCockpitSavedParameterValue(attributes);
        attributes.clear();
        attributes.put("rawValue", categoriesStr);
        attributes.put("parameterQualifier", "selectedCategories");
        CockpitSavedParameterValue pv_catv = CockpitManager.getInstance().createCockpitSavedParameterValue(attributes);
        cockpitSavedQuery.addToCockpitSavedParameterValues(pv_cata);
        cockpitSavedQuery.addToCockpitSavedParameterValues(pv_catv);
    }


    protected void addCategoryItemSelectionParameters(CockpitSavedQueryModel cockpitSavedQuery, Query query)
    {
        String catVerStr = getPKsString((Collection)query
                        .getContextParameter("selectedCatalogVersions"));
        String categoriesStr = getPKsString((Collection)query
                        .getContextParameter("selectedCategories"));
        CockpitSavedParameterValueModel pv_cata = (CockpitSavedParameterValueModel)this.modelService.create(CockpitSavedParameterValueModel.class);
        pv_cata.setRawValue(catVerStr);
        pv_cata.setParameterQualifier("selectedCatalogVersions");
        pv_cata.setCockpitSavedQuery(cockpitSavedQuery);
        pv_cata.setOperatorQualifier(Operator.IN.getQualifier());
        this.modelService.save(pv_cata);
        CockpitSavedParameterValueModel pv_catv = (CockpitSavedParameterValueModel)this.modelService.create(CockpitSavedParameterValueModel.class);
        pv_catv.setRawValue(categoriesStr);
        pv_catv.setParameterQualifier("selectedCategories");
        pv_catv.setCockpitSavedQuery(cockpitSavedQuery);
        pv_catv.setOperatorQualifier(Operator.IN.getQualifier());
        this.modelService.save(pv_catv);
    }


    public Query getQuery(CockpitSavedQueryModel savedQuery)
    {
        if(savedQuery.getSelectedTypeCode() == null)
        {
            throw new IllegalArgumentException("Saved query has no type code set.");
        }
        SearchType type = UISessionUtils.getCurrentSession().getSearchService().getSearchType(savedQuery.getSelectedTypeCode());
        Query ret = new Query(Collections.singletonList(type), savedQuery.getSimpleText(), 0, 0);
        List<SearchParameterValue> parameterValues = new ArrayList<>();
        String templateCode = savedQuery.getSelectedTemplateCode();
        if(templateCode != null)
        {
            ret.setContextParameter("objectTemplate",
                            UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(templateCode));
        }
        for(CockpitSavedParameterValueModel pv : savedQuery.getCockpitSavedParameterValues())
        {
            SearchParameterValue spv;
            if(!isEmptyRawValueValid(pv) && (pv.getRawValue() == null || pv.getRawValue().isEmpty()))
            {
                if(log.isDebugEnabled())
                {
                    log.warn("Invalid SavedSearchParameterValue: " + pv.getParameterQualifier());
                }
                continue;
            }
            if(pv.getParameterQualifier().equals("selectedCategories"))
            {
                Set<CategoryModel> cat = new HashSet<>();
                for(String s : pv.getRawValue().split(","))
                {
                    cat.add((CategoryModel)this.modelService.get(PK.parse(s)));
                }
                ret.setContextParameter("selectedCategories", cat);
                continue;
            }
            if(pv.getParameterQualifier().equals("selectedCatalogVersions"))
            {
                Set<CatalogVersionModel> cat = new HashSet<>();
                if(pv.getRawValue() != null)
                {
                    for(String s : pv.getRawValue().split(","))
                    {
                        cat.add((CatalogVersionModel)this.modelService.get(PK.parse(s)));
                    }
                }
                ret.setContextParameter("selectedCatalogVersions", cat);
                continue;
            }
            if(pv.getReference().booleanValue())
            {
                List<SearchParameterValue> orValues = new ArrayList<>();
                SearchParameterDescriptor searchParameterDescriptor = this.searchService.getSearchDescriptor(getTypeService().getPropertyDescriptor(pv.getParameterQualifier()));
                Operator operator1 = new Operator(pv.getOperatorQualifier());
                if(pv.getRawValue() != null)
                {
                    for(String s : pv.getRawValue().split(","))
                    {
                        Item objectValue = JaloSession.getCurrentSession().getItem(PK.parse(s));
                        SearchParameterValue searchParameterValue = new SearchParameterValue(searchParameterDescriptor, objectValue, operator1);
                        orValues.add(searchParameterValue);
                    }
                }
                ret.addParameterOrValues(orValues);
                continue;
            }
            SearchParameterDescriptor descriptor = this.searchService.getSearchDescriptor(getTypeService().getPropertyDescriptor(pv.getParameterQualifier()));
            Operator operator = new Operator(pv.getOperatorQualifier());
            if(descriptor instanceof de.hybris.platform.cockpit.services.search.impl.ClassAttributeSearchDescriptor)
            {
                Object unwrappedValue = unwrapSingleValue(decode(pv.getRawValue()));
                spv = (unwrappedValue != null) ? new SearchParameterValue(descriptor, unwrappedValue, operator) : null;
            }
            else
            {
                spv = new SearchParameterValue(descriptor, wrap(pv.getRawValue(), operator), operator);
            }
            if(spv != null)
            {
                parameterValues.add(spv);
            }
        }
        ret.setParameterValues(parameterValues);
        for(CockpitSavedSortCriterionModel sortCriterion : savedQuery.getCockpitSavedSortCriteria())
        {
            PropertyDescriptor sortProperty = getTypeService().getPropertyDescriptor(sortCriterion.getCriterionQualifier());
            ret.addSortCriterion(sortProperty, sortCriterion.getAsc().booleanValue());
        }
        return ret;
    }


    protected boolean isEmptyRawValueValid(CockpitSavedParameterValueModel queryParameterValue)
    {
        return queryParameterValue.getOperatorQualifier().equals("isEmpty");
    }
}
