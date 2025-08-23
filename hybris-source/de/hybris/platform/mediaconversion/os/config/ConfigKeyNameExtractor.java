package de.hybris.platform.mediaconversion.os.config;

class ConfigKeyNameExtractor implements NameExtractor<String>
{
    private final String prefix;


    ConfigKeyNameExtractor(String prefix)
    {
        this.prefix = prefix;
    }


    public String extract(String keyIn)
    {
        String key = keyIn;
        if(key.startsWith(this.prefix))
        {
            key = key.substring(this.prefix.length());
        }
        while(key.length() > 0 && key.charAt(0) == '.')
        {
            key = key.substring(1);
        }
        int idx = key.indexOf('.');
        return (idx == -1) ? key : key.substring(0, idx);
    }
}
