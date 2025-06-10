package com.ynnz.store.swing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.UserInfo;

public class UserFrame extends ParentFrame {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	private IUserinfoDao userinfoDao = new UserinfoDaoImpl();

	private JTable table;

	private JButton addBtn;// 新增用户按钮
	private JButton cleanBtn;// 清空选择编辑的用户
	private JButton delBtn;// 删除用户按钮
	private JTextField nameTxt, mobileTxt, basesalaryTxt, commionsionRateTxt;
	private JComboBox<String> genderTxt, roleTxt;
	private JScrollPane listPanel;
	private JPanel userPanel;

	private int userId = 0;// 存储table中选中的用户的Id

	public UserFrame() {
		super("员工信息管理");
		init();// 窗体初始化
		userList();// 创建用户列表信息面板
		createUserForm();// 创建编辑用户信息面板
		editUserEvent();// 绑定新增事件
		delUserEvent();// 绑定删除事件
		clearSelect();
		this.setResizable(false);
	}

	/**
	 * 设置窗体参数
	 *
	 */
	private void init() {
		this.setLayout(null);
		this.setBounds(300, 150, 800, 500); // 增加窗口大小
		delBtn = new JButton("删除员工");
		delBtn.setBounds(60, 330, 90, 20);
		this.add(delBtn);
	}

	/**
	 * 查看员工信息列表
	 */
	private void userList() {
		String[] head = { "员工ID", "员工姓名", "手机号码", "性别", "基本工资", "提成比例", "角色" };
		List<UserInfo> users = userinfoDao.getUserList();

		Object data[][] = new Object[0][head.length];
		if (users != null) {
			data = new Object[users.size()][head.length];
			int i = 0;
			for (Iterator<UserInfo> iterator = users.iterator(); iterator.hasNext();) {
				UserInfo userInfo = (UserInfo) iterator.next();
				data[i] = new Object[] { userInfo.getSaleManId(), userInfo.getSaleManName(), userInfo.getMobile(),
						userInfo.getGender(), userInfo.getBasesalary(), userInfo.getCommissionRate(),
						userInfo.getRole() };
				i++;
			}
		}
		table = new JTable(data, head);
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 0, 580, 300);
		this.add(listPanel);
		// new 新table时候，需要重新绑定事件，不然无法触发事件
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int rowNo = table.getSelectedRow();// 获取选中的行号
				TableModel model = table.getModel();// 把table里的数据转存到Model里
				if (rowNo < 0) {// 因为移除或添加表单行数据时会触发该事件，所以添加判断避免报错
					return;
				}
				userId = (int) model.getValueAt(rowNo, 0);

