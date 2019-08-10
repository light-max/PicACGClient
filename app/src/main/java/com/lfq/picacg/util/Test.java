package com.lfq.picacg.util;

import okhttp3.*;

import java.io.File;
import java.util.Map;

public class Test {

    public static Request getFileRequest(String url, File file, Map<String, String> maps) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (maps == null) {
//            builder.addPart(
//                    Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""),
//                    RequestBody.create(MediaType.parse("image/png"), file)).build();
            builder.build();
        } else {
//            for (String key : maps.keySet()) {
//                builder.addFormDataPart(key, maps.get(key));
//            }
//
//            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/png"), file)
//            );

        }
        RequestBody body = builder.build();
        return new Request.Builder().url(url).post(body).build();
    }

}
