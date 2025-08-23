package de.hybris.platform.warehousing.atp.formula.services;

import de.hybris.platform.warehousing.model.AtpFormulaModel;
import java.util.Collection;
import java.util.Map;

public interface AtpFormulaService
{
    AtpFormulaModel getAtpFormulaByCode(String paramString);


    Collection<AtpFormulaModel> getAllAtpFormula();


    AtpFormulaModel createAtpFormula(AtpFormulaModel paramAtpFormulaModel);


    AtpFormulaModel updateAtpFormula(AtpFormulaModel paramAtpFormulaModel);


    void deleteAtpFormula(String paramString);


    Long getAtpValueFromFormula(AtpFormulaModel paramAtpFormulaModel, Map<String, Object> paramMap);
}
