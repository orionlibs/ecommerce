package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class ModelGetPk extends AbstractPerformanceTest
{
    private ModelService modelService;
    private LanguageModel language;


    public void executeBlock()
    {
        this.language.getPk();
    }


    public String getTestName()
    {
        return "Model getPk";
    }


    public void cleanup()
    {
        this.modelService.remove(this.language);
    }


    public void prepare()
    {
        this.language = (LanguageModel)this.modelService.create(LanguageModel.class);
        this.language.setIsocode("$$test$$");
        this.modelService.save(this.language);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
