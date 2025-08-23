/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util.au;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.core.OrderComparator;
import org.zkoss.zk.au.AuResponse;

/**
 * Registry for processors capable of processing different {@link org.zkoss.zk.au.AuResponse}
 */
public class AuResponseProcessorRegistry
{
    private final List<AuResponseProcessor> processors = new ArrayList<>();


    public AuResponseProcessorRegistry registerProcessor(final AuResponseProcessor<?> processor)
    {
        processors.add(processor);
        processors.sort(OrderComparator.INSTANCE);
        return this;
    }


    public void process(final AuResponse auResponse)
    {
        final Optional<AuResponseProcessor> auResponseProcessor = processors.stream()
                        .filter(processor -> processor.canHandle(auResponse)).findFirst();
        auResponseProcessor.ifPresent(processor -> processor.process(auResponse));
    }


    protected List<AuResponseProcessor> getProcessors()
    {
        return Collections.unmodifiableList(processors);
    }
}
