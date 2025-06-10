package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import com.ynnz.store.dao.ISalesDetailDao;
import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.SalesDetailDaoImpl;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.Sales;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.Constants;
import com.ynnz.store.util.DateUtil;
import com.ynnz.store.util.FrameUtil;
import com.ynnz.store.util.RoundTool;
import com.ynnz.store.util.StringUtils;

public class SaleStatisticFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;

	private IUserinfoDao userinfoDao = new UserinfoDaoImpl();
	private JComboBox<String> dchkTxt;
	private JTextField dateSTxt, dateETxt;
	private JButton queryBtn;
	private DefaultTableModel model;
	private JComboBox<UserInfo> dgyTxt;
	private JLabel numLbl, crasherLbl;
	private ISalesDetailDao salesDetailDao = new SalesDetailDaoImpl();

	public SaleStatisticFrame() {
		super("销售统计");
		init();
		queryPanel();
		tableListPane();
		accountPanel();
		btnEvent();
	}

	public void init() {
		this.setLayout(null);
		this.setBounds(300, 150, 800, 500);
		this.setResizable(false);
	}

	/**
	 * 查询条件面板构造
	 */
	private void queryPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 700, 30);

		JLabel dgName = new JLabel("导购员：");
		dgName.setBounds(10, 10, 60, 20);
		dgyTxt = new JComboBox<UserInfo>();
		dgyTxt.setBounds(70, 10, 90, 20);

		List<UserInfo> users = userinfoDao.getUserList();
		// 从数据库查询导购员
		UserInfo u = new UserInfo("--请选择--");
		dgyTxt.addItem(u);
		if (users != null && users.size() > 0) {
			for (Iterator<UserInfo> ite = users.iterator(); ite.hasNext();) {
				UserInfo userInfo = (UserInfo) ite.next();
				if (Constants.ROLE_DGY.equals(userInfo.getRole())) {
					dgyTxt.addItem(userInfo);
				}
			}
		}

		JLabel dateLbl = new JLabel("日期：");
		dateLbl.setBounds(170, 10, 70, 20);
		dateSTxt = new JTextField();
		dateSTxt.setBounds(210, 10, 120, 20);
		JLabel zLbl = new JLabel("至");
		zLbl.setBounds(330, 10, 15, 20);
		dateETxt = new JTextField();
		dateETxt.setBounds(345, 10, 120, 20);

		dchkTxt = new JComboBox<String>();
		dchkTxt.setBounds(480, 10, 90, 20);
		dchkTxt.addItem("全部");
		dchkTxt.addItem("本日");
		dchkTxt.addItem("上个月");

		queryBtn = new JButton("查询");
		queryBtn.setBounds(580, 10, 60, 20);

		panel.add(dgName);
		panel.add(dgyTxt);
		panel.add(dateLbl);
		panel.add(dateSTxt);
		panel.add(zLbl);
		panel.add(dateETxt);
		panel.add(dchkTxt);
		panel.add(queryBtn);
		this.add(panel);
	}

	/**
	 * 销售统计列表面板
	 */
	private void tableListPane() {
		String[] head = { "小票流水号", "购物日期", "购物金额", "单笔利润", "导购员", "收银员" };
		JTable table;
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		model = new DefaultTableModel();
		table = new JTable(model);
		Vector<String> colName = new Vector<String>();
		colName.addAll(Arrays.asList(head));
		model.setDataVector(null, colName);
		JScrollPane listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 40, 780, 300);
		this.add(listPanel);
		numLbl = new JLabel();
		numLbl.setBounds(10, 50, 100, 20);
		crasherLbl = new JLabel();
		crasherLbl.setBounds(10, 80, 300, 20);
		getTableList(0, null, null);
	}

	/**
	 * 结算面板
	 */
	private void accountPanel() {
		JPanel statisticPanel = new JPanel();
		statisticPanel.setLayout(null);
		statisticPanel.setBounds(0, 350, 800, 140);
		JLabel allLbl = new JLabel("统计结果");
		allLbl.setBounds(70, 30, 100, 20);

		statisticPanel.add(allLbl);
		statisticPanel.add(numLbl);
		statisticPanel.add(crasherLbl);
		this.add(statisticPanel);
	}

	private void btnEvent() {

		dchkTxt.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String date = (String) e.getItem();
				if (!StringUtils.isNotEmpty(date)) {
					return;
				}
				if (date.contains("全部")) {
					dateSTxt.setText("");
					dateETxt.setText("");
				}

				else if (date.contains("本日")) {
					Date d = new Date();
					dateSTxt.setText(DateUtil.getDateStart(d));
					dateETxt.setText(DateUtil.getDateEnd(d));
				}

				else if (date.contains("上个月")) {
					Date d = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					c.add(Calendar.MONTH, -1);
					dateSTxt.setText(DateUtil.getMonthStart(c.getTime()));
					dateETxt.setText(DateUtil.getMonthEnd(c.getTime()));
				}

			}
		});

		// 点查询按钮
		queryBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String startDate = dateSTxt.getText();
				String endDate = dateETxt.getText();
				Object dgySelect = dgyTxt.getSelectedItem();
				int dgyID = 0;
				if (dgySelect != null && !((UserInfo) dgySelect).getSaleManName().contains("选择")) {
					UserInfo user = (UserInfo) dgySelect;
					dgyID = user.getSaleManId();
				}
				if (StringUtils.isNotEmpty(startDate)) {
					Date sd = DateUtil.getDate(startDate);
					if (sd == null) {
						JOptionPane.showMessageDialog(null, "请按2020-08-12的格式输入起始日期！");
						return;
					}
					startDate = DateUtil.getDateStart(sd);// 加上时分秒
				}
				if (StringUtils.isNotEmpty(endDate)) {
					Date ed = DateUtil.getDate(endDate);
					if (ed == null) {
						JOptionPane.showMessageDialog(null, "请按2020-08-12的格式输入结束日期！");
						return;
					}
					endDate = DateUtil.getDateEnd(ed);
				}

				FrameUtil.removeAllRows(model);
				getTableList(dgyID, startDate, endDate);
			}
		});
	}

	/**
	 * 加载表格统计数据
	 */
	private void getTableList(int dgyID, String startDate, String endDate) {
		List<Sales> saleList = salesDetailDao.getSalesStatistic(dgyID, startDate, endDate);
		int num = saleList.size();
		numLbl.setText("销售记录" + num + "条！");
		float saleMoney = 0f;
		float profit = 0f;
		for (Sales g : saleList) {
			Vector row = copyToVector(g);
			model.addRow(row);
			saleMoney = saleMoney + g.getAmount();
			profit = profit + g.getProfit();
		}
		saleMoney = RoundTool.roundFloat(saleMoney, 2, BigDecimal.ROUND_DOWN);
		profit = RoundTool.roundFloat(profit, 2, BigDecimal.ROUND_DOWN);
		;
		crasherLbl.setText("销售金额￥" + saleMoney + "元，利润￥" + profit + "元！");
	}

	private Vector copyToVector(Sales s) {
		Vector v = new Vector();
		v.add(s.getReceiptsCode());
		v.add(DateUtil.getDate(s.getSalesDate()));
		v.add(RoundTool.roundFloat(s.getAmount(), 2, BigDecimal.ROUND_DOWN));
		v.add(RoundTool.roundFloat(s.getProfit(), 2, BigDecimal.ROUND_DOWN));
		v.add(s.getSalesMan().getSaleManName());
		v.add(s.getCrasher().getSaleManName());
		return v;
	}
}
