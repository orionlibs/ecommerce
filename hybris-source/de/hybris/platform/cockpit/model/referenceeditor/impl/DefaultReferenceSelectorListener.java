package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.SelectorModel;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultReferenceSelectorListener implements ReferenceSelectorListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReferenceSelectorListener.class);
    protected final DefaultReferenceSelectorModel model;
    protected SearchProvider searchProvider = null;
    protected TypeService typeService = null;
    protected EditorListener editorListener = null;
    protected AdditionalReferenceEditorListener additionalEditorListener = null;
    private UIConfigurationService uiConfigurationService;


    public DefaultReferenceSelectorListener(DefaultReferenceSelectorModel model, EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        if(model == null)
        {
            throw new IllegalArgumentException("Reference selector model and root type must be specified.");
        }
        this.model = model;
        this.editorListener = editorListener;
        this.additionalEditorListener = additionalListener;
    }


    public void abortAndCloseAdvancedMode()
    {
        this.model.clearTemporaryItems();
        this.model.setMode(SelectorModel.Mode.NORMAL_MODE);
    }


    public void addItem(Object item)
    {
        this.model.addItem(getTypeService().wrapItem(item));
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
                    List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession()
                                    .getSearchService().getSearchType(rootSearchType));
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
                    initialValues = TypeTools.getInitialValues(this.model.getAutocompleteSearchType(), sourceItem, getTypeService(),
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
                query.addParameterValue(new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, TypeTools.container2Item(getTypeService(), entry
                                .getValue()), searchDescriptor.getDefaultOperator()));
            }
        }
    }


    private CreateContext getCreateContext()
    {
        Object ret = this.model.getParameters().get("createContext");
        if(ret instanceof CreateContext)
        {
            return (CreateContext)ret;
        }
        return null;
    }


    public void triggerSearch(String searchTerm)
    {
        this.model.setSearchResult(Collections.EMPTY_LIST);
    }


    public void clearTemporaryItems()
    {
        this.model.clearTemporaryItems();
    }


    public void addItems(List<Object> items)
    {
        this.model.addItems(getTypeService().wrapItems(items));
    }


    public void addToNotConfirmedItems(Collection items)
    {
        this.model.addItemsNotConfirmed(items);
    }


    public void removeTemporaryItem(int index)
    {
        this.model.removeTemporaryItem(index);
    }


    public void removeTemporaryItem(Object item)
    {
        this.model.removeTemporaryItem(item);
    }


    public void confirmAndCloseAdvancedMode()
    {
        this.model.clearTemporaryItems();
        this.model.setMode(SelectorModel.Mode.NORMAL_MODE);
    }


    public void moveItem(int fromIndex, int toIndex)
    {
        this.model.moveItem(fromIndex, toIndex);
    }


    public void addTemporaryItem(Object item)
    {
        this.model.addTemporaryItem(item);
    }


    public void removeItem(int index)
    {
        this.model.removeItem(index);
    }


    public void removeItems(Collection indexes)
    {
        this.model.removeItems(indexes);
    }


    public void changeMode(SelectorModel.Mode mode)
    {
        this.model.setMode(mode);
    }


    public void moveTemporaryItem(int fromIndex, int toIndex)
    {
        this.model.moveTemporaryItem(fromIndex, toIndex);
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = UISessionUtils.getCurrentSession().getSearchService().getSearchProvider();
        }
        return this.searchProvider;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public void addTemporaryItems(Collection item)
    {
        this.model.addTemporaryItems(item);
    }


    public void deselectTemporaryItem(Object item)
    {
        this.model.deselectTemporaryItem(item);
    }


    public void deselectTemporaryItems()
    {
        this.model.deselectTemporaryItems();
    }


    public void selectTemporaryItem(Object item)
    {
        this.model.selectTemporaryItem(item);
    }


    public void selectItem(Object item)
    {
        this.model.selectItem(item);
    }


    public void deselectItem(Object item)
    {
        this.model.deselectItem(item);
    }


    public void selectTemporaryItems(Collection items)
    {
        this.model.selectTemporaryItems(items);
    }


    public void selectItems(Collection items)
    {
        this.model.selectItems(items);
    }


    public void saveActualItems()
    {
        this.model.saveItems();
        this.model.setMode(SelectorModel.Mode.VIEW_MODE);
    }


    public void selectorNormaMode()
    {
        Object value = this.model.getValue();
        Collection<Object> items = (value instanceof Collection) ? (Collection<Object>)value : Collections.<Object>singleton(value);
        this.model.addItemsNotConfirmed(items);
        this.model.selectItems(items);
        this.model.setMode(SelectorModel.Mode.NORMAL_MODE);
    }


    public void selectorAdvancedMode()
    {
        this.model.initAdvanceModeModels();
        this.model.setMode(SelectorModel.Mode.ADVANCED_MODE);
    }


    public void cancel()
    {
        this.model.setMode(SelectorModel.Mode.VIEW_MODE);
        this.model.cancel();
    }


    public void showAddItemPopupEditor(CreateContext context)
    {
        showAddItemPopupEditor(this.model.getRootType(), context.getSourceObject(), context);
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


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    private UICockpitPerspective getCurrentPerspective()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective();
    }


    public void doEnterPressed()
    {
        if(this.editorListener != null)
        {
            this.editorListener.actionPerformed("enter_pressed");
        }
    }


    public void doOpenReferencedItem(TypedObject item)
    {
        if(this.additionalEditorListener != null)
        {
            this.additionalEditorListener.openItemRequestPerformed(item);
        }
    }
}
