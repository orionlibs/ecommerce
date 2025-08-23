package com.hybris.cis.api.executor;

public class ServiceMethodRequest<T>
{
    private T model;


    public ServiceMethodRequest(T model)
    {
        this.model = model;
    }


    public T getModel()
    {
        return this.model;
    }


    public void setModel(T model)
    {
        this.model = model;
    }


    public String toString()
    {
        return "ServiceMethodRequest [model=" + ((this.model == null) ? "null" : this.model.toString()) + "]";
    }


    public static <T> ServiceMethodRequest<T> create(T model)
    {
        return new ServiceMethodRequest<>(model);
    }
}
