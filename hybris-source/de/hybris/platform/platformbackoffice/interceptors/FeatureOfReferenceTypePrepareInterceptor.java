package de.hybris.platform.platformbackoffice.interceptors;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

public class FeatureOfReferenceTypePrepareInterceptor implements PrepareInterceptor<ClassAttributeAssignmentModel>
{
    public void onPrepare(ClassAttributeAssignmentModel assignment, InterceptorContext ctx) throws InterceptorException
    {
        if(assignment.getReferenceType() != null && wasFeatureTypeChangedFromReferenceToNonReference(assignment))
        {
            assignment.setReferenceType(null);
        }
    }


    protected boolean wasFeatureTypeChangedFromReferenceToNonReference(ClassAttributeAssignmentModel assignment)
    {
        return (!assignment.getItemModelContext().isNew() && assignment
                        .getItemModelContext()
                        .getOriginalValue("attributeType") == ClassificationAttributeTypeEnum.REFERENCE && assignment
                        .getAttributeType() != ClassificationAttributeTypeEnum.REFERENCE);
    }
}
