package de.hybris.platform.mediaconversion.os.process;

class Handle<T>
{
    private T value;


    public T get()
    {
        return this.value;
    }


    public void set(T value)
    {
        this.value = value;
    }
}
