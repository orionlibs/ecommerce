package de.hybris.platform.ruleengine.kie.api.builder.impl;

import org.kie.api.builder.KieScanner;
import org.kie.api.builder.KieScannerFactoryService;
import org.kie.api.internal.utils.KieService;

public class DummyKieScannerFactoryService implements KieScannerFactoryService, KieService
{
    public KieScanner newKieScanner()
    {
        return (KieScanner)new DummyKieScanner();
    }
}
