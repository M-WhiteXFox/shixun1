package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.dao.IGoodsTypeDao;
import com.ynnz.store.dao.impl.GoodsDaoImpl;
import com.ynnz.store.dao.impl.GoodsTypeDaoImpl;
import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.GoodsType;
import com.ynnz.store.util.DateUtil;
import com.ynnz.store.util.StringUtils;

public class GoodsViewFrame extends ParentFrame {

	private JTextField codeTxt, goodsNameTxt, dateSTxt, dateETxt;
	private JButton queryBtn;
	private JLabel tipLbl;
	private JComboBox<String> dchkTxt;
	private JComboBox<GoodsType> firstTypeTxt, secondTypeTxt;

	private IGoodsTypeDao goodsTypeDao = new GoodsTypeDaoImpl();
	private IGoodsDao goodsDao = new GoodsDaoImpl();

	private DefaultTableModel model;

	private static final long serialVersionUID = 1L;

	public GoodsViewFrame() {
		super("商品浏览");
		init();
		queryPanel();
		tableListPane();
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
		panel.setBounds(0, 0, 800, 70);

		JLabel codeLbl = new JLabel("货号/条形码：");
		codeLbl.setBounds(10, 10, 90, 20);
		codeTxt = new JTextField();
		codeTxt.setBounds(100, 10, 100, 20);
		panel.add(codeLbl);
		panel.add(codeTxt);

		JLabel goodsNameLbl = new JLabel("商品名称：");
		goodsNameLbl.setBounds(205, 10, 80, 20);
		goodsNameTxt = new JTextField();
		goodsNameTxt.setBounds(285, 10, 100, 20);
		panel.add(goodsNameLbl);
		panel.add(goodsNameTxt);

		JLabel dateLbl = new JLabel("入库时间：");
		dateLbl.setBounds(390, 10, 80, 20);
		dateSTxt = new JTextField();
		dateSTxt.setBounds(470, 10, 100, 20);
		JLabel zLbl = new JLabel("至");
		zLbl.setBounds(570, 10, 15, 20);
		dateETxt = new JTextField();
		dateETxt.setBounds(585, 10, 100, 20);
		panel.add(dateLbl);
		panel.add(dateSTxt);
		panel.add(zLbl);
		panel.add(dateETxt);

		dchkTxt = new JComboBox<String>();
		dchkTxt.setBounds(685, 10, 80, 20);
		dchkTxt.addItem("全部");
		dchkTxt.addItem("本日");
		dchkTxt.addItem("上个月");
		panel.add(dchkTxt);

		JLabel firstTypeLbl = new JLabel("一级分类：");
		firstTypeLbl.setBounds(10, 35, 80, 20);
		firstTypeTxt = new JComboBox<GoodsType>();
		firstTypeTxt.setBounds(95, 35, 100, 20);
		List<GoodsType> types = goodsTypeDao.getGoodsTypeList(0);
		// 从数据库查询
		GoodsType u = new GoodsType("--请选择--");
		firstTypeTxt.addItem(u);
		if (types != null && types.size() > 0) {
			for (Iterator<GoodsType> ite = types.iterator(); ite.hasNext();) {
				GoodsType type = (GoodsType) ite.next();
				firstTypeTxt.addItem(type);
			}
		}
		panel.add(firstTypeLbl);
		panel.add(firstTypeTxt);

		JLabel secondTypeLbl = new JLabel("二级分类：");
		secondTypeLbl.setBounds(195, 35, 80, 20);
		GoodsType second = new GoodsType("--请选择--");
		secondTypeTxt = new JComboBox<GoodsType>();
		secondTypeTxt.addItem(second);
		secondTypeTxt.setBounds(280, 35, 100, 20);
		panel.add(secondTypeLbl);
		panel.add(secondTypeTxt);

		int num = 0;
		tipLbl = new JLabel("当前共" + num + "条商品信息！");
		tipLbl.setBounds(385, 35, 170, 20);
		panel.add(tipLbl);

		queryBtn = new JButton("查询");
		queryBtn.setBounds(575, 35, 60, 20);
		panel.add(queryBtn);

		this.add(panel);
	}

