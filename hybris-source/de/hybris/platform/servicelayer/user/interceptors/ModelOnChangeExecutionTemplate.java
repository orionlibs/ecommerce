package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Objects;

public abstract class ModelOnChangeExecutionTemplate<T extends AbstractItemModel>
{
    protected final T model;
    protected final InterceptorContext ctx;
    protected final ModelService modelService;
    protected final ItemModelContext modelContext;


    public ModelOnChangeExecutionTemplate(T model, InterceptorContext ctx)
    {
        this.model = (T)Objects.<AbstractItemModel>requireNonNull((AbstractItemModel)model);
        this.ctx = Objects.<InterceptorContext>requireNonNull(ctx);
        this.modelService = Objects.<ModelService>requireNonNull(ctx.getModelService());
        this.modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)model);
    }


    protected abstract boolean modelHasChanged();


    protected abstract void executeOnChange();


    public void executeIfContentChanged()
    {
        if(contentHasChanged())
        {
            executeOnChange();
        }
    }


    private boolean contentHasChanged()
    {
        return (modelIsBeingModified() && modelHasChanged());
    }


    private boolean modelIsBeingModified()
    {
        return (!this.ctx.isNew(this.model) && !this.ctx.isRemoved(this.model) && this.ctx.isModified(this.model));
    }


    protected <T> T getOriginal(String attributeQualifier)
    {
        if(this.ctx.isModified(this.model, attributeQualifier))
        {
            return (T)this.modelContext.getOriginalValue(attributeQualifier);
        }
        return (T)this.modelService.getAttributeValue(this.model, attributeQualifier);
    }
}
