package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.DateUtil;
import com.ynnz.store.util.FrameUtil;
import com.ynnz.store.util.RoundTool;
import com.ynnz.store.util.StringUtils;

/**
 * 工资核算统计页面
 *
 * @author 吕琼华
 *
 */
public class PayStatisticFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> dchkTxt;
	private JTextField dateSTxt, dateETxt;
	private JButton hsBtn;
	private IUserinfoDao userinfoDao = new UserinfoDaoImpl();
	private DefaultTableModel model;

	public PayStatisticFrame() {
		super("工资核算");
		init();
		queryPanel();
		tableListPane();
		btnEvent();
		this.setVisible(true);
	}

	public void init() {
		this.setLayout(null);
		this.setBounds(300, 100, 700, 450);
		this.setResizable(false);
	}

	/**
	 * 查询条件面板构造
	 */
	private void queryPanel() {
		JPanel queryPanel = new JPanel();
		queryPanel.setLayout(null);
		queryPanel.setBounds(0, 0, 700, 40);

		JLabel dateLbl = new JLabel("日期：");
		dateLbl.setBounds(140, 10, 60, 20);
		dateSTxt = new JTextField();
		dateSTxt.setBounds(200, 10, 120, 20);
		JLabel zLbl = new JLabel("至");
		zLbl.setBounds(320, 10, 15, 20);
		dateETxt = new JTextField();
		dateETxt.setBounds(335, 10, 120, 20);

		dchkTxt = new JComboBox<String>();
		dchkTxt.setBounds(480, 10, 90, 20);
		dchkTxt.addItem("当前月");
		dchkTxt.addItem("上个月");

		hsBtn = new JButton("核算");
		hsBtn.setBounds(580, 10, 60, 20);

		this.add(dateLbl);
		this.add(dateSTxt);
		this.add(zLbl);
		this.add(dchkTxt);
		this.add(dateETxt);
		this.add(hsBtn);
	}

	/**
	 * 工资核算列表面板
	 */
	private void tableListPane() {
		String[] head = { "员工姓名", "员工类型", "联系方式", "基本工资", "提成比率", "月销售额", "应发工资" };
		JTable table;
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		model = new DefaultTableModel(); // 新建bai一个默认数据模型du
		table = new JTable(model); // 用数据模型创建JTable，JTable会自动监听到数zhi据模型中的数据改变并显示出来
		Vector<String> colName = new Vector<String>();
		colName.addAll(Arrays.asList(head));
		model.setDataVector(null, colName);
		JScrollPane listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 45, 660, 360);
		this.add(listPanel);

		// 默认先加载当前月数据
		Date d = new Date();
		dateSTxt.setText(DateUtil.getMonthStart(d));
		dateETxt.setText(DateUtil.getMonthEnd(d));
		String startDate = DateUtil.getDateStart(d);
		String dateMonth = DateUtil.getYMOfDate(startDate);
		getTableList(dateMonth);
	}

	/**
	 * 按钮绑定触发事件
	 */
	private void btnEvent() {

		// 下拉列表事件
		dchkTxt.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String date = (String) e.getItem();
				if (!StringUtils.isNotEmpty(date)) {
					return;
				}
				Date d = new Date();
				Calendar c = Calendar.getInstance();
				if (date.contains("当前月")) {
					c.setTime(d);
				} else if (date.contains("上个月")) {
					c.setTime(d);
					c.add(Calendar.MONTH, -1);
				}
				dateSTxt.setText(DateUtil.getMonthStart(c.getTime()));
				dateETxt.setText(DateUtil.getMonthEnd(c.getTime()));

			}
		});

		// 核算按钮
		hsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String startDate = dateSTxt.getText();
				String endDate = dateETxt.getText();
				String sm = null;
				String em = null;
				if (StringUtils.isNotEmpty(startDate)) {
					Date sd = DateUtil.getDate(startDate);
					if (sd == null) {
						JOptionPane.showMessageDialog(null, "请按2020-08-12的格式输入起始日期！");
						return;
					}
					sm = DateUtil.getYMOfDate(startDate);
					startDate = DateUtil.getDateStart(sd);// 加上时分秒
				}
				if (StringUtils.isNotEmpty(endDate)) {
					Date ed = DateUtil.getDate(endDate);
					if (ed == null) {
						JOptionPane.showMessageDialog(null, "请按2020-08-12的格式输入结束日期！");
						return;
					}
					em = DateUtil.getYMOfDate(endDate);
					endDate = DateUtil.getDateEnd(ed);
				}
				if (sm == null || em == null || !sm.equals(em)) {
					JOptionPane.showMessageDialog(null, "填写的开始日期和结束日期不在同一个月，请重新填写！");
					return;
				}
				String dateMonth = DateUtil.getYMOfDate(startDate);
				FrameUtil.removeAllRows(model);
				getTableList(dateMonth);
			}
		});

	}

	/**
	 * 加载表格薪资统计数据
	 */
	private void getTableList(String monthDate) {
		List<UserInfo> userList = userinfoDao.getUserSalaryStatistic(monthDate);
		for (UserInfo g : userList) {
			Vector row = copyToVector(g);
			model.addRow(row);
		}
	}

	private Vector copyToVector(UserInfo s) {
		Vector v = new Vector();
		v.add(s.getSaleManName());
		v.add(s.getRole());
		v.add(s.getMobile());
		float base = RoundTool.roundFloat(s.getBasesalary(), 2, BigDecimal.ROUND_DOWN);
		float comi = RoundTool.roundBigDecimal(s.getCommissionRate(), 2, BigDecimal.ROUND_DOWN);
		v.add(base);
		v.add(comi);
		float sale = s.getSaleMoney();
		if (sale != 0) {
			sale = RoundTool.roundFloat(sale, 2, BigDecimal.ROUND_DOWN);
		}
		v.add(sale);
		float salary = 0f;
		if (sale == 0) {
			salary = base;
		} else {
			salary = RoundTool.roundFloat((base + sale * comi), 2, BigDecimal.ROUND_DOWN);
		}
		v.add(salary);
		return v;
	}
}
