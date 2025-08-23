package de.hybris.platform.adaptivesearchbackoffice.editors.facets;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.Component;

public class FacetValuesMultiReferenceEditor<T> extends DefaultMultiReferenceEditor<T>
{
    protected static final String LAST_INPUT_KEY = "lastInput";
    protected static final String EDITOR_DATA_KEY = "editorData";
    protected static final int MAX_ITERATIONS = 3;


    public String getStringRepresentationOfObject(T obj)
    {
        if(obj instanceof AbstractAsFacetValueConfigurationModel)
        {
            AbstractAsFacetValueConfigurationModel facetValueConfiguration = (AbstractAsFacetValueConfigurationModel)obj;
            WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)getEditorContext().getParameter("wim");
            List<AsFacetValueData> facetValues = resolveFacetValues(widgetInstanceManager.getWidgetslot(), 0);
            if(CollectionUtils.isNotEmpty(facetValues))
            {
                Optional<AsFacetValueData> optionalFacetValue = facetValues.stream().filter(facetValueData -> facetValueData.getValue().equals(facetValueConfiguration.getValue())).findFirst();
                if(optionalFacetValue.isPresent())
                {
                    AsFacetValueData facetValue = optionalFacetValue.get();
                    return facetValue.getName() + " (" + facetValue.getName() + ")";
                }
            }
        }
        return super.getStringRepresentationOfObject(obj);
    }


    protected List<AsFacetValueData> resolveFacetValues(Widgetslot widgetslot, int iterationNumber)
    {
        if(iterationNumber >= 3)
        {
            return Collections.emptyList();
        }
        Widgetslot parentWidgetslot = WidgetTreeUIUtils.getParentWidgetslot((Component)widgetslot);
        TypeAwareSelectionContext selectionContext = (TypeAwareSelectionContext)parentWidgetslot.getViewModel().getValue("lastInput", TypeAwareSelectionContext.class);
        if(selectionContext == null)
        {
            return resolveFacetValues(parentWidgetslot, iterationNumber + 1);
        }
        Object editorData = selectionContext.getParameters().get("editorData");
        if(editorData instanceof AbstractFacetConfigurationEditorData)
        {
            AsFacetData facet = ((AbstractFacetConfigurationEditorData)editorData).getFacet();
            if(facet == null)
            {
                return Collections.emptyList();
            }
            return CollectionUtils.isNotEmpty(facet.getAllValues()) ? facet.getAllValues() : facet.getValues();
        }
        return resolveFacetValues(parentWidgetslot, iterationNumber + 1);
    }
}
