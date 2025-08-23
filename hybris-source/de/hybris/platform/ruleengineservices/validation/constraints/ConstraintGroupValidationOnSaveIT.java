package de.hybris.platform.ruleengineservices.validation.constraints;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.daos.ConstraintDao;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Collections;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ConstraintGroupValidationOnSaveIT extends ServicelayerTest
{
    private static final String CONSTRAINT_ID = "testPattern";
    @Resource
    protected ModelService modelService;
    @Resource
    protected ConstraintDao constraintDao;


    @Before
    public void setUp() throws Exception
    {
        importCsv("/ruleengineservices/test/constraint/validation-test-data.impex", "UTF-8");
    }


    @Test
    public void testConstraintGroupSave()
    {
        ConstraintGroupModel constraintGroup = (ConstraintGroupModel)this.modelService.create(ConstraintGroupModel.class);
        constraintGroup.setId("defaultBackofficeValidationGroup");
        constraintGroup.setInterfaceName("de.hybris.platform.validation.groupinterfaces.DefaultBackofficeValidationGroup");
        AbstractConstraintModel objectPatternConstraint = getObjectPatternConstraint();
        constraintGroup.setConstraints(Collections.singleton(objectPatternConstraint));
        objectPatternConstraint.setConstraintGroups(Collections.singleton(constraintGroup));
        this.modelService.saveAll(new Object[] {constraintGroup, objectPatternConstraint});
    }


    protected AbstractConstraintModel getObjectPatternConstraint()
    {
        return (AbstractConstraintModel)this.constraintDao.getAllConstraints().stream().filter(c -> "testPattern".equals(c.getId())).findFirst()
                        .orElseThrow(() -> new RuntimeException("No Constraint with ID testPattern"));
    }
}
