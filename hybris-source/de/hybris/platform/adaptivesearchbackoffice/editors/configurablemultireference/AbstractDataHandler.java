package de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference;

import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public abstract class AbstractDataHandler<D extends AbstractEditorData, V extends ItemModel> implements DataHandler<D, V>
{
    protected static final String SEARCH_PROFILE_PARAM = "searchProfile";
    private AsConfigurationService asConfigurationService;
    private AsUidGenerator asUidGenerator;


    public ListModel<D> loadData(Collection<V> initialValue, SearchResultData searchResult, Map<String, Object> parameters)
    {
        Map<Object, D> mapping = new LinkedHashMap<>();
        loadDataFromSearchResult(mapping, searchResult, parameters);
        loadDataFromInitialValue(mapping, initialValue, parameters);
        ListModelList<D> listModel = new ListModelList(mapping.values());
        postLoadData(initialValue, searchResult, parameters, listModel);
        return (ListModel<D>)listModel;
    }


    public List<V> getValue(ListModel<D> data)
    {
        List<V> value = new ArrayList<>();
        for(int index = 0; index < data.getSize(); index++)
        {
            AbstractEditorData abstractEditorData = (AbstractEditorData)data.getElementAt(index);
            if(abstractEditorData.getModel() != null)
            {
                value.add((V)abstractEditorData.getModel());
            }
        }
        return value;
    }


    public V getItemValue(D data)
    {
        return (V)data.getModel();
    }


    protected void postLoadData(Collection<V> initialValue, SearchResultData searchResult, Map<String, Object> parameters, ListModelList<D> data)
    {
    }


    public Object getAttributeValue(D editorData, String attributeName)
    {
        try
        {
            return PropertyUtils.getProperty(editorData, attributeName);
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e)
        {
            throw new EditorRuntimeException(e);
        }
    }


    public Class<?> getAttributeType(D editorData, String attributeName)
    {
        try
        {
            return PropertyUtils.getPropertyType(editorData, attributeName);
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e)
        {
            throw new EditorRuntimeException(e);
        }
    }


    public void setAttributeValue(D editorData, String attributeName, Object attributeValue)
    {
        try
        {
            PropertyUtils.setProperty(editorData, attributeName, attributeValue);
            if(editorData.getModel() != null)
            {
                editorData.getModel().setProperty(attributeName, attributeValue);
            }
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e)
        {
            throw new EditorRuntimeException(e);
        }
    }


    protected D getOrCreateEditorData(Map<Object, D> mapping, String key)
    {
        AbstractEditorData abstractEditorData = (AbstractEditorData)mapping.get(key);
        if(abstractEditorData == null)
        {
            abstractEditorData = (AbstractEditorData)createEditorData();
            mapping.put(key, (D)abstractEditorData);
        }
        return (D)abstractEditorData;
    }


    public AsConfigurationService getAsConfigurationService()
    {
        return this.asConfigurationService;
    }


    @Required
    public void setAsConfigurationService(AsConfigurationService asConfigurationService)
    {
        this.asConfigurationService = asConfigurationService;
    }


    public AsUidGenerator getAsUidGenerator()
    {
        return this.asUidGenerator;
    }


    @Required
    public void setAsUidGenerator(AsUidGenerator asUidGenerator)
    {
        this.asUidGenerator = asUidGenerator;
    }


    protected abstract void loadDataFromSearchResult(Map<Object, D> paramMap, SearchResultData paramSearchResultData, Map<String, Object> paramMap1);


    protected abstract void loadDataFromInitialValue(Map<Object, D> paramMap, Collection<V> paramCollection, Map<String, Object> paramMap1);


    protected abstract D createEditorData();
}
