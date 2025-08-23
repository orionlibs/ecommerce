package com.hybris.backoffice.excel.validators.engine;

import com.google.common.collect.Lists;
import com.hybris.backoffice.daos.BackofficeValidationDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.validation.daos.ConstraintDao;
import de.hybris.platform.validation.localized.LocalizedAttributeConstraint;
import de.hybris.platform.validation.localized.LocalizedConstraintsRegistry;
import de.hybris.platform.validation.localized.TypeLocalizedConstraints;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.validation.groups.Default;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelLocalizedConstraintsProviderTest
{
    @Mock
    private LocalizedConstraintsRegistry localizedConstraintsRegistry;
    @Mock
    private ConstraintDao constraintDao;
    @Mock
    private BackofficeValidationDao validationDao;
    @InjectMocks
    private ExcelLocalizedConstraintsProvider facade;
    private static final Class PRODUCT_MODEL_CLASS = ProductModel.class;
    private static final String DEFAULT_CONSTRAINT_GROUP = "default";
    private ConstraintGroupModel defaultConstraintGroupModel;
    private static final Class DEFAULT_CONSTRAINT_GROUP_CLASS = Default.class;
    private static final List<String> CONSTRAINT_GROUPS = Lists.newArrayList((Object[])new String[] {"default"});


    @Before
    public void setUp() throws Exception
    {
        this.defaultConstraintGroupModel = (ConstraintGroupModel)Mockito.mock(ConstraintGroupModel.class);
        initializeLocalizedConstraintsRegistry();
        BDDMockito.given(this.validationDao.getConstraintGroups(CONSTRAINT_GROUPS)).willReturn(Lists.newArrayList((Object[])new ConstraintGroupModel[] {this.defaultConstraintGroupModel}));
        BDDMockito.given(this.constraintDao.getTargetClass(this.defaultConstraintGroupModel)).willReturn(DEFAULT_CONSTRAINT_GROUP_CLASS);
    }


    @Test
    public void shouldReturnEmptyListWhenNoConstraintsAreDefined()
    {
        setProductConstraints(Collections.emptyList());
        Collection<LocalizedAttributeConstraint> constraints = this.facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS, CONSTRAINT_GROUPS);
        Assertions.assertThat(constraints).isEmpty();
    }


    @Test
    public void shouldReturnConstraintForIncludedGroups()
    {
        LocalizedAttributeConstraint constraintInDefaultGroup = (LocalizedAttributeConstraint)Mockito.mock(LocalizedAttributeConstraint.class);
        BDDMockito.given(Boolean.valueOf(constraintInDefaultGroup.definedForGroups(new Class[] {DEFAULT_CONSTRAINT_GROUP_CLASS}))).willReturn(Boolean.valueOf(true));
        setProductConstraints(Lists.newArrayList((Object[])new LocalizedAttributeConstraint[] {constraintInDefaultGroup}));
        Collection<LocalizedAttributeConstraint> constraints = this.facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS, CONSTRAINT_GROUPS);
        Assertions.assertThat(constraints).hasSize(1);
        Assertions.assertThat(constraints.iterator().next()).isEqualTo(constraintInDefaultGroup);
    }


    @Test
    public void shouldNotReturnConstraintFromNotIncludedGroup()
    {
        LocalizedAttributeConstraint constraintInDifferentGroup = (LocalizedAttributeConstraint)Mockito.mock(LocalizedAttributeConstraint.class);
        BDDMockito.given(Boolean.valueOf(constraintInDifferentGroup.definedForGroups(new Class[] {DEFAULT_CONSTRAINT_GROUP_CLASS}))).willReturn(Boolean.valueOf(false));
        setProductConstraints(Lists.newArrayList((Object[])new LocalizedAttributeConstraint[] {constraintInDifferentGroup}));
        Collection<LocalizedAttributeConstraint> constraints = this.facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS, CONSTRAINT_GROUPS);
        Assertions.assertThat(constraints).isEmpty();
    }


    @Test
    public void shouldReturnConstraintFromIncludedGroupAndFilterOutOthers()
    {
        LocalizedAttributeConstraint constraintInDefaultGroup = (LocalizedAttributeConstraint)Mockito.mock(LocalizedAttributeConstraint.class);
        LocalizedAttributeConstraint constraintInOtherGroup = (LocalizedAttributeConstraint)Mockito.mock(LocalizedAttributeConstraint.class);
        BDDMockito.given(Boolean.valueOf(constraintInDefaultGroup.definedForGroups(new Class[] {DEFAULT_CONSTRAINT_GROUP_CLASS}))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(constraintInOtherGroup.definedForGroups(new Class[] {DEFAULT_CONSTRAINT_GROUP_CLASS}))).willReturn(Boolean.valueOf(false));
        setProductConstraints(Lists.newArrayList((Object[])new LocalizedAttributeConstraint[] {constraintInDefaultGroup, constraintInOtherGroup}));
        Collection<LocalizedAttributeConstraint> constraints = this.facade.getLocalizedAttributeConstraints(PRODUCT_MODEL_CLASS, CONSTRAINT_GROUPS);
        Assertions.assertThat(constraints).hasSize(1);
        Assertions.assertThat(constraints.iterator().next()).isEqualTo(constraintInDefaultGroup);
    }


    private void initializeLocalizedConstraintsRegistry()
    {
        TypeLocalizedConstraints constraintsForSupertypes = (TypeLocalizedConstraints)Mockito.mock(TypeLocalizedConstraints.class);
        BDDMockito.given(constraintsForSupertypes.getConstraints()).willReturn(Lists.newArrayList());
        BDDMockito.given(this.localizedConstraintsRegistry.get((Class)Matchers.any())).willReturn(constraintsForSupertypes);
    }


    private void setProductConstraints(List<LocalizedAttributeConstraint> productConstraints)
    {
        TypeLocalizedConstraints constraintsForProduct = (TypeLocalizedConstraints)Mockito.mock(TypeLocalizedConstraints.class);
        BDDMockito.given(constraintsForProduct.getConstraints()).willReturn(productConstraints);
        BDDMockito.given(this.localizedConstraintsRegistry.get(PRODUCT_MODEL_CLASS)).willReturn(constraintsForProduct);
    }
}
