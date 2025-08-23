package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import java.util.function.Function;

@FunctionalInterface
public interface GroupAssignmentCellValueProvider extends Function<ClassFeatureGroupAssignmentModel, String>
{
}
