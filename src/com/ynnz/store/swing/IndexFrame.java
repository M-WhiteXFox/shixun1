package com.ynnz.store.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.ynnz.store.dao.IDictionaryDao;
import com.ynnz.store.dao.impl.DictionaryDaoImpl;
import com.ynnz.store.pojo.Dictionary;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.Constants;
import com.ynnz.store.util.DataMapUtil;
import com.ynnz.store.util.DateUtil;

public class IndexFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JMenu menuJxc, menuTj, menuWh, menuGl;
	private JMenuItem itemSyt, itemRk, itemLl, itemTh, itemXs, itemGz, itemFl, itemYg, itemPz,itemPwd;
	private IDictionaryDao dictionaryDao = new DictionaryDaoImpl();
	private JButton logOut;
	private IndexFrame indexFrm = this;

	public IndexFrame() {
		super("首页");
		this.setLayout(null);
		this.setBounds(200, 70, 900, 530);
		menuJxc = new JMenu("进销存管理");
		itemSyt = new JMenuItem("收银台");
		itemRk = new JMenuItem("商品入库");
		itemLl = new JMenuItem("商品浏览");
		itemTh = new JMenuItem("退货管理");
		menuTj = new JMenu("数据统计分析");
		itemXs = new JMenuItem("销售统计");
		itemGz = new JMenuItem("工资核算");
		menuWh = new JMenu("基础数据维护");
		itemFl = new JMenuItem("商品分类管理");
		itemYg = new JMenuItem("员工管理");
		menuGl = new JMenu("系统管理");
		itemPz = new JMenuItem("系统配置");
		itemPwd=new JMenuItem("密码维护");
		init();
		menuEvent();
		this.setResizable(false);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);// 关闭窗口时程序退出
		this.setVisible(true);
	}

	/**
	 * 首页面初始化
	 */
	private void init() {
		JMenuBar menuBar = new JMenuBar();

		checkRight(menuBar);

		this.setJMenuBar(menuBar);

		JLabel loginInfo = new JLabel();
		loginInfo.setBounds(250, 20, 180, 30);
		UserInfo u = DataMapUtil.LOGIN_INFO.get(Constants.LOGIN_USER);
		String name = u.getSaleManName() + "（" + u.getRole() + "），";
		loginInfo.setText(name + "欢迎您！");
		this.add(loginInfo);
		JLabel loginTime = new JLabel();
		loginTime.setBounds(430, 20, 180, 30);
		loginTime.setText(DateUtil.getChinaDateTime(new Date()));
		this.add(loginTime);
		logOut = new JButton("退出");
		logOut.setBounds(800, 20, 60, 30);
		this.add(logOut);

		JTabbedPane pan = new JTabbedPane(JTabbedPane.NORTH);// 外面套一层选项卡面板
		pan.setBounds(0, 40, 900, 470);
		JPanel imgPanel = new JPanel();
		imgPanel.setBounds(0, 0, 900, 470);
		URL path = this.getClass().getResource("img/index.jpg");
		Dictionary dic = dictionaryDao.getDictionaryByTypeAndName(Constants.DICTIONARY_CONFIG,
				Constants.DICTIONARY_CONFIG_IMG);

		JLabel labl = new JLabel(new ImageIcon(path));
		labl.setBounds(0, 0, 900, 470);
		imgPanel.add(labl);
		imgPanel.setOpaque(false);
		pan.add("首页", imgPanel);// 需要加选项卡的话，往pan里再加面板
		this.add(pan, BorderLayout.CENTER);

	}

	/**
	 * 菜单权限判断
	 */
	private void checkRight(JMenuBar menuBar) {
		UserInfo u = DataMapUtil.LOGIN_INFO.get(Constants.LOGIN_USER);
		String role = u.getRole();

		if ("导购员".equals(role)) {
			menuJxc.add(itemSyt);
			menuJxc.add(itemRk);
			menuJxc.add(itemLl);
			menuBar.add(menuJxc);
			menuWh.add(itemFl);
			menuBar.add(menuJxc);
			menuBar.add(menuWh);
			menuGl.add(itemPwd);
			menuBar.add(menuGl);
		} else if ("店长".equals(role)) {
			menuJxc.add(itemSyt);
			menuJxc.add(itemRk);
			menuJxc.add(itemLl);
			menuJxc.add(itemTh);
			menuBar.add(menuJxc);

			menuTj.add(itemXs);
			menuTj.add(itemGz);
			menuBar.add(menuTj);

			menuWh.add(itemFl);
			menuWh.add(itemYg);
			menuBar.add(menuWh);

			menuGl.add(itemPz);
			menuGl.add(itemPwd);
			menuBar.add(menuGl);
		} else {//收银员
			menuJxc.add(itemSyt);
			menuBar.add(menuJxc);
			menuGl.add(itemPwd);
			menuBar.add(menuGl);
		}

	}

	/**
	 * 给菜单绑定事件
	 */
	private void menuEvent() {
		// “员工管理”菜单绑定事件
		itemYg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				UserFrame userFrm = new UserFrame();
			}
		});

		// “收银台”菜单绑定事件
		itemSyt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CrashFrame frm = new CrashFrame();
			}
		});

		// “商品入库”菜单绑定事件
		itemRk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsStorageFrame frm = new GoodsStorageFrame();
			}
		});

		// “商品浏览”菜单绑定事件
		itemLl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsViewFrame frm = new GoodsViewFrame();
			}
		});

		// “商品退货”菜单绑定事件
		itemTh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsReturnFrame frm = new GoodsReturnFrame();
			}
		});

		// “销售统计”菜单绑定事件
		itemXs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SaleStatisticFrame frm = new SaleStatisticFrame();
			}
		});

		// “工资核算”菜单绑定事件
		itemGz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PayStatisticFrame frm = new PayStatisticFrame();
			}
		});

		// “商品分类”菜单绑定事件
		itemFl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsTypeFrame frm = new GoodsTypeFrame();
			}
		});

		// “系统配置”菜单绑定事件
		itemPz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigFrame frm = new ConfigFrame();
			}
		});

		// “密码维护”菜单绑定事件
		itemPwd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PasswordFrame frm = new PasswordFrame();
			}
		});

		logOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				indexFrm.dispose();
				DataMapUtil.LOGIN_INFO.put(Constants.LOGIN_USER, null);
				LoginFrame login = new LoginFrame();
			}
		});
	}
}
