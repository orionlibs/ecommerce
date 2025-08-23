package com.hybris.classificationgroupsservices.handlers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import org.apache.commons.lang3.RegExUtils;

public class ClassFeatureGroupAssignmentFullQualifierHandler extends AbstractDynamicAttributeHandler<String, ClassFeatureGroupAssignmentModel>
{
    private static final String NON_ALPHA_NUMERIC_CHARACTER_REGEX = "\\W";
    private static final String SEPARATOR = "_";


    private static String removeNonAlphaNumericCharacters(String name)
    {
        return RegExUtils.removePattern(name, "\\W");
    }


    public String get(ClassFeatureGroupAssignmentModel model)
    {
        String fullQualifier = String.join("_", new CharSequence[] {getCatalogId(model), getVersion(model), getCategoryCode(model),
                        getAttributeCode(model)});
        return removeNonAlphaNumericCharacters(fullQualifier);
    }


    private String getCatalogId(ClassFeatureGroupAssignmentModel model)
    {
        return model.getClassAttributeAssignment().getClassificationAttribute().getSystemVersion().getCatalog().getId();
    }


    private String getVersion(ClassFeatureGroupAssignmentModel model)
    {
        return model.getClassAttributeAssignment().getClassificationAttribute().getSystemVersion().getVersion();
    }


    private String getCategoryCode(ClassFeatureGroupAssignmentModel model)
    {
        return model.getClassAttributeAssignment().getClassificationClass().getCode();
    }


    private String getAttributeCode(ClassFeatureGroupAssignmentModel model)
    {
        return model.getClassAttributeAssignment().getClassificationAttribute().getCode();
    }
}
