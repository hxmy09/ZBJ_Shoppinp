package com.android.shop.shopapp.upload;


import com.android.shop.shopapp.model.request.ProductReqeust;
import com.android.shop.shopapp.network.services.UploadsImService;
import com.android.shop.shopapp.upload.api.response.CountingRequestBody;

import java.io.File;

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

public class FileUploaderModel implements FileUploaderContract.Model
{

    private final UploadsImService service;


    public FileUploaderModel(UploadsImService service)
    {
        this.service = service;
    }


    private MultipartBody.Part parseRequestBody(String str, String name)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), str);
        return MultipartBody.Part.createFormData(name, "", requestBody);
    }

    @Override
    public Single<ResponseBody> uploadImageWithoutProgress(ProductReqeust request)
    {
        return service.postImage("application/json", request.getDesc(), String.valueOf(request.getPrice()), request.getGroupName(), request.getUserName(), request.getProductId(), createMultipartBody(request.getImg()));
    }

    private MultipartBody.Part createMultipartBody(String filePath)
    {
        File file = new File(filePath);
        RequestBody requestBody = createRequestBody(file);
        return MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
    }

    private RequestBody createRequestBody(File file)
    {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }

    @Override
    public Flowable<Double> uploadImage(ProductReqeust request)
    {
//        List<MultipartBody.Part> parts = new ArrayList();
//        parts.add(parseRequestBody(request.getDesc(),"desc"));
//        parts.add(parseRequestBody(String.valueOf(request.getPrice()),"price"));
//        parts.add(parseRequestBody(request.getGroupName(),"group"));
//        parts.add(createMultipartBody(request.getImg()));
        return Flowable.create(emitter -> service.postImage("application/json", request.getDesc(), String.valueOf(request.getPrice()), request.getGroupName(), request.getUserName(), request.getProductId(), createMultipartBody(request.getImg(), emitter)).subscribe(result -> emitter.onComplete(), emitter::onError), BackpressureStrategy.LATEST);
    }

    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter)
    {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("upload", file.getName(), createCountingRequestBody(file, emitter));
    }

    private RequestBody createCountingRequestBody(File file, FlowableEmitter<Double> emitter)
    {
        RequestBody requestBody = createRequestBody(file);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) ->
        {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
}
