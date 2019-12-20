### JavaCV环境搭建
##### OpenCV + javaCV 下载

[OpenCV，下载地址](http://opencv.org/downloads.html)
（OpenCV-2.4.5.exe，约272M）

[javaCV，下载地址](https://github.com/bytedeco/javacv)
（javacv-0.5-bin.zip，约11M，github页面向下滚动，有zip包和maven两种方式引用）

##### OpenCV安装
* 1：安装OpenCV，地址：E:\ProgramFiles\opencv
* 2：设置环境变量（注意：openCV环境变量设置完成之后，重启电脑）
    ```
    OPENCV  E:\ProgramFiles\opencv
    PATH    E:\ProgramFiles\opencv\build\x86\vc10\bin;
    ```
* 3、重启电脑

##### eclipse对环境进行测试
* 1、创建java project【人脸识别，核心类更改引入的“openCV相关文件”地址 】
    （haarcascade_frontalface_alt2（有的用的是haarcascade_frontalface_alt）、haarcascade_eye：它们分别是OpenCV提供的不同分类器，前者是人脸，后者是人眼）
* 2、导入javaCV的jar包
* 3、创建测试类，进行测试


```
package com.xxl.test;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;

public class MyTest {
	public static String savedImageFile = "d:\\my.jpg";


	static class TimerAction implements ActionListener {
		private Graphics2D g;
		private CanvasFrame canvasFrame;
		private int width,height;
		private int delta=10;
		private int count = 0;
		private Timer timer;
		
		public void setTimer(Timer timer){
			this.timer = timer;
		}
 
		public TimerAction(CanvasFrame canvasFrame){
			this.g = (Graphics2D)canvasFrame.getCanvas().getGraphics();
			this.canvasFrame = canvasFrame;
			this.width = canvasFrame.getCanvas().getWidth();
			this.height = canvasFrame.getCanvas().getHeight();
		}
		public void actionPerformed(ActionEvent e) {
			int offset = delta*count;
			if(width-offset>=offset && height-offset >= offset) {        
			g.drawRect(offset, offset, width-2*offset, height-2*offset);
			canvasFrame.repaint();
					count++;
			}else{
			timer.stop();
			count = 0;
			}            
		}
    }

	public static void main(String[] args) throws Exception {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.start();
		CanvasFrame canvasFrame = new CanvasFrame("Camera");
		IplImage image = grabber.grab();
		int width = image.width();
		int height = image.height();
		canvasFrame.setCanvasSize(width, height);
			
		final BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D bGraphics = bImage.createGraphics();     
			
		TimerAction timerAction = new TimerAction(canvasFrame);
		final Timer timer=new Timer(10, timerAction);
		timerAction.setTimer(timer);
	 
		canvasFrame.getCanvas().addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){     
				timer.start(); 
				try {
					ImageIO.write(bImage, "jpg", new File(savedImageFile));
				} catch (IOException e1) {
					e1.printStackTrace();
				}        
			}             
		});
			
		while(canvasFrame.isVisible() && (image=grabber.grab()) != null){
			if(!timer.isRunning()) {
				canvasFrame.showImage(image);
				bGraphics.drawImage(image.getBufferedImage(),null,0,0);  
			}
		}
			
		cvReleaseImage(image); 
		grabber.stop();
		canvasFrame.dispose();
		
	}

}
```

