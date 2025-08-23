package de.hybris.platform.servicelayer.internal.polyglot;

public final class PolyglotPersistenceServiceLayerSupport
{
    private static final UnitOfWorkHolder UNIT_OF_WORK_HOLDER = new UnitOfWorkHolder();
    private static final UnitOfWorkInterceptor UNIT_OF_WORK_INTERCEPTOR = new UnitOfWorkInterceptor(UNIT_OF_WORK_HOLDER);


    public static ServiceLayerPersistenceInterceptor getServiceLayerPersistenceInterceptor()
    {
        return (ServiceLayerPersistenceInterceptor)UNIT_OF_WORK_INTERCEPTOR;
    }


    static UnitOfWorkHolder getUnitOfWorkHolder()
    {
        return UNIT_OF_WORK_HOLDER;
    }
}
