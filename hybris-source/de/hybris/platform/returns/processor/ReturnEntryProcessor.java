package de.hybris.platform.returns.processor;

import de.hybris.platform.returns.model.ReturnEntryModel;
import java.util.List;

public interface ReturnEntryProcessor
{
    void process(List<ReturnEntryModel> paramList);
}
