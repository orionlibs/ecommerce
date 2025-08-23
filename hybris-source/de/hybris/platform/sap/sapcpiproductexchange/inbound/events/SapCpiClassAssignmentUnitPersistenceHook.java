/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcpiproductexchange.inbound.events;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2services.odata.persistence.hook.PrePersistHook;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SapCpiClassAssignmentUnitPersistenceHook implements PrePersistHook
{
    private static final Logger LOG = LoggerFactory.getLogger(SapCpiClassAssignmentUnitPersistenceHook.class);
    private ClassificationSystemService classificationSystemService;


    @Override
    public Optional<ItemModel> execute(ItemModel item)
    {
        if(item instanceof ClassAttributeAssignmentModel)
        {
            LOG.info("The persistence hook sapCpiClassAssignmentUnitPersistenceHook is called!");
            final ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)item;
            String unitCode = assignment.getSapCpiAssignmentUnitCode();
            assignment.setUnit(unitCode == null || unitCode.matches("\\s*") ? null : classificationSystemService.getAttributeUnitForCode(assignment.getSystemVersion(), unitCode));
            assignment.setSapCpiAssignmentUnitCode(null);
            return Optional.of(item);
        }
        return Optional.of(item);
    }


    protected ClassificationSystemService getClassificationSystemService()
    {
        return classificationSystemService;
    }


    @Required
    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }
}
