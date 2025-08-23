package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleSelectorModel;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultSimpleReferenceSelectorModel extends AbstractSimpleReferenceSelectorModel
{
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewSelector";
    protected static final String ADVANCED_SEARCH_VIEW_CONFIG_CODE = "advancedSearch";
    private SimpleSelectorModel.Mode mode = SimpleSelectorModel.Mode.VIEW_MODE;
    private ObjectType rootType = null;
    private ObjectType rootSearchType = null;
    private ObjectType autocompletionSearchType = null;
    private List<? extends Object> autoCompleteResult;
    private TypeService typeService = null;
    private Object value;
    private int minAutoCompleteTextLength = 1;
    private int maxAutoCompleteResultSize = 20;
    private Map<String, ? extends Object> parameters = new HashMap<>();


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public DefaultSimpleReferenceSelectorModel()
    {
        this(null);
    }


    public DefaultSimpleReferenceSelectorModel(ObjectType rootType)
    {
        this.rootType = rootType;
        this.autoCompleteResult = new ArrayList();
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    public int getPageSize()
    {
        return 0;
    }


    public List<? extends Object> getAutoCompleteResult()
    {
        if(this.autoCompleteResult == null)
        {
            this.autoCompleteResult = new ArrayList();
        }
        return this.autoCompleteResult;
    }


    public Object getValue()
    {
        return this.value;
    }


    public int getMinAutoCompleteTextLength()
    {
        return this.minAutoCompleteTextLength;
    }


    public int getMaxAutoCompleteResultSize()
    {
        return this.maxAutoCompleteResultSize;
    }


    public SimpleSelectorModel.Mode getMode()
    {
        return this.mode;
    }


    public List<? extends Object> getSearchResult()
    {
        return Collections.EMPTY_LIST;
    }


    public int getTotalSize()
    {
        return 0;
    }


    public AdvancedSearchModel getAdvancedSearchModel()
    {
        return null;
    }


    public ObjectType getRootType()
    {
        return this.rootType;
    }


    public ObjectType getRootSearchType()
    {
        return (this.rootSearchType == null) ? this.rootType : this.rootSearchType;
    }


    public ObjectType getAutocompleteSearchType()
    {
        return (this.autocompletionSearchType == null) ? getRootSearchType() : this.autocompletionSearchType;
    }


    public MutableTableModel getTableModel()
    {
        return null;
    }


    public void cancel()
    {
        this.autoCompleteResult.clear();
        fireCancel();
    }


    public void doSearch(ObjectTemplate objectType, AdvancedSearchParameterContainer parameterContainer, int currentPage)
    {
    }


    public DefaultAdvancedSearchModel createAdvancedTableModel()
    {
        return null;
    }


    public MutableTableModel createDefaultTableModel()
    {
        return null;
    }


    public void setValue(Object value)
    {
        this.value = value;
        fireItemChanged();
    }


    public void setMode(SimpleSelectorModel.Mode mode)
    {
        if(!this.mode.equals(mode))
        {
            this.mode = mode;
            fireModeChanged();
        }
    }


    public void setAutoCompleteResult(List<? extends Object> autoCompleteResult)
    {
        if(this.autoCompleteResult != autoCompleteResult)
        {
            this.autoCompleteResult = autoCompleteResult;
            this.mode = SimpleSelectorModel.Mode.NORMAL_MODE;
            fireAutoCompleteResultChanged();
        }
    }


    public void setSearchResult(List<? extends Object> searchResult)
    {
    }


    public void setMinAutoCompleteTextLength(int minAutoCompleteTextLength)
    {
        this.minAutoCompleteTextLength = minAutoCompleteTextLength;
    }


    public void setMaxAutoCompleteResultSize(int maxAutoCompleteResultSize)
    {
        this.maxAutoCompleteResultSize = maxAutoCompleteResultSize;
    }


    public void setPageSize(int pageSize)
    {
    }


    public void setTotalSize(int totalSize)
    {
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    protected SearchProvider getSearchProvider()
    {
        return null;
    }


    public void setRootType(ObjectType rootType)
    {
        if((this.rootType != null && !this.rootType.equals(rootType)) || (this.rootType == null && rootType != null))
        {
            this.rootType = rootType;
            fireRootTypeChanged();
            if(this.rootSearchType == null)
            {
                fireRootSearchTypeChanged();
            }
        }
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        if(this.rootSearchType != rootSearchType)
        {
            this.rootSearchType = rootSearchType;
            fireRootSearchTypeChanged();
        }
    }


    public void setAutocompleteSearchType(ObjectType autocompletionSearchType)
    {
        if(this.autocompletionSearchType != autocompletionSearchType)
        {
            this.autocompletionSearchType = autocompletionSearchType;
            fireRootSearchTypeChanged();
        }
    }


    public void saveItem(Object currentValue)
    {
        if(this.value != currentValue)
        {
            this.value = currentValue;
            fireItemChanged();
        }
    }
}
