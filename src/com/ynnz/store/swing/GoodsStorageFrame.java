package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.dao.IGoodsTypeDao;
import com.ynnz.store.dao.impl.GoodsDaoImpl;
import com.ynnz.store.dao.impl.GoodsTypeDaoImpl;
import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.GoodsType;

public class GoodsStorageFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JPanel goodsPanel;
	private JTextField codeTxt, goodsNameTxt, buyTxt, saleTxt, discountTxt, stoNumTxt;
	private JLabel kcTip;
	private JButton readBtn, storageBtn, cancelBtn;
	private JComboBox<GoodsType> firstTypeTxt;
	private JComboBox<GoodsType> secondTypeTxt = new JComboBox<GoodsType>();
	private GoodsStorageFrame storageFrame = this;

	private Goods editGoods;// 要编辑的货品
	private IGoodsTypeDao goodsTypeDao = new GoodsTypeDaoImpl();
	private IGoodsDao goodsDao = new GoodsDaoImpl();

	public GoodsStorageFrame() {
		super("商品入库");
		this.setLayout(null);
		this.setBounds(300, 150, 500, 330);
		init();
		btnEvent();
		this.setResizable(false);
		this.setVisible(true);
	}

	private void init() {
		JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.NORTH);// 外面套一层选项卡面板
		tabPanel.setBounds(0, 10, 500, 230);
		goodsPanel = new JPanel();
		goodsPanel.setLayout(null);
		goodsPanel.setBounds(0, 0, 500, 230);
		JLabel codeLbl = new JLabel("货号/条形码：");
		codeLbl.setBounds(50, 10, 100, 20);
		codeTxt = new JTextField();
		codeTxt.setBounds(150, 10, 120, 20);
		readBtn = new JButton("读取信息");
		readBtn.setBounds(280, 10, 100, 20);
		goodsPanel.add(codeLbl);
		goodsPanel.add(codeTxt);
		goodsPanel.add(readBtn);

		JLabel goodsNameLbl = new JLabel("商品名称：");
		goodsNameLbl.setBounds(50, 35, 80, 20);
		goodsNameTxt = new JTextField();
		goodsNameTxt.setBounds(150, 35, 220, 20);
		goodsPanel.add(goodsNameLbl);
		goodsPanel.add(goodsNameTxt);

		JLabel goodsTypeLbl = new JLabel("商品分类：");
		goodsTypeLbl.setBounds(50, 60, 80, 20);
		firstTypeTxt = new JComboBox<GoodsType>();
		firstTypeTxt.setBounds(150, 60, 100, 20);

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
		GoodsType second = new GoodsType("--请选择--");
		secondTypeTxt = new JComboBox<GoodsType>();
		secondTypeTxt.addItem(second);
		secondTypeTxt.setBounds(260, 60, 100, 20);
		goodsPanel.add(goodsTypeLbl);
		goodsPanel.add(firstTypeTxt);
		goodsPanel.add(secondTypeTxt);

		JLabel buyLbl = new JLabel("进货价格：");
		buyLbl.setBounds(50, 85, 80, 20);
		buyTxt = new JTextField();
		buyTxt.setBounds(150, 85, 220, 20);
		goodsPanel.add(buyLbl);
		goodsPanel.add(buyTxt);

		JLabel saleLbl = new JLabel("零售价格：");
		saleLbl.setBounds(50, 110, 80, 20);
		saleTxt = new JTextField();
		saleTxt.setBounds(150, 110, 220, 20);
		goodsPanel.add(saleLbl);
		goodsPanel.add(saleTxt);

		JLabel discountLbl = new JLabel("折扣：");
		discountLbl.setBounds(50, 135, 80, 20);
		discountTxt = new JTextField(1);
		discountTxt.setBounds(150, 135, 60, 20);
		JLabel tipLbl = new JLabel("0~1之间的小数，如0.8表示八折");
		tipLbl.setBounds(215, 135, 200, 20);
		goodsPanel.add(discountLbl);
		goodsPanel.add(discountTxt);
		goodsPanel.add(tipLbl);

		JLabel stoNumLbl = new JLabel("本次入库数量：");
		stoNumLbl.setBounds(20, 160, 110, 20);
		stoNumTxt = new JTextField();
		stoNumTxt.setBounds(150, 160, 80, 20);
		goodsPanel.add(stoNumLbl);
		goodsPanel.add(stoNumTxt);
		kcTip = new JLabel("当前库存数量：" + 0);
		kcTip.setBounds(235, 160, 130, 20);
		kcTip.setVisible(false);
		goodsPanel.add(kcTip);

		JPanel btnPanel = new JPanel();
		btnPanel.setBounds(0, 240, 500, 50);
		storageBtn = new JButton("入库");
		cancelBtn = new JButton("取消");
		btnPanel.add(storageBtn);
		btnPanel.add(cancelBtn);

		tabPanel.add("入库信息", goodsPanel);// 需要加选项卡的话，往pan里再加面板
		this.add(tabPanel);
		this.add(btnPanel);
	}

	/**
	 * 按钮绑定触发事件
	 */
	private void btnEvent() {

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

		// 读取信息按钮
		readBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String barCode = codeTxt.getText();
				String code = codeTxt.getText();
				Goods goods;
				if (code == null || code.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请输入货品条码！");
					return;
				} else {
					goods = goodsDao.getGoodsByBarCode(barCode);
				}
				if (goods != null) {
					editGoods = goods;
					goodsNameTxt.setText(goods.getGoodsName());
					buyTxt.setText(goods.getStorePrice() + "");
					saleTxt.setText(goods.getSalePrice() + "");
					discountTxt.setText(goods.getDiscount() + "");
					stoNumTxt.setText(0 + "");
					GoodsType s = goods.getGoodsType();
					GoodsType f = goods.getGoodsType().getParentType();
					int fTypeNum = firstTypeTxt.getItemCount();
					for (int i = 0; i < fTypeNum; i++) {
						int id = firstTypeTxt.getItemAt(i).getTypeId();
						if (f != null && f.getTypeId() == id) {
							firstTypeTxt.setSelectedIndex(i);
							break;
						}
					}

					int sTypeNum = secondTypeTxt.getItemCount();
					for (int i = 0; i < sTypeNum; i++) {
						int id = secondTypeTxt.getItemAt(i).getTypeId();
						if (f != null && s.getTypeId() == id) {
							secondTypeTxt.setSelectedIndex(i);
							break;
						}
					}
					kcTip.setText("当前库存数量：" + goods.getStockNum());
					kcTip.setVisible(true);
				}
			}
		});

		// 入库按钮
		storageBtn.addActionListener(new ActionListener() {
			private Pattern patter;
			private Matcher matcher;

			@Override
			public void actionPerformed(ActionEvent e) {
				Goods goods = new Goods();
				String code = codeTxt.getText();
				if (code == null || code.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请输入货品条码！");
					return;
				} else if (code.trim().length() > 15) {
					JOptionPane.showMessageDialog(null, "货品条码的长度不能超过15位！");
					return;
				} else {
					goods.setBarCode(code);
				}
				Goods go = goodsDao.getGoodsByBarCode(code.trim());
				if (go != null && editGoods == null) {// 不是点“读取信息”调出的货品信息
					JOptionPane.showMessageDialog(null, "货品条码重复，请重新输入或直接调出货品信息！");
					return;
				}
				String name = goodsNameTxt.getText();
				if (name == null || name.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "请输入货品名称！");
					return;
				} else if (name.trim().length() > 25) {
					JOptionPane.showMessageDialog(null, "货品名称字数不能超过10个汉字！");
					return;
				} else {
					goods.setGoodsName(name);
				}

				GoodsType sType = (GoodsType) secondTypeTxt.getSelectedItem();
				if (sType != null && sType.getParentType() != null
						&& sType.getParentType().getTypeName().toString().contains("选择")) {
					JOptionPane.showMessageDialog(null, "请选择分类！");
					return;
				} else {
					goods.setGoodsType(sType);
				}

				String buy = buyTxt.getText();
				String salaryReg = "[1-9]\\d*|[1-9]\\d*\\.[0-9]{1,2}";
				patter = Pattern.compile(salaryReg);
				matcher = patter.matcher(buy);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写进货价格，且只能是整数或包含1-2位小数的数字！");
					return;
				} else {
					goods.setStorePrice(Float.parseFloat(buy));
				}

				String sale = saleTxt.getText();
				patter = Pattern.compile(salaryReg);
				matcher = patter.matcher(sale);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写零售价格，且只能是整数或包含1-2位小数的数字！");
					return;
				} else {
					goods.setSalePrice(Float.parseFloat(sale));
				}

				String discount = discountTxt.getText();
				String discountReg = "(0\\.([1-9]\\d?|[0-9][1-9])|1|1.0)";
				patter = Pattern.compile(discountReg);
				matcher = patter.matcher(discount);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写折扣，且只能是0到1(包括1)之间1位小数的数字！");
					return;
				} else {
					goods.setDiscount(Float.parseFloat(discount));
				}

				String stoNum = stoNumTxt.getText();
				String stoNumReg = "[1-9]\\d*";
				patter = Pattern.compile(stoNumReg);
				matcher = patter.matcher(stoNum);
				if (!matcher.matches()) {
					JOptionPane.showMessageDialog(null, "请填写本次入库数量！");
					return;
				} else {
					goods.setStockNum(Integer.parseInt(stoNum));
				}
				goods.setStockDate(new Date());

				boolean ret = false;
				String msg = "";
				if (editGoods != null) {// 编辑货品信息时候需要设置id
					int num = Integer.parseInt(stoNum) + editGoods.getStockNum();
					goods.setStockNum(num);
					ret = goodsDao.updateStoreNum(editGoods.getGoodsId(), num);
					msg = "编辑";
				} else {// 新增货品
					ret = goodsDao.addGoods(goods);
					msg = "新增";
				}
				if (ret) {
					JOptionPane.showMessageDialog(null, msg + "入库数量成功！");
					kcTip.setText("当前库存数量：" + goods.getStockNum());
				} else {
					JOptionPane.showMessageDialog(null, msg + "货品入库失败！");
				}
			}
		});

		// 取消按钮
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				storageFrame.dispose();
			}
		});
	}

}
