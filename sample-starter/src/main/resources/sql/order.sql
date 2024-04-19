----------------------- 大概思路 start-------------------------------------------------------------------
----------------------- 第3.1点 买家频繁查询我的订单 ------------------------------------------------------
-- （1）建立订单表(order_info_库号_表号)、订单详情表(order_detail_库号_表号)，订单编号=原生成规则+用户id的后几位（根据要分多少表决定取几位）
--     分库分表，sharding-key=订单编号，因为订单编号的后几位是用户id后几位，保证了同用户的所有订单都会落在同个库/表上
--     用订单编号作为sharding-key也方便后续根据订单编号查找时可以计算去哪个库/表查找
-- （2）用户id、订单编号加索引
-- （3）分冷热数据。跑定时任务将半年前的订单数据归档到单独的数据库
-- （4）商品等基础信息放redis缓存
-- （5）压测看服务器最大并发数，用sentinel做服务限流
-- （6）读写分离
----------------------- 第3.2点 卖家频繁查询我的订单 -------------------------------------------------------
-- （1）建立商家订单表(seller_order_库号_表号)，冗余商家维度的订单数据，生成订单时同步生成商家订单表信息，这表仅供商家查询用
--     分库分表，sharding-key=商家id，保证同个商家的所有订单都会落在同个库/表上
-- （2）卖家id、订单编号加索引
-- （3）对于单个卖家订单量特别大的情况，可以考虑单独一个库/表
----------------------- 第3.3点 平台客服频繁搜索客诉订单 ----------------------------------------------------
-- （1）建立客诉表，多存一个反转后的订单编号字段，这样用订单尾号查询时可以走索引（查询时先把入参反转再传入）
-- （2）客户姓名、订单编号反转字段加索引
-- （3）跑定时任务将半年前的订单数据归档到单独的数据库
----------------------- 第3.4点 平台运营进行订单数据分析，如排行榜 -------------------------------------------
-- （1）将买家订单数和卖家订单数放入redis的zset里，每次有新订单都更新zset里的数据
-- （2）看运营最关心的还有哪些项，可以通过跑ETL汇总这些数据，迁移到如ES中
----------------------- 第4点 库存扣减 -------------------------------------------------------------------
-- （1）库存放缓存，利用redis的HINCRBY特性通过lua脚本减可售库存、增预占库存，解决并发超扣和性能问题（Redis使用集群模式）
----------------------- 大概思路 end---------------------------------------------------------------------

