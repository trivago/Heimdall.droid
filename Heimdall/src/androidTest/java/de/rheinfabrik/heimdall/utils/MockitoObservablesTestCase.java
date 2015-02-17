package de.rheinfabrik.heimdall.utils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Test case that can be used to test everything that is subscription based.
 */
public class MockitoObservablesTestCase extends MockitoTestCase {

    // Members

    private final PublishSubject<Void> mOnTearDownSubject = PublishSubject.create();

    // Public Api

    protected <T> void subscribe(Observable<T> observable) {
        subscribe(observable, null, null);
    }

    protected <T> void subscribe(Observable<T> observable, Action1<T> onNext) {
        subscribe(observable, onNext, null);
    }

    protected <T> void subscribe(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError) {
        bindObservable(observable).unsafeSubscribe(new Subscriber<T>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (onError != null) {
                    onError.call(e);
                }
            }

            @Override
            public void onNext(T t) {
                if (onNext != null) {
                    onNext.call(t);
                }
            }
        });
    }

    // Private Api

    private  <T> Observable<T> bindObservable(Observable<T> observable) {
        return observable
                .takeUntil(mOnTearDownSubject)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate());
    }

    // Test lifecycle

    @Override
    protected void tearDown() throws Exception {
        mOnTearDownSubject.onNext(null);

        super.tearDown();
    }
}
