package others.clases.observer_pattern;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ObserverSuscribe {

    public static void main(String[] args) {
        Observable<Integer> observable = Observable.range(1,10);

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("Suscribed");
            }
            @Override
            public void onNext(Object value) {
                System.out.println("Received: " + value);
            }
            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("Error: " + e.getMessage());
            }
            @Override
            public void onComplete() {
                System.out.println("Completedd");
            }
        };


        observable.subscribe(observer);
        //agregar dos observer mas
    }

}
