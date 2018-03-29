package ru.hotmule.swapiclient.common;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Utils {
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static String categoryFromUrl(String url) {
        return url.split("/")[4];
    }

    public static String idFromUrl(String url) {
        return url.split("/")[5];
    }
}
