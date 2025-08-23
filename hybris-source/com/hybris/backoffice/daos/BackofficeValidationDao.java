package com.hybris.backoffice.daos;

import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Collection;

public interface BackofficeValidationDao
{
    Collection<ConstraintGroupModel> getConstraintGroups(Collection<String> paramCollection);
}
