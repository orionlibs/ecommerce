package de.hybris.platform.payment.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PaymentManager extends GeneratedPaymentManager
{
    public static final PaymentManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PaymentManager)em.getExtension("payment");
    }
}
