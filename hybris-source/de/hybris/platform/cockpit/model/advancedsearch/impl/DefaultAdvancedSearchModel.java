package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;
import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.editor.impl.DefaultTextUIEditor;
import de.hybris.platform.cockpit.model.editor.search.impl.AbstractConditionUIEditor;
import de.hybris.platform.cockpit.model.meta.DefaultPropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.DefaultSearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.ValuePair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultAdvancedSearchModel extends AbstractAdvancedSearchModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAdvancedSearchModel.class);
    private final Map<SearchField, SearchFieldConfiguration> searchFieldMap = new HashMap<>();
    private final Map<SearchFieldGroup, SearchFieldGroupConfiguration> searchFieldGroupMap = new HashMap<>();
    private boolean sortAscending = false;
    private PropertyDescriptor sortedByProperty = null;
    private final List<SearchField> visibleFields = new ArrayList<>();
    private final List<PropertyDescriptor> sortProperties = new ArrayList<>();
    private AdvancedSearchParameterContainer parameterContainer = (AdvancedSearchParameterContainer)new DefaultParameterContainer();
    private ObjectTemplate rootType = null;
    private ObjectTemplate selectedType = null;
    private AdvancedSearchConfiguration selectedConfig = null;
    private final List<ObjectTemplate> types = new ArrayList<>();
    private DefaultSearchFieldGroup rootGroup = null;
    private TypeService typeService = null;
    private EditorFactory editorFactory = null;
    private final AdvancedSearchConfiguration config;
    private final String configCode;
    private boolean excludeRootType;
    private boolean includeSubTypes;
    private boolean includeSubTypesForRelatedTypes;
    private UIConfigurationService uiConfigurationService = null;


    public DefaultAdvancedSearchModel(AdvancedSearchConfiguration config, String configCode)
    {
        this(config, configCode, null);
    }


    public DefaultAdvancedSearchModel(AdvancedSearchConfiguration config, String configCode, AdvancedSearchParameterContainer parameterContainer)
    {
        if(config == null)
        {
            throw new IllegalArgumentException("Advanced search configuration can not be null.");
        }
        if(configCode == null)
        {
            throw new IllegalArgumentException("Configuration code can not be null.");
        }
        this.config = config;
        this.configCode = configCode;
        if(parameterContainer != null)
        {
            this.parameterContainer = parameterContainer;
        }
        loadConfiguration();
        addAdvancedSearchModelListener((AdvancedSearchModelListener)new Object(this));
    }


    public void resetToInitialSearchParameters()
    {
        if(!isExcludeRootType())
        {
            this.selectedType = this.rootType;
        }
        else if(!this.types.isEmpty())
        {
            this.selectedType = this.types.get(0);
        }
        getParameterContainer().setSortProperty(null);
        loadSearchMask(this.selectedType);
        fireSelectedTypeChanged();
    }


    public void setSelectedType(ObjectTemplate type)
    {
        if((this.selectedType == null && type != null) || (this.selectedType != null && !this.selectedType.equals(type)))
        {
            if(!this.rootType.equals(type) || !this.excludeRootType)
            {
                this.selectedType = type;
                loadSearchMask(this.selectedType);
                fireSelectedTypeChanged();
            }
        }
        if(this.selectedType != null && !this.selectedType.equals(getParameterContainer().getSelectedType()))
        {
            getParameterContainer().setSelectedType(this.selectedType);
        }
    }


    public void setTypes(Collection<ObjectTemplate> types)
    {
        if(this.types != types)
        {
            this.types.clear();
            this.types.addAll(types);
            fireTypesChanged();
        }
    }


    public void setParameterContainer(AdvancedSearchParameterContainer paramContainer)
    {
        if(paramContainer == null)
        {
            throw new IllegalArgumentException("Advanced search parameter container can not be null.");
        }
        this.parameterContainer = paramContainer;
    }


    public AdvancedSearchParameterContainer getParameterContainer()
    {
        return this.parameterContainer;
    }


    public void setVisibleSearchFieldGroups(List<SearchFieldGroup> groups)
    {
        boolean changed = false;
        for(SearchFieldGroup group : groups)
        {
            if(showGroupDirectly(group))
            {
                changed = true;
            }
        }
        if(changed)
        {
            fireSearchFieldGroupVisibilityChanged();
        }
    }


    public boolean hideSearchFieldGroup(SearchFieldGroup group)
    {
        boolean changed = hideGroupDirectly(group);
        if(changed)
        {
            fireSearchFieldGroupVisibilityChanged();
        }
        return changed;
    }


    protected boolean hideGroupDirectly(SearchFieldGroup group)
    {
        boolean changed = false;
        if(group != null && this.rootGroup.getAllVisibleSearchFieldGroups().contains(group) && group.isVisible())
        {
            for(SearchField field : group.getAllVisibleSearchFields())
            {
                if(hideFieldDirectly(field))
                {
                    changed = true;
                }
            }
        }
        return changed;
    }


    public boolean showSearchFieldGroup(SearchFieldGroup group)
    {
        boolean changed = false;
        changed = showGroupDirectly(group);
        if(changed)
        {
            fireSearchFieldGroupVisibilityChanged();
        }
        return changed;
    }


    public void setVisibleSearchFields(List<SearchField> searchFields)
    {
        boolean changed = false;
        this.visibleFields.clear();
        for(SearchField field : searchFields)
        {
            if(showFieldDirectly(field))
            {
                changed = true;
            }
        }
        if(changed)
        {
            fireSearchFieldVisibilityChanged();
        }
    }


    public boolean hideSearchField(SearchField field)
    {
        boolean changed = hideFieldDirectly(field);
        if(changed)
        {
            fireSearchFieldVisibilityChanged();
        }
        return changed;
    }


    protected boolean hideFieldDirectly(SearchField field)
    {
        boolean changed = false;
        if(field != null && (field.isVisible() || this.visibleFields.contains(field)) && this.rootGroup
                        .getAllVisibleSearchFields().contains(field))
        {
            if(field instanceof DefaultSearchField)
            {
                ((DefaultSearchField)field).setVisible(false);
                this.visibleFields.remove(field);
                SearchFieldConfiguration fieldConfig = this.searchFieldMap.get(field);
                if(fieldConfig != null)
                {
                    fieldConfig.setVisible(false);
                }
                changed = true;
            }
            else
            {
                LOG.error("Could not change field visibility (Reason: Unknown field type).");
            }
        }
        return changed;
    }


    public boolean showSearchField(SearchField field)
    {
        boolean changed = false;
        changed = showFieldDirectly(field);
        if(changed)
        {
            fireSearchFieldVisibilityChanged();
        }
        return changed;
    }


    public void setSortableProperties(List<PropertyDescriptor> sortProps)
    {
        if(this.sortProperties != sortProps)
        {
            this.sortProperties.clear();
            if(sortProps != null)
            {
                this.sortProperties.addAll(sortProps);
            }
            fireSortSearchFieldsChanged();
        }
    }


    public boolean addSortableProperty(PropertyDescriptor sortProp)
    {
        boolean changed = false;
        if(!this.sortProperties.contains(sortProp) && sortProp != null)
        {
            changed = this.sortProperties.add(sortProp);
            fireSortSearchFieldsChanged();
        }
        return changed;
    }


    public boolean removeSortableProperty(PropertyDescriptor sortProp)
    {
        boolean changed = this.sortProperties.remove(sortProp);
        if(changed)
        {
            fireSortSearchFieldsChanged();
        }
        return changed;
    }


    public void setSortCriterion(PropertyDescriptor sortProperty, boolean asc)
    {
        if((this.sortedByProperty == null && sortProperty != null) || (this.sortedByProperty != null &&
                        !this.sortedByProperty.equals(sortProperty)) || this.sortAscending != asc)
        {
            this.sortedByProperty = sortProperty;
            this.sortAscending = asc;
            fireSortSearchFieldsChanged();
        }
    }


    public void setSortedByProperty(PropertyDescriptor sortProperty)
    {
        if((this.sortedByProperty == null && sortProperty != null) || (this.sortedByProperty != null &&
                        !this.sortedByProperty.equals(sortProperty)))
        {
            this.sortedByProperty = sortProperty;
            fireSortSearchFieldsChanged();
        }
    }


    public void setSortAscending(boolean asc)
    {
        if(this.sortAscending != asc)
        {
            this.sortAscending = asc;
            fireSortSearchFieldsChanged();
        }
    }


    public UIEditor getEditor(SearchField searchField)
    {
        UIEditor uIEditor1, editor = null;
        DefaultTextUIEditor defaultTextUIEditor = new DefaultTextUIEditor();
        PropertyDescriptor propDescr = getPropertyDescriptor(searchField);
        if(propDescr != null)
        {
            EditorFactory editorFactory = getEditorFactory();
            String editorType = propDescr.getEditorType();
            Collection<PropertyEditorDescriptor> matchingEditorDescriptors = editorFactory.getMatchingEditorDescriptors(editorType);
            PropertyEditorDescriptor editorDescriptor = null;
            if(matchingEditorDescriptors == null || matchingEditorDescriptors.isEmpty())
            {
                LOG.warn("No matching editors found. Using default one.");
            }
            else
            {
                editorDescriptor = matchingEditorDescriptors.iterator().next();
                String defSearchMode = null;
                if(editorDescriptor instanceof DefaultPropertyEditorDescriptor)
                {
                    defSearchMode = ((DefaultPropertyEditorDescriptor)editorDescriptor).getDefaultSearchMode();
                }
                ValuePair<String, Map<String, Object>> editorInfo = getEditorInfo(searchField, propDescr, defSearchMode);
                uIEditor1 = editorDescriptor.createUIEditor((String)editorInfo.getFirst());
                if(uIEditor1 instanceof AbstractConditionUIEditor)
                {
                    ((AbstractConditionUIEditor)uIEditor1).setParameters((Map)editorInfo.getSecond());
                }
                if(uIEditor1 instanceof ReferenceUIEditor)
                {
                    SearchType rootSearchType = null;
                    SearchType rootType = getRootSearchType(propDescr);
                    if(rootType == null)
                    {
                        LOG.warn("Could not root type for reference editor (Reason: No root search type could be retrieved for property descriptor '" + propDescr + "').");
                    }
                    else
                    {
                        ((ReferenceUIEditor)uIEditor1).setRootType((ObjectType)rootType);
                        ((ReferenceUIEditor)uIEditor1).setRootSearchType((ObjectType)rootSearchType);
                    }
                }
            }
        }
        return uIEditor1;
    }


    public String getEditorMode(SearchField searchField, PropertyDescriptor propDescr)
    {
        return (String)getEditorInfo(searchField, propDescr, null).getFirst();
    }


    public Map<String, String> getParametersForSearchField(SearchField searchField)
    {
        SearchFieldConfiguration searchFieldConfiguration = this.searchFieldMap.get(searchField);
        if(searchFieldConfiguration != null)
        {
            return searchFieldConfiguration.getParameters();
        }
        return Collections.EMPTY_MAP;
    }


    private ValuePair<String, Map<String, Object>> getEditorInfo(SearchField searchField, PropertyDescriptor propDescr, String defaultMode)
    {
        String editorMode = (defaultMode == null) ? "default" : defaultMode;
        Map<String, Object> params = null;
        SearchFieldConfiguration searchFieldConf = this.searchFieldMap.get(searchField);
        if(searchFieldConf != null)
        {
            if(MapUtils.isNotEmpty(searchFieldConf.getParameters()))
            {
                params = new HashMap<>();
                params.putAll(searchFieldConf.getParameters());
            }
            List<EditorConditionEntry> conditionEntries = searchFieldConf.getConditionEntries();
            SearchFieldConfiguration.EntryListMode entryListMode = searchFieldConf.getEntryListMode();
            if(CollectionUtils.isNotEmpty(conditionEntries) && entryListMode != null)
            {
                if(params == null)
                {
                    params = new HashMap<>();
                }
                params.put("entryListMode", entryListMode);
                params.put("conditionEntries", conditionEntries);
            }
        }
        if(searchFieldConf == null || StringUtils.isBlank(searchFieldConf.getEditor()))
        {
            PropertyDescriptor.Multiplicity multi = propDescr.getMultiplicity();
            if(multi != null && multi.equals(PropertyDescriptor.Multiplicity.RANGE))
            {
                editorMode = "range";
            }
            else if(multi != null && !multi.equals(PropertyDescriptor.Multiplicity.SINGLE))
            {
                editorMode = "multi";
            }
        }
        else
        {
            editorMode = searchFieldConf.getEditor();
        }
        return new ValuePair(editorMode, (params == null) ? Collections.EMPTY_MAP : params);
    }


    public List<SearchField> getHiddenSearchFields()
    {
        return this.rootGroup.getAllHiddenSearchFields();
    }


    public SearchFieldGroup getRootSearchFieldGroup()
    {
        return (SearchFieldGroup)this.rootGroup;
    }


    public List<SearchField> getSearchFields()
    {
        return this.rootGroup.getAllSearchFields();
    }


    public ObjectTemplate getSelectedType()
    {
        return this.selectedType;
    }


    public List<PropertyDescriptor> getSortableProperties()
    {
        return Collections.unmodifiableList(this.sortProperties);
    }


    public List<ObjectTemplate> getTypes()
    {
        return this.types;
    }


    public List<SearchField> getVisibleSearchFields()
    {
        return Collections.unmodifiableList(this.visibleFields);
    }


    public PropertyDescriptor getSortedByProperty()
    {
        if(this.sortedByProperty == null && this.sortProperties != null && !this.sortProperties.isEmpty())
        {
            this.sortedByProperty = this.sortProperties.get(0);
        }
        return this.sortedByProperty;
    }


    public boolean isSortAscending()
    {
        return this.sortAscending;
    }


    public PropertyDescriptor getPropertyDescriptor(SearchField field)
    {
        PropertyDescriptor propDescr = null;
        SearchFieldConfiguration fieldConf = this.searchFieldMap.get(field);
        if(fieldConf != null)
        {
            propDescr = fieldConf.getPropertyDescriptor();
        }
        return propDescr;
    }


    public SearchField getSearchField(PropertyDescriptor descriptor)
    {
        for(Map.Entry<SearchField, SearchFieldConfiguration> entry : this.searchFieldMap.entrySet())
        {
            PropertyDescriptor propertyDescriptor = ((SearchFieldConfiguration)entry.getValue()).getPropertyDescriptor();
            if(descriptor.getQualifier().equalsIgnoreCase(propertyDescriptor.getQualifier()))
            {
                return entry.getKey();
            }
        }
        return null;
    }


    protected boolean showGroupDirectly(SearchFieldGroup group)
    {
        boolean changed = false;
        if(group != null && this.rootGroup.getAllSearchFieldGroups().contains(group) &&
                        !group.getAllHiddenSearchFields().isEmpty())
        {
            for(SearchField field : group.getAllHiddenSearchFields())
            {
                if(showFieldDirectly(field))
                {
                    changed = true;
                }
            }
        }
        return changed;
    }


    protected boolean showFieldDirectly(SearchField field)
    {
        boolean changed = false;
        if(field != null && (!field.isVisible() || !this.visibleFields.contains(field)) && getSearchFields().contains(field))
        {
            if(field instanceof DefaultSearchField)
            {
                ((DefaultSearchField)field).setVisible(true);
                if(!this.visibleFields.contains(field))
                {
                    this.visibleFields.add(field);
                }
                SearchFieldConfiguration fieldConfig = this.searchFieldMap.get(field);
                if(fieldConfig != null)
                {
                    fieldConfig.setVisible(true);
                }
                changed = true;
            }
            else
            {
                LOG.error("Could not change field visibility (Reason: Unknown field type).");
            }
        }
        return changed;
    }


    protected SearchType getRootSearchType(PropertyDescriptor propDescr)
    {
        SearchType searchType = null;
        String valueTypeCode = getTypeService().getValueTypeCode(propDescr);
        if(valueTypeCode != null)
        {
            try
            {
                searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(getTypeService().getObjectType(valueTypeCode));
            }
            catch(Exception e)
            {
                LOG.warn("Could not get search type for property descriptor (Reason: '" + e.getMessage() + "').", e);
            }
        }
        return searchType;
    }


    private void loadConfiguration()
    {
        this.rootType = this.config.getRootType();
        this.excludeRootType = this.config.isExcludeRootType();
        this.includeSubTypes = this.config.isIncludeSubTypes();
        this.includeSubTypesForRelatedTypes = this.config.isIncludeSubTypesForRelatedTypes();
        loadRootType();
        if(getParameterContainer().getSelectedType() != null && this.types
                        .contains(getParameterContainer().getSelectedType()))
        {
            this.selectedType = getParameterContainer().getSelectedType();
        }
        else if(!isExcludeRootType() && !this.config.getRootType().equals(this.selectedType))
        {
            this.selectedType = this.config.getRootType();
        }
        else if(!this.types.isEmpty())
        {
            this.selectedType = this.types.get(0);
        }
        loadSearchMask(this.selectedType);
    }


    private void loadSearchMask(ObjectTemplate type)
    {
        if(type == null)
        {
            throw new IllegalArgumentException("Type can not be null.");
        }
        this.parameterContainer.clear();
        this.searchFieldGroupMap.clear();
        this.searchFieldMap.clear();
        this.visibleFields.clear();
        this.sortProperties.clear();
        this.sortedByProperty = null;
        AdvancedSearchConfiguration localConf = (AdvancedSearchConfiguration)getUIConfigurationService().getComponentConfiguration(type, this.configCode, AdvancedSearchConfiguration.class);
        SearchFieldGroupConfiguration rootConfigGroup = localConf.getRootGroupConfiguration();
        if(rootConfigGroup == null)
        {
            LOG.error("Can not create root group. Reason: No root group configuration found for type '" + type.getCode() + "'.");
            if(type.equals(this.rootType))
            {
                throw new IllegalStateException("No root group configuration available for root type");
            }
            if(LOG.isInfoEnabled())
            {
                LOG.info("Using root type as fallback.");
                this.selectedType = this.rootType;
                loadSearchMask(this.rootType);
                return;
            }
        }
        this.selectedConfig = localConf;
        this.rootGroup = createSearchFieldGroup(rootConfigGroup);
        SearchType searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(type);
        if(searchType == null)
        {
            LOG.warn("Can not load sort fields (Reason: Search type can not be retrieved).");
        }
        else if(searchType.getSortProperties() != null && !searchType.getSortProperties().isEmpty())
        {
            for(PropertyDescriptor sortProp : searchType.getSortProperties())
            {
                if(UISessionUtils.getCurrentSession().getSearchService().isSortable(sortProp))
                {
                    this.sortProperties.add(sortProp);
                }
            }
            if(this.sortProperties.contains(getParameterContainer().getSortProperty()))
            {
                this.sortedByProperty = getParameterContainer().getSortProperty();
            }
            else if(CollectionUtils.isNotEmpty(this.sortProperties))
            {
                this.sortedByProperty = this.sortProperties.get(0);
            }
            else
            {
                LOG.warn("No sortable property is defined, using PK instead. Please check the base configuration for type '" + type + "' and make sure there is at least one sortable sort property.");
                this.sortedByProperty = getTypeService().getPropertyDescriptor("Item.pk");
                this.sortProperties.add(this.sortedByProperty);
            }
        }
    }


    private UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    private void loadRootType()
    {
        if(this.rootType == null)
        {
            throw new IllegalArgumentException("Root type can not be null.");
        }
        this.parameterContainer.clear();
        this.searchFieldGroupMap.clear();
        this.searchFieldMap.clear();
        this.visibleFields.clear();
        this.types.clear();
        this.sortProperties.clear();
        this.sortedByProperty = null;
        if(!isExcludeRootType())
        {
            this.types.add(this.rootType);
        }
        for(ObjectTemplate confType : this.config.getRelatedTypes())
        {
            if(!this.types.contains(confType))
            {
                this.types.add(confType);
                if(isIncludeSubTypesForRelatedTypes())
                {
                    for(ObjectTemplate subType : TypeTools.getTemplatesForCreation(UISessionUtils.getCurrentSession()
                                    .getTypeService(), confType.getBaseType()))
                    {
                        if(!this.types.contains(subType))
                        {
                            this.types.add(subType);
                        }
                    }
                }
            }
        }
        if(!isExcludeRootType() && isIncludeSubTypes())
        {
            for(ObjectTemplate subType : TypeTools.getTemplatesForCreation(getTypeService(), this.rootType.getBaseType()))
            {
                if(!this.types.contains(subType))
                {
                    this.types.add(subType);
                }
            }
        }
    }


    public void setExcludeRootType(boolean excludeRootType)
    {
        this.excludeRootType = excludeRootType;
    }


    public boolean isExcludeRootType()
    {
        return this.excludeRootType;
    }


    public void setIncludeSubTypes(boolean includeSubTypes)
    {
        this.includeSubTypes = includeSubTypes;
    }


    public boolean isIncludeSubTypes()
    {
        return this.includeSubTypes;
    }


    public void setIncludeSubTypesForRelatedTypes(boolean includeSubTypesForRelatedTypes)
    {
        this.includeSubTypesForRelatedTypes = includeSubTypesForRelatedTypes;
    }


    public boolean isIncludeSubTypesForRelatedTypes()
    {
        return this.includeSubTypesForRelatedTypes;
    }


    private DefaultSearchFieldGroup createSearchFieldGroup(SearchFieldGroupConfiguration groupConfig)
    {
        DefaultSearchFieldGroup group = null;
        if(groupConfig == null)
        {
            throw new IllegalArgumentException("Group configuration can not be null.");
        }
        group = createAndMapGroup(groupConfig);
        List<SearchField> fields = new ArrayList<>();
        for(SearchFieldConfiguration fieldConf : groupConfig.getSearchFieldConfigurations())
        {
            fields.add(createAndMapField(fieldConf));
        }
        group.setSearchFields(fields);
        List<SearchFieldGroup> subGroups = new ArrayList<>();
        for(SearchFieldGroupConfiguration subGroupConfig : groupConfig.getSearchFieldGroupConfigurations())
        {
            subGroups.add(createSearchFieldGroup(subGroupConfig));
        }
        group.setSearchFieldGroups(subGroups);
        return group;
    }


    private DefaultSearchFieldGroup createAndMapGroup(SearchFieldGroupConfiguration groupConfig)
    {
        DefaultSearchFieldGroup group = null;
        if(groupConfig != null)
        {
            String label = (groupConfig instanceof DefaultSearchFieldGroupConfiguration) ? ((DefaultSearchFieldGroupConfiguration)groupConfig).getLabelWithFallback() : groupConfig.getLabel();
            group = new DefaultSearchFieldGroup(label);
            if(!this.searchFieldGroupMap.containsKey(group))
            {
                this.searchFieldGroupMap.put(group, groupConfig);
            }
        }
        return group;
    }


    private SearchField createAndMapField(SearchFieldConfiguration fieldConfig)
    {
        DefaultSearchField defaultSearchField;
        SearchField field = null;
        if(fieldConfig != null)
        {
            defaultSearchField = fieldConfig.getSearchField();
            if(!this.searchFieldMap.containsKey(defaultSearchField))
            {
                this.searchFieldMap.put(defaultSearchField, fieldConfig);
                if(defaultSearchField.isVisible() && !this.visibleFields.contains(defaultSearchField))
                {
                    this.visibleFields.add(defaultSearchField);
                }
            }
        }
        return (SearchField)defaultSearchField;
    }


    protected void storeAdvancedSearchConfiguration()
    {
        if(this.selectedConfig != null)
        {
            getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)this.selectedConfig,
                            UISessionUtils.getCurrentSession().getUser(), this.selectedType, this.configCode, AdvancedSearchConfiguration.class);
        }
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected EditorFactory getEditorFactory()
    {
        if(this.editorFactory == null)
        {
            this.editorFactory = (EditorFactory)SpringUtil.getBean("EditorFactory");
        }
        return this.editorFactory;
    }
}
