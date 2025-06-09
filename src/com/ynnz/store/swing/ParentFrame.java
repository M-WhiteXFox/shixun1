package com.ynnz.store.swing;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ParentFrame extends JFrame {

	public ParentFrame(String title) {
		super("NIKE专卖店销售系统-"+title);
		URL path = this.getClass().getResource("img/logo.jpg");
		//添加logo图片
		ImageIcon icon = new ImageIcon(path);
		this.setIconImage(icon.getImage());
	}

}
