package de.hybris.platform.platformbackoffice.services;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

public interface ClassificationAttributeAssignmentService
{
    ClassAttributeAssignmentModel findClassAttributeAssignment(String paramString);


    ClassAttributeAssignmentModel findClassAttributeAssignment(String paramString1, String paramString2, String paramString3, String paramString4);
}
