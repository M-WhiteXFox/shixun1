package com.ynnz.store.swing;

import com.ynnz.store.dao.IDictionaryDao;
import com.ynnz.store.dao.impl.DictionaryDaoImpl;
import com.ynnz.store.pojo.Dictionary;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.Constants;
import com.ynnz.store.util.DataMapUtil;
import com.ynnz.store.util.DateUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import java.awt.Component;

public class IndexFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JMenu menuJxc, menuTj, menuWh, menuGl;
	private JMenuItem itemSyt, itemRk, itemRkRecord, itemLl, itemTh, itemXs, itemGz, itemFl, itemYg, itemPz, itemPwd;
	private IDictionaryDao dictionaryDao = new DictionaryDaoImpl();
	private JButton logOut;
	private IndexFrame indexFrm = this;
	private JTabbedPane mainTabbedPane;
	private Map<String, JPanel> tabPanels = new HashMap<>();

	// 定义颜色常量
	private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
	private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
	private static final Color MENU_BACKGROUND = new Color(255, 255, 255);
	private static final Color MENU_SELECTED = new Color(232, 240, 254);
	private static final Font MAIN_FONT = new Font("微软雅黑", Font.PLAIN, 14);
	private static final Font MENU_FONT = new Font("微软雅黑", Font.PLAIN, 14);
	private static final Font TITLE_FONT = new Font("微软雅黑", Font.BOLD, 16);

	public IndexFrame() {
		super("首页");
		this.setLayout(new BorderLayout());
		this.setBounds(200, 70, 1000, 600);

		// 设置窗口背景色
		this.getContentPane().setBackground(BACKGROUND_COLOR);

		// 初始化菜单
		initMenus();

		init();
		menuEvent();
		this.setResizable(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);

		// 添加窗口大小改变监听器
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeComponents();
			}
		});
	}

	private void resizeComponents() {
		int width = this.getWidth();
		int height = this.getHeight();

		// 调整主标签页面板大小
		mainTabbedPane.setBounds(0, 40, width, height - 40);

		// 调整所有标签页内容大小
		for (JPanel panel : tabPanels.values()) {
			panel.setSize(width, height - 40);
			// 调整面板内所有组件的大小
			Component[] components = panel.getComponents();
			for (Component comp : components) {
				if (comp instanceof JScrollPane) {
					JScrollPane scrollPane = (JScrollPane) comp;
					scrollPane.setSize(width - 20, height - 100);
					scrollPane.setLocation(10, 40);
				} else if (comp instanceof JPanel) {
					JPanel subPanel = (JPanel) comp;
					if (subPanel.getLayout() == null) { // 如果是绝对布局
						subPanel.setSize(width, height - 40);
					}
				}
			}
			panel.revalidate();
			panel.repaint();
		}
	}

	private void initMenus() {
		menuJxc = createMenu("进销存管理");
		itemSyt = createMenuItem("收银台");
		itemRk = createMenuItem("商品入库");
		itemRkRecord = createMenuItem("入库记录");
		itemLl = createMenuItem("商品浏览");
		itemTh = createMenuItem("退货管理");

		menuTj = createMenu("数据统计分析");
		itemXs = createMenuItem("销售统计");
		itemGz = createMenuItem("工资核算");

		menuWh = createMenu("基础数据维护");
		itemFl = createMenuItem("商品分类管理");
		itemYg = createMenuItem("员工管理");

		menuGl = createMenu("系统管理");
		itemPz = createMenuItem("系统配置");
		itemPwd = createMenuItem("密码维护");
	}

	private JMenu createMenu(String text) {
		JMenu menu = new JMenu(text);
		menu.setFont(MENU_FONT);
		menu.setBackground(MENU_BACKGROUND);
		return menu;
	}

	private JMenuItem createMenuItem(String text) {
		JMenuItem item = new JMenuItem(text);
		item.setFont(MENU_FONT);
		item.setBackground(MENU_BACKGROUND);
		item.setBorder(new EmptyBorder(5, 10, 5, 10));

		item.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				item.setBackground(MENU_SELECTED);
			}

			public void mouseExited(MouseEvent e) {
				item.setBackground(MENU_BACKGROUND);
			}
		});

		return item;
	}

	private void init() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(MENU_BACKGROUND);
		menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		checkRight(menuBar);
		this.setJMenuBar(menuBar);

		// 用户信息面板
		JPanel userPanel = new JPanel();
		userPanel.setLayout(null);
		userPanel.setBounds(0, 0, 900, 40);
		userPanel.setBackground(BACKGROUND_COLOR);

		JLabel loginInfo = new JLabel();
		loginInfo.setBounds(250, 10, 180, 30);
		UserInfo u = DataMapUtil.LOGIN_INFO.get(Constants.LOGIN_USER);
		String name = u.getSaleManName() + "（" + u.getRole() + "），";
		loginInfo.setText(name + "欢迎您！");
		loginInfo.setFont(TITLE_FONT);
		userPanel.add(loginInfo);

		JLabel loginTime = new JLabel();
		loginTime.setBounds(430, 10, 180, 30);
		loginTime.setText(DateUtil.getChinaDateTime(new Date()));
		loginTime.setFont(MAIN_FONT);
		userPanel.add(loginTime);

		logOut = new JButton("退出");
		logOut.setBounds(800, 10, 60, 30);
		logOut.setFont(MAIN_FONT);
		logOut.setBackground(PRIMARY_COLOR);
		logOut.setForeground(Color.WHITE);
		logOut.setFocusPainted(false);
		logOut.setBorderPainted(false);
		logOut.setCursor(new Cursor(Cursor.HAND_CURSOR));

		logOut.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				logOut.setBackground(PRIMARY_COLOR.darker());
			}

			public void mouseExited(MouseEvent e) {
				logOut.setBackground(PRIMARY_COLOR);
			}
		});

		userPanel.add(logOut);
		this.add(userPanel, BorderLayout.NORTH);

		// 主标签页面板
		mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainTabbedPane.setFont(TITLE_FONT);
		mainTabbedPane.setBackground(BACKGROUND_COLOR);

		// 添加首页标签
		JPanel homePanel = new JPanel(new BorderLayout());
		URL path = this.getClass().getResource("img/index.jpg");
		Dictionary dic = dictionaryDao.getDictionaryByTypeAndName(Constants.DICTIONARY_CONFIG,
				Constants.DICTIONARY_CONFIG_IMG);

		JLabel labl = new JLabel(new ImageIcon(path));
		labl.setHorizontalAlignment(JLabel.CENTER);
		labl.setVerticalAlignment(JLabel.CENTER);
		homePanel.add(labl, BorderLayout.CENTER);
		mainTabbedPane.addTab("首页", homePanel);
		tabPanels.put("首页", homePanel);

		this.add(mainTabbedPane, BorderLayout.CENTER);
	}

	private void menuEvent() {
		// "员工管理"菜单绑定事件
		itemYg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserFrame userFrm = new UserFrame();
				userFrm.setVisible(false);
				addNewTab("员工管理", userFrm.getContentPane());
			}
		});

		// "收银台"菜单绑定事件
		itemSyt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CrashFrame frm = new CrashFrame();
				frm.setVisible(false);
				addNewTab("收银台", frm.getContentPane());
			}
		});

		// "商品入库"菜单绑定事件
		itemRk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsStorageFrame frm = new GoodsStorageFrame();
				frm.setVisible(false);
				addNewTab("商品入库", frm.getContentPane());
			}
		});

		// "商品浏览"菜单绑定事件
		itemLl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsViewFrame frm = new GoodsViewFrame();
				frm.setVisible(false);
				addNewTab("商品浏览", frm.getContentPane());
			}
		});

		// "商品退货"菜单绑定事件
		itemTh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsReturnFrame frm = new GoodsReturnFrame();
				frm.setVisible(false);
				addNewTab("退货管理", frm.getContentPane());
			}
		});

		// "销售统计"菜单绑定事件
		itemXs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaleStatisticFrame frm = new SaleStatisticFrame();
				frm.setVisible(false);
				addNewTab("销售统计", frm.getContentPane());
			}
		});

		// "工资核算"菜单绑定事件
		itemGz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PayStatisticFrame frm = new PayStatisticFrame();
				frm.setVisible(false);
				addNewTab("工资核算", frm.getContentPane());
			}
		});

		// "商品分类管理"菜单绑定事件
		itemFl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsTypeFrame frm = new GoodsTypeFrame();
				frm.setVisible(false);
				addNewTab("商品分类管理", frm.getContentPane());
			}
		});

		// "系统配置"菜单绑定事件
		itemPz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigFrame frm = new ConfigFrame();
				frm.setVisible(false);
				addNewTab("系统配置", frm.getContentPane());
			}
		});

		// "密码维护"菜单绑定事件
		itemPwd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PasswordFrame frm = new PasswordFrame();
				frm.setVisible(false);
				addNewTab("密码维护", frm.getContentPane());
			}
		});

		// "入库记录"菜单绑定事件
		itemRkRecord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsStorageRecordFrame frm = new GoodsStorageRecordFrame();
				frm.setVisible(false);
				addNewTab("入库记录", frm.getContentPane());
			}
		});

		logOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				indexFrm.dispose();
				LoginFrame loginFrm = new LoginFrame();
			}
		});
	}

	private void addNewTab(String title, Container content) {
		// 创建带有关闭按钮的标签页标题面板
		JPanel tabTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tabTitlePanel.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(MENU_FONT);

		JButton closeButton = new JButton("×");
		closeButton.setFont(new Font("Arial", Font.BOLD, 14));
		closeButton.setForeground(Color.GRAY);
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setFocusPainted(false);
		closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 添加鼠标悬停效果
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setForeground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setForeground(Color.GRAY);
			}
		});

		// 添加关闭按钮点击事件
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = mainTabbedPane.indexOfTabComponent(tabTitlePanel);
				if (index != -1) {
					mainTabbedPane.removeTabAt(index);
					tabPanels.remove(title);
				}
			}
		});

		tabTitlePanel.add(titleLabel);
		tabTitlePanel.add(closeButton);

		// 创建内容面板
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(content, BorderLayout.CENTER);
		contentPanel.setBackground(BACKGROUND_COLOR);

		// 添加标签页
		mainTabbedPane.addTab(title, contentPanel);
		mainTabbedPane.setTabComponentAt(mainTabbedPane.getTabCount() - 1, tabTitlePanel);

		// 存储标签页面板引用
		tabPanels.put(title, contentPanel);

		// 切换到新标签页
		mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount() - 1);
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
			menuJxc.add(itemRkRecord);
			menuJxc.add(itemLl);
			menuBar.add(menuJxc);
			menuWh.add(itemFl);
			menuBar.add(menuWh);
			menuGl.add(itemPwd);
			menuBar.add(menuGl);
		} else if ("店长".equals(role)) {
			menuJxc.add(itemSyt);
			menuJxc.add(itemRk);
			menuJxc.add(itemRkRecord);
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
		} else {// 收银员
			menuJxc.add(itemSyt);
			menuBar.add(menuJxc);
			menuGl.add(itemPwd);
			menuBar.add(menuGl);
		}
	}
}