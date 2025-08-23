package de.hybris.platform.payment.commands;

public interface Command<R, O>
{
    O perform(R paramR);
}
