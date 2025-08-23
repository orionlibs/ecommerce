package de.hybris.platform.cockpit.helpers;

import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.core.model.ItemModel;
import java.util.Set;

public interface ModelHelper
{
    void saveModel(ItemModel paramItemModel, boolean paramBoolean) throws ValueHandlerException;


    void saveModel(ItemModel paramItemModel, boolean paramBoolean1, boolean paramBoolean2) throws ValueHandlerException;


    void saveModels(Set<ItemModel> paramSet, boolean paramBoolean) throws ValueHandlerException;


    void saveModels(Set<ItemModel> paramSet, boolean paramBoolean1, boolean paramBoolean2) throws ValueHandlerException;


    void removeModel(ItemModel paramItemModel, boolean paramBoolean) throws ValueHandlerException;


    void removeModel(ItemModel paramItemModel, boolean paramBoolean1, boolean paramBoolean2) throws ValueHandlerException;


    boolean isWritable(ItemModel paramItemModel, String paramString, boolean paramBoolean);
}
