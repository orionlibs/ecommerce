package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.Predicate;

public class NewItemPersistencePredicate implements Predicate
{
    private final ModelService modelService = UISessionUtils.getCurrentSession().getModelService();


    public boolean evaluate(Object object)
    {
        return !this.modelService.isNew(((TypedObject)object).getObject());
    }
}
