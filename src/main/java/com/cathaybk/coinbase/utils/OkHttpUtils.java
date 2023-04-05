package com.cathaybk.coinbase.utils;

import com.cathaybk.coinbase.exception.ApiProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class OkHttpUtils {
    private static OkHttpClient okHttpClient;

    public static void SetOkHttpClient(OkHttpClient client) {
        okHttpClient = client;
    }

    public static <T> T simpleGet(@NonNull String url, Class<T> clasz) throws ApiProcessingException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return processResponse(response, clasz);
        } catch (Exception e) {
            throw new ApiProcessingException();
        }
    }

    public static void simpleGet(@NonNull String url, Callback callback) {
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(callback);
    }

    @SuppressWarnings("unchecked")
    public static <T> T processResponse(Response response, Class<T> clasz) throws IOException {
        if (response.isSuccessful()) {
            String respStr = Objects.requireNonNull(response.body()).string();
            ObjectMapper objectMapper = new ObjectMapper();
            if (Objects.equals(String.class, clasz)) return (T) respStr;
            return objectMapper.readValue(respStr, clasz);
        }
        return null;
    }
}
