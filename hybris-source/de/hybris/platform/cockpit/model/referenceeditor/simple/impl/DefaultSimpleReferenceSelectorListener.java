package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleSelectorModel;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.services.search.impl.GenericSearchParameterDescriptor;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultSimpleReferenceSelectorListener implements SimpleReferenceSelectorListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSimpleReferenceSelectorListener.class);
    private final DefaultSimpleReferenceSelectorModel model;
    private final AdditionalReferenceEditorListener additionalEditorListener;


    public DefaultSimpleReferenceSelectorModel getModel()
    {
        return this.model;
    }


    private SearchProvider searchProvider = null;
    private TypeService typeService = null;
    private UIConfigurationService uiConfigurationService;


    public DefaultSimpleReferenceSelectorListener(DefaultSimpleReferenceSelectorModel model, AdditionalReferenceEditorListener additionalEditorListener)
    {
        if(model == null)
        {
            throw new IllegalArgumentException("Reference selector model and root type must be specified.");
        }
        this.model = model;
        this.additionalEditorListener = additionalEditorListener;
    }


    public void abortAndCloseAdvancedMode()
    {
        this.model.setMode(SimpleSelectorModel.Mode.VIEW_MODE);
    }


    public void triggerAutoCompleteSearch(String searchTerm)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Starting search for '" + searchTerm + "'.");
            }
            SearchProvider searchProvider = getSearchProvider();
            if(searchProvider == null)
            {
                LOG.error("Could not perform search since no search provider could be loaded.");
            }
            else
            {
                ObjectType rootSearchType = this.model.getAutocompleteSearchType();
                List<? extends Object> resultList = Collections.EMPTY_LIST;
                if(rootSearchType != null)
                {
                    List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService().getSearchType(rootSearchType));
                    Query query = new Query(searchTypes, searchTerm, 0, this.model.getMaxAutoCompleteResultSize());
                    addPredefinedFields(query);
                    query.setNeedTotalCount(false);
                    query.setExcludeSubTypes(false);
                    ExtendedSearchResult searchResult = searchProvider.search(query);
                    if(searchResult != null)
                    {
                        resultList = searchResult.getResult();
                    }
                }
                this.model.setAutoCompleteResult(resultList);
            }
        }
        catch(Exception e)
        {
            LOG.error("Auto complete search failed (Reason: '" + e.getMessage() + "').", e);
            this.model.setAutoCompleteResult(Collections.EMPTY_LIST);
        }
    }


    protected void addPredefinedFields(Query query)
    {
        Map<String, Object> initialValues = null;
        CreateContext context = getCreateContext();
        if(context != null)
        {
            TypedObject sourceItem = context.getSourceObject();
            if(sourceItem != null)
            {
                try
                {
                    initialValues = TypeTools.getInitialValues(getModel().getRootSearchType(), sourceItem, getTypeService(),
                                    getUIConfigurationService());
                }
                catch(Exception e)
                {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        if(initialValues != null)
        {
            for(Map.Entry<String, Object> entry : initialValues.entrySet())
            {
                PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(entry.getKey());
                GenericSearchParameterDescriptor searchDescriptor = (GenericSearchParameterDescriptor)UISessionUtils.getCurrentSession().getSearchService().getSearchDescriptor(propertyDescriptor);
                query.addParameterValue(new SearchParameterValue((SearchParameterDescriptor)searchDescriptor,
                                TypeTools.container2Item(getTypeService(), entry.getValue()), searchDescriptor.getDefaultOperator()));
            }
        }
    }


    private CreateContext getCreateContext()
    {
        Object ret = getModel().getParameters().get("createContext");
        if(ret instanceof CreateContext)
        {
            return (CreateContext)ret;
        }
        return null;
    }


    public void saveActualItem(Object currentValue)
    {
        this.model.saveItem(currentValue);
        this.model.setMode(SimpleSelectorModel.Mode.VIEW_MODE);
    }


    public void selectorNormaMode()
    {
        this.model.setMode(SimpleSelectorModel.Mode.NORMAL_MODE);
    }


    public void selectorAdvancedMode()
    {
        this.model.setMode(SimpleSelectorModel.Mode.ADVANCED_MODE);
    }


    public void cancel()
    {
        this.model.setMode(SimpleSelectorModel.Mode.VIEW_MODE);
        this.model.cancel();
    }


    public void showAddItemPopupEditor(CreateContext context)
    {
        showAddItemPopupEditor(this.model.getRootType(), context.getSourceObject(), context);
    }


    public void doOpenReferencedItem(TypedObject item)
    {
        if(this.additionalEditorListener != null)
        {
            this.additionalEditorListener.openItemRequestPerformed(item);
        }
    }


    @Deprecated
    public void showAddItemPopupEditor(ObjectType type, TypedObject sourceItem, CreateContext context)
    {
        Map<String, Object> initialValues = null;
        if(sourceItem != null)
        {
            try
            {
                initialValues = TypeTools.getInitialValues(type, sourceItem, getTypeService(), getUIConfigurationService());
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        ObjectTemplate template = getTypeService().getObjectTemplate(type.getCode());
        getCurrentPerspective().createItemInPopupEditor((ObjectType)template, (initialValues == null) ? Collections.EMPTY_MAP : initialValues, context);
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = UISessionUtils.getCurrentSession().getSearchService().getSearchProvider();
        }
        return this.searchProvider;
    }


    private UICockpitPerspective getCurrentPerspective()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective();
    }
}
