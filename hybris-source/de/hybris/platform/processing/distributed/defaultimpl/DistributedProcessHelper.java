package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.processing.distributed.ProcessConcurrentlyModifiedException;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;

public final class DistributedProcessHelper
{
    public static <T> T doInTxWithOptimisticLocking(Supplier<T> action)
    {
        Objects.requireNonNull(action, "action mustn't be null");
        return doInTransaction(() -> doWithOptimisticLocking(action));
    }


    public static <T> T doInTransaction(Supplier<T> action)
    {
        Objects.requireNonNull(action, "action mustn't be null");
        if(Transaction.current().isRunning())
        {
            return action.get();
        }
        try
        {
            return (T)Transaction.current().execute((TransactionBody)new Object(action));
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    public static <T> T doWithOptimisticLocking(Supplier<T> action)
    {
        Objects.requireNonNull(action, "action mustn't be null");
        if(HJMPUtils.isOptimisticLockingEnabled())
        {
            return action.get();
        }
        HJMPUtils.enableOptimisticLocking();
        try
        {
            return action.get();
        }
        catch(Throwable t)
        {
            if(HJMPUtils.isConcurrentModificationException(t))
            {
                throw new ProcessConcurrentlyModifiedException(t);
            }
            throw t;
        }
        finally
        {
            HJMPUtils.clearOptimisticLockingSetting();
        }
    }


    public static boolean isCreated(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        return DistributedProcessState.CREATED.equals(process.getState());
    }


    public static boolean isFinished(DistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        return ImmutableSet.of(DistributedProcessState.FAILED, DistributedProcessState.SUCCEEDED, DistributedProcessState.STOPPED)
                        .contains(process.getState());
    }


    public static FlushInBatchesContext flushInBatches(ModelService modelService, int flushSize)
    {
        Objects.requireNonNull(modelService, "modelService mustn't be null");
        Preconditions.checkArgument((flushSize > 0), "flushSize must be greater than 0");
        return new FlushInBatchesContext(modelService, flushSize);
    }


    public static DistributedProcessHandler getHandler(ApplicationContext applicationContext, String handlerBeanId)
    {
        Objects.requireNonNull(applicationContext, "applicationContext mustn't be null");
        Objects.requireNonNull(handlerBeanId, "handlerBeanId mustn't be null");
        return (DistributedProcessHandler)applicationContext.getBean(handlerBeanId, DistributedProcessHandler.class);
    }


    public static void requireProcessToBeInState(DistributedProcessModel process, DistributedProcessState state)
    {
        Objects.requireNonNull(process, "process mustn't be null");
        Objects.requireNonNull(state, "state mustn't be null");
        Preconditions.checkState(state.equals(process.getState()), "Process %s with code '%s' expected to be in state '%s' but it's in '%s' state", process, process
                        .getCode(), state, process
                        .getState());
    }


    public static Function<Supplier<DistributedProcessModel>, DistributedProcessModel> runInEnvironment(DistributedProcessHandler handler, EnvFeature... features)
    {
        ImmutableSet<EnvFeature> requiredFeatures = Sets.immutableEnumSet(Arrays.asList(features));
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> none = s -> (DistributedProcessModel)s.get();
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> ol = requiredFeatures.contains(EnvFeature.OPTIMISTIC_LOCKING) ? (s -> (DistributedProcessModel)doWithOptimisticLocking(())) : none;
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> tx = requiredFeatures.contains(EnvFeature.TX) ? (s -> (DistributedProcessModel)doInTransaction(())) : ol;
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> sld = requiredFeatures.contains(EnvFeature.SLD) ? (s -> (DistributedProcessModel)PersistenceUtils.doWithChangedPersistenceLegacyMode(false, ())) : tx;
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> duc = s -> executeWithDisabledUniquenessCheck(handler, ());
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> pp = requiredFeatures.contains(EnvFeature.POSTPROCESSING) ? (s -> postProcess(handler, ())) : duc;
        Function<Supplier<DistributedProcessModel>, DistributedProcessModel> res = requiredFeatures.contains(EnvFeature.RUN_AS_ADMIN) ? (s -> runAsAdmin(handler, ())) : pp;
        return res;
    }


    private static DistributedProcessModel runAsAdmin(DistributedProcessHandler handler, Supplier<DistributedProcessModel> action)
    {
        Objects.requireNonNull(handler, "handler mustn't be null");
        Objects.requireNonNull(action, "action mustn't be null");
        ApplicationContext applicationContext = Registry.getApplicationContext();
        SessionService sessionService = (SessionService)applicationContext.getBean("sessionService", SessionService.class);
        UserService userService = (UserService)applicationContext.getBean("userService", UserService.class);
        return (DistributedProcessModel)sessionService.executeInLocalView((SessionExecutionBody)new Object(action), (UserModel)userService
                        .getAdminUser());
    }


    public static DistributedProcessModel executeWithDisabledUniquenessCheck(DistributedProcessHandler handler, Supplier<DistributedProcessModel> action)
    {
        Objects.requireNonNull(handler, "handler mustn't be null");
        Objects.requireNonNull(action, "action mustn't be null");
        Set<String> typeCodes = handler.getTypesWithDisabledUniquenessCheck();
        if(typeCodes == null || typeCodes.isEmpty())
        {
            return action.get();
        }
        Map<String, Object> sessionParameters = Collections.singletonMap("disable.UniqueAttributesValidator.for.types", typeCodes);
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        return (DistributedProcessModel)sessionService.executeInLocalViewWithParams(sessionParameters, (SessionExecutionBody)new Object(action));
    }


    public static DistributedProcessModel postProcess(DistributedProcessHandler handler, Supplier<DistributedProcessModel> action)
    {
        Objects.requireNonNull(handler, "handler mustn't be null");
        Objects.requireNonNull(action, "action mustn't be null");
        DistributedProcessModel process = action.get();
        if(isFinished(process))
        {
            handler.onFinished(process);
        }
        return process;
    }
}
