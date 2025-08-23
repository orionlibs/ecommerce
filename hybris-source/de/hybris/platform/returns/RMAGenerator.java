package de.hybris.platform.returns;

import de.hybris.platform.returns.model.ReturnRequestModel;

public interface RMAGenerator
{
    String generateRMA(ReturnRequestModel paramReturnRequestModel);
}
