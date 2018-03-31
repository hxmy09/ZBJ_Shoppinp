package com.android.shop.shopapp.upload;

import com.android.shop.shopapp.model.request.ProductReqeust;

import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public interface FileUploaderContract
{
    interface View
    {
        void showThumbnail(String path);

        void showErrorMessage(String message);

        void uploadCompleted();

        void setUploadProgress(int progress);
    }

    interface Presenter
    {
        void onImageSelected(ProductReqeust request);

        void onImageSelectedWithoutShowProgress(ProductReqeust request);

        void cancel();
    }

    interface Model
    {
        Flowable<Double> uploadImage(ProductReqeust request);

        Single<ResponseBody> uploadImageWithoutProgress(ProductReqeust request);
    }
}
