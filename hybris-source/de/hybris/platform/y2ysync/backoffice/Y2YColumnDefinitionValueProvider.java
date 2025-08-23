package de.hybris.platform.y2ysync.backoffice;

import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class Y2YColumnDefinitionValueProvider implements ReferenceEditorSearchFacade
{
    private static final Logger LOG = LoggerFactory.getLogger(Y2YColumnDefinitionValueProvider.class);
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String COLUMN_SEARCH_PARAM = "y2yColumnDefinition";
    private TypeService typeService;


    public Pageable search(SearchQueryData searchQueryData)
    {
        Optional<SearchAttributeDescriptor> columnDefinitionOptional = searchQueryData.getAttributes().stream().filter(i -> i.getAttributeName().equals("y2yColumnDefinition")).findFirst();
        if(!columnDefinitionOptional.isPresent())
        {
            LOG.error("Could not find {} parameter in search data for Y2YColumnDefinitionValueProvider. Check y2ysyncbackoffice-backoffice-config.xml", "y2yColumnDefinition");
            return (Pageable)asPageable(Collections.emptyList());
        }
        Y2YColumnDefinitionModel columnDefinition = (Y2YColumnDefinitionModel)searchQueryData.getAttributeValue(columnDefinitionOptional.get());
        Y2YStreamConfigurationModel streamConfiguration = columnDefinition.getStreamConfiguration();
        if(streamConfiguration == null)
        {
            LOG.error("Y2YColumnDefinitionModel passed to search provider doesn't have stream configuration assigned");
            return (Pageable)asPageable(Collections.emptyList());
        }
        ComposedTypeModel streamItemType = streamConfiguration.getItemTypeForStream();
        return (Pageable)asPageable(this.typeService.getAttributeDescriptorsForType(streamItemType));
    }


    private PageableList asPageable(Collection<AttributeDescriptorModel> list)
    {
        return new PageableList(new ArrayList<>(list), 10);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
