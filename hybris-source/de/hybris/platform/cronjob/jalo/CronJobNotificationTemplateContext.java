package de.hybris.platform.cronjob.jalo;

public interface CronJobNotificationTemplateContext
{
    String getCronJobName();


    String getStartDate();


    String getEndDate();


    String getDuration();


    String getResult();


    String getStatus();
}
