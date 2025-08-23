package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;

public interface ModelWalkerCallback
{
    boolean foundNewWrapper(ModelWrapper paramModelWrapper);


    void foundNewDependencies(ModelWrapper paramModelWrapper, String paramString, Collection<ModelWrapper> paramCollection);
}
