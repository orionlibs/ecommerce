package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.Assume;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class PersistenceTestUtils
{
    public static <T extends AbstractItemModel> void saveAndVerifyThatPersistedThroughSld(ModelService modelService, T item)
    {
        Assertions.assertThat(modelService).isNotNull();
        Assertions.assertThat(item).isNotNull();
        if(modelService == null || item == null)
        {
            return;
        }
        Assume.assumeFalse(PersistenceUtils.isPersistenceLagacyModeEnabledInConfig());
        DefaultModelService modelServiceSpy = (DefaultModelService)Mockito.spy(modelService);
        WritePersistenceGateway writePersistenceGatewaySpy = (WritePersistenceGateway)Mockito.spy(modelServiceSpy.getWritePersistenceGateway());
        ((DefaultModelService)Mockito.doReturn(writePersistenceGatewaySpy).when(modelServiceSpy)).getWritePersistenceGateway();
        modelServiceSpy.save(item);
        ArgumentCaptor<ChangeSet> changeSet = ArgumentCaptor.forClass(ChangeSet.class);
        ((WritePersistenceGateway)Mockito.verify(writePersistenceGatewaySpy, Mockito.times(1))).persist((ChangeSet)changeSet.capture());
        Assertions.assertThat(((ChangeSet)changeSet.getValue()).getEntityRecords().stream().anyMatch(er -> er.getPK().equals(item.getPk()))).isTrue();
    }


    public static void verifyThatUnderlyingPersistenceObjectIsSld(ItemModel item)
    {
        Assertions.assertThat(item).isNotNull();
        if(item == null)
        {
            return;
        }
        Assume.assumeFalse(PersistenceUtils.isPersistenceLagacyModeEnabledInConfig());
        if(item.getItemModelContext() instanceof ItemModelContextImpl)
        {
            ItemModelContextImpl itemModelContext = (ItemModelContextImpl)item.getItemModelContext();
            Assertions.assertThat(((PersistenceObjectInternal)itemModelContext.getPersistenceSource().getLatest()).isBackedByJaloItem())
                            .isFalse();
        }
    }


    public static Date adjustToDB(Date date)
    {
        if(Config.isMySQLUsed() && !Config.getBoolean("mysql.allow.fractional.seconds", true))
        {
            return new Date(date.getTime() / 1000L * 1000L);
        }
        return date;
    }
}
