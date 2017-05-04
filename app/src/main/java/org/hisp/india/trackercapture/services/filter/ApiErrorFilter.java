package org.hisp.india.trackercapture.services.filter;

import android.widget.Toast;

import org.hisp.india.core.common.ProductFlavor;
import org.hisp.india.core.exceptions.ApiException;
import org.hisp.india.core.exceptions.ErrorCodes;
import org.hisp.india.core.services.filter.InterceptFilter;
import org.hisp.india.core.services.log.LogService;
import org.hisp.india.core.services.network.NetworkProvider;
import org.hisp.india.trackercapture.BuildConfig;

import es.dmoral.toasty.Toasty;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by nhancao on 4/16/17.
 */

public class ApiErrorFilter implements InterceptFilter {

    private NetworkProvider networkProvider;
    private ProductFlavor productFlavor;
    private LogService logService;

    public ApiErrorFilter(NetworkProvider networkProvider, LogService logService) {
        this.networkProvider = networkProvider;
        this.logService = logService;
        this.productFlavor = ProductFlavor.getFlavor(BuildConfig.FLAVOR);
    }

    @Override
    public <T> Observable.Transformer<T, T> execute() {
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    switch (productFlavor) {
                        case DEVELOP:
                            Toast.makeText(networkProvider.getContext(), throwable.toString(),
                                           Toast.LENGTH_SHORT)
                                 .show();
                            break;
                        case PRODUCTION:
                            if (throwable instanceof ApiException) {
                                int code = ((ApiException) throwable).getCode();
                                if (code == ErrorCodes.NETWORK_NOT_AVAILABLE_ERROR) {
                                    Toasty.error(networkProvider.getContext(), "Oops! Network error.",
                                                 Toast.LENGTH_SHORT, true).show();
                                } else {
                                    logService.log(throwable.toString());
                                    Toasty.error(networkProvider.getContext(), "Oops! Something error?",
                                                 Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                logService.log(throwable.toString());
                                Toasty.error(networkProvider.getContext(), "Oops! Something error?",
                                             Toast.LENGTH_SHORT, true).show();
                            }
                            break;
                    }
                    return Observable.error(throwable);
                });
    }
}
