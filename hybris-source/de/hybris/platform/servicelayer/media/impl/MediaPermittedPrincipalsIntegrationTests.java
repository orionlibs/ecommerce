package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.Transaction;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MediaPermittedPrincipalsIntegrationTests extends ServicelayerBaseTest
{
    @Resource
    private UserService userService;
    @Resource
    private ModelService modelService;


    @Test
    public void shouldCorrectlySetPermittedPrincipalWhenSettingTwice() throws InterruptedException
    {
        AtomicInteger size = new AtomicInteger(0);
        Transaction.current().begin();
        createUserRight();
        MediaModel media = createMedia();
        Transaction.current().commit();
        Long pk = Long.valueOf(media.getPk().getLongValue());
        media.setPermittedPrincipals(Arrays.asList(new PrincipalModel[] {(PrincipalModel)this.userService.getUserForUID("admin")}));
        this.modelService.save(media);
        Assertions.assertThat(media.getPermittedPrincipals()).hasSize(1);
        Thread thread = new Thread((Runnable)new Object(this, pk, size));
        thread.start();
        thread.join();
        Assertions.assertThat(size.get()).isEqualTo(1);
    }


    @Test
    public void shouldCorrectlySetDeniedPrincipalWhenSettingTwice() throws InterruptedException
    {
        AtomicInteger size = new AtomicInteger(0);
        Transaction.current().begin();
        createUserRight();
        MediaModel media = createMedia();
        Transaction.current().commit();
        Long pk = Long.valueOf(media.getPk().getLongValue());
        media.setDeniedPrincipals(Arrays.asList(new PrincipalModel[] {(PrincipalModel)this.userService.getUserForUID("admin")}));
        this.modelService.save(media);
        Assertions.assertThat(media.getDeniedPrincipals()).hasSize(1);
        Thread thread = new Thread((Runnable)new Object(this, pk, size));
        thread.start();
        thread.join();
        Assertions.assertThat(size.get()).isEqualTo(1);
    }


    private MediaModel createMedia()
    {
        MediaModel media = (MediaModel)this.modelService.create(CatalogUnawareMediaModel.class);
        media.setCode(UUID.randomUUID().toString());
        this.modelService.save(media);
        return media;
    }


    private void createUserRight()
    {
        UserRightModel userRight = (UserRightModel)this.modelService.create(UserRightModel.class);
        userRight.setCode("read");
        this.modelService.save(userRight);
    }
}
