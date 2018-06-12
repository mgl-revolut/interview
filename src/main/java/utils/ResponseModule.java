package utils;

import spark.Response;

public final class ResponseModule {

    public static <T> String ok(T t, Response response) {

        response.status(200);
        return Json.serialize(t);
    }
}
