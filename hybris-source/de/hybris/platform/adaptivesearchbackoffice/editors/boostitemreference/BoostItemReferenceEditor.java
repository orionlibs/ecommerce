package de.hybris.platform.adaptivesearchbackoffice.editors.boostitemreference;

import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.zk.ui.Component;

public class BoostItemReferenceEditor<T> extends DefaultReferenceEditor<T>
{
    @Resource
    private ModelService modelService;
    @Resource
    private AsSearchProviderFactory asSearchProviderFactory;


    public void render(Component parent, EditorContext<T> context, EditorListener<T> listener)
    {
        AbstractAsBoostItemConfigurationModel parentObject = (AbstractAsBoostItemConfigurationModel)context.getParameters().get("parentObject");
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = (AbstractAsConfigurableSearchConfigurationModel)this.modelService.getAttributeValue(parentObject, "searchConfiguration");
        AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)this.modelService.getAttributeValue(searchConfiguration, "searchProfile");
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        Optional<AsIndexTypeData> indexTypeData = searchProvider.getIndexTypeForCode(searchProfile.getIndexType());
        if(!indexTypeData.isPresent())
        {
            throw new EditorRuntimeException("Index property not found: " + searchProfile.getIndexType());
        }
        context.setParameter("restrictToType", ((AsIndexTypeData)indexTypeData.get()).getItemType());
        if(((AsIndexTypeData)indexTypeData.get()).isCatalogVersionAware())
        {
            context.setParameter("referenceSearchCondition_catalogVersion", searchProfile.getCatalogVersion());
        }
        populateContextWithPKCondition(context, searchConfiguration);
        super.render(parent, context, listener);
    }


    protected void populateContextWithPKCondition(EditorContext<T> context, AbstractAsConfigurableSearchConfigurationModel searchConfiguration)
    {
        List<PK> pks = resolveConditionValue(searchConfiguration);
        if(CollectionUtils.isNotEmpty(pks))
        {
            context.setParameter("referenceSearchCondition_pk_doesNotContain", "{{" + (String)pks
                            .stream().map(pk -> pk.getLongValueAsString() + "L").collect(Collectors.joining(",")) + "}}");
        }
    }


    protected List<PK> resolveConditionValue(AbstractAsConfigurableSearchConfigurationModel searchConfiguration)
    {
        List<AsPromotedItemModel> promotedItems = (searchConfiguration.getPromotedItems() == null) ? Collections.<AsPromotedItemModel>emptyList() : searchConfiguration.getPromotedItems();
        List<AsExcludedItemModel> excludedItems = (searchConfiguration.getExcludedItems() == null) ? Collections.<AsExcludedItemModel>emptyList() : searchConfiguration.getExcludedItems();
        return (List<PK>)Stream.concat(promotedItems.stream(), excludedItems.stream()).filter(boostItem -> (boostItem.getItem() != null))
                        .map(boostItem -> boostItem.getItem().getPk()).collect(Collectors.toList());
    }
}
