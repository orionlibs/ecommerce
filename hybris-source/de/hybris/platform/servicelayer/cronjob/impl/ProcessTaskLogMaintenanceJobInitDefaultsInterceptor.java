package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.ProcessTaskLogMaintenanceJobModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public class ProcessTaskLogMaintenanceJobInitDefaultsInterceptor implements InitDefaultsInterceptor<ProcessTaskLogMaintenanceJobModel>
{
    private int queryCount = 500;
    private int age = 50;
    private int numberOfLogs = 10;


    public void onInitDefaults(ProcessTaskLogMaintenanceJobModel model, InterceptorContext ctx)
    {
        model.setQueryCount(getQueryCount());
        model.setAge(getAge());
        model.setNumberOfLogs(getNumberOfLogs());
    }


    public int getQueryCount()
    {
        return this.queryCount;
    }


    public void setQueryCount(int queryCount)
    {
        this.queryCount = queryCount;
    }


    public int getAge()
    {
        return this.age;
    }


    public void setAge(int age)
    {
        this.age = age;
    }


    public int getNumberOfLogs()
    {
        return this.numberOfLogs;
    }


    public void setNumberOfLogs(int numberOfLogs)
    {
        this.numberOfLogs = numberOfLogs;
    }
}
