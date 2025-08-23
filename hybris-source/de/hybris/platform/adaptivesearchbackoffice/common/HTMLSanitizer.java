package de.hybris.platform.adaptivesearchbackoffice.common;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HTMLSanitizer
{
    protected static final PolicyFactory POLICY = (new HtmlPolicyBuilder()).allowElements(new String[] {"pre", "address", "em", "hr"}).allowAttributes(new String[] {"class"}).onElements(new String[] {"em"}).toFactory().and(Sanitizers.BLOCKS).and(Sanitizers.FORMATTING)
                    .and(Sanitizers.LINKS).and(Sanitizers.TABLES).and(Sanitizers.STYLES);


    public static String sanitizeHTML(String untrustedHTML)
    {
        return POLICY.sanitize(untrustedHTML);
    }
}
