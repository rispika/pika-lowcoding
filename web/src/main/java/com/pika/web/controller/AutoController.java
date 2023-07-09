package com.pika.web.controller;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("auto")
public class AutoController {

    /**
     * 得到根路径
     *
     * @return {@link String}
     */
    @GetMapping("/getRootPath")
    public String getRootPath() throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) path = new File("");
        String absolutePath = path.getAbsolutePath();
        String rootPath = absolutePath.substring(0, absolutePath.length() - 15);
        return rootPath;
    }

}
