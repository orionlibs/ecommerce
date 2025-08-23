package de.hybris.platform.licence;

import de.hybris.platform.util.Base64;
import java.util.Properties;

public final class DefaultLicence extends Licence
{
    static final long serialVersionUID = 37341285532234132L;
    private final Properties props = new Properties();
    private final String encodedSignature = "MCwCFEcObi4mygKjc+4CFR028SlLMzcGAhR+JyISQJdqbvpbdXe/Wat5DHGHag==";


    protected DefaultLicence()
    {
        this.props.put("licence.advancedsecurity", "true");
        this.props.put("licence.highperformance", "true");
        this.props.put("licence.version", "5.0");
        this.props.put("licence.email", "support@hybris.com");
        this.props.put("licence.name", "hybris Demo Licence");
        this.props.put("licence.eulaversion", "2.0");
        this.props.put("licence.id", "000001-001");
        this.props.put("licence.date", "2013.03.27");
        this.props.put("licence.clustering", "true");
        this.props.put("licence.endcustomer", "hybris AG");
        this.props.put("licence.expiration", "");
    }


    public Properties getLicenceProperties()
    {
        return this.props;
    }


    public byte[] getSignature()
    {
        return Base64.decode("MCwCFEcObi4mygKjc+4CFR028SlLMzcGAhR+JyISQJdqbvpbdXe/Wat5DHGHag==");
    }
}
