package com.ynnz.store.swing;

import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.Constants;
import com.ynnz.store.util.DataMapUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LoginFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;

	// 数据访问对象
	private final IUserinfoDao userinfoDao = new UserinfoDaoImpl();

	// 界面组件
	private JTextField nameTxt;
	private JPasswordField pwdTxt;
	private static JButton btn;
	private final LoginFrame login = this;
	private JCheckBox rememberPasswordCheck;
	private static final String CONFIG_FILE = "login.properties";

	public LoginFrame() {
		super("登录");
		initComponents();
		bindEvents();
		this.setResizable(false);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);// 关闭窗口时程序退出
		this.setVisible(true);
		loadSavedLoginInfo();
	}

	/**
	 * 初始化界面组件
	 */
	private void initComponents() {
		this.setLayout(null);

		JPanel imgPanel = new JPanel();
		try {
			String imagePath = System.getProperty("user.dir") + "/src/com/ynnz/store/swing/img/login.png";
			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				ImageIcon bgIcon = new ImageIcon(imagePath);
				int imgWidth = bgIcon.getIconWidth();
				int imgHeight = bgIcon.getIconHeight();
				this.setBounds(300, 150, imgWidth, imgHeight); // 根据图片大小设置窗口
				imgPanel.setBounds(0, 0, imgWidth, imgHeight);
				JLabel labl = new JLabel(bgIcon);
				labl.setBounds(0, 0, imgWidth, imgHeight);
				imgPanel.add(labl);

				int inputWidth = 240;
				int inputHeight = 38;
				int inputX = (imgWidth - inputWidth) / 2;
				int nameY = imgHeight / 3;
				int pwdY = nameY + inputHeight + 18;
				int btnWidth = 120;
				int btnHeight = 40;
				int btnX = (imgWidth - btnWidth) / 2;
				int btnY = pwdY + inputHeight + 28;

				// 用户名输入框，带提示词
				nameTxt = new JTextField("请输入手机号");
				nameTxt.setBounds(inputX, nameY, inputWidth, inputHeight);
				nameTxt.setForeground(Color.GRAY);
				nameTxt.setFont(new Font("微软雅黑", Font.PLAIN, 18));
				nameTxt.setBackground(Color.WHITE);
				nameTxt.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
				nameTxt.setCaretColor(Color.BLACK);
				nameTxt.setOpaque(true);
				nameTxt.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						if (nameTxt.getText().equals("请输入手机号")) {
							nameTxt.setText("");
							nameTxt.setForeground(Color.BLACK);
						}
					}

					@Override
					public void focusLost(FocusEvent e) {
						if (nameTxt.getText().isEmpty()) {
							nameTxt.setForeground(Color.GRAY);
							nameTxt.setText("请输入手机号");
						}
					}
				});

				// 密码输入框，带提示词
				pwdTxt = new JPasswordField("请输入密码");
				pwdTxt.setBounds(inputX, pwdY, inputWidth, inputHeight);
				pwdTxt.setForeground(Color.GRAY);
				pwdTxt.setFont(new Font("微软雅黑", Font.PLAIN, 18));
				pwdTxt.setBackground(Color.WHITE);
				pwdTxt.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
				pwdTxt.setCaretColor(Color.BLACK);
				pwdTxt.setOpaque(true);
				pwdTxt.setEchoChar((char) 0); // 显示明文提示
				pwdTxt.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						String pwd = new String(pwdTxt.getPassword());
						if (pwd.equals("请输入密码")) {
							pwdTxt.setText("");
							pwdTxt.setForeground(Color.BLACK);
							pwdTxt.setEchoChar('●'); // 输入时显示为密码
						}
					}

					@Override
					public void focusLost(FocusEvent e) {
						String pwd = new String(pwdTxt.getPassword());
						if (pwd.isEmpty()) {
							pwdTxt.setForeground(Color.GRAY);
							pwdTxt.setText("请输入密码");
							pwdTxt.setEchoChar((char) 0); // 显示明文提示
						}
					}
				});

				// 记住密码选项
				rememberPasswordCheck = new JCheckBox("记住密码");
				rememberPasswordCheck.setBounds(inputX, pwdY + inputHeight + 5, 100, 20);
				rememberPasswordCheck.setFont(new Font("微软雅黑", Font.PLAIN, 14));
				rememberPasswordCheck.setForeground(Color.BLACK);
				rememberPasswordCheck.setOpaque(false);

				labl.add(nameTxt);
				labl.add(pwdTxt);
				labl.add(rememberPasswordCheck);

				// 登录按钮美化
				btn = new JButton("登录");
				btn.setBounds(btnX, btnY, btnWidth, btnHeight);
				btn.setFont(new Font("微软雅黑", Font.BOLD, 20));
				btn.setForeground(Color.WHITE);
				btn.setBackground(new Color(33, 150, 243)); // 蓝色主色
				btn.setBorder(new LineBorder(new Color(33, 150, 243), 2, true));
				btn.setFocusPainted(false);
				btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btn.setBackground(new Color(30, 136, 229)); // 悬停更深蓝
					}

					@Override
					public void mouseExited(MouseEvent e) {
						btn.setBackground(new Color(33, 150, 243));
					}
				});
				labl.add(btn);
				imgPanel.setOpaque(false);
				this.add(imgPanel);
			} else {
				System.out.println("登录背景图片不存在: " + imagePath);
				// 设置默认窗口大小
				this.setBounds(300, 150, 400, 300);
				imgPanel.setBounds(0, 0, 400, 300);
				JLabel labl = new JLabel("登录");
				labl.setBounds(0, 0, 400, 300);
				imgPanel.add(labl);
			}
		} catch (Exception e) {
			System.out.println("加载登录背景图片失败: " + e.getMessage());
			e.printStackTrace();
			// 设置默认窗口大小
			this.setBounds(300, 150, 400, 300);
			imgPanel.setBounds(0, 0, 400, 300);
			JLabel labl = new JLabel("登录");
			labl.setBounds(0, 0, 400, 300);
			imgPanel.add(labl);
		}
	}

	/**
	 * 绑定所有事件
	 */
	private void bindEvents() {
		// 用户名输入框回车事件
		nameTxt.addActionListener(e -> doLogin());
		// 密码输入框回车事件
		pwdTxt.addActionListener(e -> doLogin());
		// 登录按钮点击事件
		btn.addActionListener(e -> doLogin());
	}

	/**
	 * 登录过程
	 */
	private void doLogin() {
		String name = nameTxt.getText();
		String pwd = new String(pwdTxt.getPassword());
		if (name == null || name.trim().isEmpty() || name.equals("请输入手机号")) {
			JOptionPane.showMessageDialog(null, "请输入用户名！");
			return;
		}
		if (pwd == null || pwd.trim().isEmpty() || pwd.equals("请输入密码")) {
			JOptionPane.showMessageDialog(null, "请输入密码！");
			return;
		}
		UserInfo u = userinfoDao.getUserByMobile(name);
		if (u == null) {
			JOptionPane.showMessageDialog(null, "用户名错误！");
			return;
		}
		if (!pwd.equals(u.getPwd())) { // 如果密码加密还需要先解密再比较
			JOptionPane.showMessageDialog(null, "登录密码错误！");
			return;
		}
		DataMapUtil.LOGIN_INFO.put(Constants.LOGIN_USER, u);
		login.dispose();
		new IndexFrame();
		if (rememberPasswordCheck.isSelected()) {
			saveLoginInfo(name, pwd);
		} else {
			clearSavedLoginInfo();
		}
	}

	// 保存登录信息
	private void saveLoginInfo(String username, String password) {
		try {
			Properties props = new Properties();
			props.setProperty("username", username);
			props.setProperty("password", password);
			props.setProperty("remember", "true");

			File configFile = new File(CONFIG_FILE);
			FileOutputStream fos = new FileOutputStream(configFile);
			props.store(fos, "Login Information");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 清除保存的登录信息
	private void clearSavedLoginInfo() {
		try {
			File configFile = new File(CONFIG_FILE);
			if (configFile.exists()) {
				configFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 加载保存的登录信息
	private void loadSavedLoginInfo() {
		try {
			File configFile = new File(CONFIG_FILE);
			if (configFile.exists()) {
				Properties props = new Properties();
				FileInputStream fis = new FileInputStream(configFile);
				props.load(fis);
				fis.close();

				String username = props.getProperty("username");
				String password = props.getProperty("password");
				String remember = props.getProperty("remember");

				if ("true".equals(remember)) {
					nameTxt.setText(username);
					pwdTxt.setText(password);
					rememberPasswordCheck.setSelected(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
