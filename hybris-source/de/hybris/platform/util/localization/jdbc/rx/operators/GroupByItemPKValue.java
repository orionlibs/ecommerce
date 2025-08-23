package de.hybris.platform.util.localization.jdbc.rx.operators;

import de.hybris.platform.util.localization.jdbc.LocalizableRow;
import rx.Observable;
import rx.Subscriber;

public class GroupByItemPKValue<T extends LocalizableRow> implements Observable.Operator<Iterable<T>, T>
{
    public Subscriber<? super T> call(Subscriber<? super Iterable<T>> subscriber)
    {
        return (Subscriber<? super T>)new GroupBySubscriber(subscriber);
    }
}
