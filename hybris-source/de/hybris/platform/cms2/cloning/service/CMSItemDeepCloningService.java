package de.hybris.platform.cms2.cloning.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;

public interface CMSItemDeepCloningService
{
    ItemModel deepCloneComponent(ItemModel paramItemModel, ModelCloningContext paramModelCloningContext);


    String generateCloneItemUid();


    String generateCloneItemUid(String paramString);


    String generateCloneComponentName(String paramString);
}
