package de.hybris.platform.couponwebservices.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponwebservices.dto.CodeGenerationConfigurationWsDTO;
import org.springframework.util.Assert;

public class CodeGenerationConfigurationWsPopulator implements Populator<CodeGenerationConfigurationModel, CodeGenerationConfigurationWsDTO>
{
    public void populate(CodeGenerationConfigurationModel source, CodeGenerationConfigurationWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setName(source.getName());
        target.setCodeSeparator(source.getCodeSeparator());
        target.setCouponPartCount(Integer.valueOf(source.getCouponPartCount()));
        target.setCouponPartLength(Integer.valueOf(source.getCouponPartLength()));
    }
}
