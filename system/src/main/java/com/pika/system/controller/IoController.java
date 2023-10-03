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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.HashSet;
import java.util.Set;


@Api(tags = "IO操作类")
@Controller
@RequestMapping("io")
@Slf4j
public class IoController implements InitializingBean {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private OkHttpClient okHttpClient;
    private Set<String> IMAGE_TYPE_SET;
    @Override
    public void afterPropertiesSet() {
        IMAGE_TYPE_SET = new HashSet<>();
        IMAGE_TYPE_SET.add(MediaType.IMAGE_GIF_VALUE);
        IMAGE_TYPE_SET.add(MediaType.IMAGE_JPEG_VALUE);
        IMAGE_TYPE_SET.add(MediaType.IMAGE_PNG_VALUE);
    }

    /**
     * 上传图片
     *
     * @param file 文件
     * @return {@link R}
     */
    @ApiOperation("上传图片")
    @PostMapping("/uploadImage")
    @ResponseBody
    public R uploadImage(@ApiParam(required = true, value = "图片文件") @NotNull MultipartFile file) {
        String contentType = file.getContentType();
        if (IMAGE_TYPE_SET.contains(contentType)) {
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
