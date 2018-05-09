package com.android.shop.shopapp.upload;

import android.content.Context;


import com.android.shop.shopapp.model.ProductModel;

import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.ResponseBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public interface FileUploaderContract {
    interface View {
        void showThumbnail(String path);

        void showErrorMessage(String message);

        void uploadCompleted();

        void setUploadProgress(int progress);
    }

    interface Presenter {
        void onImageSelected(ProductModel request, Context context);

        void onImageSelectedWithoutShowProgress(ProductModel request);

        void cancel();
    }

    interface Model {
        Flowable<Double> uploadImage(ProductModel request, Context context);

        Single<ResponseBody> uploadImageWithoutProgress(ProductModel request);
    }
}