	/**
	 * 销售统计列表面板
	 */
	private void tableListPane() {
		String[] head = { "ID", "货号/条形码", "商品名称", "商品类别", "进货价", "零售价", "折扣", "当前库存", "入库时间" };
		JTable table;
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		model = new DefaultTableModel();
		table = new JTable(model);
		Vector<String> colName = new Vector<String>();
		colName.addAll(Arrays.asList(head));
		model.setDataVector(null, colName);
		JScrollPane listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 75, 780, 320);
		this.add(listPanel);
		getTableListData(null, null, null, null, null);
	}

	/**
	 * 清空表里的数据
	 *
	 * @param modelo
	 */
	public static void removeAllRows(DefaultTableModel modelo) {
		int a = modelo.getRowCount();
		for (int i = 0; i < a; i++) {
			modelo.removeRow(0);
		}
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

		// 级联菜单事件
		firstTypeTxt.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				GoodsType type = (GoodsType) e.getItem();
				if (type == null || type.getTypeId() == 0) {
					secondTypeTxt.removeAllItems();
					return;
				}
				List<GoodsType> types = goodsTypeDao.getGoodsTypeList(type.getTypeId());
				secondTypeTxt.removeAllItems();
				// 从数据库查询
				GoodsType u = new GoodsType("--请选择--");
				secondTypeTxt.addItem(u);
				if (types != null && types.size() > 0) {
					for (Iterator<GoodsType> ite = types.iterator(); ite.hasNext();) {
						GoodsType t = (GoodsType) ite.next();
						t.setParentType(type);
						secondTypeTxt.addItem(t);
					}
				}

			}
		});

		// 点查询按钮
		queryBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String code = codeTxt.getText();
				String name = goodsNameTxt.getText();
				String startDate = dateSTxt.getText();
				String endDate = dateETxt.getText();
				GoodsType sType = (GoodsType) secondTypeTxt.getSelectedItem();
				if (StringUtils.isNotEmpty(startDate)) {
					Date sd = DateUtil.getDate(startDate);
					if (sd == null) {
						JOptionPane.showMessageDialog(null, "请按2020-08-12的格式输入入库起始日期！");
						return;
					}
					startDate = DateUtil.getDateStart(sd);// 加上时分秒
				}
				if (StringUtils.isNotEmpty(endDate)) {
					Date ed = DateUtil.getDate(endDate);
					if (ed == null) {
						JOptionPane.showMessageDialog(null, "请按2020-08-12的格式输入入库结束日期！");
						return;
					}
					endDate = DateUtil.getDateEnd(ed);
				}

				removeAllRows(model);
				getTableListData(code, name, startDate, endDate, sType);
			}
		});
	}

	/**
	 * 构造列表数据
	 *
	 * @param barcode
	 * @param goodsName
	 * @param startDate
	 * @param endDate
	 * @param goodsType
	 */
	private void getTableListData(String barcode, String goodsName, String startDate, String endDate,
			GoodsType goodsType) {
		List<Goods> goodsList = goodsDao.getGoodsListView(barcode, goodsName, startDate, endDate, goodsType);
		int num = goodsList.size();
		tipLbl.setText("当前共" + num + "条商品信息！");
		for (Goods g : goodsList) {
			Vector row = copyToVector(g);
			model.addRow(row);
		}
	}

	private Vector copyToVector(Goods sd) {
		Vector v = new Vector();
		v.add(sd.getGoodsId());// 隐藏列
		v.add(sd.getBarCode());
		v.add(sd.getGoodsName());
		v.add(sd.getGoodsType().getTypeName());
		v.add(sd.getStorePrice());
		v.add(sd.getSalePrice());
		v.add(sd.getDiscount());
		v.add(sd.getStockNum());// 隐藏列
		v.add(DateUtil.getDateTime(sd.getStockDate()));
		return v;
	}

}
