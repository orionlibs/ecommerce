package de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference;

import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import java.util.Collection;
import java.util.Map;
import org.zkoss.zul.ListModel;

public interface DataHandler<D extends de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData, V>
{
    String getTypeCode();


    ListModel<D> loadData(Collection<V> paramCollection, SearchResultData paramSearchResultData, Map<String, Object> paramMap);


    Collection<V> getValue(ListModel<D> paramListModel);


    V getItemValue(D paramD);


    Class<?> getAttributeType(D paramD, String paramString);


    Object getAttributeValue(D paramD, String paramString);


    void setAttributeValue(D paramD, String paramString, Object paramObject);
}
