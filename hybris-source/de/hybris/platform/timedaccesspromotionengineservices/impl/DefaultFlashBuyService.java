package de.hybris.platform.timedaccesspromotionengineservices.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengineservices.maintenance.RuleMaintenanceService;
import de.hybris.platform.servicelayer.cronjob.CronJobDao;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.JobDao;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.daos.FlashBuyDao;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCronJobModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

public class DefaultFlashBuyService implements FlashBuyService
{
    private static final String PROMOTION_SOURCE_RULE_MUST_NOT_BE_NULL = "Promotion source rule must not be null";
    private static final String SET_MAX_ORDER_QUANTITY_JOB_CODE = "setMaxOrderQuantityJob";
    private static final String RESET_MAX_ORDER_QUANTITY_JOB_CODE = "resetMaxOrderQuantityJob";
    private FlashBuyDao flashBuyDao;
    private RuleMaintenanceService ruleMaintenanceService;
    private PromotionDao promotionDao;
    private JobDao jobDao;
    private CronJobDao cronJobDao;
    private ModelService modelService;
    private CronJobService cronJobService;
    private ProductDao productDao;
    private static final int MINUS_SECOND = 5;


    public Optional<ProductModel> getProductForPromotion(AbstractPromotionModel promotion)
    {
        ServicesUtil.validateParameterNotNull(promotion, "Parameter promotion must not be null");
        return getFlashBuyDao().findProductByPromotion(promotion);
    }


    public AbstractPromotionModel getPromotionByCode(String promotionCode)
    {
        ServicesUtil.validateParameterNotNull(promotionCode, "Parameter promotionCode must not be null");
        return getPromotionDao().findPromotionByCode(promotionCode);
    }


