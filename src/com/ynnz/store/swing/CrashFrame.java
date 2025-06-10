package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
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
import javax.swing.table.DefaultTableModel;

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.dao.ISalesDetailDao;
import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.GoodsDaoImpl;
import com.ynnz.store.dao.impl.SalesDetailDaoImpl;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.Sales;
import com.ynnz.store.pojo.SalesDetail;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.Constants;
import com.ynnz.store.util.DataMapUtil;
import com.ynnz.store.util.DateUtil;
import com.ynnz.store.util.RoundTool;

public class CrashFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JButton addBtn, reduceBtn, crashBtn;// 结算按钮
	private JLabel allLbl, numLbl, xpVal;
	private JTextField ysTxt, ssTxt, zlTxt;
	private IUserinfoDao userinfoDao = new UserinfoDaoImpl();
	private ISalesDetailDao salesDetailDao = new SalesDetailDaoImpl();
	private IGoodsDao goodsDao = new GoodsDaoImpl();
	private JComboBox<UserInfo> dgyTxt;
	private JTextField codeTxt;
	private JScrollPane listPanel;
	private DefaultTableModel model;
	private CrashFrame crashFrm = this;
	private String[] head = { "货号/条形码", "商品名称", "商品类别", "零售价", "折扣", "折后价", "数量", "商品ID", "库存数量" };

	public CrashFrame() {
		super("收银台");
		crashBtn = new JButton("结算");
		init();
		queryPanel();
		tableListPane();
		accountPanel();
		crashBtnEvent();
	}

	/**
	 * 窗体初始化
	 */
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
		panel.setBounds(0, 0, 800, 30);
		JLabel codeLbl = new JLabel("货号/条形码：");
		codeLbl.setBounds(10, 10, 100, 20);
		codeTxt = new JTextField();
		codeTxt.setBounds(105, 10, 120, 20);
		JLabel dgName = new JLabel("导购员：");
		dgName.setBounds(260, 10, 60, 20);
		dgyTxt = new JComboBox<UserInfo>();
		dgyTxt.setBounds(320, 10, 80, 20);
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
		addBtn = new JButton("增加");
		addBtn.setBounds(405, 10, 60, 20);
		reduceBtn = new JButton("减少");
		reduceBtn.setBounds(470, 10, 60, 20);
		JLabel xpLbl = new JLabel("小票流水号：");
		xpLbl.setBounds(540, 10, 90, 20);
		String ls = DateUtil.getNowTimeStamp();
		xpVal = new JLabel(DateUtil.getNowTimeStamp());
		xpVal.setBounds(630, 10, 130, 20);
		panel.add(codeLbl);
		panel.add(codeTxt);
		panel.add(dgName);
		panel.add(dgyTxt);
		panel.add(addBtn);
		panel.add(reduceBtn);
		panel.add(xpLbl);
		panel.add(xpVal);
		this.add(panel);
	}

	/**
	 * 商品列表面板
	 */
	private void tableListPane() {
		Object data[][] = new Object[0][head.length];
		JTable table = new JTable(data, head);

		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

		model = new DefaultTableModel();
		table = new JTable(model);

		listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 40, 780, 300);
		Vector<String> colName = new Vector<String>();
		colName.addAll(Arrays.asList(head));
		model.setDataVector(null, colName);

		this.add(listPanel);
	}

	/**
	 * 结算面板
	 */
	private void accountPanel() {
		JPanel accountPnl = new JPanel();
		accountPnl.setLayout(null);
		accountPnl.setBounds(0, 350, 800, 140);
		float allMoney = 0f;
		allLbl = new JLabel("共：￥" + allMoney + "元");
		allLbl.setBounds(10, 30, 100, 20);
		int num = 0;
		numLbl = new JLabel("商品数量：" + num);
		numLbl.setBounds(10, 50, 100, 20);
		UserInfo user = DataMapUtil.LOGIN_INFO.get(Constants.LOGIN_USER);
		JLabel crasherLbl = new JLabel("收银员：" + user.getSaleManName());
		crasherLbl.setBounds(270, 80, 100, 20);
		JLabel ysLbl = new JLabel("应收：");
		ysLbl.setBounds(420, 30, 60, 20);
		ysTxt = new JTextField();
		ysTxt.setEnabled(false);
		ysTxt.setBounds(460, 30, 120, 20);
		JLabel ssLbl = new JLabel("实收：");
		ssLbl.setBounds(420, 55, 60, 20);
		ssTxt = new JTextField();
		ssTxt.setBounds(460, 55, 120, 20);
		JLabel zlLbl = new JLabel("找零：");
		zlLbl.setBounds(420, 80, 60, 20);
		zlTxt = new JTextField();
		zlTxt.setEnabled(false);
		zlTxt.setBounds(460, 80, 120, 20);

		crashBtn.setBounds(600, 55, 60, 20);

		accountPnl.add(allLbl);
		accountPnl.add(numLbl);
		accountPnl.add(crasherLbl);
		accountPnl.add(ysLbl);
		accountPnl.add(ysTxt);
		accountPnl.add(ssLbl);
		accountPnl.add(ssTxt);
		accountPnl.add(zlLbl);
		accountPnl.add(zlTxt);
		accountPnl.add(crashBtn);
		this.add(accountPnl);
	}

	/**
	 * 结算按钮触发事件
	 */
	private void crashBtnEvent() {
		crashBtn.addActionListener(new ActionListener() {
			private Pattern patter;
			private Matcher matcher;

			@Override
			public void actionPerformed(ActionEvent e) {
				int num = model.getRowCount();
				if (num == 0) {
					JOptionPane.showMessageDialog(null, "请先添加商品！");
					return;
				}
				Object dgySelect = dgyTxt.getSelectedItem();
				UserInfo user = null;
				if (dgySelect == null || dgySelect.toString().contains("选择")) {
					JOptionPane.showMessageDialog(null, "请选择导购员！");
					return;
				} else {
					user = (UserInfo) dgySelect;
				}
				String buy = ssTxt.getText();
				String salaryReg = "[1-9]\\d*|[1-9]\\d*\\.[0-9]{1,2}";
				patter = Pattern.compile(salaryReg);
				matcher = patter.matcher(buy);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请正确填写实收价格，只能是整数或包含1-2位小数的数字！");
					return;
				}
				float ysMoney = Float.parseFloat(ysTxt.getText());
				float ssMoney = Float.parseFloat(ssTxt.getText());
				if (ssMoney < ysMoney) {
					JOptionPane.showMessageDialog(null, "实收金额不足，请补足！");
					return;
				}
				zlTxt.setText(RoundTool.roundFloat((ssMoney - ysMoney), 2, BigDecimal.ROUND_DOWN) + "");

				int sel = JOptionPane.showConfirmDialog(null, "已计算找零，确认结算吗？");
				if (sel == 0) {
					int rownum = model.getRowCount();
					float saleNum = 0f;
					SalesDetail salesDetail = null;
					List<SalesDetail> sDetails = new ArrayList<SalesDetail>();
					for (int i = 0; i < rownum; i++) {
						salesDetail = new SalesDetail();
						float sale = (float) model.getValueAt(i, 3);
						float discount = (float) model.getValueAt(i, 4);
						int goodsNum = (int) model.getValueAt(i, 6);
						float saleP = RoundTool.roundFloat((sale * discount * goodsNum), 2, BigDecimal.ROUND_DOWN);
						saleNum = RoundTool.roundFloat((saleNum + saleP), 2, BigDecimal.ROUND_DOWN);
						Goods g = new Goods();
						g.setGoodsId((int) model.getValueAt(i, 7));
						salesDetail.setGoods(g);
						salesDetail.setSaleNum(goodsNum);
						salesDetail.setAloneAmount(saleP);
						sDetails.add(salesDetail);
					}
					Sales sales = new Sales();
					sales.setReceiptsCode(xpVal.getText());
					sales.setAmount(saleNum);
					sales.setSalesMan(user);
					sales.setCrasher(DataMapUtil.LOGIN_INFO.get(Constants.LOGIN_USER));
					int retNum = salesDetailDao.addSalesDetails(sales, sDetails);
					updateGoodsNum();// 更新库存
					JOptionPane.showMessageDialog(null, "结算成功！");
					crashFrm.dispose();
				}

			}

			// 更新库存
			private void updateGoodsNum() {
				int rownum = model.getRowCount();
				for (int i = 0; i < rownum; i++) {
					int goodsNum = (int) model.getValueAt(i, 6);
					int goodsId = (int) model.getValueAt(i, 7);
					int stockNum = (int) model.getValueAt(i, 8);
					goodsDao.updateStoreNum(goodsId, (stockNum - goodsNum));
				}

			}
		});

		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String barCode = codeTxt.getText();
				if (barCode == null || barCode.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入商品条码！");
					return;
				}
				Goods goods = goodsDao.getGoodsByBarCode(barCode);
				if (goods == null) {
					JOptionPane.showMessageDialog(null, "条码错误，请重新输入！");
					return;
				}
				int rownum = model.getRowCount();
				boolean hasGoods = false;// 是否已经选了某个商品
				float saleNum = 0f;
				for (int i = 0; i < rownum; i++) {
					String code = (String) model.getValueAt(i, 0);
					if (barCode.equals(code)) {
						int goodsNum = (int) model.getValueAt(i, 6);
						model.setValueAt((goodsNum + 1), i, 6);// 修改数量值
						hasGoods = true;
					}
					float sale = (float) model.getValueAt(i, 3);
					float discount = (float) model.getValueAt(i, 4);
					int num = (int) model.getValueAt(i, 6);
					saleNum = RoundTool.roundFloat((saleNum + sale * discount * num), 2, BigDecimal.ROUND_DOWN);
				}
				if (hasGoods == false) {
					Vector row = copyToVector(goods);
					model.addRow(row);
					float sale = goods.getSalePrice();
					float discount = goods.getDiscount();
					saleNum = RoundTool.roundFloat((saleNum + sale * discount), 2, BigDecimal.ROUND_DOWN);
					rownum = rownum + 1;
				}
				allLbl.setText("共：￥" + saleNum + "元");
				numLbl.setText("商品数量：" + rownum);
				ysTxt.setText("" + saleNum);
			}
		});

		reduceBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String barCode = codeTxt.getText();
				if (barCode == null || barCode.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入商品条码！");
					return;
				}
				Goods goods = goodsDao.getGoodsByBarCode(barCode);
				if (goods == null) {
					JOptionPane.showMessageDialog(null, "条码错误，请重新输入！");
					return;
				}
				boolean removeGoods = false;// 是否已经选了某个商品
				int rownum = model.getRowCount();
				int removeRow = -1;
				float saleNum = 0f;
				for (int i = 0; i < rownum; i++) {
					removeRow = -1;
					String code = (String) model.getValueAt(i, 0);
					if (barCode.equals(code)) {
						int goodsNum = (int) model.getValueAt(i, 6);
						goodsNum = goodsNum - 1;
						if (goodsNum >= 1) {
							model.setValueAt(goodsNum, i, 6);// 修改数量值
						} else {
							removeGoods = true;
							removeRow = i;
						}
					}
					if (removeRow == -1) {
						float sale = (float) model.getValueAt(i, 3);
						float discount = (float) model.getValueAt(i, 4);
						int gnum = (int) model.getValueAt(i, 6);
						saleNum = RoundTool.roundFloat((saleNum + sale * discount * gnum), 2, BigDecimal.ROUND_DOWN);
					}
				}
				allLbl.setText("共：￥" + saleNum + "元");
				numLbl.setText("商品数量：" + rownum);
				ysTxt.setText("" + saleNum);
				if (removeGoods == true) {
					model.removeRow(removeRow);
					JOptionPane.showMessageDialog(null, "商品已经移除！");
					return;
				}

			}
		});
	}

	private Vector copyToVector(Goods sd) {
		Vector v = new Vector();
		v.add(sd.getBarCode());
		v.add(sd.getGoodsName());
		v.add(sd.getGoodsType().getTypeName());
		v.add(sd.getSalePrice());
		v.add(sd.getDiscount());
		v.add(sd.getSalePrice() * sd.getDiscount());
		v.add(1);// 数量应该能动态变化
		v.add(sd.getGoodsId());// 隐藏列
		v.add(sd.getStockNum());// 隐藏列
		return v;
	}

	public void closePa() {
		this.remove(listPanel);
	}

}
