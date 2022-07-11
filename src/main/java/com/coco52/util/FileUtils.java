package com.coco52.util;

import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class FileUtils {

    public static final String UPLOAD_DIR ="/root/project/files";

    /**
     * 获取上传文件的md5
     * @param file
     * @return
     * @throws IOException
     */
    public static String getMd5(MultipartFile file) {
        try {
            //获取文件的byte信息
            byte[] uploadBytes = file.getBytes();
            // 拿到一个MD5转换器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            //转换为16进制
            return new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return true  大于
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
//        long len = file.length();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return true;
        }
        return false;


    }

    /**
     * 保存文件 到 配置文件内存在的目录中
     *
     * @param file
     * @return
     * @throws FileNotFoundException 传入空文件时抛出此异常
     * @throws FileUploadException  上传时文件太大或者文件上传时发生错误时抛出此异常
     */
    public static String saveFile(MultipartFile file) throws FileNotFoundException, FileUploadException {
        String separator = File.separator;
        SimpleDateFormat sdf = new SimpleDateFormat(separator + "yyyy" + separator + "MM" + separator + "dd" + separator);
        String format = sdf.format(new Date());

        if (file.isEmpty()) {
            throw new FileNotFoundException(ResultCode.FILE_IS_NULL.getMessage());
        }
        log.debug(file.getOriginalFilename() + " 文件开始上传");

        if (FileUtils.checkFileSize(file.getSize(), 3, "M")) {
            throw new FileUploadException(ResultCode.FILE_TOO_BIG.getMessage());
        }

        // 生成  /root/project/files/2021/08/23/abc.text 文件
        String targetDir = UPLOAD_DIR + separator + format;

        File folder = new File(targetDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
        File generateFile = new File(folder, newName);
        try {
            file.transferTo(generateFile.getAbsoluteFile());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileUploadException(ResultCode.FILE_UPLOAD_ERROR.getMessage());
        }
        return format + newName;
    }

}
