package com.imageHandle;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.File;
import javax.imageio.ImageIO;

public class ZoomImage {
	private static Component component = new Canvas();
	// ".pcx","tga",".tif"这三种格式目前还不支持；
	// 这些定义的格式经过我测试过是可以支持的。
	private static String[] imageFormatArray = new String[]{".jpg",".jpeg",".gif",".png",".bmp"};

	/**
	 * 测试用例
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		ZoomImage zoomImage = new ZoomImage();
		zoomImage.listFormt();
		// 缩小四倍
		zoomImage.zoom("C:/zoomsource",0.25,false);
		// 放大四倍
		zoomImage.zoom("C:/zoomsource",4,false);
	}

	/**
	 * 查看图像I/O库所支持的图像格式有哪些格式
	 */
	public void listFormt()
	{
		String readerMIMETypes[] = ImageIO.getReaderMIMETypes();
		String writerMIMETypes[] = ImageIO.getWriterMIMETypes();
		System.out.println(readerMIMETypes);
		System.out.println(writerMIMETypes);
	}

	/**
	 * 校验图像文件的格式是否可以进行缩放
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean verifyImageFormat(String fileName)
	{
		boolean result = false;
		for(int i = 0;i < imageFormatArray.length;i++)
		{
			if(fileName.toLowerCase().lastIndexOf(imageFormatArray[i]) == (fileName.length() - imageFormatArray[i].length()))
			{
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 将目录下的所有图像进行放大缩小
	 * 
	 * @param strDir 图像的目录
	 * @param zoomRatio 放大缩小的倍率
	 * @param rebuild 是否重新创建，即已经存在的图像是否覆盖重建
	 * @throws Exception
	 */
	public void zoom(String strDir,double zoomRatio,boolean rebuild) throws Exception
	{
		File fileDir = new File(strDir);
		if(!fileDir.exists())
		{
			System.out.println("Not exist:" + strDir);
			return;
		}
		String dirTarget = strDir + "/Zoom" + zoomRatio;
		File fileTarget = new File(dirTarget);
		if(!fileTarget.exists())
		{
			fileTarget.mkdir();
		}
		File[] files = fileDir.listFiles();
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0;i < files.length;i++)
		{
			String fileFullName = files[i].getCanonicalPath();
			String fileShortName = files[i].getName();
			if(!new File(fileFullName).isDirectory())// 排除二级目录，如果想就再递归一次，这里省略
			{
				if(verifyImageFormat(fileShortName))
				{	
					System.out.println("Begin Zoom:" + fileFullName);
					stringBuilder = new StringBuilder();
					stringBuilder.append(dirTarget).append("/").append(fileShortName);
					if(!new File(stringBuilder.toString()).exists() || rebuild)
					{
						try
						{
							createZoomSizeImage(fileFullName,stringBuilder.toString(),zoomRatio);
						}
						catch(Exception e)
						{
							System.out.printf("createZoomSizeImage Error:" + fileFullName,e);
						}
					}
					System.out.println("End Zoom:" + fileFullName);
			
				}
				else
				{
					System.out.printf("Can't Zoom:" + fileFullName);
				}
			}
		}
	}

	/**
	 * 按比例进行放大缩小图像，zoomRatio = 1为原大，zoomRatio > 1为放大，zoomRatio < 1 为缩小
	 * 
	 * @param fileName
	 * @param fileNameTarget
	 * @param zoomRatio
	 * @throws Exception
	 */
	public void createZoomSizeImage(String fileName,String fileNameTarget,double zoomRatio) throws Exception
	{
		Image image = ImageIO.read(new File(fileName));
		int width = new Double(image.getWidth(null) * zoomRatio).intValue();
		int height = new Double(image.getHeight(null) * zoomRatio).intValue();
		AreaAveragingScaleFilter areaAveragingScaleFilter = new AreaAveragingScaleFilter(width,height);
		FilteredImageSource filteredImageSource = new FilteredImageSource(image.getSource(),areaAveragingScaleFilter);
		BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		Graphics graphics = bufferedImage.createGraphics();
		graphics.drawImage(component.createImage(filteredImageSource),0,0,null);
		ImageIO.write(bufferedImage,"JPEG",new File(fileNameTarget));
	}
}
