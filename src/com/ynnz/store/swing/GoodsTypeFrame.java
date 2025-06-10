package com.ynnz.store.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.dao.IGoodsTypeDao;
import com.ynnz.store.dao.impl.GoodsDaoImpl;
import com.ynnz.store.dao.impl.GoodsTypeDaoImpl;
import com.ynnz.store.pojo.GoodsType;
import com.ynnz.store.util.StringUtils;

public class GoodsTypeFrame extends ParentFrame {

	private static final long serialVersionUID = 1L;
	private JScrollPane listPanel;
	private JTable table;
	private JPanel typeInfoPanel;
	private JTextField typeNameTxt;
	private JComboBox<GoodsType> parentTypeTxt;
	private JButton addBtn;// 新增分类按钮
	private JButton delBtn;// 删除分类按钮
	private JButton cleanBtn;// 清空表单选择的员工信息
	private IGoodsTypeDao goodsTypeDao = new GoodsTypeDaoImpl();
	private IGoodsDao goodsDao = new GoodsDaoImpl();
	private int editTypeId = 0;

	public GoodsTypeFrame() {
		super("商品分类管理");
		init();// 窗体初始化
		typeList();// 创建用户列表信息面板
		typeInfoFrame();// 创建编辑用户信息面板
		btnEvent();// 绑定按钮事件
		this.setResizable(false);
	}

	/**
	 * 设置窗体参数
	 *
	 */
	private void init() {
		this.setLayout(null);
		this.setBounds(300, 150, 800, 500); // 增加窗口大小
		this.setResizable(false);
	}

