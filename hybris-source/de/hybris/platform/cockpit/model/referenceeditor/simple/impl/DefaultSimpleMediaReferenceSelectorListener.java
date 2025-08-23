package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.impl.GenericSearchParameterDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultSimpleMediaReferenceSelectorListener extends DefaultSimpleReferenceSelectorListener
{
    private static final String MEDIA_MIME = "media.mime";
    private static final String MIME_TYPES = "mimeTypes";
    private static final String SEMICOLON = ";";


    public DefaultSimpleMediaReferenceSelectorListener(DefaultSimpleReferenceSelectorModel model, AdditionalReferenceEditorListener additionalListener)
    {
        super(model, additionalListener);
    }


    protected void addPredefinedFields(Query query)
    {
        super.addPredefinedFields(query);
        query.setOrMode(Boolean.TRUE.booleanValue());
        PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor("media.mime");
        GenericSearchParameterDescriptor searchDescriptor = (GenericSearchParameterDescriptor)UISessionUtils.getCurrentSession().getSearchService().getSearchDescriptor(propertyDescriptor);
        String rawMimeTypes = (String)((DefaultSimpleMediaReferenceSelectorModel)getModel()).getParameters().get("mimeTypes");
        List<SearchParameterValue> searchParameterValues = new ArrayList<>();
        List<List<SearchParameterValue>> orValues = new LinkedList<>();
        if(rawMimeTypes != null)
        {
            for(String singleMimeType : rawMimeTypes.split(";"))
            {
                singleMimeType = singleMimeType.trim();
                SearchParameterValue searchParameter = new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, singleMimeType, searchDescriptor.getDefaultOperator());
                searchParameterValues.add(searchParameter);
            }
            orValues.add(searchParameterValues);
            query.setParameterOrValues(orValues);
        }
    }
}
