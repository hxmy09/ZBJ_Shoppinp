package com.android.shop.shopapp.upload;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.android.shop.shopapp.model.request.ProductReqeust;
import com.android.shop.shopapp.network.services.UploadsImService;
import com.android.shop.shopapp.upload.api.response.CountingRequestBody;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Paulina Sadowska on 09.12.2017.
 */

public class FileUploaderModel implements FileUploaderContract.Model {

    private final UploadsImService service;


    public FileUploaderModel(UploadsImService service) {
        this.service = service;
    }


    private MultipartBody.Part parseRequestBody(String str, String name) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), str);
        return MultipartBody.Part.createFormData(name, "", requestBody);
    }

    @Override
    public Single<ResponseBody> uploadImageWithoutProgress(ProductReqeust request) {
        return service.postImage("24b75be656d4c9a34f6e16eb427daec4", request.getDesc(), String.valueOf(request.getPrice()), request.getGroupName(), request.getUserName(), createMultipartBody(request.getImg()));
    }

    private MultipartBody.Part createMultipartBody(String filePath) {
        File file = new File(filePath);
        RequestBody requestBody = createRequestBody(file);
        return MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
    }

    private RequestBody createRequestBody(File file) {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }

    @Override
    public Flowable<Double> uploadImage(ProductReqeust request, Context context) {
        return Flowable.create(emitter -> service.postImage("24b75be656d4c9a34f6e16eb427daec4", request.getDesc(), String.valueOf(request.getPrice()), request.getGroupName(), request.getUserName(), createMultipartBody(compressImg(request, context), emitter)).subscribe(result -> emitter.onComplete(), emitter::onError), BackpressureStrategy.LATEST);
    }

    private MultipartBody.Part createMultipartBody(File compileFile, FlowableEmitter<Double> emitter) {
        return MultipartBody.Part.createFormData("img", compileFile.getName(), createCountingRequestBody(compileFile, emitter));
    }

    private RequestBody createCountingRequestBody(File file, FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBody(file);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) ->
        {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }

    //压缩文件

    private File compressImg(ProductReqeust request, Context context) {
        File compressedImageFile = null;
        try {
            compressedImageFile = new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(new File(request.getImg()));
            // compressedImageFile = new Compressor(context).compressToFile(new File(request.getImg()));
        } catch (IOException e) {
        }
        return compressedImageFile;
//        new Compressor(context)
//                .compressToFileAsFlowable(new File(request.getImg()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<File>() {
//                    @Override
//                    public void accept(File file) {
//                        compressedImage = file;
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
////                        throwable.printStackTrace();
////                        showError(throwable.getMessage());
//                    }
//                });
    }
}
