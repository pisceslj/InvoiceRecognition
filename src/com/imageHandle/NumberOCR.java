package com.imageHandle;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NumberOCR {
	//字符特点
		public static int FindNumberByCrossPoint(int b[]){
			 if(b[0]==2 &&b[1]==2 &&b[2]==2 &&b[3]==2 ) return 0;
			 if(b[0]==1 &&b[1]==1 &&b[2]==1 &&b[3]==3 ) return 35;
			 if(b[0]==1 &&b[1]==2 &&b[2]==1 &&b[3]==2 ) return 4;
			 if(b[0]==1 &&b[1]==1 &&b[2]==2 &&b[3]==3 ) return 6;
			 if(b[0]==1 &&b[1]==1 &&b[2]==1 &&b[3]==2||b[3]==1 ) return 127;	
			 if(b[0]==2 &&b[1]==1 &&b[2]==2 &&b[3]==3 ) return 8;
			 if(b[0]==2 &&b[1]==1 &&b[2]==1 &&b[3]==3 ) return 9;		
			 return 10;
		}
		
		//point row_or_column = 1是行画线，row_or_column=0是列画线，site是画线位置
		public static int CrossLine(int row_or_column,int site,int [][]b,int row,int cloumn){
			int flag =0;
			int pointcount = 0;
			if(row_or_column == 1)//hang
			{
				for(int j=0;j<cloumn;j++)
				{
					if(b[site][j] == 0 ){flag =1;}
					if(b[site][j] == 1 && flag==1){pointcount++;flag =0;}
				}
			}
			flag =0;
			if(row_or_column == 0)//lie
			{
				for(int i=0;i<row;i++)
				{
					if(b[i][site] == 0 ){flag =1;}
					if(b[i][site] == 1 && flag==1){pointcount++;flag =0;}
				}
			}
			return pointcount;
		}
			
		//提取不含边界的字符并且识别数字
		public static int ExtractNumber(int[][]b,int hang,int lie){
			System.out.println("extract function");
			int rowstart = 0;
			int rowend = 0;
			int columnstart = 0;
			int columnend = 0;
			int count1 = 0;
			int flag = 0;
			
			for(int i=0;i<hang;i++){
				for(int j=0;j<lie;j++)	if(b[i][j] == 1)count1++;	
				if(count1>0 && flag ==0){rowstart = i-1;flag =1;}
				if(count1==0 && flag ==1){rowend = i+1;break;}
				count1 = 0;
				}
			
			flag=0;		
			for(int j=0;j<lie;j++){
				for(int i=0;i<hang;i++)  if(b[i][j] == 1)count1++;
				if(count1>0 && flag ==0){columnstart = j-1;flag =1;}
				if(count1==0 && flag ==1){columnend = j+1;break;}
				count1 = 0;
				}
			
			int resultrow = rowend -rowstart;
			int resultcolumn = columnend - columnstart;

			int[][] result = new int[1000][1000];
			for(int i=0;i<resultrow;i++)
				{
					for(int j=0;j<resultcolumn;j++)
					{
						result[i][j] = b[rowstart+i][columnstart+j];
						if(result[i][j] == 0){
							System.out.print(" ");
						}
						else System.out.print(1);
						
					}	
					System.out.println();
				}
			System.out.println();
			int[] site = new int[8];
			site[0] = resultrow/3;
			site[1] = resultrow/2;
			site[2] = (resultrow/3)*2;
			site[3] = resultcolumn/2;
			
			int[] cha = new int[8];
			for(int i=0;i<=2;i++) cha[i] = CrossLine( 1,site[i],result,resultrow,resultcolumn);
			for(int i=3;i<=3;i++) cha[i] = CrossLine( 0,site[i],result,resultrow,resultcolumn);
			for(int i=0;i<=3;i++){
				System.out.print(cha[i]);
			}
			//System.out.println();	
			int FirstDiff = FindNumberByCrossPoint(cha);
			if(FirstDiff != 127 && FirstDiff != 35 && FirstDiff != 10){
				return FirstDiff;
			}
			else if(FirstDiff == 127){
				return Diff127(result,resultrow,resultcolumn);
			}
			else if(FirstDiff == 35){
				return Diff35(result,resultrow,resultcolumn);
			}
			else{
				return -1;
			}	
		}
		
		private static int Diff35(int[][]b,int hang,int lie) {
			//通过统计左上半部的像素点占总像素点的比例来判断3和5，总比例偏小的为3
			float zuoShangCount = 0;
			float sumCount = 0;
			for(int i = 0; i < hang/2; i++){
				for(int j = 0;j < lie/2; j++){
					if(b[i][j] == 1) zuoShangCount++;
				}
			}
			for(int i = 0; i < hang; i++){
				for(int j = 0;j < lie; j++){
					if(b[i][j] == 1) sumCount++;
				}
			}
			float bili = zuoShangCount / sumCount;
			
			//System.out.println("Diff35左上半部的像素的比例为"+bili);
			
			if(bili < 0.27) return 3;
			else return 5;
				
			
		}

		private static int Diff127(int[][]b,int hang,int lie) {
			//通过向x y轴投影来区分1、2、7
			int[] ySum = new int[hang+1];
			int[] xSum = new int[lie+1];
			for(int i = 0; i < hang; i++){
				for(int j = 0;j < lie; j++){
					if(b[i][j] == 1) ySum[i]++;
					/*	if(b[i][j] == 1)
						System.out.print(1);
					else System.out.print(" ");*/
				}
				//System.out.println("ySum"+i+"="+ySum[i]);
			}
			//System.out.println();
			for(int i = 0; i < lie; i++){
				for(int j = 0;j < hang; j++){
					if(b[j][i] == 1) xSum[i]++;
				}
				//System.out.println("xSum"+i+"="+xSum[i]);
			}
			int maxBelow = ySum[hang/2];
			for(int i = hang/2; i < hang; i++){
				if(ySum[i] > maxBelow){
					maxBelow = ySum[i];
				}
			}
			System.out.println("Diff7黑点数之差为"+Math.abs(ySum[hang/2]+ySum[hang/2+1]+ySum[hang/2+2] - 3*maxBelow)+"   4/5lie="+4*lie/5);
			if(Math.abs(ySum[hang/2]+ySum[hang/2+1]+ySum[hang/2+2] - 3*maxBelow) < 4*lie/5) return 7;
			else {
				System.out.println("Diff12黑点数之差为"+Math.abs(2*hang - xSum[lie/2] - xSum[lie/2+1])+"   2/5之hang="+2*hang/5);
				if(Math.abs(2*hang - xSum[lie/2] - xSum[lie/2+1]) < 2*hang/5) return 1;
				else return 2;
			}
		}

		//process是噪音处理
		public static int[][] DeleteSound(int[][]b,int hang,int lie){
			int count = 0;
			int piexnumber = 4;
			//对0处理
			for(int i=0;i<hang;i++){
				for(int j=0;j<lie;j++)
				{
			
					if(b[i][j] == 0) count++;
					if(b[i][j] == 1) 
					{
						if(count <=piexnumber ) 
							for(int k=1;k<=count;k++)
								b[i][j-k] = 1;
						count =0;
					}
				}
			}
			count = 0;
			for(int j=0;j<lie;j++)
			{
				for(int i=0;i<hang;i++)
				{
			
					if(b[i][j] == 0) count++;
					if(b[i][j] == 1) 
					{
						if(count <=piexnumber ) 
							for(int k=1;k<=count;k++)
								b[i-k][j] = 1;
						count =0;
					}
				}
			}
			//对1处理
			 count = 0;
			for(int i=0;i<hang;i++){
				for(int j=0;j<lie;j++)
				{
			
					if(b[i][j] == 1) count++;
					if(b[i][j] == 0) 
					{
						if(count <=piexnumber ) 
							for(int k=1;k<=count;k++)
								b[i][j-k] = 0;
						count =0;
					}
				}
			}
			count = 0;
			for(int j=0;j<lie;j++)
			{
				for(int i=0;i<hang;i++)
				{
			
					if(b[i][j] == 1) count++;
					if(b[i][j] == 0) 
					{
						if(count <=piexnumber ) 
							for(int k=1;k<=count;k++)
								b[i-k][j] = 0;
						count =0;
					}
				}
			}
			
			return b;	
		}
		
		public static int[][] BinImage(BufferedImage bi,int width, int height, String destpath, int Threshold){
			//过滤背景色进行黑白二值化处理
			int[][]b = new int[1000][1000];
			try {
	 			BufferedImage bi2=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				Raster raster=bi.getRaster();
				WritableRaster wr=bi2.getRaster();
				int[] a=new int[4];
				for(int i=0;i<width;i++){
					for(int j=0;j<height;j++){	
						raster.getPixel(i, j, a);
						if((a[0]+a[1]+a[2])/3>Threshold){
							a[0]=255;
							a[1]=255;
							a[2]=255;
							a[3]=255;
							wr.setPixel(i, j, a);
						}else{
							a[0]=0;
							a[1]=0;
							a[2]=0;
							a[3]=255;
							wr.setPixel(i, j, a);
	 
						}
						if(a[0] ==255) b[j][i] = 0;
						else b[j][i] = 1;
					}
				}					
				ImageIO.write(bi2, "PNG", new File(destpath));	
			} catch (IOException e) {
				e.printStackTrace();
			}
			return b;
		}
		
		public static int[][] CutImageDoOCRIndex(int[][] b,int hang,int lie)
		{//将图片裁剪成单个数字
			//横向切成两部分
			int num1=0,num2=0;
			int hangcut=0;
			int[] liecut1=new int[13];
			liecut1[0]=0;
			int[] liecut2=new int[9];
			liecut2[0]=0;
			int k=1,flag=0,count=0;
			for(int i=0;i<hang;i++)
			{
				for(int j=0;j<lie;j++)
				{
					if(b[i][j]==1)
						flag=1;
					else
						count++;
				}
				if(count==lie&&flag==1)
				{
					hangcut=i+1;
					break;
				}
				else
					count=0;
			}
		
			
			//纵向切割上半部分
			flag=0;
			for(int j=0;j<lie;j++)
			{
			for(int i=0;i<hangcut;i++)			
				{
					if(b[i][j]==1)
						flag=1;
					else
						count++;
				}
				if(count==hangcut&&flag==1)
				{
					liecut1[k]=j+1;
					k++;
					num1++;
					flag=0;
				}
				else
					count=0;
			}
			
			//纵向切割下半部分
			flag=0;
			k=1;
			for(int j=0;j<lie;j++)
			{
			for(int i=hangcut;i<hang;i++)			
				{
					if(b[i][j]==1)
						flag=1;
					else
						count++;
																
				}
				if(count==(hang-hangcut)&&flag==1)
				{
					liecut2[k]=j+1;
					k++;
					num2++;
					flag=0;
				}
				else
				{
					count=0;
				}
			}
			
			//写成三维数组
			k=0;
			int[][][] cut=new int[num1+num2+2][hang][lie];
			int[][] ocrResult = new int[2][25];
			for(;k<num1;k++)
			{
				for(int i=0;i<hangcut;i++)
				{
					for(int j=liecut1[k];j<liecut1[k+1];j++)
					{
					    cut[k][i][j-liecut1[k]]=b[i][j];
					}
					
				}
				ocrResult[0][k] = ExtractNumber(cut[k],hangcut,liecut1[k+1]-liecut1[k]);		
			}
			k=0;
			for(;k<num2;k++)
			{
				for(int i=hangcut;i<hang;i++)
				{
					for(int j=liecut2[k];j<liecut2[k+1];j++)
					{
						cut[k+num1][i-hangcut][j-liecut2[k]]=b[i][j];
					}
					
			}
				ocrResult[1][k] = ExtractNumber(cut[k+num1],hang-hangcut,liecut2[k+1]-liecut2[k]);
			}
			return ocrResult;
		}
}
