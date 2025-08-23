package de.hybris.platform.core.threadregistry;

public final class HybrisJUnitUtility
{
    public static void registerJUnitMainThread()
    {
        RegistrableThread.registerThread(OperationInfo.builder()
                        .withCategory(OperationInfo.Category.TEST)
                        .asNotSuspendableOperation()
                        .asJunitOperation()
                        .build());
    }
}
