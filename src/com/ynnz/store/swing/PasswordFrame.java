package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;

import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.Constants;
import com.ynnz.store.util.DataMapUtil;

public class PasswordFrame extends ParentFrame {
	private static final long serialVersionUID = 1L;
	private JPasswordField oldPasswordTx, newPasswordTxt, surePasswordTxt;
	private JButton sureBtn;// 确认按钮
	private JButton cancelBtn;// 取消按钮
	private IUserinfoDao userinfoDao = new UserinfoDaoImpl();
	private PasswordFrame pwdFrm = this;

	public PasswordFrame() {
		super("密码维护");
		init();// 窗体初始化
		pwdFrame();
		btnEvent();
		this.setResizable(false);
	}

	/**
	 * 设置窗体参数
	 */
	private void init() {
		this.setLayout(null);
		this.setBounds(450, 200, 350, 300);// 设置窗体坐标位置和宽高
		this.setResizable(false);
	}

	private void pwdFrame() {
		JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.NORTH);
		tabPanel.setBounds(0, 0, 350, 300);
		JPanel configInfoPanel = new JPanel();
		configInfoPanel.setLayout(null);// 用定位的方式就不能有布局
		configInfoPanel.setBounds(0, 0, 350, 300);
		this.add(configInfoPanel);

		JLabel shopLbl = new JLabel("旧密码：");
		shopLbl.setBounds(60, 20, 70, 20);
		oldPasswordTx = new JPasswordField();
		oldPasswordTx.setBounds(130, 20, 120, 20);
		configInfoPanel.add(shopLbl);
		configInfoPanel.add(oldPasswordTx);

		JLabel imgLbl = new JLabel("新密码：");
		imgLbl.setBounds(60, 50, 70, 20);
		newPasswordTxt = new JPasswordField();
		newPasswordTxt.setBounds(130, 50, 120, 20);
		configInfoPanel.add(imgLbl);
		configInfoPanel.add(newPasswordTxt);

		JLabel xseLbl = new JLabel("新密码确认：");
		xseLbl.setBounds(30, 80, 90, 20);
		surePasswordTxt = new JPasswordField();
		surePasswordTxt.setBounds(130, 80, 120, 20);
		configInfoPanel.add(xseLbl);
		configInfoPanel.add(surePasswordTxt);

		sureBtn = new JButton("确定");
		sureBtn.setBounds(60, 120, 80, 20);
		cancelBtn = new JButton("取消");
		cancelBtn.setBounds(175, 120, 80, 20);
		configInfoPanel.add(sureBtn);
		configInfoPanel.add(cancelBtn);
		tabPanel.add("修改密码", configInfoPanel);
		this.add(tabPanel);
	}

	private void btnEvent() {
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pwdFrm.dispose();

			}
		});

		// 确定按钮绑定事件
		sureBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String oldPwd = oldPasswordTx.getText();
				String newPwd = newPasswordTxt.getText();
				String surePwd = surePasswordTxt.getText();

				if (oldPwd == null || oldPwd.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请填写旧密码！");
					return;
				}
				UserInfo u = DataMapUtil.LOGIN_INFO.get(Constants.LOGIN_USER);
				if (!oldPwd.equals(u.getPwd())) {
					JOptionPane.showMessageDialog(null, "旧密码输入错误，请重新输入！");
					return;
				}

				if (newPwd == null || newPwd.trim().length() < 6 || newPwd.trim().length() > 10) {
					JOptionPane.showMessageDialog(null, "请填写6~10位的新密码！");
					return;
				} else if (surePwd == null || surePwd.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请填写确认新密码！");
					return;
				}
				if (!newPwd.equals(surePwd)) {
					JOptionPane.showMessageDialog(null, "两次新密码输入不一致，请重新输入！");
					return;
				}
				u.setPwd(newPwd);
				boolean ret = userinfoDao.updateUser(u);
				if (ret) {
					JOptionPane.showMessageDialog(null, "密码修改成功！");
					pwdFrm.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "密码修改失败！");
				}
			}
		});
	}

}
