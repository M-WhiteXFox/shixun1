package com.ynnz.store.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.dao.IStorageRecordDao;
import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.dao.impl.GoodsDaoImpl;
import com.ynnz.store.dao.impl.StorageRecordDaoImpl;
import com.ynnz.store.dao.impl.UserinfoDaoImpl;
import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.StorageRecord;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.DateUtil;
import com.ynnz.store.util.StringUtils;

public class GoodsStorageRecordFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private IStorageRecordDao storageRecordDao;
    private IUserinfoDao userInfoDao;
    private IGoodsDao goodsDao;
    private List<StorageRecord> currentRecords;
    private JButton editButton;
    private JButton deleteButton;

    public GoodsStorageRecordFrame() {
        setTitle("入库记录管理");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 初始化DAO
        storageRecordDao = new StorageRecordDaoImpl();
        userInfoDao = new UserinfoDaoImpl();
        goodsDao = new GoodsDaoImpl();

        // 创建搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchTypeCombo = new JComboBox<>(new String[] { "商品名称", "入库单号" });
        searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        JButton addButton = new JButton("新增");
        JButton refreshButton = new JButton("刷新");
        editButton = new JButton("修改");
        deleteButton = new JButton("删除");

        // 初始状态下禁用修改和删除按钮
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        searchPanel.add(new JLabel("搜索类型："));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(new JLabel("搜索内容："));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(refreshButton);
        searchPanel.add(editButton);
        searchPanel.add(deleteButton);

        // 创建表格
        String[] columnNames = { "入库单号", "商品名称", "入库数量", "入库时间", "操作员" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        // 设置表格列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(120); // 入库单号
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // 商品名称
        table.getColumnModel().getColumn(2).setPreferredWidth(80); // 入库数量
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // 入库时间
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // 操作员

        // 设置表格的其他属性
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 禁止自动调整列宽
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 只允许单选
        table.getTableHeader().setReorderingAllowed(false); // 禁止拖动列
        table.setRowHeight(25); // 设置行高

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 添加表格选择监听器
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                boolean hasSelection = selectedRow != -1;
                editButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
            }
        });

        // 添加组件到窗口
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 加载数据
        loadData();

        // 添加事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchType = (String) searchTypeCombo.getSelectedItem();
                String searchContent = searchField.getText().trim();
                if (StringUtils.isEmpty(searchContent)) {
                    loadData();
                    return;
                }

                List<StorageRecord> records;
                if ("商品名称".equals(searchType)) {
                    records = storageRecordDao.getStorageRecordList(null, null, searchContent);
                } else {
                    StorageRecord record = storageRecordDao.getStorageRecordByCode(searchContent);
                    records = record != null ? List.of(record) : List.of();
                }
                updateTable(records);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddDialog();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    showEditDialog(currentRecords.get(selectedRow));
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    showDeleteDialog(currentRecords.get(selectedRow));
                }
            }
        });
    }

    private void loadData() {
        currentRecords = storageRecordDao.getStorageRecordList(null, null, null);
        updateTable(currentRecords);
        // 清除选择状态
        table.clearSelection();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void updateTable(List<StorageRecord> records) {
        tableModel.setRowCount(0);
        for (StorageRecord record : records) {
            Object[] rowData = {
                    record.getRecordCode(),
                    record.getGoodsName(),
                    record.getQuantity(),
                    DateUtil.getDate(record.getStorageDate()),
                    record.getOperatorName()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "新增入库记录", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setLayout(null);

        // 入库单号
        JLabel recordCodeLabel = new JLabel("入库单号：");
        recordCodeLabel.setBounds(20, 20, 80, 25);
        JTextField recordCodeField = new JTextField();
        recordCodeField.setBounds(100, 20, 200, 25);
        recordCodeField.setText(generateStorageNo());
        recordCodeField.setEditable(false);

        // 商品条码
        JLabel barCodeLabel = new JLabel("商品条码：");
        barCodeLabel.setBounds(20, 50, 80, 25);
        JTextField barCodeField = new JTextField();
        barCodeField.setBounds(100, 50, 120, 25);
        JButton queryGoodsButton = new JButton("查询");
        queryGoodsButton.setBounds(230, 50, 70, 25);

        // 商品名称
        JLabel goodsNameLabel = new JLabel("商品名称：");
        goodsNameLabel.setBounds(20, 80, 80, 25);
        JTextField goodsNameField = new JTextField();
        goodsNameField.setBounds(100, 80, 200, 25);
        goodsNameField.setEditable(false);

        // 入库数量
        JLabel quantityLabel = new JLabel("入库数量：");
        quantityLabel.setBounds(20, 110, 80, 25);
        JTextField quantityField = new JTextField();
        quantityField.setBounds(100, 110, 200, 25);

        // 操作员
        JLabel operatorLabel = new JLabel("操作员：");
        operatorLabel.setBounds(20, 140, 80, 25);
        JComboBox<UserInfo> operatorCombo = new JComboBox<>();
        operatorCombo.setBounds(100, 140, 200, 25);
        List<UserInfo> operators = userInfoDao.getUserList();
        for (UserInfo operator : operators) {
            operatorCombo.addItem(operator);
        }

        // 添加组件
        panel.add(recordCodeLabel);
        panel.add(recordCodeField);
        panel.add(barCodeLabel);
        panel.add(barCodeField);
        panel.add(queryGoodsButton);
        panel.add(goodsNameLabel);
        panel.add(goodsNameField);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(operatorLabel);
        panel.add(operatorCombo);

        // 查询按钮事件
        queryGoodsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String barCode = barCodeField.getText().trim();
                if (StringUtils.isNotEmpty(barCode)) {
                    Goods goods = goodsDao.getGoodsByBarCode(barCode);
                    if (goods != null) {
                        goodsNameField.setText(goods.getGoodsName());
                    } else {
                        JOptionPane.showMessageDialog(dialog, "未找到该商品！");
                        goodsNameField.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "请输入商品条码！");
                }
            }
        });

        // 确定按钮
        JButton confirmButton = new JButton("确定");
        confirmButton.setBounds(100, 180, 80, 25);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recordCode = recordCodeField.getText().trim();
                String barCode = barCodeField.getText().trim();
                String goodsName = goodsNameField.getText().trim();
                String quantityStr = quantityField.getText().trim();
                UserInfo operator = (UserInfo) operatorCombo.getSelectedItem();

                if (StringUtils.isEmpty(barCode) || StringUtils.isEmpty(goodsName)
                        || StringUtils.isEmpty(quantityStr)) {
                    JOptionPane.showMessageDialog(dialog, "请填写完整信息！");
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(dialog, "入库数量必须大于0！");
                        return;
                    }

                    Goods goods = goodsDao.getGoodsByBarCode(barCode);
                    if (goods == null) {
                        JOptionPane.showMessageDialog(dialog, "未找到该商品！");
                        return;
                    }

                    StorageRecord record = new StorageRecord();
                    record.setRecordCode(recordCode);
                    record.setGoodsId(goods.getGoodsId());
                    record.setGoodsName(goods.getGoodsName());
                    record.setQuantity(quantity);
                    record.setStorePrice(goods.getStorePrice());
                    record.setTotalAmount(goods.getStorePrice() * quantity);
                    record.setStorageDate(new Date());
                    record.setOperatorId(operator.getSaleManId());
                    record.setOperatorName(operator.getSaleManName());

                    if (storageRecordDao.addStorageRecord(record)) {
                        // 更新商品库存
                        goodsDao.updateStoreNum(goods.getGoodsId(), goods.getStockNum() + quantity);
                        JOptionPane.showMessageDialog(dialog, "添加成功！");
                        dialog.dispose();
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "添加失败！");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "入库数量必须是整数！");
                }
            }
        });

        // 取消按钮
        JButton cancelButton = new JButton("取消");
        cancelButton.setBounds(200, 180, 80, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panel.add(confirmButton);
        panel.add(cancelButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditDialog(StorageRecord record) {
        JDialog dialog = new JDialog(this, "修改入库记录", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setLayout(null);

        // 入库单号
        JLabel recordCodeLabel = new JLabel("入库单号：");
        recordCodeLabel.setBounds(20, 20, 80, 25);
        JTextField recordCodeField = new JTextField(record.getRecordCode());
        recordCodeField.setBounds(100, 20, 200, 25);
        recordCodeField.setEditable(false);

        // 商品名称
        JLabel goodsNameLabel = new JLabel("商品名称：");
        goodsNameLabel.setBounds(20, 50, 80, 25);
        JTextField goodsNameField = new JTextField(record.getGoodsName());
        goodsNameField.setBounds(100, 50, 200, 25);
        goodsNameField.setEditable(false);

        // 入库数量
        JLabel quantityLabel = new JLabel("入库数量：");
        quantityLabel.setBounds(20, 80, 80, 25);
        JTextField quantityField = new JTextField(String.valueOf(record.getQuantity()));
        quantityField.setBounds(100, 80, 200, 25);

        // 添加组件
        panel.add(recordCodeLabel);
        panel.add(recordCodeField);
        panel.add(goodsNameLabel);
        panel.add(goodsNameField);
        panel.add(quantityLabel);
        panel.add(quantityField);

        // 确定按钮
        JButton confirmButton = new JButton("确定");
        confirmButton.setBounds(100, 120, 80, 25);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String quantityStr = quantityField.getText().trim();
                if (StringUtils.isEmpty(quantityStr)) {
                    JOptionPane.showMessageDialog(dialog, "请输入入库数量！");
                    return;
                }

                try {
                    int newQuantity = Integer.parseInt(quantityStr);
                    if (newQuantity <= 0) {
                        JOptionPane.showMessageDialog(dialog, "入库数量必须大于0！");
                        return;
                    }

                    // 获取商品信息
                    Goods goods = goodsDao.getGoodsById(record.getGoodsId());
                    if (goods == null) {
                        JOptionPane.showMessageDialog(dialog, "未找到该商品！");
                        return;
                    }

                    // 计算库存变化
                    int quantityDiff = newQuantity - record.getQuantity();

                    // 更新入库记录
                    record.setQuantity(newQuantity);
                    record.setTotalAmount(goods.getStorePrice() * newQuantity);

                    if (storageRecordDao.updateStorageRecord(record)) {
                        // 更新商品库存
                        goodsDao.updateStoreNum(goods.getGoodsId(), goods.getStockNum() + quantityDiff);
                        JOptionPane.showMessageDialog(dialog, "修改成功！");
                        dialog.dispose();
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "修改失败！");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "入库数量必须是整数！");
                }
            }
        });

        // 取消按钮
        JButton cancelButton = new JButton("取消");
        cancelButton.setBounds(200, 120, 80, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panel.add(confirmButton);
        panel.add(cancelButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showDeleteDialog(StorageRecord record) {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这条入库记录吗？", "确认删除",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // 获取商品信息
                Goods goods = goodsDao.getGoodsById(record.getGoodsId());
                if (goods == null) {
                    JOptionPane.showMessageDialog(this, "商品不存在！");
                    return;
                }

                // 更新商品库存
                int newStock = goods.getStockNum() - record.getQuantity();
                if (newStock < 0) {
                    JOptionPane.showMessageDialog(this, "库存不足，无法删除！");
                    return;
                }

                // 更新库存
                if (!goodsDao.updateStoreNum(goods.getGoodsId(), newStock)) {
                    JOptionPane.showMessageDialog(this, "更新商品库存失败！");
                    return;
                }

                // 删除入库记录
                if (storageRecordDao.deleteStorageRecord(record.getRecordCode())) {
                    JOptionPane.showMessageDialog(this, "删除成功！");
                    loadData(); // 重新加载数据
                } else {
                    // 如果删除失败，回滚库存
                    goodsDao.updateStoreNum(goods.getGoodsId(), goods.getStockNum());
                    JOptionPane.showMessageDialog(this, "删除失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "操作失败：" + e.getMessage());
            }
        }
    }

    private String generateStorageNo() {
        return "RK" + DateUtil.getNowTimeStamp();
    }

    // 按钮渲染器
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // 按钮编辑器
    class ButtonEditor extends DefaultCellEditor implements TableCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            this.row = row;
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                String[] actions = label.split(" ");
                int x = button.getX();
                int width = button.getWidth();

                // 根据点击位置判断是"修改"还是"删除"
                if (x < width / 2) {
                    // 修改
                    showEditDialog(currentRecords.get(row));
                } else {
                    // 删除
                    showDeleteDialog(currentRecords.get(row));
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}