    public Optional<FlashBuyCouponModel> getFlashBuyCouponByPromotionCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Promotion code must not be null");
        return getFlashBuyDao().findFlashBuyCouponByPromotionCode(code);
    }


    public List<PromotionSourceRuleModel> getPromotionSourceRulesByProductCode(String productCode)
    {
        ServicesUtil.validateParameterNotNull(productCode, "Product code must not be null");
        return getFlashBuyDao().findPromotionSourceRuleByProduct(productCode);
    }


    public void undeployFlashBuyPromotion(PromotionSourceRuleModel promotionSourceRule)
    {
        ServicesUtil.validateParameterNotNull(promotionSourceRule, "Promotion source rule must not be null");
        promotionSourceRule.getEngineRules().stream().forEach(engineRule -> {
            String moduleName = getModuleName((DroolsRuleModel)engineRule);
            getRuleMaintenanceService().undeployRules(Arrays.asList(new PromotionSourceRuleModel[] {promotionSourceRule}, ), moduleName);
        });
    }


    public void createCronJobForFlashBuyCoupon(FlashBuyCouponModel coupon)
    {
        ServicesUtil.validateParameterNotNull(coupon, "Parameter coupon must not be null");
        deleteCronJobAndTrigger(coupon);
        if(!coupon.getActive().booleanValue() || coupon.getRule() == null)
        {
            return;
        }
        List<ProductForPromotionSourceRuleModel> productForPromotionSourceRules = getProductForPromotionSourceRule(coupon
                        .getRule());
        if(CollectionUtils.isNotEmpty(productForPromotionSourceRules))
        {
            ServicelayerJobModel setMaxQtyJob = getFlashBuyJob("setMaxOrderQuantityJob");
            FlashBuyCronJobModel setMaxQtyCronJob = createFlashBuyCronJob(coupon, setMaxQtyJob);
            if(coupon.getStartDate() == null || coupon.getStartDate().before(Calendar.getInstance().getTime()))
            {
                getCronJobService().performCronJob((CronJobModel)setMaxQtyCronJob, true);
            }
            else
            {
                createSetMaxQtyJobTrigger(coupon, setMaxQtyCronJob);
            }
            ServicelayerJobModel resetMaxQtyJob = getFlashBuyJob("resetMaxOrderQuantityJob");
            FlashBuyCronJobModel resetMaxQtyCronJob = createFlashBuyCronJob(coupon, resetMaxQtyJob);
            if(coupon.getEndDate() != null)
            {
                createResetMaxQtyJobTrigger(coupon, resetMaxQtyCronJob);
            }
        }
    }


    public void performFlashBuyCronJob(FlashBuyCouponModel coupon)
    {
        ServicesUtil.validateParameterNotNull(coupon, "Parameter coupon must not be null");
        String cronJobCode = coupon.getCouponId() + "resetMaxOrderQuantityJob";
        FlashBuyCronJobModel cronJob = (FlashBuyCronJobModel)getCronJobService().getCronJob(cronJobCode);
        if(cronJob != null)
        {
            getCronJobService().performCronJob((CronJobModel)cronJob, true);
        }
    }


    public void deleteCronJobAndTrigger(FlashBuyCouponModel coupon)
    {
        List<CronJobModel> setQuantityCronJobs = getCronJobDao().findCronJobs(coupon
                        .getCouponId() + "setMaxOrderQuantityJob");
        List<CronJobModel> resetQuantityCronJobs = getCronJobDao().findCronJobs(coupon
                        .getCouponId() + "resetMaxOrderQuantityJob");
        if(CollectionUtils.isNotEmpty(setQuantityCronJobs))
        {
            setQuantityCronJobs.forEach(cronJob -> {
                List<TriggerModel> triggerModels = cronJob.getTriggers();
                if(CollectionUtils.isNotEmpty(triggerModels))
                {
                    getModelService().removeAll(triggerModels);
                }
            });
            getModelService().removeAll(setQuantityCronJobs);
        }
        if(CollectionUtils.isNotEmpty(resetQuantityCronJobs))
        {
            resetQuantityCronJobs.forEach(cronJob -> {
                List<TriggerModel> triggerModels = cronJob.getTriggers();
                if(CollectionUtils.isNotEmpty(triggerModels))
                {
                    getModelService().removeAll(triggerModels);
                }
            });
            getModelService().removeAll(resetQuantityCronJobs);
        }
    }


    protected void createSetMaxQtyJobTrigger(FlashBuyCouponModel coupon, FlashBuyCronJobModel cronJob)
    {
        TriggerModel triggerModel = new TriggerModel();
        Date endDate = coupon.getStartDate();
        DateTime date = new DateTime(endDate);
        date = date.minusSeconds(5);
        String cronExpress = String.format("%d %d %d %d %d ? %d", new Object[] {Integer.valueOf(date.getSecondOfMinute()), Integer.valueOf(date.getMinuteOfHour()),
                        Integer.valueOf(date.getHourOfDay()), Integer.valueOf(date.getDayOfMonth()), Integer.valueOf(date.getMonthOfYear()), Integer.valueOf(date.getYear())});
        triggerModel.setActive(Boolean.TRUE);
        triggerModel.setCronJob((CronJobModel)cronJob);
        triggerModel.setCronExpression(cronExpress);
        getModelService().save(triggerModel);
    }


    protected void createResetMaxQtyJobTrigger(FlashBuyCouponModel coupon, FlashBuyCronJobModel cronJob)
    {
        TriggerModel triggerModel = new TriggerModel();
        Date endDate = coupon.getEndDate();
        DateTime date = new DateTime(endDate);
        String cronExpress = String.format("%d %d %d %d %d ? %d", new Object[] {Integer.valueOf(date.getSecondOfMinute()), Integer.valueOf(date.getMinuteOfHour()),
                        Integer.valueOf(date.getHourOfDay()), Integer.valueOf(date.getDayOfMonth()), Integer.valueOf(date.getMonthOfYear()), Integer.valueOf(date.getYear())});
        triggerModel.setActive(Boolean.TRUE);
        triggerModel.setCronJob((CronJobModel)cronJob);
        triggerModel.setCronExpression(cronExpress);
        getModelService().save(triggerModel);
    }


    protected FlashBuyCronJobModel createFlashBuyCronJob(FlashBuyCouponModel coupon, ServicelayerJobModel job)
    {
        String jobCode = coupon.getCouponId() + coupon.getCouponId();
        FlashBuyCronJobModel cronJob = (FlashBuyCronJobModel)getModelService().create(FlashBuyCronJobModel.class);
        cronJob.setFlashBuyCoupon(coupon);
        cronJob.setCode(jobCode);
        cronJob.setRemoveOnExit(Boolean.TRUE);
        cronJob.setJob((JobModel)job);
        getModelService().save(cronJob);
        return cronJob;
    }


    public List<ProductForPromotionSourceRuleModel> getProductForPromotionSourceRule(PromotionSourceRuleModel sourceRule)
    {
        ServicesUtil.validateParameterNotNull(sourceRule, "Promotion source rule must not be null");
        return getFlashBuyDao().findProductForPromotionSourceRules(sourceRule);
    }


    public List<ProductModel> getAllProductsByPromotionSourceRule(PromotionSourceRuleModel rule)
    {
        ServicesUtil.validateParameterNotNull(rule, "Promotion source rule must not be null");
        return getFlashBuyDao().findAllProductsByPromotionSourceRule(rule);
    }


    public List<FlashBuyCouponModel> getFlashBuyCouponByProduct(ProductModel product)
    {
        ServicesUtil.validateParameterNotNull(product, "Product source rule must not be null");
        return getFlashBuyDao().findFlashBuyCouponByProduct(product);
    }


    protected ServicelayerJobModel getFlashBuyJob(String jobCode)
    {
        ServicelayerJobModel job = null;
        List<JobModel> jobs = getJobDao().findJobs(jobCode);
        if(CollectionUtils.isEmpty(jobs))
        {
            job = new ServicelayerJobModel();
            job.setSpringId(jobCode);
            job.setCode(jobCode);
            getModelService().save(job);
        }
        else
        {
            job = (ServicelayerJobModel)jobs.get(0);
        }
        return job;
    }


    public List<ProductModel> getProductForCode(String productCode)
    {
        ServicesUtil.validateParameterNotNull(productCode, "Parameter productCode must not be null");
        List<ProductModel> productList = getProductDao().findProductsByCode(productCode);
        if(CollectionUtils.isNotEmpty(productList))
        {
            return productList;
        }
        return new ArrayList<>();
    }


    protected <T extends DroolsRuleModel> String getModuleName(T rule)
    {
        return RuleMappings.moduleName((DroolsRuleModel)rule);
    }


    protected RuleMaintenanceService getRuleMaintenanceService()
    {
        return this.ruleMaintenanceService;
    }


    public void setRuleMaintenanceService(RuleMaintenanceService ruleMaintenanceService)
    {
        this.ruleMaintenanceService = ruleMaintenanceService;
    }


    protected FlashBuyDao getFlashBuyDao()
    {
        return this.flashBuyDao;
    }


    public void setFlashBuyDao(FlashBuyDao flashBuyDao)
    {
        this.flashBuyDao = flashBuyDao;
    }


    protected PromotionDao getPromotionDao()
    {
        return this.promotionDao;
    }


    public void setPromotionDao(PromotionDao promotionDao)
    {
        this.promotionDao = promotionDao;
    }


    protected JobDao getJobDao()
    {
        return this.jobDao;
    }


    public void setJobDao(JobDao jobDao)
    {
        this.jobDao = jobDao;
    }


    protected CronJobDao getCronJobDao()
    {
        return this.cronJobDao;
    }


    public void setCronJobDao(CronJobDao cronJobDao)
    {
        this.cronJobDao = cronJobDao;
    }


    protected CronJobService getCronJobService()
    {
        return this.cronJobService;
    }


    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ProductDao getProductDao()
    {
        return this.productDao;
    }


    public void setProductDao(ProductDao productDao)
    {
        this.productDao = productDao;
    }
}
