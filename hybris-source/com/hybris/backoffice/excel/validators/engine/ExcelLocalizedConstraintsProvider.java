package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.daos.BackofficeValidationDao;
import de.hybris.platform.validation.daos.ConstraintDao;
import de.hybris.platform.validation.localized.LocalizedAttributeConstraint;
import de.hybris.platform.validation.localized.LocalizedConstraintsRegistry;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelLocalizedConstraintsProvider
{
    private LocalizedConstraintsRegistry localizedConstraintsRegistry;
    private ConstraintDao constraintDao;
    private BackofficeValidationDao validationDao;


    public Collection<LocalizedAttributeConstraint> getLocalizedAttributeConstraints(Class clazz, List<String> constraintGroups)
    {
        Collection<LocalizedAttributeConstraint> constraints = (Collection<LocalizedAttributeConstraint>)ClassUtils.getAllSuperclasses(clazz).stream().flatMap(aClass -> getLocalizedConstraintsRegistry().get(aClass).getConstraints().stream()).collect(Collectors.toSet());
        constraints.addAll(getLocalizedConstraintsRegistry().get(clazz).getConstraints());
        return filterByConstraintGroups(constraints, constraintGroups);
    }


    private Collection<LocalizedAttributeConstraint> filterByConstraintGroups(Collection<LocalizedAttributeConstraint> constraints, Collection<String> constraintGroups)
    {
        Objects.requireNonNull(getConstraintDao());
        Class[] constraintGroupsAsClasses = (Class[])getValidationDao().getConstraintGroups(constraintGroups).stream().map(getConstraintDao()::getTargetClass).toArray(x$0 -> new Class[x$0]);
        return (Collection<LocalizedAttributeConstraint>)constraints.stream().filter(constraint -> constraint.definedForGroups(constraintGroupsAsClasses))
                        .collect(Collectors.toList());
    }


    public LocalizedConstraintsRegistry getLocalizedConstraintsRegistry()
    {
        return this.localizedConstraintsRegistry;
    }


    @Required
    public void setLocalizedConstraintsRegistry(LocalizedConstraintsRegistry localizedConstraintsRegistry)
    {
        this.localizedConstraintsRegistry = localizedConstraintsRegistry;
    }


    public ConstraintDao getConstraintDao()
    {
        return this.constraintDao;
    }


    @Required
    public void setConstraintDao(ConstraintDao constraintDao)
    {
        this.constraintDao = constraintDao;
    }


    public BackofficeValidationDao getValidationDao()
    {
        return this.validationDao;
    }


    @Required
    public void setValidationDao(BackofficeValidationDao validationDao)
    {
        this.validationDao = validationDao;
    }
}
