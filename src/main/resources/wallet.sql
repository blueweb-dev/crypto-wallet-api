CREATE USER 'crypto-api' IDENTIFIED BY 'by9lXW5uksGgmZNYmjlb';
CREATE USER 'crypto-dba' IDENTIFIED BY 'DrOOvjsrzOQau2Tz9NYt';

CREATE DATABASE wallet CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE wallet;

GRANT SELECT,INSERT,UPDATE ON wallet.* TO 'crypto-api'@'%';
GRANT CREATE,ALTER,DROP,SELECT,UPDATE,INSERT,DELETE ON wallet.* TO 'crypto-dba'@'%';
FLUSH PRIVILEGES;

DROP TABLE IF EXISTS `tb_crypto_master`;
DROP TABLE IF EXISTS `tb_address`;
DROP TABLE IF EXISTS `tb_send_request`;
DROP TABLE IF EXISTS `tb_send`;
DROP TABLE IF EXISTS `tb_recv`;
DROP TABLE IF EXISTS `tb_orphan_address`;

CREATE TABLE `tb_crypto_master` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `decimals` int(1) DEFAULT '8' COMMENT 'Decimal Length of Tokens and Coins',
  `send_mast_addr` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Fixed Sender Address',
  `curr_sync_height` int(1) DEFAULT '0' COMMENT 'Last Block Height Synched',
  `latest_height` int(1) DEFAULT '0' COMMENT 'Max Height in the Chain Synched',
  `total_bal` decimal(36,18) DEFAULT '0.000000000000000000' COMMENT 'Total Hot Wallet Balance',
  `actual_fee` decimal(9,8) DEFAULT '0.00000000' COMMENT 'Latest Actual TX Fee of Crypto',
  `send_mast_bal` decimal(36,18) DEFAULT '0.000000000000000000' COMMENT 'Total Balance of Fixed Sender Address',
  `the_other_bal` decimal(36,18) DEFAULT '0.000000000000000000' COMMENT 'Balance Sum of The Others Except Sender Address',
  `gas_price` decimal(8,4) DEFAULT '0.0000' COMMENT 'Latest GAS price of Crypto Platform',
  `gas_used` decimal(8,4) DEFAULT '0.0000' COMMENT 'Latest GAS price of Crypto Platform',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Cryptocurrency Master';

CREATE TABLE `tb_address` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `balance` decimal(36,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT 'Cryptocurrency Balance',
  `addr` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Wallet Address',
  `tag` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address Tag',
  `uid` int(1) NOT NULL COMMENT 'Unique User ID',
  `broker_id` char(4) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Broker ID Defined at Caller Service',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`,`addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='User Addresses';

CREATE TABLE `tb_send_request` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `order_id` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order ID of Send Request',
  `uid` int(1) NOT NULL COMMENT 'Unique User ID',
  `broker_id` char(4) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Broker ID Defined at Caller Service',
  `expt_fee` decimal(32,8) DEFAULT NULL COMMENT 'Fee Got from System',
  `to_addr` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Receiver Address',
  `to_tag` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Receiver Tag',
  `amount` decimal(32,8) NOT NULL COMMENT 'Amount Send',
  `from_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Account',
  `from_addr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Address',
  `from_tag` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Tag',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`,`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Send Request History that will Never Update and Delete';

CREATE TABLE `tb_send` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `re_notify` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'N' COMMENT 'Notify Again if Send or Receive Status is Not Delievered',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `txid` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction ID',
  `tx_idx` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction Index',
  `notifiable` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Y' COMMENT 'Determine Notify TX Status',
  `confirm` int(1) NOT NULL DEFAULT '0' COMMENT 'Confirmation Count',
  `noti_cnt` int(1) NOT NULL DEFAULT '0' COMMENT 'Notified Count',
  `to_addr` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Receiver Address',
  `to_tag` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Receiver Tag',
  `amount` decimal(32,8) NOT NULL COMMENT 'Amount Send',
  `order_id` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Order ID of Send Request',
  `uid` int(1) NOT NULL COMMENT 'Unique User ID',
  `broker_id` char(4) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Broker ID Defined at Caller Service',
  `expt_fee` decimal(32,8) DEFAULT NULL COMMENT 'Fee Got from System',
  `real_fee` decimal(32,8) DEFAULT NULL COMMENT 'Fee Got from Blockchain',
  `from_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Account',
  `from_addr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Address',
  `from_tag` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Tag',
  `block_id` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Block ID TX is in',
  `tx_time` int(1) DEFAULT '0' COMMENT 'Transaction Time',
  `err_msg` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Error Message',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`,`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Cryptocurrency Send Status';

CREATE TABLE `tb_recv` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `re_notify` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'N' COMMENT 'Notify Again if Send or Receive Status is Not Delievered',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `txid` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Transaction ID',
  `tx_idx` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Transaction Index',
  `notifiable` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Y' COMMENT 'Determine Notify TX Status',
  `confirm` int(1) NOT NULL DEFAULT '0' COMMENT 'Confirmation Count',
  `noti_cnt` int(1) NOT NULL DEFAULT '0' COMMENT 'Notified Count',
  `to_account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Receiver Account',
  `to_addr` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Receiver Address',
  `to_tag` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Receiver Tag',
  `amount` decimal(32,8) NOT NULL COMMENT 'Amount Send',
  `uid` int(1) NOT NULL COMMENT 'Unique User ID',
  `broker_id` char(4) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Broker ID Defined at Caller Service',
  `from_addr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Address',
  `from_tag` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sender Tag',
  `block_id` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Block ID TX is in',
  `tx_time` int(1) DEFAULT '0' COMMENT 'Transaction Time',
  `err_msg` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Error Message',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`,`txid`,`to_addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Cryptocurrency Receive Status';

CREATE TABLE `tb_orphan_address` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `balance` decimal(36,18) DEFAULT '0.000000000000000000' COMMENT 'Balance',
  `account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Account Name',
  `addr` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Wallet Address',
  `tag` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address Tag',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`,`addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Any Addresses Not Managed';

/*
CREATE TABLE `tb_managed_address` (
  `upd_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Time of Last Data Update',
  `symbol` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Symbol of Cryptocurrency',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Name of Address',
  `balance` decimal(36,18) DEFAULT '0.000000000000000000' COMMENT 'Balance',
  `account` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Account Name',
  `addr` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Wallet Address',
  `tag` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Address Tag',
  `reg_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time of First Data Creation',
  PRIMARY KEY (`symbol`,`addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Addresses Managed by System';
*/