				UserInfo user = userinfoDao.getUser(userId);
				if (user != null) {
					nameTxt.setText(user.getSaleManName());
					mobileTxt.setText(user.getMobile());
					for (int i = 0; i < 3; i++) {
						String gen = genderTxt.getItemAt(i);
						if (user.getGender().equals(gen)) {
							genderTxt.setSelectedIndex(i);
						}
					}
					basesalaryTxt.setText(user.getBasesalary() + "");
					commionsionRateTxt.setText(user.getCommissionRate() + "");
					for (int i = 0; i < 4; i++) {
						String r = roleTxt.getItemAt(i);
						if (user.getRole().equals(r)) {
							roleTxt.setSelectedIndex(i);
						}
					}
					userId = user.getSaleManId();
					addBtn.setText("修改员工");
				}
			}
		});

	}

	/**
	 * 构造用户信息编辑面板
	 *
	 * @param x
	 * @param y
	 */
	private void createUserForm() {
		userPanel = new JPanel();
		userPanel.setLayout(null);
		userPanel.setBounds(600, 0, 190, 350);
		this.add(userPanel);
		JLabel title = new JLabel("员工信息");
		title.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		title.setBounds(60, 10, 80, 20);
		userPanel.add(title);

		JLabel name = new JLabel("员工姓名：");
		name.setBounds(20, 40, 80, 20);
		nameTxt = new JTextField();
		nameTxt.setBounds(105, 40, 80, 20);
		userPanel.add(name);
		userPanel.add(nameTxt);

		JLabel mobile = new JLabel("手机号码：");
		mobile.setBounds(20, 70, 80, 20);
		mobileTxt = new JTextField();
		userPanel.add(mobile);
		mobileTxt.setBounds(105, 70, 80, 20);
		userPanel.add(mobileTxt);
		JLabel gender = new JLabel("性别：");
		gender.setBounds(20, 100, 80, 20);

		genderTxt = new JComboBox<String>();
		genderTxt.addItem("--请选择--");
		genderTxt.addItem("男");
		genderTxt.addItem("女");
		genderTxt.setBounds(105, 100, 80, 20);

		userPanel.add(gender);
		userPanel.add(genderTxt);

		JLabel basesalary = new JLabel("基本工资：");
		basesalary.setBounds(20, 130, 80, 20);
		basesalaryTxt = new JTextField();
		basesalaryTxt.setBounds(105, 130, 80, 20);
		userPanel.add(basesalaryTxt);
		userPanel.add(basesalary);

		JLabel commionsionRate = new JLabel("提成比例：");
		commionsionRate.setBounds(20, 160, 80, 20);
		commionsionRateTxt = new JTextField();
		commionsionRateTxt.setBounds(105, 160, 80, 20);
		userPanel.add(commionsionRate);
		userPanel.add(commionsionRateTxt);

		JLabel role = new JLabel("员工角色：");
		role.setBounds(20, 190, 80, 20);
		roleTxt = new JComboBox<String>();
		roleTxt.addItem("--请选择--");
		roleTxt.addItem("收银员");
		roleTxt.addItem("导购员");
		roleTxt.addItem("店长");
		roleTxt.setMaximumRowCount(3);
		roleTxt.setBounds(105, 190, 80, 20);
		userPanel.add(role);
		userPanel.add(roleTxt);
		userPanel.repaint();
		addBtn = new JButton("新增员工");
		addBtn.setBounds(20, 240, 90, 20);
		cleanBtn = new JButton("清空选择");
		cleanBtn.setBounds(115, 240, 90, 20);
		userPanel.add(addBtn);
		userPanel.add(cleanBtn);

	}

	/**
	 * 删除用户列表的面板 重新创建之前需要调用此方法先删除
	 */
	public void closePa() {
		this.remove(listPanel);
	}

	/**
	 * "新增用户按钮"监听事件，新增/编辑用户信息
	 */
	private void editUserEvent() {
		addBtn.addActionListener(new ActionListener() {
			private Pattern patter;
			private Matcher matcher;

			@Override
			public void actionPerformed(ActionEvent e) {
				UserInfo user = new UserInfo();
				String name = nameTxt.getText();
				if (name == null || name.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "员工姓名不能为空！");
					return;
				} else if (name.trim().length() > 20) {
					JOptionPane.showMessageDialog(null, "员工姓名字数不能超过10个汉字！");
					return;
				} else {
					user.setSaleManName(name);
				}
				String mobile = mobileTxt.getText();
				String mobileReg = "1\\d{10}";
				patter = Pattern.compile(mobileReg);
				matcher = patter.matcher(mobile);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写手机号码，且只能是1开头的11位数字！");
					return;
				} else {
					user.setMobile(mobile);
				}
				UserInfo us = userinfoDao.getUserByMobile(mobile);
				if (us != null && userId == 0) {
					JOptionPane.showMessageDialog(null, "手机号码和其他员工的冲突，请重新填写！");
					return;
				}

				Object gender = genderTxt.getSelectedItem();
				if (gender == null || gender.toString().contains("选择")) {
					JOptionPane.showMessageDialog(null, "请选择性别！");
					return;
				} else {
					user.setGender(gender.toString());
				}

				String basesalary = basesalaryTxt.getText();
				String salaryReg = "[1-9]\\d*|[1-9]\\d*\\.[0-9]";
				patter = Pattern.compile(salaryReg);
				matcher = patter.matcher(basesalary);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写基本工资，且只能是整数或包含1位小数类型的数字！");
					return;
				} else {
					user.setBasesalary(Float.parseFloat(basesalary));
				}

				String commionRate = commionsionRateTxt.getText();
				String commionRateReg = "0\\.([1-9]\\d?|[0-9][1-9])";
				patter = Pattern.compile(commionRateReg);
				matcher = patter.matcher(commionRate);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写提成比例，且只能是0到1之间最多包含两位小数的数字！");
					return;
				} else {
					user.setCommissionRate(new BigDecimal(commionRate));
				}

				Object role = roleTxt.getSelectedItem();
				if (role != null && role.toString().contains("选择")) {
					JOptionPane.showMessageDialog(null, "请选择角色！");
					return;
				} else {
					user.setRole(role.toString());
				}
				user.setPwd("123456");// 设置默认密码
				boolean ret = false;
				String msg = "";
				if (userId != 0) {// 编辑员工信息时候需要设置id
					user.setSaleManId(userId);
					ret = userinfoDao.updateUser(user);
					msg = "编辑";
				} else {// 新增员工
					ret = userinfoDao.addUser(user);
					msg = "新增";
				}
				if (ret) {
					JOptionPane.showMessageDialog(null, msg + "员工信息成功！");
					closePa();
					userList();
				} else {
					JOptionPane.showMessageDialog(null, msg + "员工信息失败！");
				}
			}
		});
	}

	/**
	 * 删除员工信息事件绑定
	 */
	private void delUserEvent() {

		delBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNo = table.getSelectedRow();
				if (rowNo < 0) {
					JOptionPane.showMessageDialog(null, "请选中要删除的员工信息！");
					return;
				} else {
					int sel = JOptionPane.showConfirmDialog(null, "确认要删除该员工信息吗？");
					if (sel == 0) {
						boolean ret = userinfoDao.delUser(userId);
						if (ret) {
							// 清空编辑表单
							table.getSelectionModel().clearSelection();
							userId = 0;
							nameTxt.setText("");
							mobileTxt.setText("");
							genderTxt.setSelectedIndex(0);
							basesalaryTxt.setText("");
							commionsionRateTxt.setText("");
							roleTxt.setSelectedIndex(0);
							addBtn.setText("新增员工");
							closePa();
							userList();
							JOptionPane.showMessageDialog(null, "选中员工信息删除成功！");
						} else {
							JOptionPane.showMessageDialog(null, "员工信息删除失败！");
						}
					}
				}
			}
		});
	}

	/**
	 * 清空选择的员工
	 */
	private void clearSelect() {
		cleanBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNo = table.getSelectedRow();
				if (rowNo < 0) {
					return;
				}
				table.getSelectionModel().clearSelection();
				userId = 0;
				nameTxt.setText("");
				mobileTxt.setText("");
				genderTxt.setSelectedIndex(0);
				basesalaryTxt.setText("");
				commionsionRateTxt.setText("");
				roleTxt.setSelectedIndex(0);
				addBtn.setText("新增员工");
			}
		});
	}
}
