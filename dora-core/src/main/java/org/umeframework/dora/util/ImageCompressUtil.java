/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Element;

abstract public class ImageCompressUtil {

	/**
	 * 直接指定压缩后的宽高：
	 * (先保存原文件，再压缩、上传)
	 * 壹拍项目中用于二维码压缩
	 * @param oldFile 要进行压缩的文件全路径
	 * @param width 压缩后的宽度
	 * @param height 压缩后的高度
	 * @param quality 压缩质量
	 * @param smallIcon 文件名的小小后缀(注意，非文件后缀名称),入压缩文件名是yasuo.jpg,则压缩后文件名是yasuo(+smallIcon).jpg
	 * @return 返回压缩后的文件的全路径
	 * @throws IOException 
	 */
	public static String zipImageFile(
			String newImageFilePath,
			InputStream oldFile,
			int newWidth,
			int height,
			float quality) throws IOException {

		if (oldFile == null) {
			return null;
		}

		try {
			/**原文件,对服务器上的临时文件进行处理 */
			BufferedImage srcFile = ImageIO.read(oldFile);

			if(srcFile == null){
				return null;
			}

			//原文件宽度
			int oldWidth = srcFile.getWidth();
			//原文件高度
			int oldHeight = srcFile.getHeight();

			int newHeight = 0;

			if (oldWidth < newWidth) {
				newWidth = oldWidth;
				newHeight = oldHeight;

			} else {

				newHeight = (newWidth * oldHeight) / oldWidth;
			}

			/** 宽,高设定 */
			BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(srcFile, 0, 0, newWidth, newHeight, null);
			/** 压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(newImageFilePath);
			//压缩图片
			saveAsJPEG(100, tag, quality, out);
			out.close();
		} catch (FileNotFoundException e) {
			throw new IOException("No found file.", e);
		} catch (IOException e) {
			throw new IOException("Zipped file failed.", e);
		}
		return newImageFilePath;
	}

	/**
	 * 以JPEG编码保存图片
	 * @param dpi  分辨率
	 * @param image_to_save  要处理的图像图片
	 * @param JPEGcompression  压缩比
	 * @param fos 文件输出流
	 * @throws IOException
	 */
	public static void saveAsJPEG(
			Integer dpi,
			BufferedImage tag,
			float quality,
			FileOutputStream fos) throws IOException {

		// Image writer
		ImageWriter imageWriter = (ImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
		imageWriter.setOutput(ios);
		//and metadata
		IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(tag), null);
		if (dpi != null) {
			//new metadata
			Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
			Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
			jfif.setAttribute("Xdensity", Integer.toString(dpi));
			jfif.setAttribute("Ydensity", Integer.toString(dpi));
		}
		if (quality >= 0 && quality <= 1f) {
			// new Compression
			JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
			jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(quality);
		}
		//new Write and clean up
		imageWriter.write(imageMetaData, new IIOImage(tag, null, null), null);
		ios.close();
		imageWriter.dispose();
	}
}
