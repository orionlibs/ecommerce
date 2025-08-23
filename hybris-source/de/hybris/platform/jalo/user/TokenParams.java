package de.hybris.platform.jalo.user;

public class TokenParams
{
    final String uid;
    final String languageISO;
    final String plainTextPassword;
    final User user;
    final Integer ttl;
    final String delimiter;


    TokenParams(Builder builder)
    {
        this.uid = builder.uid;
        this.languageISO = builder.languageISO;
        this.plainTextPassword = builder.plainTextPassword;
        this.user = builder.user;
        this.ttl = builder.ttl;
        this.delimiter = builder.delimiter;
    }


    public String getUid()
    {
        return this.uid;
    }


    public String getLanguageISO()
    {
        return this.languageISO;
    }


    public String getPlainTextPassword()
    {
        return this.plainTextPassword;
    }


    public User getUser()
    {
        return this.user;
    }


    public Integer getTtl()
    {
        return this.ttl;
    }


    public String getDelimiter()
    {
        return this.delimiter;
    }
}
