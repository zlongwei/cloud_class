package com.study.security.oss.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.study.security.common.exception.BaseException;
import com.study.security.common.msg.ObjectRestResponse;
import com.study.security.oss.cloud.OSSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传
 *
 * @author allen
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OSSFactory ossFactory;

    // 允许上传的格式
    private static final List<String> IMAGE_TYPE =
            Arrays.asList(".bmp", ".jpg", ".jpeg", ".gif", ".png");

    /**
     * 上传文件到oss
     */
    @PostMapping("/upload")
    public ObjectRestResponse<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new BaseException("上传文件不能为空");
        }
        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = ossFactory.build().uploadSuffix(file.getBytes(), suffix);
        return new ObjectRestResponse<>().data(url);
    }

    /**
     * 根据文件名删除oss上的文件
     * http://localhost:9992/file/delete?fileName=images/2019/04/28/1556429167175766.jpg
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    @GetMapping("delete")
    public ObjectRestResponse<String> delete(@RequestParam("fileName") String fileName) throws Exception {
        String reslut = ossFactory.build().delete(fileName);
        return new ObjectRestResponse<>().data(reslut);
    }

    /**
     * 查询oss上的所有文件
     * http://localhost:9992/file/list
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public List<OSSObjectSummary> list() throws Exception {
        //return this.fileUploadService.list();
        return null;
    }

    /**
     * 根据文件名下载oss上的文件
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @RequestMapping("download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
        //this.fileUploadService.exportOssFile(response.getOutputStream(), fileName);
    }

}
