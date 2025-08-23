package de.hybris.bootstrap.beangenerator;

import de.hybris.bootstrap.beangenerator.definitions.model.ClassNameAware;
import java.util.Collection;

public interface BeansPostProcessor
{
    public static final BeansPostProcessor DEFAULT;


    Collection<? extends ClassNameAware> postProcess(Collection<? extends ClassNameAware> paramCollection);


    static
    {
        DEFAULT = (beans -> beans);
    }
}