	/**
	 * 查看商品分类信息列表
	 */
	private void typeList() {
		String[] head = { "分类ID", "分类名称", "父级分类" };
		Object data[][] = new Object[0][head.length];
		List<GoodsType> typeList = goodsTypeDao.getGoodsTypeList();
		if (typeList != null) {
			data = new Object[typeList.size()][head.length];
			int i = 0;
			for (Iterator<GoodsType> iterator = typeList.iterator(); iterator.hasNext();) {
				GoodsType type = (GoodsType) iterator.next();
				String pTypeName = "无";
				if (type.getParentType() != null && StringUtils.isNotEmpty(type.getParentType().getTypeName())) {
					pTypeName = type.getParentType().getTypeName();
				}
				data[i] = new Object[] { type.getTypeId(), type.getTypeName(), pTypeName };
				i++;
			}
		}
		table = new JTable(data, head);
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		listPanel = new JScrollPane(table, v, h);
		listPanel.setBounds(10, 0, 410, 350);
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
				int typeId = (int) model.getValueAt(rowNo, 0);

				GoodsType type = goodsTypeDao.getTypeById(typeId);
				if (type != null) {
					typeNameTxt.setText(type.getTypeName());
					if (type.getParentType() != null && type.getParentType().getTypeId() != 0) {
						int num = parentTypeTxt.getItemCount();
						for (int i = 0; i < num; i++) {
							int id = parentTypeTxt.getItemAt(i).getTypeId();
							if (type.getParentType().getTypeId() == id) {
								parentTypeTxt.setSelectedIndex(i);
							}
						}
					} else {
						parentTypeTxt.setSelectedIndex(0);
					}
					editTypeId = type.getTypeId();
					addBtn.setText("修改分类");
				}
			}
		});

	}

	/**
	 * 构造分类信息编辑面板
	 *
	 */
	private void typeInfoFrame() {
		JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.NORTH);
		tabPanel.setBounds(600, 0, 190, 350);
		typeInfoPanel = new JPanel();
		typeInfoPanel.setLayout(null);
		typeInfoPanel.setBounds(0, 0, 190, 350);
		this.add(typeInfoPanel);

		JLabel typeNameLbl = new JLabel("分类名称：");
		typeNameLbl.setBounds(0, 40, 80, 20);
		typeNameTxt = new JTextField();
		typeNameTxt.setBounds(80, 40, 100, 20);
		typeInfoPanel.add(typeNameLbl);
		typeInfoPanel.add(typeNameTxt);

		JLabel parentTypeLbl = new JLabel("父级分类：");
		parentTypeLbl.setBounds(0, 80, 80, 20);
		parentTypeTxt = new JComboBox<GoodsType>();
		GoodsType u = new GoodsType("--请选择--");
		parentTypeTxt.addItem(u);
		List<GoodsType> types = goodsTypeDao.getGoodsTypeList(0);
		if (types != null && types.size() > 0) {
			for (Iterator<GoodsType> ite = types.iterator(); ite.hasNext();) {
				GoodsType type = (GoodsType) ite.next();
				parentTypeTxt.addItem(type);
			}
		}
		parentTypeTxt.setBounds(80, 80, 100, 20);
		typeInfoPanel.add(parentTypeLbl);
		typeInfoPanel.add(parentTypeTxt);

		delBtn = new JButton("删除分类");
		delBtn.setBounds(5, 120, 90, 20);
		addBtn = new JButton("新增分类");
		addBtn.setBounds(100, 120, 90, 20);
		cleanBtn = new JButton("清空信息");
		cleanBtn.setBounds(5, 145, 90, 20);
		typeInfoPanel.add(delBtn);
		typeInfoPanel.add(addBtn);
		typeInfoPanel.add(cleanBtn);
		tabPanel.add("商品分类信息", typeInfoPanel);
		this.add(tabPanel);
	}

	private void btnEvent() {
		delBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNo = table.getSelectedRow();
				if (rowNo < 0) {
					JOptionPane.showMessageDialog(null, "请选中要删除的分类信息！");
					return;
				} else {
					int sel = JOptionPane.showConfirmDialog(null, "确认要删除该分类信息吗？");
					if (sel == 0) {
						TableModel model = table.getModel();// 把table里的数据转存到Model里
						int typeId = (int) model.getValueAt(rowNo, 0);
						int childrenType = goodsTypeDao.getChildrenCount(typeId);
						if (childrenType > 0) {
							JOptionPane.showMessageDialog(null, "分类下有子分类存在不能删除！");
							return;
						}
						int goodsNum = goodsDao.getGoodsCountByType(typeId);
						if (goodsNum > 0) {
							JOptionPane.showMessageDialog(null, "分类下有商品" + "" + "存在不能删除！");
							return;
						}
						boolean ret = goodsTypeDao.deleteTypeById(typeId);// 具体调用删除数据库操作
						if (ret) {
							table.getSelectionModel().clearSelection();
							editTypeId = 0;
							typeNameTxt.setText("");
							parentTypeTxt.setSelectedIndex(0);
							addBtn.setText("新增分类");
							closePa();
							typeList();
							JOptionPane.showMessageDialog(null, "分类信息删除成功！");
						} else {
							JOptionPane.showMessageDialog(null, "分类信息删除失败！");
						}
					}
				}
			}
		});

		addBtn.addActionListener(new ActionListener() {
			private Pattern patter;
			private Matcher matcher;

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = typeNameTxt.getText();
				if (name == null || name.trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "类型名称不能为空！");
					return;
				} else if (name.trim().length() > 20) {
					JOptionPane.showMessageDialog(null, "类型名称字数不能超过10个汉字！");
					return;
				} else {

				}
				GoodsType pType = (GoodsType) parentTypeTxt.getSelectedItem();
				int pTypeId = 0;
				if (pType != null && !pType.getTypeName().contains("选择")) {
					pTypeId = pType.getTypeId();
				}
				boolean ret = false;
				String msg = "";
				if (editTypeId != 0) {// 编辑分类信息时候需要设置id
					ret = goodsTypeDao.updateGoodsType(name, pTypeId, editTypeId);
					msg = "编辑";
				} else {// 新增员工
					ret = goodsTypeDao.addGoodsType(name, pTypeId);
					msg = "新增";
				}
				if (ret) {
					JOptionPane.showMessageDialog(null, msg + "分类信息成功！");
					closePa();
					typeList();
				} else {
					JOptionPane.showMessageDialog(null, msg + "分类信息失败！");
				}
			}
		});

		cleanBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNo = table.getSelectedRow();
				if (rowNo < 0) {
					return;
				}
				table.getSelectionModel().clearSelection();
				editTypeId = 0;
				typeNameTxt.setText("");
				parentTypeTxt.setSelectedIndex(0);
				addBtn.setText("新增分类");
			}
		});
	}

	/**
	 * 移除列表面板
	 */
	public void closePa() {
		this.remove(listPanel);
	}
}
