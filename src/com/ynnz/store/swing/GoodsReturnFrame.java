package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.dao.ISalesDetailDao;
import com.ynnz.store.dao.impl.GoodsDaoImpl;
import com.ynnz.store.dao.impl.SalesDetailDaoImpl;
import com.ynnz.store.pojo.SalesDetail;
import com.ynnz.store.util.DateUtil;
import com.ynnz.store.util.RoundTool;
import com.ynnz.store.util.StringUtils;

/**
 * 退货窗口页面
 *
 * @author 吕琼华
 *
 */
public class GoodsReturnFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JLabel dealLbl, refundLbl;
	private JTextField numberTxt;
	private JTable table;
	private JButton queryBtn, returnBtn, cancelBtn;
	private GoodsReturnFrame returnFrame = this;
	private DefaultTableModel model;
	private ISalesDetailDao salesDetailDao = new SalesDetailDaoImpl();
	private IGoodsDao goodsDao = new GoodsDaoImpl();

	public GoodsReturnFrame() {
		super("商品退货");
		init();
		queryPanel();
		tableListPane();
		btnEvent();
	}

	public void init() {
		this.setLayout(null);
		this.setBounds(300, 80, 700, 450);
		this.setResizable(false);
	}

	/**
	 * 查询条件面板构造
	 */
	private void queryPanel() {
		JPanel queryPanel = new JPanel();
		queryPanel.setLayout(null);
		queryPanel.setBounds(0, 0, 700, 40);

		JLabel numberLbl = new JLabel("购物小票流水号：");
		numberLbl.setBounds(120, 10, 120, 20);
		numberTxt = new JTextField();
		numberTxt.setBounds(240, 10, 120, 20);
		queryPanel.add(numberLbl);
		queryPanel.add(numberTxt);
		queryBtn = new JButton("查询");
		queryBtn.setBounds(365, 10, 60, 20);
		queryPanel.add(queryBtn);

		float dealMoney = 0f;
		dealLbl = new JLabel("交易金额：￥" + dealMoney);
		dealLbl.setBounds(20, 375, 140, 20);
		float refundMoney = 0f;
		refundLbl = new JLabel("退款金额：￥" + refundMoney);
		refundLbl.setBounds(170, 375, 140, 20);
		returnBtn = new JButton("退货");
		returnBtn.setBounds(320, 375, 60, 20);
		cancelBtn = new JButton("取消");
		cancelBtn.setBounds(390, 375, 60, 20);
		this.add(dealLbl);
		this.add(refundLbl);
		this.add(queryPanel);
		this.add(returnBtn);
		this.add(cancelBtn);
	}

	/**
	 * 销售统计列表面板
	 */
	private void tableListPane() {
		String[] head = { "流水单ID", "商品ID", "货号/条形码", "商品名称", "实收价格", "数量", "导购员", "购买日期", "流水单总额" };
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		model = new DefaultTableModel(); // 新建bai一个默认数据模型du
		Vector<String> colName = new Vector<String>();
		colName.addAll(Arrays.asList(head));
		model.setDataVector(null, colName);
		table = new JTable(model); // 用数据模型创建JTable，JTable会自动监听到数zhi据模型中的数据改变并显示出来
		JScrollPane listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 45, 660, 320);
		this.add(listPanel);
		tableSelectEvent();// 绑定表格里选一行的事件
	}

	private Vector copyToVector(SalesDetail sd) {
		Vector v = new Vector();
		v.add(sd.getSales().getSalesId());// 隐藏列
		v.add(sd.getGoods().getGoodsId());// 隐藏列
		v.add(sd.getGoods().getBarCode());
		v.add(sd.getGoods().getGoodsName());
		v.add(sd.getAloneAmount());
		v.add(sd.getSaleNum());
		v.add(sd.getSales().getSalesMan().getSaleManName());
		v.add(DateUtil.getDateTime(sd.getSales().getSalesDate()));
		v.add(sd.getSales().getAmount());// 隐藏列
		return v;
	}

	/**
	 * 给表单选择行绑定事件
	 */
	private void tableSelectEvent() {
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean b = e.getValueIsAdjusting();
				if (b) {// true表示鼠标按下，false表示鼠标放开
					return;
				}
				int rowNo = table.getSelectedRow();// 获取选中的行号
				if (rowNo < 0) {// 因为移除或添加表单行数据时会触发该事件，所以添加判断避免报错
					return;
				}
				float aloneAmount = (float) model.getValueAt(rowNo, 4);
				refundLbl.setText("退款金额：￥" + RoundTool.roundFloat(aloneAmount, 2, BigDecimal.ROUND_DOWN));
			}
		});
	}

	/**
	 * 按钮绑定触发事件
	 */
	private void btnEvent() {

		// 查询按钮
		queryBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String rcode = numberTxt.getText();
				rcode = rcode.trim();
				model.setRowCount(0); // 清空表格
				List<SalesDetail> sds = salesDetailDao.getSaleGoodsByCode(rcode);
				float saleAmount = 0f;
				for (SalesDetail g : sds) {
					saleAmount = g.getSales().getAmount();
					Vector row = copyToVector(g);
					model.addRow(row);
				}
				dealLbl.setText("交易金额：￥" + saleAmount);
			}
		});

		// 退货按钮
		returnBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNo = table.getSelectedRow();// 获取选中的行号
				if (rowNo < 0) {
					JOptionPane.showMessageDialog(null, "请选择要退货的商品！");
					return;
				}
				int saleId = (int) model.getValueAt(rowNo, 0);
				int goodsId = (int) model.getValueAt(rowNo, 1);
				boolean delRet = salesDetailDao.deleteSaleDetail(saleId, goodsId);
				float returnNum = Float.parseFloat(model.getValueAt(rowNo, 5).toString());
				if (delRet) {// 退货成功更新库存
					goodsDao.returnStoreNum(goodsId, (int) returnNum);// 更新库存
					model.removeRow(rowNo);
				}
				int tableRows = model.getRowCount();
				if (tableRows <= 0) {// 收银单下的商品移除完后将收银单删除
					salesDetailDao.deleteSale(saleId);
				}
				JOptionPane.showMessageDialog(null, "退货成功！");
			}
		});

		// 取消按钮
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				returnFrame.dispose();
			}
		});
	}

}
