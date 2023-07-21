package com.pika.system.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.pika.common.R;
import com.pika.system.utils.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;


@Api(tags = "IO操作类")
@Controller
@RequestMapping("io")
@Slf4j
public class IoController {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private OkHttpClient okHttpClient;

    @ApiOperation("上传头像")
    @PostMapping("/uploadAvatar")
    @ResponseBody
    public R uploadAvatar(@ApiParam(required = true, value = "头像文件") @NotNull MultipartFile file) {
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg")) {
            return R.fail(500, "图片格式错误!");
        }
        String upload = minioUtil.upload(file);
        if (StrUtil.isBlank(upload)) {
            return R.fail(500, "图片上传失败!请稍后再试!");
        }
        log.info("upload: {}", upload);
        return R.ok().data("upload", upload);
    }

    /**
     * 预览图片
     *
     * @param image 图片
     * @return {@link R}
     */
    @ApiOperation("预览图片")
    @GetMapping("/previewImage/{image}")
    public void previewImage(@ApiParam(required = true, value = "base64后的image路径") @PathVariable String image,
                              HttpServletResponse response) {
        image = Base64.decodeStr(image);
        String imageUrl = minioUtil.preview(image);
        try {
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .method("GET", null)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .build();
            Response okRes = okHttpClient.newCall(request).execute();
            byte[] avatarBytes = okRes.body().bytes();
            ServletOutputStream outputStream = response.getOutputStream();
            IoUtil.write(outputStream, true, avatarBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
