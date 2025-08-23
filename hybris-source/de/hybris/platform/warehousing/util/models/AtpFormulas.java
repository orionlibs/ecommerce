package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.warehousing.model.AtpFormulaModel;
import de.hybris.platform.warehousing.util.builder.ATPFormulaModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class AtpFormulas extends AbstractItems<AtpFormulaModel>
{
    public static final String ATPFORMULA_NAME = "hybris";
    public static final String ATPFORMULA_CUSTOM_NAME = "custom";
    private WarehousingDao<AtpFormulaModel> atpFormulaDao;


    public AtpFormulaModel Hybris()
    {
        Boolean AVAILABILITY = Boolean.TRUE;
        Boolean ALLOCATION = Boolean.TRUE;
        Boolean CANCELLATION = Boolean.TRUE;
        Boolean INCREASE = Boolean.TRUE;
        Boolean RESERVED = Boolean.TRUE;
        Boolean SHRINKAGE = Boolean.TRUE;
        Boolean WASTAGE = Boolean.TRUE;
        Boolean RETURNED = Boolean.FALSE;
        Boolean EXTERNAL = Boolean.TRUE;
        return (AtpFormulaModel)getOrSaveAndReturn(() -> (AtpFormulaModel)getAtpFormulaDao().getByCode("hybris"), () -> ATPFormulaModelBuilder.aModel().withCode("hybris").withFormula(AVAILABILITY, ALLOCATION, CANCELLATION, INCREASE, RESERVED, SHRINKAGE, WASTAGE, RETURNED, EXTERNAL).build());
    }


    public AtpFormulaModel customFormula(Boolean includeAvailability, Boolean includeAllocation, Boolean includeCancellation, Boolean includeIncrease, Boolean includeReserved, Boolean includeShrinkage, Boolean includeWastage, Boolean includeReturned, Boolean includeExternal)
    {
        AtpFormulaModel atpFormulaModel = (AtpFormulaModel)getOrSaveAndReturn(() -> (AtpFormulaModel)getAtpFormulaDao().getByCode("custom"),
                        () -> ATPFormulaModelBuilder.aModel().withCode("custom").withFormula(includeAvailability, includeAllocation, includeCancellation, includeIncrease, includeReserved, includeShrinkage, includeWastage, includeReturned, includeExternal).build());
        getModelService().save(atpFormulaModel);
        return atpFormulaModel;
    }


    protected WarehousingDao<AtpFormulaModel> getAtpFormulaDao()
    {
        return this.atpFormulaDao;
    }


    @Required
    public void setAtpFormulaDao(WarehousingDao<AtpFormulaModel> atpFormulaDao)
    {
        this.atpFormulaDao = atpFormulaDao;
    }
}
