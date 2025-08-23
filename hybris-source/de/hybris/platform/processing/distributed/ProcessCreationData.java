package de.hybris.platform.processing.distributed;

import java.util.stream.Stream;

public interface ProcessCreationData
{
    Stream<? extends BatchCreationData> initialBatches();


    String getHandlerBeanId();


    String getNodeGroup();
}
