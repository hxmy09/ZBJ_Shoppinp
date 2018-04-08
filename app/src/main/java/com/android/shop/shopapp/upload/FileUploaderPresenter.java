package com.android.shop.shopapp.upload;

import android.content.Context;
import android.text.TextUtils;

import com.android.shop.shopapp.model.request.ProductReqeust;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class FileUploaderPresenter implements FileUploaderContract.Presenter
{

    private final FileResolver fileResolver;
    private final FileUploaderContract.Model model;
    private final FileUploaderContract.View view;

    private Disposable photoUploadDisposable;

    public FileUploaderPresenter(FileUploaderContract.View view, FileResolver fileResolver, FileUploaderContract.Model model)
    {
        this.view = view;
        this.model = model;
        this.fileResolver = fileResolver;
    }

    @Override
    public void onImageSelected(ProductReqeust request,Context context)
    {
        String path = request.getImg();
        if (TextUtils.isEmpty(path))
        {
            view.showErrorMessage("incorrect file uri");
            return;
        }
        view.showThumbnail(path);
        photoUploadDisposable = model.uploadImage(request,context).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(progress -> view.setUploadProgress((int) (100 * progress)), error -> view.showErrorMessage(error.getMessage()), view::uploadCompleted);
    }

    @Override
    public void onImageSelectedWithoutShowProgress(ProductReqeust request)
    {
        String path = request.getImg();
        //  String filePath = fileResolver.getFilePath(selectedImage);
        if (TextUtils.isEmpty(path))
        {
            view.showErrorMessage("incorrect file uri");
            return;
        }
        view.showThumbnail(path);
        photoUploadDisposable = model.uploadImageWithoutProgress(request).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> view.uploadCompleted(), error -> view.showErrorMessage(error.getMessage()));
    }

    @Override
    public void cancel()
    {
        if (photoUploadDisposable != null && !photoUploadDisposable.isDisposed())
        {
            photoUploadDisposable.dispose();
        }
    }
}
