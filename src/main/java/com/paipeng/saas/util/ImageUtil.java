package com.paipeng.saas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

public class ImageUtil {
    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class.getSimpleName());
    private static final String BASE64PREFIX = "base64,";

    public static boolean isFileExite(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
    public static String saveBase64ImageToLocal(String base64Image, String savePath) throws Exception {
        log.info("saveBase64ImageToLocal");
        int pos = base64Image.indexOf(BASE64PREFIX);
        // 判断前缀是否存在
        if (-1 == pos) {
            throw new Exception("400");
        }
        // 获取扩展名
        String ext = base64Image.substring(base64Image.indexOf("/") + 1, base64Image.indexOf(";"));

        log.info("ext: " + ext);
        // 获取唯一id，作为本地保存的名字
        String uuid = UUID.randomUUID().toString();
        String imageName = uuid + "." + ext;

        // 判断uuid 是否存在如果存在着重新申请，申请5次
        int time = 0;
        int total = 5;
        while (isFileExite(savePath + imageName)) {
            uuid = UUID.randomUUID().toString();
            imageName = uuid + "." + ext;
            if (total == time++) {
                throw new Exception("500");
            }
        }

        String saveImagePath = savePath + File.separator + imageName;
        log.info("saveImagePath: " + saveImagePath);

        // 删除前缀
        base64Image = base64Image.substring(pos + BASE64PREFIX.length());
        // Base64解码
        byte[] b = Base64.getDecoder().decode(base64Image.trim());
        if (b == null || b.length <= 0) {
            log.error("400 error:decodeBase64 error invalid");
            throw new Exception("400");
        }
        BufferedImage image;
        log.info("decodeBase64 length: " + b.length);
        try {
            // 判断保存路径是否存在，如果不存在着创建
            File fileDirPath = new File(savePath);
            if (!fileDirPath.exists() && !fileDirPath.isDirectory()) {
                fileDirPath.mkdirs();
            }
            // 生成jpeg图片
            log.info("try to write byte to file");
            OutputStream out = new FileOutputStream(saveImagePath);
            out.write(b);
            out.flush();
            out.close();
            log.info("write byte to file success");
            log.info("convert byte to BufferedImage");
            image = ImageIO.read(new ByteArrayInputStream(b));
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage());
            throw new Exception("500");
        }

        //将图片转换成bufferimage获取图片信息
        if (image == null) {
            throw new Exception("500");
        }

        int iWidth = image.getWidth();
        int iHeight = image.getHeight();
        long iSize = b.length;
        log.info("image size: " + iWidth + "-" + iHeight + ", " + iSize);
        log.info("check width");

        log.info("return imageName: " + imageName);

        return imageName;
    }
}