CREATE TABLE `order_info`
(
    `id`              varchar(32) NOT NULL COMMENT '订单id',
    `order_no`        varchar(32)    DEFAULT NULL COMMENT '订单编号',
    `user_id`         varchar(32)    DEFAULT NULL COMMENT '买家id',
    `seller_id`       varchar(32) NULL DEFAULT NULL COMMENT '商家id',
    `order_status`    int(11) DEFAULT NULL COMMENT '订单状态（0-待支付，1-待发货，2-已发货，3-已完成，4-已关闭）',
    `total_amount`    decimal(10, 2) DEFAULT NULL COMMENT '总金额',
    `payment_amount`  decimal(10, 2) DEFAULT NULL COMMENT '实际金额',
    `shipping_fee`    decimal(10, 2) DEFAULT NULL COMMENT '运费',
    `discount_amount` decimal(10, 2) DEFAULT NULL COMMENT '优惠金额',
    `pay_type`        int(11) DEFAULT NULL COMMENT '支付方式（1-支付宝，2-微信）',
    `pay_status`      int(11) DEFAULT NULL COMMENT '支付状态（0-未支付，1-支付成功，2-支付失败）',
    `order_time`      datetime       DEFAULT NULL COMMENT '下单时间',
    `pay_time`        datetime       DEFAULT NULL COMMENT '支付时间',
    `remark`          varchar(255)   DEFAULT NULL COMMENT '备注',
    `del_flag`        tinyint(4) DEFAULT '0' COMMENT '是否逻辑删除（0-否，1-是）',
    `create_time`     datetime       DEFAULT NULL COMMENT '创建时间',
    `create_by`       varchar(255)   DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime       DEFAULT NULL COMMENT '更新时间',
    `update_by`       varchar(255)   DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY               `order_orderNo` (`order_no`) USING BTREE COMMENT '订单编号',
    KEY               `order_userId` (`user_id`) USING BTREE COMMENT '买家id'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='订单表';

CREATE TABLE `order_detail`
(
    `id`           varchar(32) NOT NULL COMMENT '订单id',
    `order_no`     varchar(32) NULL DEFAULT NULL COMMENT '订单编号',
    `sku_id`       varchar(32) NULL DEFAULT NULL COMMENT '商品id',
    `quantity`     int(11) NULL DEFAULT NULL COMMENT '商品数量',
    `unit_price`   decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
    `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
    `remark`       varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备注',
    `del_flag`     tinyint(4) DEFAULT '0' COMMENT '是否逻辑删除（0-否，1-是）',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_user`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '创建人',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_user`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    INDEX          `orderDetail_orderNo`(`order_no`) USING BTREE COMMENT '订单编号'
) COMMENT = '订单详情表';

CREATE TABLE `seller_order`
(
    `id`           varchar(32) NOT NULL COMMENT '订单id',
    `order_no`     varchar(32) NULL DEFAULT NULL COMMENT '订单编号',
    `seller_id`    varchar(32) NULL DEFAULT NULL COMMENT '商家id',
    `buyer_id`     varchar(32) NULL DEFAULT NULL COMMENT '买家id',
    `order_status` int(11) NULL DEFAULT NULL COMMENT '订单状态（0-待支付，1-待发货，2-已发货，3-已完成，4-已关闭）',
    `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
    `pay_type`     int(11) NULL DEFAULT NULL COMMENT '支付方式（1-支付宝，2-微信）',
    `order_time`   datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
    `pay_time`     datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
    `remark`       varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备注',
    `del_flag`     tinyint(4) DEFAULT '0' COMMENT '是否逻辑删除（0-否，1-是）',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_user`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '创建人',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_user`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    INDEX          `sellerOrder_orderNo`(`order_no`) USING BTREE COMMENT '订单编号',
    INDEX          `sellerOrder_sellerId`(`seller_id`) USING BTREE COMMENT '商家id'
) COMMENT = '商家订单表';

CREATE TABLE `customer_complaint`
(
    `id`                varchar(32) NOT NULL COMMENT '订单id',
    `order_no`          varchar(32) NULL DEFAULT NULL COMMENT '订单编号',
    `order_no_reversed` varchar(32) NULL COMMENT '订单编号反转',
    `seller_id`         varchar(32) NULL DEFAULT NULL COMMENT '商家id',
    `customer_id`       varchar(32) NULL DEFAULT NULL COMMENT '客户id',
    `customer_name`     varchar(255) NULL DEFAULT NULL COMMENT '客户名称',
    `complaint_type`    int NULL DEFAULT NULL COMMENT '投诉类型（1-发货问题，2-卖家服务态度...）',
    `complaint_content` varchar(512) NULL DEFAULT NULL COMMENT '投诉内容',
    `complaint_time`    datetime(0) NULL DEFAULT NULL COMMENT '投诉时间',
    `handing_status`    int NULL COMMENT '处理状态（0-待处理，1-处理中，2-已解决，3-已关闭）',
    `handing_time`      datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
    `remark`            varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备注',
    `del_flag`          tinyint(4) DEFAULT '0' COMMENT '是否逻辑删除（0-否，1-是）',
    `create_time`       datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_user`       varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '创建人',
    `update_time`       datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_user`       varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    INDEX               `customerComplaint_orderNoReversed`(`order_no_reversed`) USING BTREE COMMENT '订单编号反转',
    INDEX               `customerComplaint_customerName`(`customer_name`) USING BTREE COMMENT '客户名称'
) COMMENT = '客诉表';




