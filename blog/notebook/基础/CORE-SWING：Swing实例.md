### Swing简介
> Swing是一个用于开发Java应用程序用户界面的开发工具包。
以抽象窗口工具包（AWT）为基础使跨平台应用程序可以使用任何可插拔的外观风格。Swing开发人员只用很少的代码就可以利用Swing丰富、灵活的功能和模块化组件来创建优雅的用户界面。 工具包中所有的包都是以swing作为名称，例如javax.swing,javax.swing.event。


### swing小结
**Java布局方式**
- java.awt FlowLayout ：将组件按从左到右而后从上到下的顺序依次排列，一行不能放完则折到下一行继续放置
- java.awt GridLayout ：形似一个无框线的表格，每个单元格中放一个组件
- java.awt BorderLayout： 将组件按东、南、西、北、中五个区域放置，每个方向最多只能放置一个组件
- java.awt GridBagLayout ：非常灵活，可指定组件放置的具体位置及占用单元格数目

**刷新JPanel**：UiIndexMain.indexmain.repaint();

**提示框：**
- JOptionPane.showMessageDialog(null, "这是一个简单的消息框");
- JOptionPane.showMessageDialog(null, "提示正文", "标题", - JOptionPane.ERROR_MESSAGE);
- JOptionPane.showConfirmDialog(null,"这是一个有三个按钮的确认框，\n按任意按钮返回");
- JOptionPane.showInputDialog(null,"这是一个可供用户输入信息的对话框");

**销毁窗口：**
- mainfFrame.setVisible(true);    // 登陆成功，主窗口可见
- loginFrame.dispose();    // 销毁登陆窗口

**java调用webkit内核，做自己的浏览器：... **

### 实例A：Server启动器
```
package com.xxl.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {
	
	public JButton startBtn;
	public JButton stopBtn;
	public JButton exitBtn;
	
	public Main() {
		
		// 界面元素
		startBtn = new JButton("启动");
		startBtn.addActionListener(this);
		stopBtn = new JButton("停止");
		stopBtn.addActionListener(this);
		exitBtn = new JButton("退出");
		exitBtn.addActionListener(this);
		
		this.setLayout(new FlowLayout());
		this.add(startBtn);
		this.add(stopBtn);
		this.add(exitBtn);
		
		// 主界面
		this.setTitle("服务器");
		this.setSize(250, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startBtn) {
			//MsgServer.getInstance().start();
			this.getContentPane().setBackground(Color.GREEN);
		} else if (e.getSource() == stopBtn) {
			//MsgServer.getInstance().stop();
			this.getContentPane().setBackground(Color.GRAY);
		} else if (e.getSource() == exitBtn) {
			System.exit(0);
		}
		
	}

	public static void main(String[] args) {
		new Main();
	}

}

```

### 实例B：人脸识别，UI模块

```
package com.xxl.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * 首窗口
 * @author xuxueli
 *
 */
@SuppressWarnings("serial")
public class IndexUI extends JFrame{
	/**首窗口：实例*/
	public static IndexUI indexui;
	/**首窗口：标签面板*/
	public static JTabbedPane tabbedpane;
	
	/**
	 * 构造
	 */
	public IndexUI() {
		tabbedpane=new JTabbedPane();
		tabbedpane.add("人脸识别",getTabbedpaneFaceLogin());
		tabbedpane.add("视屏监控",getTabbedpaneVideoFind());
		this.add(tabbedpane);
		
		this.setTitle("人脸识别登录系统");
		this.setSize(1000,618);
		this.setLocationRelativeTo(null);//居中
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭
		this.setResizable(false); //不可调整大小
		this.setVisible(true);
		//overwrite windowClosing
		/*this.addWindowListener(
			new java.awt.event.WindowAdapter(){
				public void windowClosing(java.awt.event.WindowEvent evt) {
					
				}
		});*/

	
	}
	
	/**
	 * 标签窗口1:人脸识别
	 * @return
	 */
	private Component getTabbedpaneFaceLogin() {
		Panel p = new Panel();
		p.setBackground(Color.GRAY);
		return p;
	}

	/**
	 * 标签窗口2:视屏监控
	 * @return
	 */
	private Component getTabbedpaneVideoFind() {
		Panel p = new Panel();
		p.setBackground(Color.WHITE);
		return p;
	}
	

	/***** main测试 ************************************************************/
	public static void main(String[] args) 
	{
		indexui = new IndexUI();
	}
}

```
