package com.pk.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 二维码生成工具
 */
public class QrcodeUtil {
    private static int[] imageArrayBg;
    private static int widthbg = 0,heightbg=0;
    private static Logger logger = LoggerFactory.getLogger(QrcodeUtil.class);


    /**
     * 获取二维码
     * @param content 二维码内容
     * @param format 格式
     * @param width 宽
     * @param height 高
     * @return
     */
    public static byte[] generateQrCode(String content, String format, int width, int height) {
        //定义二维码的参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//指定二维码的编码格式
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//指定二维码的纠错等级,纠错等级越高,则存储的信息越少,一般是指定M级
        hints.put(EncodeHintType.MARGIN, 2);//设置二维码周围的空白

        //生成二维码
        byte[] bytes = null;
        ByteArrayOutputStream outputStream = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//                file=new File("D:/Img.png");//将指定的二维码图片生成在指定的地方
            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
            bytes = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    public static void getComposeImg(String content, String format, int width, int height, HttpServletResponse response, String picType) {
        int x=130,y=188;
        if (imageArrayBg == null) {
            try {
                BufferedImage imageBg = null;
                if("qs".equals(picType)){
                    x = 155; y = 386;
                    imageBg = ImageIO.read(QrcodeUtil.class.getClassLoader().getResource("static/img/invite_bg_qs.png"));
                }else{
                    imageBg = ImageIO.read(QrcodeUtil.class.getClassLoader().getResource("static/img/invite_bg.png"));
                }
                widthbg = imageBg.getWidth();//图片宽度
                heightbg = imageBg.getHeight();//图片高度
                imageArrayBg = new int[widthbg * heightbg];
                imageArrayBg = imageBg.getRGB(0,0,widthbg,heightbg,imageArrayBg,0,widthbg);
            } catch (IOException e) {
                e.printStackTrace();
                imageArrayBg = null;
                logger.error("error",e);
            }
            //从图片中读取RGB
        }
        if(imageArrayBg != null) {
            int[] imageArrayQrcode = new int[width * height];
            //定义二维码的参数
            HashMap<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//指定二维码的编码格式
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//指定二维码的纠错等级,纠错等级越高,则存储的信息越少,一般是指定M级
            hints.put(EncodeHintType.MARGIN, 2);//设置二维码周围的空白
            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                BufferedImage imageQrcode = MatrixToImageWriter.toBufferedImage(bitMatrix);

                int[] imageArrayTwo = new int[width*height];
                imageArrayTwo = imageQrcode.getRGB(0,0,width,height,imageArrayTwo,0,width);
                BufferedImage imageNew = new BufferedImage(widthbg,heightbg,BufferedImage.TYPE_INT_RGB);
                imageNew.setRGB(0,0,widthbg,heightbg,imageArrayBg,0,widthbg);//设置底部的RGB

                imageNew.setRGB(x,y,width,height,imageArrayTwo,0,width);//设置上部分的RGB

                ImageIO.write(imageNew,format,response.getOutputStream());

                imageArrayBg = null;
            } catch (WriterException e) {
                logger.error("error",e);
            } catch (IOException e) {
                logger.error("error",e);
            }
        }else{
            logger.error("error",new RuntimeException("Compose image fail!!"));
        }
    }

}

