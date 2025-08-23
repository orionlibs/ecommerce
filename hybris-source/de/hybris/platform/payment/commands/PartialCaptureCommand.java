package de.hybris.platform.payment.commands;

import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.result.CaptureResult;

public interface PartialCaptureCommand extends Command<PartialCaptureRequest, CaptureResult>
{
}
