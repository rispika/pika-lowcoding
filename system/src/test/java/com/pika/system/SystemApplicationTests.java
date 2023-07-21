package com.pika.system;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class SystemApplicationTests {

    @Resource
    private OkHttpClient okHttpClient;

    @Test
    void test() {
        try {
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:5001/pika-lowcoding/2023-07/17/660293c2-a1bc-43d0-8e22-fed0d6882a45.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=pika-admin%2F20230720%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230720T145656Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=73f622bce1d1d29a3c9224f1e5078f84e4598116f2f0cf2e234f2bfd948500cd")
                    .method("GET", null)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response.body().bytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
