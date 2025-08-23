package de.hybris.platform.cockpit.session.impl;

public class AdminPerspective extends BaseUICockpitPerspective
{
    private BrowserAreaListener browserAreaListener;


    protected BrowserAreaListener getBrowserAreaListener()
    {
        if(this.browserAreaListener == null)
        {
            this.browserAreaListener = (BrowserAreaListener)new Object(this, this);
        }
        return this.browserAreaListener;
    }
}
