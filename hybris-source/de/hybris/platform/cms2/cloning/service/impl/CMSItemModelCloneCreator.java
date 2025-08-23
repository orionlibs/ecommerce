package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

public class CMSItemModelCloneCreator extends ItemModelCloneCreator
{
    public CMSItemModelCloneCreator()
    {
        this(null, null, null);
    }


    public CMSItemModelCloneCreator(ModelService modelService, I18NService i18nService, TypeService typeService)
    {
        super(modelService, i18nService, typeService);
    }


    public ItemModel copy(ItemModel original) throws ItemModelCloneCreator.CannotCloneException
    {
        return copy((ComposedTypeModel)null, original, (ItemModelCloneCreator.CopyContext)new CMSCopyContext());
    }


    public ItemModel copy(ItemModel original, ModelCloningContext cloningContext) throws ItemModelCloneCreator.CannotCloneException
    {
        return copy((ComposedTypeModel)null, original, (ItemModelCloneCreator.CopyContext)new CMSCopyContext(cloningContext));
    }


    public ItemModel copy(ItemModel original, CMSCopyContext ctx) throws ItemModelCloneCreator.CannotCloneException
    {
        return copy((ComposedTypeModel)null, original, (ItemModelCloneCreator.CopyContext)ctx);
    }


    public ItemModel copy(ComposedTypeModel targetType, ItemModel original, ModelCloningContext ctx) throws ItemModelCloneCreator.CannotCloneException
    {
        return copy(targetType, original, (ItemModelCloneCreator.CopyContext)new CMSCopyContext(ctx));
    }
}
