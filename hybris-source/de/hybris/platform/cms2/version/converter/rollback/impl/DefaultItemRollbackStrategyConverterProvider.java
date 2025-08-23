package de.hybris.platform.cms2.version.converter.rollback.impl;

import de.hybris.platform.cms2.version.converter.rollback.ItemRollbackConverter;
import de.hybris.platform.cms2.version.converter.rollback.ItemRollbackStrategyConverterProvider;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemRollbackStrategyConverterProvider implements ItemRollbackStrategyConverterProvider
{
    private List<ItemRollbackConverter> cmsItemRollbackConverters;


    public Optional<ItemRollbackConverter> getConverter(ItemModel itemModel)
    {
        return getCmsItemRollbackConverters().stream()
                        .filter(itemConverter -> itemConverter.getConstrainedBy().test(itemModel))
                        .findFirst();
    }


    protected List<ItemRollbackConverter> getCmsItemRollbackConverters()
    {
        return this.cmsItemRollbackConverters;
    }


    @Required
    public void setCmsItemRollbackConverters(List<ItemRollbackConverter> cmsItemRollbackConverters)
    {
        this.cmsItemRollbackConverters = cmsItemRollbackConverters;
    }
}
