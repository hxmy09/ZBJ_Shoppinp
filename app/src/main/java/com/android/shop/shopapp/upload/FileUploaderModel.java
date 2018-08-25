package com.android.shop.shopapp.upload;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.android.shop.shopapp.model.Detail;
import com.android.shop.shopapp.model.ProductModel;
import com.android.shop.shopapp.network.services.UploadsImService;
import com.android.shop.shopapp.upload.api.response.CountingRequestBody;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
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


    private RequestBody parseRequestBody(String str) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), TextUtils.isEmpty(str) ? "" : str);
    }

    @Override
    public Single<ResponseBody> uploadImageWithoutProgress(ProductModel request) {
        //这个现在有问题。不能运行

        return service.postImage(parseRequestBody(request.getDetails().toString()), null, parseRequestBody(request.getDesc()), parseRequestBody(String.valueOf(request.getPrice())), parseRequestBody(request.getGroupName()), parseRequestBody(request.getUserName()), parseRequestBody(request.getKeyWords()));
    }

    private List<MultipartBody.Part> createMultipartBody(List<String> paths) {

        List<MultipartBody.Part> list = new ArrayList<>();
        for (String filePath : paths) {
            File file = new File(filePath);
            RequestBody requestBody = createRequestBody(file);
            list.add(MultipartBody.Part.createFormData("upload", file.getName(), requestBody));
        }
        return list;
    }

    private RequestBody createRequestBody(File file) {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }

    @Override
    public Flowable<Double> uploadImage(ProductModel request, Context context) {
        return Flowable.create(emitter -> service.postImage(parseRequestBody(new Gson().toJson(request.getDetails())), createMultipartBody(request.getDetails(), context, emitter), parseRequestBody(request.getDesc()), parseRequestBody(String.valueOf(request.getPrice())), parseRequestBody(request.getGroupName()), parseRequestBody(request.getUserName()), parseRequestBody("")).subscribe(result -> emitter.onComplete(), emitter::onError), BackpressureStrategy.LATEST);
    }

    private MultipartBody.Part[] createMultipartBody(List<Detail> paths, Context context, FlowableEmitter<Double> emitter) {

        MultipartBody.Part[] array = new MultipartBody.Part[]{};
        List<MultipartBody.Part> list = new ArrayList<>();
        for (Detail detail : paths) {
            File file = compressImg(detail.getImg(), context);
            RequestBody requestBody = createRequestBody(file);
            list.add(MultipartBody.Part.createFormData("img", file.getName(), createCountingRequestBody(file, emitter)));
        }

        return list.toArray(array);

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

    private File compressImg(String img, Context context) {
        File compressedImageFile = null;
        try {
            compressedImageFile = new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(90)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(new File(img));
            // compressedImageFile = news1 Compressor(context).compressToFile(news1 File(request.getImg()));
        } catch (IOException e) {
        }
        return compressedImageFile;
//        news1 Compressor(context)
//                .compressToFileAsFlowable(news1 File(request.getImg()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(news1 Consumer<File>() {
//                    @Override
//                    public void accept(File file) {
//                        compressedImage = file;
//                    }
//                }, news1 Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
////                        throwable.printStackTrace();
////                        showError(throwable.getMessage());
//                    }
//                });
    }
}
