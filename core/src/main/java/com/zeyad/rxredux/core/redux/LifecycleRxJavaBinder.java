package com.zeyad.rxredux.core.redux;

import org.reactivestreams.Publisher;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;

/**
 * @author by ZIaDo on 6/14/17.
 */
public class LifecycleRxJavaBinder {
    public static <T> FlowableTransformer<T, T> applyFlowable(@NonNull final LifecycleOwner lifecycleOwner) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(upstream);
                return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, liveData));
            }
        };
        //        return flowable -> {
        //            LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(flowable);
        //            return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, liveData));
        //        };
    }

    public static <T> ObservableTransformer<T, T> applyObservable(@NonNull final LifecycleOwner lifecycleOwner,
            final BackpressureStrategy strategy) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(upstream.toFlowable(strategy));
                return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, liveData))
                        .toObservable();
            }
        };
        //        return observable -> {
        //            LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(observable.toFlowable(strategy));
        //            return Flowable.fromPublisher(LiveDataReactiveStreams
        //                    .toPublisher(lifecycleOwner, liveData)).toObservable();
        //        };
    }

    public static <T> ObservableTransformer<T, T> applyObservable(@NonNull final LifecycleOwner lifecycleOwner) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                LiveData<T> liveData = LiveDataReactiveStreams
                        .fromPublisher(upstream.toFlowable(BackpressureStrategy.BUFFER));
                return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, liveData))
                        .toObservable();
            }
        };
        //        return observable -> {
        //            LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(observable
        //                    .toFlowable(BackpressureStrategy.BUFFER));
        //            return Flowable.fromPublisher(LiveDataReactiveStreams
        //                    .toPublisher(lifecycleOwner, liveData)).toObservable();
        //        };
    }

    public static <T> SingleTransformer<T, T> applySingle(@NonNull final LifecycleOwner lifecycleOwner) {
        return new SingleTransformer<T, T>() {
            @Override
            public SingleSource<T> apply(@NonNull Single<T> upstream) {
                LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(upstream.toFlowable());
                return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, liveData))
                        .singleOrError();
            }
        };
        //        return single -> {
        //            LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(single.toFlowable());
        //            return Flowable.fromPublisher(LiveDataReactiveStreams
        //                    .toPublisher(lifecycleOwner, liveData)).singleOrError();
        //        };
    }

    public static <T> MaybeTransformer<T, T> applyMaybe(@NonNull final LifecycleOwner lifecycleOwner) {
        return new MaybeTransformer<T, T>() {
            @Override
            public MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
                LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(upstream.toFlowable());
                return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, liveData))
                        .firstElement();
            }
        };
        //        return maybe -> {
        //            LiveData<T> liveData = LiveDataReactiveStreams.fromPublisher(maybe.toFlowable());
        //            return Flowable.fromPublisher(LiveDataReactiveStreams
        //                    .toPublisher(lifecycleOwner, liveData)).firstElement();
        //        };
    }
}
