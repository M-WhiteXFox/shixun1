CREATE TABLE IF NOT EXISTS goods_storage (
    id INT PRIMARY KEY AUTO_INCREMENT,
    storage_no VARCHAR(50) NOT NULL COMMENT '入库单号',
    goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    storage_num INT NOT NULL COMMENT '入库数量',
    storage_time DATETIME NOT NULL COMMENT '入库时间',
    operator VARCHAR(50) NOT NULL COMMENT '操作员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品入库记录表'; 