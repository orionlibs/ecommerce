package de.hybris.platform.payment.methods;

import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.request.CaptureRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.DeleteSubscriptionRequest;
import de.hybris.platform.payment.commands.request.EnrollmentCheckRequest;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.commands.request.SubscriptionDataRequest;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.commands.result.EnrollmentCheckResult;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.commands.result.SubscriptionDataResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.commands.result.VoidResult;

public interface CardPaymentService extends PaymentMethod
{
    AuthorizationResult authorize(AuthorizationRequest paramAuthorizationRequest);


    AuthorizationResult authorize(SubscriptionAuthorizationRequest paramSubscriptionAuthorizationRequest);


    CaptureResult capture(CaptureRequest paramCaptureRequest);


    CaptureResult partialCapture(PartialCaptureRequest paramPartialCaptureRequest);


    EnrollmentCheckResult enrollmentCheck(EnrollmentCheckRequest paramEnrollmentCheckRequest);


    VoidResult voidCreditOrCapture(VoidRequest paramVoidRequest);


    RefundResult refundStandalone(StandaloneRefundRequest paramStandaloneRefundRequest);


    RefundResult refundFollowOn(FollowOnRefundRequest paramFollowOnRefundRequest);


    SubscriptionResult createSubscription(CreateSubscriptionRequest paramCreateSubscriptionRequest);


    SubscriptionResult updateSubscription(UpdateSubscriptionRequest paramUpdateSubscriptionRequest);


    SubscriptionDataResult getSubscriptionData(SubscriptionDataRequest paramSubscriptionDataRequest);


    SubscriptionResult deleteSubscription(DeleteSubscriptionRequest paramDeleteSubscriptionRequest);
}
