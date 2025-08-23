package de.hybris.platform.campaigns.dao.impl;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.campaigns.dao.CampaignDao;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultCampaignDaoIT extends ServicelayerTransactionalTest
{
    @Resource
    private CampaignDao campaignDao;
    @Resource
    private ModelService modelService;
    @Resource
    private TimeService timeService;
    private List<CampaignModel> allCampaigns;
    private List<CampaignModel> activeCampaigns;
    private Date nowDate;


    @Before
    public void setUp() throws Exception
    {
        Instant instant = Instant.ofEpochMilli(getTimeService().getCurrentTime().getTime());
        LocalDateTime now = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        this.nowDate = Date.from(now.toInstant(ZoneOffset.UTC));
        Date pastDate = Date.from(now.minusDays(2L).toInstant(ZoneOffset.UTC));
        Date futureDate = Date.from(now.plusDays(2L).toInstant(ZoneOffset.UTC));
        CampaignModel activeCampaign = createCampaign("active campaign", pastDate, futureDate, true);
        CampaignModel rollingActiveCampaign = createCampaign("rolling active campaign", pastDate, null, true);
        CampaignModel sinceEverActiveCampaign = createCampaign("since ever active campaign", null, futureDate, true);
        this.activeCampaigns = Lists.newArrayList();
        this.activeCampaigns.add(activeCampaign);
        this.activeCampaigns.add(rollingActiveCampaign);
        this.activeCampaigns.add(sinceEverActiveCampaign);
        this.allCampaigns = Lists.newArrayList();
        this.allCampaigns.add(activeCampaign);
        this.allCampaigns.add(rollingActiveCampaign);
        this.allCampaigns.add(sinceEverActiveCampaign);
        this.allCampaigns.add(createCampaign("disabled campaign", pastDate, futureDate, false));
        this.allCampaigns.add(createCampaign("past campaign", pastDate, pastDate, false));
        this.allCampaigns.add(createCampaign("rolling disabled campaign", pastDate, null, false));
        this.allCampaigns.add(createCampaign("forthcoming campaign", futureDate, null, true));
    }


    @Test
    public void shouldProvideAllCampaigns() throws Exception
    {
        List<CampaignModel> localAllCampaigns = getCampaignDao().findAllCampaigns();
        Assertions.assertThat(localAllCampaigns).containsExactlyElementsOf(this.allCampaigns);
    }


    @Test
    public void shouldProvideCampaignByCode() throws Exception
    {
        String code = ((CampaignModel)this.allCampaigns.iterator().next()).getCode();
        CampaignModel matchingCampaign = getCampaignDao().findCampaignByCode(code);
        Assertions.assertThat(matchingCampaign.getCode()).isEqualTo(code);
    }


    @Test
    public void shouldRaiseExceptionIfNoCampaignWithGivenCode() throws Exception
    {
        String code = "you will never find me";
        Assertions.assertThat(this.allCampaigns.stream().map(CampaignModel::getCode).anyMatch("you will never find me"::equals)).isFalse();
        Throwable throwable = Assertions.catchThrowable(() -> getCampaignDao().findCampaignByCode("you will never find me"));
        Assertions.assertThat(throwable).isInstanceOf(UnknownIdentifierException.class);
    }


    @Test
    public void shouldProvideOnlyActiveCampaigns() throws Exception
    {
        List<CampaignModel> actualActiveCampaigns = getCampaignDao().findActiveCampaigns(this.nowDate);
        Assertions.assertThat(actualActiveCampaigns).containsExactlyElementsOf(this.activeCampaigns);
    }


    protected CampaignModel createCampaign(String code, Date pastDate, Date futureDate, boolean enabled)
    {
        CampaignModel campaign = (CampaignModel)getModelService().create(CampaignModel.class);
        campaign.setCode(code);
        campaign.setStartDate(pastDate);
        campaign.setEndDate(futureDate);
        campaign.setEnabled(Boolean.valueOf(enabled));
        getModelService().save(campaign);
        return campaign;
    }


    protected CampaignDao getCampaignDao()
    {
        return this.campaignDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }
}
