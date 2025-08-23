package com.hybris.classificationgroupsservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class ClassificationgroupsservicesManager extends GeneratedClassificationgroupsservicesManager
{
    public static final ClassificationgroupsservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (ClassificationgroupsservicesManager)em.getExtension("classificationgroupsservices");
    }
}
