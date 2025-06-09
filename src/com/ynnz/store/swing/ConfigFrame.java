package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.ynnz.store.dao.IDictionaryDao;
import com.ynnz.store.dao.impl.DictionaryDaoImpl;
import com.ynnz.store.pojo.Dictionary;
import com.ynnz.store.util.Constants;

public class ConfigFrame extends ParentFrame {
	private static final long serialVersionUID = 1L;
	private JTextField shopNameTxt, imgTxt, xseTxt;
	private JButton sureBtn;// 确认按钮
	private JButton cancelBtn;// 取消按钮
	private IDictionaryDao dictionaryDao = new DictionaryDaoImpl();
	private Dictionary store, imgPath, kpi;
	private ConfigFrame configFrm = this;

	public ConfigFrame() {
		super("系统配置");
		store = new Dictionary();
		imgPath = new Dictionary();
		kpi = new Dictionary();
		init();// 窗体初始化
		configInfoFrame();
		btnEvent();
		this.setResizable(false);
		this.setVisible(true);// 必须放在页面所有部分布局完，不然页面有些组件显示不全
	}

	/**
	 * 设置窗体参数
	 */
	private void init() {
		this.setLayout(null);
		this.setBounds(450, 200, 350, 300);// 设置窗体坐标位置和宽高
		this.setResizable(false);
	}

	private void configInfoFrame() {
		List<Dictionary> list = dictionaryDao.getListByType(Constants.DICTIONARY_CONFIG);
		JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.NORTH);
		tabPanel.setBounds(0, 0, 350, 300);
		JPanel configInfoPanel = new JPanel();
		configInfoPanel.setLayout(null);// 用定位的方式就不能有布局
		configInfoPanel.setBounds(0, 0, 350, 300);
		this.add(configInfoPanel);

		if (list != null && list.size() >= 3) {
			store = list.get(0);
			imgPath = list.get(1);
			kpi = list.get(2);
		}

		JLabel shopLbl = new JLabel("门店名称：");
		shopLbl.setBounds(60, 20, 70, 20);
		shopNameTxt = new JTextField(store.getTypeValue()==null?"":store.getTypeValue());
		shopNameTxt.setBounds(130, 20, 100, 20);
		configInfoPanel.add(shopLbl);
		configInfoPanel.add(shopNameTxt);

		JLabel imgLbl = new JLabel("首页广告图片路径：");
		imgLbl.setBounds(10, 50, 120, 20);
		imgTxt = new JTextField(imgPath.getTypeValue()==null?"":imgPath.getTypeValue());
		imgTxt.setBounds(130, 50, 170, 20);
		configInfoPanel.add(imgLbl);
		configInfoPanel.add(imgTxt);

		JLabel xseLbl = new JLabel("月度考核保底销售额：");
		xseLbl.setBounds(0, 80, 130, 20);
		xseTxt = new JTextField(kpi.getTypeValue()==null?"":kpi.getTypeValue());
		xseTxt.setBounds(130, 80, 100, 20);
		configInfoPanel.add(xseLbl);
		configInfoPanel.add(xseTxt);

		sureBtn = new JButton("确定");
		sureBtn.setBounds(60, 120, 80, 20);
		cancelBtn = new JButton("取消");
		cancelBtn.setBounds(175, 120, 80, 20);
		configInfoPanel.add(sureBtn);
		configInfoPanel.add(cancelBtn);
		tabPanel.add("基本配置", configInfoPanel);
		this.add(tabPanel);
	}

	private void btnEvent() {
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				configFrm.dispose();

			}
		});

		//确定按钮绑定事件
		sureBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = shopNameTxt.getText();
				store.setTypeName(Constants.DICTIONARY_CONFIG_NAME);
				store.setDicType(Constants.DICTIONARY_CONFIG);

				if (name == null || name.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请填写门店名称！");
					return;
				} else if (name.trim().length() > 20) {
					JOptionPane.showMessageDialog(null, "门店名称字数不能超过10个字！");
					return;
				} else {
					store.setTypeValue(name);
				}
				boolean ret1 = false;
				if (store.getId() == 0) {
					ret1 = dictionaryDao.addDictionary(store);
				} else {
					ret1 = dictionaryDao.updateDictionary(store);
				}
				String img = imgTxt.getText();
				imgPath.setTypeName(Constants.DICTIONARY_CONFIG_IMG);
				imgPath.setDicType(Constants.DICTIONARY_CONFIG);
				if (img == null || img.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请填写首页广告图片路径！");
					return;
				} else if (img.trim().length() > 20) {
					JOptionPane.showMessageDialog(null, "首页广告图片路径不能超过40个字符！");
					return;
				} else {
					imgPath.setTypeValue(img);
				}
				boolean ret2 = false;
				if (imgPath.getId() == 0) {
					ret2 = dictionaryDao.addDictionary(imgPath);
				} else {
					ret2 = dictionaryDao.updateDictionary(imgPath);
				}
				String kpiBase = xseTxt.getText();
				kpi.setTypeName(Constants.DICTIONARY_CONFIG_KPI);
				kpi.setDicType(Constants.DICTIONARY_CONFIG);
				if (kpiBase == null || kpiBase.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请填写月度考核保底销售额！");
					return;
				} else if (kpiBase.trim().length() > 20) {
					JOptionPane.showMessageDialog(null, "月度考核保底销售额不能超过40个字符！");
					return;
				} else {
					kpi.setTypeValue(kpiBase);
				}
				boolean ret3 = false;
				if (kpi.getId() == 0) {
					ret3 = dictionaryDao.addDictionary(kpi);
				} else {
					ret3 = dictionaryDao.updateDictionary(kpi);
				}
				if (ret1 && ret2 && ret3) {
					configFrm.dispose();
					JOptionPane.showMessageDialog(null, "配置信息编辑成功！");
				} else {
					JOptionPane.showMessageDialog(null, "配置信息编辑失败！");
				}
			}
		});
	}

}
