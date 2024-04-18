/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : localhost:3306
 Source Schema         : njtech

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 15/08/2023 10:21:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bareland
-- ----------------------------
DROP TABLE IF EXISTS `bareland`;
CREATE TABLE `bareland`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CN_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bareland
-- ----------------------------
INSERT INTO `bareland` VALUES ('bareland.1', '1', '916.35', '35824.48', 'bareland', '荒地', NULL);
INSERT INTO `bareland` VALUES ('bareland.2', '2', '353.64', '4145.82', 'bareland', '荒地', NULL);

-- ----------------------------
-- Table structure for buildings
-- ----------------------------
DROP TABLE IF EXISTS `buildings`;
CREATE TABLE `buildings`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CN_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of buildings
-- ----------------------------
INSERT INTO `buildings` VALUES ('buildings.1', '19', '131.13', '969.86', 'College of P.E.', '体育学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.10', '33', '80.32', '169.05', 'Building 13, Yaqing', '亚青13幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.100', '126', '118.56', '306.75', 'Building 56, Xuefuyuan', '学府苑56幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.101', '127', '141.47', '340.04', 'Building 57, Xuefuyuan', '学府苑57幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.102', '128', '116.33', '250.54', 'Building 58, Xuefuyuan', '学府苑58幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.103', '129', '85.06', '190.13', 'Building 59, Xuefuyuan', '学府苑59幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.104', '130', '79.29', '155.05', 'Building 60, Xuefuyuan', '学府苑60幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.105', '131', '146.39', '1336.6', 'Tianbao Pavilion', '天宝阁', '天宝阁.jpg');
INSERT INTO `buildings` VALUES ('buildings.106', '132', '230.51', '1091.43', 'Building 11, Tanxiang', '檀香11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.107', '134', '68.85', '119.95', 'Building 10, Tanxiang', '檀香10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.108', '135', '59.67', '93.94', 'Building 9, Tanxiang', '檀香9幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.109', '136', '73.71', '145.75', 'Building 8, Tanxiang', '檀香8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.11', '34', '119.73', '300.07', 'Building 12, Yaqing', '亚青12幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.110', '137', '55.74', '100.51', 'Building 7, Tanxiang', '檀香7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.111', '138', '73.83', '152.99', 'Building 5, Tanxiang', '檀香5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.112', '139', '73.09', '153.15', 'Building 6, Tanxiang', '檀香6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.113', '140', '74.29', '148.36', 'Building 10, Xiangshan', '象山10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.114', '141', '71.19', '163.54', 'Building 7, Xiangshan', '象山7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.115', '142', '74.89', '159.26', 'Building 4, Tanxiang', '檀香4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.116', '143', '75.81', '160.71', 'Building 3, Tanxiang', '檀香3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.117', '144', '54.68', '113.8', 'Building 1, Tanxiang', '檀香1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.118', '145', '70.91', '143.96', 'Building 2, Tanxiang', '檀香2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.119', '146', '436.05', '4839.35', 'Renzhi Building', '仁智楼', '仁智楼.jpg');
INSERT INTO `buildings` VALUES ('buildings.12', '35', '117.17', '315.92', 'Building 15, Yaqing', '亚青15幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.120', '147', '233.93', '2325.4', 'Tonghe Building', '同和楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.121', '148', '206.88', '1404.73', 'Xiangshan Canteen', '象山餐厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.122', '149', '85.38', '413.74', 'Xiangshan Waterside Pavilion', '象山水榭', NULL);
INSERT INTO `buildings` VALUES ('buildings.123', '150', '243.53', '3259.56', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.124', '151', '167.76', '481.76', 'Building 61, Xuefuyuan', '学府苑61幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.125', '152', '117.58', '274.22', 'Building 63, Xuefuyuan', '学府苑63幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.126', '153', '104.9', '245.4', 'Building 62, Xuefuyuan', '学府苑62幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.127', '154', '87.16', '168.95', 'Building 64, Xuefuyuan', '学府苑64幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.128', '155', '113.8', '276.73', 'Building 65, Xuefuyuan', '学府苑65幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.129', '156', '78.86', '163.2', 'Building 68, Xuefuyuan', '学府苑68幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.13', '36', '83.6', '205.47', 'Building 16, Yaqing', '亚青16幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.130', '157', '76.6', '146.83', 'Building 69, Xuefuyuan', '学府苑69幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.131', '158', '104.59', '231.09', 'Building 67, Xuefuyuan', '学府苑67幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.132', '159', '76.6', '141.07', 'Building J6', 'J6', NULL);
INSERT INTO `buildings` VALUES ('buildings.133', '160', '72.51', '132.88', 'Building J5', 'J5', NULL);
INSERT INTO `buildings` VALUES ('buildings.134', '161', '68.07', '122.69', 'Building J4', 'J4', NULL);
INSERT INTO `buildings` VALUES ('buildings.135', '162', '105.87', '133.45', 'Building J3', 'J3', NULL);
INSERT INTO `buildings` VALUES ('buildings.136', '163', '71.03', '95.55', 'Building J2', 'J2', NULL);
INSERT INTO `buildings` VALUES ('buildings.137', '164', '151.79', '1344.04', 'Advanced Engineering Composite Research Center', '先进工程复合材料研究中心', NULL);
INSERT INTO `buildings` VALUES ('buildings.138', '165', '110.34', '723.1', 'Modern Wood Structure Laboratory', '现代木结构实验室', NULL);
INSERT INTO `buildings` VALUES ('buildings.139', '166', '148.51', '957.95', 'College of Civil Engineering', '土木工程学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.14', '37', '79.82', '203.7', 'Building 17, Yaqing', '亚青17幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.140', '167', '200.5', '1839.82', 'Dikun Building', '地坤楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.141', '168', '217.41', '2184.54', 'Tiangong Building', '天工楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.142', '169', '327.77', '4404.47', 'College of Material Science and Technology', '材料科学与技术学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.143', '170', '168.83', '1036.3', 'College of Safety Science and Engineering', '安全科学和工程学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.144', '171', '338.62', '4301.05', 'Research Center for Biochemical Engineering', '生化工程研究中心', NULL);
INSERT INTO `buildings` VALUES ('buildings.145', '172', '173.87', '1252.24', 'Biobased Chemicals Manufacturing Sub center', '生物基化学品制造分中心', NULL);
INSERT INTO `buildings` VALUES ('buildings.146', '174', '370.27', '4877.32', 'Jiangsu Industrial Biotechnology Innovation Center', '江苏工业生物技术创新中心', NULL);
INSERT INTO `buildings` VALUES ('buildings.147', '175', '230.49', '2952.12', 'College of Foreign Languages and Literature', '外国语言文学学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.148', '176', '291.71', '4943.26', 'Yifu Library', '逸夫图书馆', '逸夫图书馆.jpg');
INSERT INTO `buildings` VALUES ('buildings.149', '177', '92.99', '434.19', 'College of Overseas Education', '海外教育学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.15', '38', '84.8', '206.35', 'Building 20, Yaqing', '亚青20幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.150', '178', '329.99', '4648.19', 'Gymnasium', '体育馆', '行健体育馆.jpg');
INSERT INTO `buildings` VALUES ('buildings.151', '179', '238.97', '2468.63', 'Natatorium', '游泳馆', '游泳馆.jpg');
INSERT INTO `buildings` VALUES ('buildings.152', '180', '228.45', '2079.33', 'Zonghe Gymnasium', '综合馆', NULL);
INSERT INTO `buildings` VALUES ('buildings.153', '181', '233.54', '1873.17', 'Pujiang Canteen', '浦江食堂', NULL);
INSERT INTO `buildings` VALUES ('buildings.154', '182', '189.9', '707.29', 'Building 4, Jingxingyuan', '景星苑4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.155', '183', '130.42', '407', 'Building 3, Jingxingyuan', '景星苑3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.156', '184', '119.13', '290.67', 'Building 5, Jingxingyuan', '景星苑5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.157', '185', '120.67', '332.18', 'Building 6, Jingxingyuan', '景星苑6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.158', '186', '210.2', '819.5', 'Building 2, Jingxingyuan', '景星苑2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.159', '187', '205.55', '708.24', 'Building 1, Jingxingyuan', '景星苑1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.16', '39', '84.05', '216.37', 'Building 19, Yaqing', '亚青19幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.160', '188', '99.84', '567.71', 'Tianyou Building', '天佑楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.161', '189', '140.48', '641.14', 'Thermal Science and Engineering Laboratory', '热科学和工程实验室', NULL);
INSERT INTO `buildings` VALUES ('buildings.162', '190', '95.32', '458.51', 'Advanced Chemical Manufacturing Research Institute', '先进化学制造研究所', NULL);
INSERT INTO `buildings` VALUES ('buildings.163', '191', '139.5', '906.51', 'Building A, Duxue Building', '笃学A楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.164', '193', '284.91', '3577.61', 'Duxue Building', '笃学楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.165', '194', '304.89', '3538.16', 'Houxue Building', '厚学楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.166', '195', '375.47', '5129.4', 'Chenyi Building', '沉毅楼', '沉毅楼.jpg');
INSERT INTO `buildings` VALUES ('buildings.167', '196', '261.92', '3199.44', 'College of Mathematics and Science', '数理科学学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.168', '197', '426.21', '6605.59', 'Shangde Building', '尚德楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.169', '199', '148.68', '700.33', 'College of Computer Science and Technology', '计算机科学与技术学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.17', '40', '114.64', '308.3', 'Building 18, Yaqing', '亚青18幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.170', '201', '123.22', '559', 'College of Energy Science and Technology', '能源科学与技术学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.171', '202', '130.95', '596.36', 'Mechanical Research Institute', '机械研究所', NULL);
INSERT INTO `buildings` VALUES ('buildings.172', '203', '133.95', '512.79', 'Building C, Chongde', '崇德楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.173', '204', '205.68', '1382.44', 'College of Electrical Engineering', '电气科学与控制工程学院', NULL);
INSERT INTO `buildings` VALUES ('buildings.174', '205', '403.74', '7443.31', 'Hongyi Building', '弘毅楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.175', '206', '69.46', '197.73', 'Xin Building', '新斋', NULL);
INSERT INTO `buildings` VALUES ('buildings.176', '207', '80.74', '254.16', 'Ru Building', '新斋', NULL);
INSERT INTO `buildings` VALUES ('buildings.177', '208', '74.21', '224.37', 'Ming Building', '明斋', NULL);
INSERT INTO `buildings` VALUES ('buildings.178', '209', '193.08', '966.14', 'Mingde Building', '明德楼', '明德楼.jpg');
INSERT INTO `buildings` VALUES ('buildings.179', '210', '180.27', '1388.38', 'Boya Hall', '博雅堂', '博雅堂.jpg');
INSERT INTO `buildings` VALUES ('buildings.18', '41', '117.53', '295.43', 'Building 21, Yaqing', '亚青21幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.180', '212', '399', '6012.02', 'Administrative building', '行政楼', '行政楼.jpg');
INSERT INTO `buildings` VALUES ('buildings.181', '213', '172.55', '685.74', 'Building 12, Nanyuan', '南苑12幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.182', '214', '179.33', '743.77', 'Building 11, Nanyuan', '南苑11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.183', '215', '145.96', '469.01', 'Building 4, Nanyuan', '南苑4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.184', '216', '146.34', '488.35', 'Building 3, Nanyuan', '南苑3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.185', '217', '66.06', '161.86', 'Building 2, Nanyuan', '南苑2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.186', '218', '68.65', '164.69', 'Building 1, Nanyuan', '南苑1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.187', '219', '66.01', '188.9', 'Jing Building', '景斋', NULL);
INSERT INTO `buildings` VALUES ('buildings.188', '220', '70.87', '228.46', 'Yi Building', '怡斋', NULL);
INSERT INTO `buildings` VALUES ('buildings.189', '221', '119.79', '640.79', 'Yaji Hall', '雅集馆', NULL);
INSERT INTO `buildings` VALUES ('buildings.19', '42', '118.78', '315.33', 'Building 22, Yaqing', '亚青22幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.190', '222', '147.26', '1029.75', 'Nanyuan Canteen', '南苑餐厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.191', '223', '153.09', '514.63', 'Building 5, Nanyuan', '南苑5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.192', '224', '145.37', '456.73', 'Building 6, Nanyuan', '南苑6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.193', '225', '138.84', '349.04', 'Building 8, Nanyuan', '南苑8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.194', '226', '141.66', '384.14', 'Building 7, Nanyuan', '南苑7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.195', '227', '138.9', '407.29', 'Building 9, Nanyuan', '南苑9幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.196', '228', '135.6', '371.16', 'Building 10, Nanyuan', '南苑10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.197', '229', '132.44', '424.59', 'Building A1, Xiyuan', '西苑A1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.198', '230', '137.48', '391.05', 'Building A2, Xiyuan', '西苑A2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.199', '231', '129.19', '417.92', 'Building B1, Xiyuan', '西苑B1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.2', '24', '80.36', '184.66', 'Building 5, Yaqing', '亚青5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.20', '44', '142.29', '315.28', 'Building 1, Delinyuan', '德邻苑1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.200', '233', '135.07', '425.21', 'Building B2, Xiyuan', '西苑B2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.201', '234', '135.55', '421.07', 'Building C1, Xiyuan', '西苑C1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.202', '235', '133.01', '415.87', 'Building C2, Xiyuan', '西苑C2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.203', '236', '106.78', '296.7', 'Building D1, Xiyuan', '西苑D1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.204', '237', '102.87', '248.63', 'Building D2, Xiyuan', '西苑D2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.205', '238', '67.82', '139.93', 'Building G1, Xiyuan', '西苑G1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.206', '239', '63.54', '122.64', 'Building G2, Xiyuan', '西苑G2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.207', '240', '69.25', '160.15', 'Building F1, Xiyuan', '西苑F1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.208', '241', '68.13', '164.25', 'Building F2, Xiyuan', '西苑F2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.209', '242', '96.65', '244.62', 'Building E1, Xiyuan', '西苑E1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.21', '45', '140.95', '305.18', 'Building 33, Yaqing', '亚青33幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.210', '243', '100.7', '260.41', 'Building E2, Xiyuan', '西苑E2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.211', '244', '76.81', '186.15', 'Building H1, Xiyuan', '西苑H1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.212', '245', '71.32', '172.36', 'Building H2, Xiyuan', '西苑H2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.213', '246', '124.83', '765.24', 'Xiyuan Canteen', '西苑餐厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.214', '247', '112.1', '591.58', 'Cainiao Posthouse', '菜鸟驿站', NULL);
INSERT INTO `buildings` VALUES ('buildings.215', '248', '234.88', '2255.68', 'Dongyuan Canteen', '东苑餐厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.216', '249', '122.49', '502.44', 'Student Service Hall', '学生服务大厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.217', '250', '120.75', '493.61', 'Cuizhuyuan', '翠竹苑', NULL);
INSERT INTO `buildings` VALUES ('buildings.218', '251', '81.66', '207.79', 'Building 27, Beiyuan', '北苑27幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.219', '252', '73.38', '167.57', 'Building 26, Beiyuan', '北苑26幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.22', '46', '176.12', '758.23', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.220', '253', '137.66', '891.76', 'School Hospital', '校医院', NULL);
INSERT INTO `buildings` VALUES ('buildings.221', '254', '72.04', '194.64', 'Building 24-1, Beiyuan', '北苑24-1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.222', '255', '63.03', '154.98', 'Building 24-2, Beiyuan', '北苑24-2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.223', '256', '60.65', '144.81', 'Building 22, Beiyuan', '北苑22幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.224', '257', '54.69', '128.11', 'Building 23, Beiyuan', '北苑23幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.225', '258', '50.21', '99.51', 'Building 21, Beiyuan', '北苑21幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.226', '259', '57.55', '127.21', 'Building 20, Beiyuan', '北苑20幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.227', '260', '50.7', '107.04', 'Building 19, Beiyuan', '北苑19幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.228', '261', '50.76', '114.68', 'Building 18, Beiyuan', '北苑18幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.229', '262', '58.42', '107.83', 'Building 17, Beiyuan', '北苑17幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.23', '47', '287.43', '1566.23', 'Building 17, Xiangshan', '象山17幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.230', '263', '137.93', '413.82', 'Building 15, Beiyuan', '北苑15幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.231', '264', '66.59', '159.48', 'Building 16, Beiyuan', '北苑16幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.232', '266', '170.73', '825.48', 'Beiyuan Canteen', '北苑餐厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.233', '267', '211.07', '813.62', 'Building 14, Dongyuan', '东苑14幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.234', '268', '97.76', '323.06', 'Building 13, Dongyuan', '东苑13幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.235', '269', '92.49', '269.45', 'Building 11, Dongyuan', '东苑11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.236', '270', '90.62', '247.94', 'Building 9, Dongyuan', '东苑9幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.237', '271', '98.3', '287.48', 'Building 7, Dongyuan', '东苑7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.238', '272', '96.29', '272.15', 'Building 12, Dongyuan', '东苑12幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.239', '273', '93.78', '263.41', 'Building 10, Dongyuan', '东苑10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.24', '48', '156.8', '531.55', 'Building 14, Xiangshan', '象山14幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.240', '274', '66.56', '181.63', 'Building 8, Dongyuan', '东苑8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.241', '275', '64.56', '156.79', 'Building 6, Dongyuan', '东苑6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.242', '276', '64.22', '172.84', 'Building 5, Dongyuan', '东苑5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.243', '277', '56.61', '115.35', 'Building 4, Dongyuan', '东苑4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.244', '278', '99.03', '270.33', 'Building 3, Dongyuan', '东苑3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.245', '279', '104.03', '301.16', 'Building 1, Dongyuan', '东苑2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.246', '280', '65.19', '160.97', 'Building 2, Dongyuan', '东苑幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.247', '284', '61.55', '138.92', 'Building 14, Beiyuan', '北苑14幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.248', '285', '52.38', '115.68', 'Building 13, Beiyuan', '北苑13幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.249', '286', '60.85', '136.52', 'Building 12, Beiyuan', '北苑12幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.25', '49', '155.96', '469.88', 'Building 13, Xiangshan', '象山13幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.250', '287', '59.17', '131.24', 'Building 11, Beiyuan', '北苑11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.251', '288', '58.68', '121.04', 'Building 10, Beiyuan', '北苑10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.252', '289', '59.89', '126.84', 'Building 9, Beiyuan', '北苑9幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.253', '290', '61.08', '143.78', 'Building 8, Beiyuan', '北苑8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.254', '291', '64.17', '170.62', 'Building 7, Beiyuan', '北苑7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.255', '292', '63.45', '139.29', 'Building 5, Beiyuan', '北苑5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.256', '293', '65.19', '138.47', 'Building 6, Beiyuan', '北苑6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.257', '294', '65.12', '140.19', 'Building 4, Beiyuan', '北苑4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.258', '295', '68.45', '151.93', 'Building 2, Beiyuan', '北苑2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.259', '296', '59.57', '112.88', 'Building 3, Beiyuan', '北苑3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.26', '50', '156.69', '487.18', 'Building 15, Xiangshan', '象山15幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.260', '297', '59.2', '117.41', 'Building 1, Beiyuan', '北苑1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.261', '300', '234.14', '1120.2', 'Guiyuan', '桂苑', NULL);
INSERT INTO `buildings` VALUES ('buildings.262', '347', '104.15', '267.4', 'Building 66, Xuefuyuan', '学府苑66幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.263', '303', '141.33', '336.46', 'Building 32, Yaqing', '亚青32幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.264', '304', '149.95', '353.05', 'Building 31, Yaqing', '亚青31幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.265', '305', '145.6', '371.56', 'Building 30, Yaqing', '亚青30幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.266', '306', '145.9', '311.15', 'Building 29, Yaqing', '亚青29幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.267', '307', '141.09', '284.65', 'Building 28, Yaqing', '亚青28幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.268', '308', '142.08', '276.47', 'Building 27, Yaqing', '亚青27幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.269', '309', '147.97', '330.85', 'Building 26, Yaqing', '亚青26幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.27', '51', '156.35', '497.2', 'Building 16, Xiangshan', '象山16幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.270', '310', '198.26', '1617', 'Yaqing Canteen', '亚青餐厅', NULL);
INSERT INTO `buildings` VALUES ('buildings.271', '311', '224.26', '794.18', 'Building 25, Yaqing', '亚青25幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.272', '312', '235.27', '935.27', 'Building 24, Yaqing', '亚青24幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.273', '313', '226.21', '845.55', 'Building 23, Yaqing', '亚青23幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.274', '315', '169.82', '805.93', 'Mingda Building', '明达楼', NULL);
INSERT INTO `buildings` VALUES ('buildings.275', '316', '159.68', '570.8', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.276', '317', '183.61', '611', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.277', '318', '191.58', '674.48', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.278', '319', '188.13', '650.38', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.279', '320', '265.12', '1014.42', 'Under construction...', '在建……', NULL);
INSERT INTO `buildings` VALUES ('buildings.28', '52', '80.85', '216.93', 'Building 12, Xiangshan', '象山12幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.280', '321', '132.56', '688.82', 'Driver\'s School', '驾校', NULL);
INSERT INTO `buildings` VALUES ('buildings.281', '322', '162.48', '732.15', 'Bioenergy Research Institute', '生物能源研究所', NULL);
INSERT INTO `buildings` VALUES ('buildings.29', '53', '80.19', '198.35', 'Building 11, Xiangshan', '象山11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.3', '25', '79.11', '175.54', 'Building 6, Yaqing', '亚青6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.30', '54', '74.44', '179.41', 'Building 9, Xiangshan', '象山9幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.31', '55', '79.23', '189.18', 'Building 8, Xiangshan', '象山8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.32', '56', '81.03', '203.05', 'Building 5, Xiangshan', '象山5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.33', '57', '54.31', '114.2', 'Building 3, Xiangshan', '象山3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.34', '58', '53.76', '115.09', 'Building 1, Xiangshan', '象山1幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.35', '59', '61.41', '119.36', 'Building 6, Xiangshan', '象山6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.36', '60', '61.07', '122.79', 'Building 4, Xiangshan', '象山4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.37', '61', '58.68', '110.43', 'Building 2, Xiangshan', '象山2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.38', '62', '186.94', '487.37', 'Building 2, Delinyuan', '德邻苑2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.39', '63', '187.18', '488.01', 'Young teachers\' apartment', '青教公寓', NULL);
INSERT INTO `buildings` VALUES ('buildings.4', '26', '151.52', '386.02', 'Building 8, Yaqing', '亚青8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.40', '65', '184.89', '446.7', 'Building 4, Delinyuan', '德邻苑4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.41', '66', '117.47', '400.36', 'Building 55, Xuefuyuan', '学府苑55幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.42', '67', '121.73', '423.44', 'Building 54, Xuefuyuan', '学府苑54幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.43', '69', '69.85', '213.61', 'Building 53, Xuefuyuan', '学府苑53幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.44', '70', '66.95', '187.74', 'Building 52, Xuefuyuan', '学府苑52幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.45', '71', '74.4', '212.67', 'Building 47, Xuefuyuan', '学府苑47幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.46', '72', '71.48', '203.12', 'Building 46, Xuefuyuan', '学府苑46幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.47', '73', '62.29', '185.08', 'Building 71, Xuefuyuan', '学府苑71幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.48', '74', '65.01', '180.01', 'Building 72, Xuefuyuan', '学府苑72幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.49', '75', '73.04', '205.78', 'Building 73, Xuefuyuan', '学府苑73幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.5', '27', '107.95', '256.39', 'Building 7, Yaqing', '亚青7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.50', '76', '79.7', '258.98', 'Building 25, Xuefuyuan', '学府苑25幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.51', '77', '74.86', '207.51', 'Building 24, Xuefuyuan', '学府苑24幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.52', '78', '79.24', '268.51', 'Building 23, Xuefuyuan', '学府苑23幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.53', '79', '76', '217.75', 'Building 22, Xuefuyuan', '学府苑22幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.54', '80', '95.33', '262.05', 'Building 21, Xuefuyuan', '学府苑21幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.55', '81', '62.25', '107.79', 'Building 20, Xuefuyuan', '学府苑20幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.56', '82', '58.23', '116.59', 'Building 19, Xuefuyuan', '学府苑19幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.57', '83', '56.09', '93.47', 'Building 18, Xuefuyuan', '学府苑18幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.58', '84', '73.12', '173.92', 'Building 17, Xuefuyuan', '学府苑17幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.59', '85', '54.71', '120.43', 'Building 16, Xuefuyuan', '学府苑16幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.6', '28', '115.96', '306.9', 'Building 9, Yaqing', '亚青8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.60', '86', '89.56', '187.96', 'Building 10, Xuefuyuan', '学府苑10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.61', '87', '70.69', '131.43', 'Building 11, Xuefuyuan', '学府苑11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.62', '88', '86.07', '307.31', 'Building 12, Xuefuyuan', '学府苑12幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.63', '89', '92.79', '356.12', 'Building 13, Xuefuyuan', '学府苑13幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.64', '90', '89.29', '325.37', 'Building 14, Xuefuyuan', '学府苑14幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.65', '91', '89.47', '249.4', 'Building 15, Xuefuyuan', '学府苑15幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.66', '92', '83.4', '218.93', 'Building 8, Xuefuyuan', '学府苑8幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.67', '93', '78.46', '173.76', 'Building 9, Xuefuyuan', '学府苑9幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.68', '94', '74.29', '166.37', 'Building 7, Xuefuyuan', '学府苑7幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.69', '95', '41.76', '75.35', 'Building 6, Xuefuyuan', '学府苑6幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.7', '30', '82.02', '186.43', 'Building 10, Yaqing', '亚青10幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.70', '96', '37.27', '55.45', 'Building 5, Xuefuyuan', '学府苑5幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.71', '97', '38.08', '55.56', 'Building 4, Xuefuyuan', '学府苑4幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.72', '98', '79.33', '186.07', 'Building 3, Xuefuyuan', '学府苑3幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.73', '99', '63.25', '140.87', 'Building 2, Xuefuyuan', '学府苑2幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.74', '100', '43.57', '81.04', 'Building 11, Xuefuyuan', '学府苑11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.75', '101', '67.81', '188.04', 'Building 70, Xuefuyuan', '学府苑70幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.76', '102', '67.55', '179.33', 'Building 44, Xuefuyuan', '学府苑44幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.77', '103', '118.48', '355.7', 'Building 45, Xuefuyuan', '学府苑45幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.78', '104', '139.82', '408.36', 'Building 49, Xuefuyuan', '学府苑49幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.79', '105', '142.3', '383.21', 'Building 51, Xuefuyuan', '学府苑51幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.8', '31', '77.9', '164.95', 'Building 11, Yaqing', '亚青11幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.80', '106', '120.14', '317.98', 'Building 48, Xuefuyuan', '学府苑48幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.81', '107', '132.83', '367.14', 'Building 50, Xuefuyuan', '学府苑50幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.82', '108', '138.53', '345.38', 'Building 43, Xuefuyuan', '学府苑43幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.83', '109', '69.88', '155.07', 'Building 42, Xuefuyuan', '学府苑42幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.84', '110', '64.65', '111.71', 'Building 41, Xuefuyuan', '学府苑41幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.85', '111', '62.07', '113.23', 'Building 40, Xuefuyuan', '学府苑40幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.86', '112', '70.47', '128.87', 'Building 39, Xuefuyuan', '学府苑39幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.87', '113', '74.74', '143.33', 'Building 34, Xuefuyuan', '学府苑34幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.88', '114', '76.92', '145.26', 'Building 35, Xuefuyuan', '学府苑35幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.89', '115', '90.9', '211.12', 'Building 36, Xuefuyuan', '学府苑36幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.9', '32', '78.07', '176.17', 'Building 14, Yaqing', '亚青14幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.90', '116', '93.08', '211.35', 'Building 37, Xuefuyuan', '学府苑37幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.91', '117', '113.04', '284.16', 'Building 38, Xuefuyuan', '学府苑38幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.92', '118', '87.97', '209.45', 'Building 33, Xuefuyuan', '学府苑33幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.93', '119', '59.76', '143.71', 'Building 32, Xuefuyuan', '学府苑32幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.94', '120', '69.31', '182.43', 'Building 31, Xuefuyuan', '学府苑31幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.95', '121', '76.36', '220.1', 'Building 30, Xuefuyuan', '学府苑30幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.96', '122', '70.7', '177.49', 'Building 29, Xuefuyuan', '学府苑29幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.97', '123', '70.3', '171.28', 'Building 28, Xuefuyuan', '学府苑28幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.98', '124', '69.91', '186.16', 'Building 27, Xuefuyuan', '学府苑27幢', NULL);
INSERT INTO `buildings` VALUES ('buildings.99', '125', '76.32', '168.58', 'Building 44, Xuefuyuan', '学府苑44幢', NULL);

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES ('01', '地信2001');
INSERT INTO `class` VALUES ('02', '遥感2001');
INSERT INTO `class` VALUES ('03', '遥感2002');
INSERT INTO `class` VALUES ('04', '测绘2001');
INSERT INTO `class` VALUES ('05', '测绘2002');

-- ----------------------------
-- Table structure for experiment
-- ----------------------------
DROP TABLE IF EXISTS `experiment`;
CREATE TABLE `experiment`  (
  `eid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `esteps` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `edesc` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of experiment
-- ----------------------------
INSERT INTO `experiment` VALUES ('01', '基本工具', '[{\"step\":\"首先在地图面板上练习各种地图基本操作：书签、平移、拉框放大、缩小、Pan、Full Extent\"},{\"step\":\"在地图面板上用Popup工具，单击地图面板显示Html要素属性\"},{\"step\":\"最后在地图面板上利用测量工具进行长度和面积的测量练习，截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉地图基本操作；\r实验要求：自主操作，并截图记录，提交截图和实验文档；\r实验模块：地图基本操作--基本工具');
INSERT INTO `experiment` VALUES ('02', '加载数据', '[{\"step\":\"首先在地图面板上练习各种加载数据的基本操作，了解常见加载数据的几种基本方式、类型以及步骤,\"},{\"step\":\"在地图面板添加底图，并进行坐标系转换，如由CGCS2000转为墨卡托投影,\"},{\"step\":\"练习加载数据的方式，如xy转点以及添加各种矢量数据（json、shp）,\"},{\"step\":\"自主练习坐标系转换并加载kml数据以及样例csv,截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉地图基本操作；\r实验要求：自主操作，并截图记录，提交截图和实验文档；\r实验模块：地图基本操作--加载数据');
INSERT INTO `experiment` VALUES ('03', '导出数据', '[{\"step\":\"首先在地图面板上练习各种导出数据的基本操作，了解导出数据的基本步骤\"},{\"step\":\"选择导出范围为全部要素，将“China”图层导出为GeoJSON和CSV数据\"},{\"step\":\"选择导出范围为视图范围内的要素，将“cities”图层导出为Shapefile数据\"},{\"step\":\"自主练习数据导出功能，将roads图层导出全部要素和视图范围下的KML、png、pdf等数据\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉地图基本操作；\r实验要求：自主操作，并截图记录，提交截图和实验文档；\r实验模块：地图基本操作--导出数据');
INSERT INTO `experiment` VALUES ('04', '图层通用属性设置', '[{\"step\":\"首先在地图面板上练习图层通用属性设置的基本操作\"},{\"step\":\"将“南京工业大学”图层组缩放至图层，展开“南京工业大学”图层组中“buildings”图层，可修改图层名、视图范围、不透明度、是否可见和缩放范围\"},{\"step\":\"改变“buildings”图层的标注字段为“OBJECTID”，改变标注字段的样式\"},{\"step\":\"查看图层属性表，并进行符号化操作：分别进行single symbol、按类别、按数量设色，最后添加图表符号化\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉地图基本操作；\r实验要求：自主操作，并截图记录，提交截图和实验文档；\r实验模块：地图制图--图层通用属性设置');
INSERT INTO `experiment` VALUES ('05', '地图整饰与输出', '[{\"step\":\"首先在地图面板上地图整饰输出的基本操作\"},{\"step\":\"首先调整视图为Layout View，插入Position Control、Title、Legend、Scale等控件\"},{\"step\":\"单击导出进行导出设置，并输出打印pdf文件\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉地图整饰与输出的基本操作和要领；\r实验要求：按步骤自主操作，并截图记录，提交截图和实验文档；\r实验模块：地图制图 -- 地图整饰与输出');
INSERT INTO `experiment` VALUES ('06', '属性查询', '[{\"step\":\"首先熟悉属性查询的基本操作，了解查询图层和查询要素的基本步骤\"},{\"step\":\"在地图面板上通过查询图层操作查询“roads”图层\"},{\"step\":\"在地图面板上通过查询要素操作，获取“China”图层的“name”属性的为一只，并输入条件查询语句name like \'%Jiang%\'，以查询China图层name字段中包含\'Jiang\'的要素\"},{\"step\":\"参照上一步操作步骤，自主探究并查询“roads”图层中长度(SHAPE_Leng)大于1500km的要素、“cities”图层中人口(population)大于1000w的城市要素\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS属性查询的基本操作和要领；\n实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\n实验模块：空间数据查询 -- 属性查询');
INSERT INTO `experiment` VALUES ('07', '位置查询', '[{\"step\":\"首先熟悉位置查询的基本操作，理解位置查询要素的基本方法(intersect、contain、cross、within)等等\"},{\"step\":\"利用intersect方法查询京港澳高速(G4)经过的省级行政区，具体操作步骤为选择目标图层为China，源图层为roads，在地图面板上选择京港澳高速，显示最终查询结果和要素表格列表，并截图记录\"},{\"step\":\"在上一步操作的基础上，熟悉其他方法操作，练习查询①利用contain方法查询国家主要高速被完全包含于的省级行政区；②利用within方法查询江苏省、四川省、黑龙江省内包含的cities城市；③对比contain与within查询方法，说明它们的不同之处\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS位置查询的基本操作和要领；\r实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\r实验模块：空间数据查询 -- 位置查询');
INSERT INTO `experiment` VALUES ('08', '图形查询', '[{\"step\":\"首先熟悉图形查询的基本操作，了解图形查询要素的基本步骤和原理\"},{\"step\":\"在地图面板上绘制MultiLineString并查询其extent所覆盖的要素\"},{\"step\":\"尝试在地图面板上绘制MultiPolygon和Circle，查询相应extent的要素\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS图形查询的基本操作和要领；\r实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\r实验模块：空间数据查询 -- 图形查询');
INSERT INTO `experiment` VALUES ('09', '属性编辑', '[{\"step\":\"首先熟悉属性编辑的基本操作，了解属性编辑要素的基本步骤和原理\"},{\"step\":\"选择即将属性编辑的图层，以选择“buildings”为例，模糊查询“体育学院”，单击定位至右侧地图面板，并编辑相应属性“名称”为“样例1”\"},{\"step\":\"删除模糊查询“demo”的要素，导出结果excel，作为压缩包成果之一\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS属性编辑的基本操作和要领；\r实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\r实验模块：空间数据编辑 -- 属性编辑');
INSERT INTO `experiment` VALUES ('10', '图形编辑', '[{\"step\":\"首先熟悉图形编辑的基本操作，了解图形编辑要素的基本步骤和原理\"},{\"step\":\"选择即将图形编辑的图层，以选择“buildings”为例，单击“修改用地”按钮，选择要修改的要素，之后单击“提交”按钮，最后刷新页面\"},{\"step\":\"将上述操作应用至实际，修改“demo”边界，截图记录\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS图形编辑的基本操作和要领；\r实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\r实验模块：空间数据编辑 -- 图形编辑');
INSERT INTO `experiment` VALUES ('11', '要素增加', '[{\"step\":\"首先熟悉要素增加的基本操作，了解要素增加的基本步骤和原理\"},{\"step\":\"选择即将增加要素所属的图层，以选择“buildings”为例，单击“增加用地”按钮，在地图面板上绘制增加的要素，绘制完成后自动弹出属性输入，之后单击“确定”按钮，最后刷新页面，截图记录\"},{\"step\":\"参考上述步骤，分别练习增加“water”、“greenland”图层的要素，名称自拟，并截图记录\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS要素增加的基本操作和要领；\r实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\r实验模块：空间数据编辑 -- 要素增加');
INSERT INTO `experiment` VALUES ('12', '最短路径分析', '[{\"step\":\"首先在地图面板上熟悉最短路径分析步骤\"},{\"step\":\"首先在地图面板上选取停靠点（stops），默认设置为2个停靠点\"},{\"step\":\"在地图面板上选取obstacle障碍物，完成最短路径分析\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS属性查询的基本操作和要领；\n实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\n实验模块：空间分析 -- 最短路径分析');
INSERT INTO `experiment` VALUES ('13', '热力图', '[{\"step\":\"首先在地图面板上熟悉热力图分析的具体步骤\"},{\"step\":\"首先在地图面板上加载json矢量数据\"},{\"step\":\"在对话框上选取要量级分析的value字段，并设置输出像元大小等参数，完成点密度即热力图的分析\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS属性查询的基本操作和要领；\n实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\n实验模块：空间分析 -- 热力图');
INSERT INTO `experiment` VALUES ('14', '地理处理', '[{\"step\":\"首先在地图面板上熟悉地理处理分析（缓冲区、intersect、union、dissolve、difference）的具体步骤\"},{\"step\":\"首先输入缓冲区半径及单位进行缓冲区分析\"},{\"step\":\"依次进行intersect、difference、union、dissolve等操作，体会这些操作的含义\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS属性查询的基本操作和要领；\n实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\n实验模块：空间分析 -- 地理处理');
INSERT INTO `experiment` VALUES ('15', 'GIS数据', '[{\"step\":\"首先在地图面板上熟悉几种GIS数据：TIN、泰森多边形、最小包络矩形、凸包以及插值分析操作\"},{\"step\":\"首先在地图上选取8个点，按照指示依次生成TIN、泰森多边形、最小包络矩形、凸包\"},{\"step\":\"最后在生成的数据中进行IDW反距离权重插值\"},{\"step\":\"截图并完成实验报告，上传至本网站平台，打包成压缩包，命名格式为学号+姓名.zip或学号+姓名.rar\"}]', '实验目的：熟悉GIS属性查询的基本操作和要领；\n实验要求：按周步骤自主操作，并截图记录，提交截图和实验文档；\n实验模块：空间分析 -- GIS数据');

-- ----------------------------
-- Table structure for greenland
-- ----------------------------
DROP TABLE IF EXISTS `greenland`;
CREATE TABLE `greenland`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CN_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of greenland
-- ----------------------------
INSERT INTO `greenland` VALUES ('greenland.1', '', '1663.09', '51833.39', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.10', '10', '716.41', '14744.7', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.11', '11', '695.6', '19318.93', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.12', '12', '409.91', '8923.75', 'Chi fir forest', '池杉林', NULL);
INSERT INTO `greenland` VALUES ('greenland.2', '2', '2223.34', '120741.53', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.3', '3', '674.03', '16942.7', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.4', '4', '1156.09', '38758.52', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.5', '5', '402.98', '8222.35', 'Shijun Garden', '时钧园', '时钧园.jpg');
INSERT INTO `greenland` VALUES ('greenland.6', '6', '525.27', '17889.49', 'Central Forest Park', '中央森林公园', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.7', '7', '593.58', '13308.96', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.8', '8', '837.2', '35816.55', 'forest', '森林', '森林.jpg');
INSERT INTO `greenland` VALUES ('greenland.9', '9', '775.22', '35332.34', 'forest', '森林', '森林.jpg');

-- ----------------------------
-- Table structure for njtech
-- ----------------------------
DROP TABLE IF EXISTS `njtech`;
CREATE TABLE `njtech`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CN_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of njtech
-- ----------------------------
INSERT INTO `njtech` VALUES ('njtech.1', '2', '6970.72', '2509054.35', 'njtech', '南京工业大学', '南京工业大学.jpg');

-- ----------------------------
-- Table structure for performexperiments
-- ----------------------------
DROP TABLE IF EXISTS `performexperiments`;
CREATE TABLE `performexperiments`  (
  `pfid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `eid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pfgrade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pfresult` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `pfcomment` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `pfimageurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`pfid`) USING BTREE,
  INDEX `eid`(`eid`) USING BTREE,
  INDEX `sid`(`sid`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  CONSTRAINT `performexperiments_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `experiment` (`eid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `performexperiments_ibfk_2` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `performexperiments_ibfk_3` FOREIGN KEY (`cid`) REFERENCES `class` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of performexperiments
-- ----------------------------
INSERT INTO `performexperiments` VALUES ('2020211360110102', '202021136011', '02', '01', NULL, '2020211360110102202021136011ct.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360120102', '202021136012', '02', '01', '60', '2020211360120102202021136011cx.zip', '继续努力', '');
INSERT INTO `performexperiments` VALUES ('2020211360130102', '202021136013', '02', '01', '77', '2020211360130102202021136013hcy.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360140102', '202021136014', '02', '01', '70', '2020211360140102202021136013jzh.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360150102', '202021136015', '02', '01', '95', '2020211360150102202021136015kh.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360160102', '202021136016', '02', '01', NULL, '2020211360160102202021136016lt.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360170102', '202021136017', '02', '01', '82', '2020211360170102202021136017mxj.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360180102', '202021136018', '02', '01', '88', '2020211360180102202021136018pc.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360190102', '202021136019', '02', '01', NULL, '2020211360190102202021136019qbz.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360200102', '202021136020', '02', '01', NULL, '2020211360200102202021136020qrx.zip', NULL, '');
INSERT INTO `performexperiments` VALUES ('2020211360210101', '202021136021', '01', '01', '80', '2020211360210101assignment_demo.zip', '还不错，继续努力！', '');

-- ----------------------------
-- Table structure for publishexperiments
-- ----------------------------
DROP TABLE IF EXISTS `publishexperiments`;
CREATE TABLE `publishexperiments`  (
  `pbid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `eid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pbname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pbstart_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `pbend_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`pbid`) USING BTREE,
  INDEX `eid`(`eid`) USING BTREE,
  INDEX `tid`(`tid`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  CONSTRAINT `publishexperiments_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `experiment` (`eid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `publishexperiments_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `teacher` (`tid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `publishexperiments_ibfk_3` FOREIGN KEY (`cid`) REFERENCES `class` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of publishexperiments
-- ----------------------------
INSERT INTO `publishexperiments` VALUES ('11110101', '01', '1111', '01', '地信2001班“基本工具”实验', '2023-06-25 11:42:25', '2023-06-29 11:41:25');
INSERT INTO `publishexperiments` VALUES ('11110102', '02', '1111', '01', '地信2001班“加载数据”实验', '2023-06-25 10:20:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110103', '03', '1111', '01', '地信2001班“导出数据”实验', '2023-06-25 10:20:01', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110104', '04', '1111', '01', '地信2001班“图层通用属性设置”实验', '2023-06-25 10:20:17', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110105', '05', '1111', '01', '地信2001班“地图整饰与输出”实验', '2023-06-25 10:20:02', '2024-07-01 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110106', '06', '1111', '01', '地信2001班“属性查询”实验', '2023-06-25 10:19:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110107', '07', '1111', '01', '地信2001班“位置查询”实验', '2023-06-25 10:21:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110108', '08', '1111', '01', '地信2001班“图形查询”实验', '2023-06-25 10:19:10', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110109', '09', '1111', '01', '地信2001班“属性编辑”实验', '2023-06-25 10:21:10', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110110', '10', '1111', '01', '地信2001班“图形编辑”实验', '2023-06-25 10:22:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110111', '11', '1111', '01', '地信2001班“要素增加”实验', '2023-06-25 10:21:40', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110112', '12', '1111', '01', '地信2001班“最短路径分析”实验', '2023-06-25 10:21:09', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110113', '13', '1111', '01', '地信2001班“热力图”实验', '2023-06-25 10:22:55', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110114', '14', '1111', '01', '地信2001班“地理处理”实验', '2023-06-25 10:23:06', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110115', '15', '1111', '01', '地信2001班“GIS数据”实验', '2023-06-25 10:24:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110201', '01', '1111', '02', '遥感2001班“基本工具”实验', '2023-06-25 10:20:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110301', '01', '1111', '03', '遥感2002班“基本工具”实验', '2023-06-25 10:20:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110401', '01', '1111', '04', '测绘2001班“基本工具”实验', '2023-06-25 10:20:00', '2024-06-25 00:00:00');
INSERT INTO `publishexperiments` VALUES ('11110501', '01', '1111', '05', '测绘2002班“基本工具”实验', '2023-06-25 10:20:00', '2024-06-25 00:00:00');

-- ----------------------------
-- Table structure for releasenotices
-- ----------------------------
DROP TABLE IF EXISTS `releasenotices`;
CREATE TABLE `releasenotices`  (
  `rid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `rname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `rtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `rcontent` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`rid`) USING BTREE,
  INDEX `tid`(`tid`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  CONSTRAINT `releasenotices_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `teacher` (`tid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `releasenotices_ibfk_2` FOREIGN KEY (`cid`) REFERENCES `class` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of releasenotices
-- ----------------------------
INSERT INTO `releasenotices` VALUES ('1111012023-03-01 12:11:17', '1111', '01', '关于尽快提交“地图基本操作”实验的通知', '2023-03-01 12:11:17', '请大家尽快完成“地图基本操作”模块实验，提交结果为压缩包，命名方式：“学号+姓名.zip”，其中包括实验报告（word或pdf文档）以及最终结果截图。');
INSERT INTO `releasenotices` VALUES ('1111012023-03-01 19:16:04', '1111', '01', '关于地信2001班“地图基本操作”实验提交截止的通知', '2023-03-01 19:16:04', '请大家没有完成“地图基本操作”模块实验的，将结果（压缩包，命名方式：“学号+姓名.zip”，其中包括实验报告（word或pdf文档）以及最终结果截图）提交至邮箱：GISLkShi@outlook.com。尽快，过时不候，谢谢！');
INSERT INTO `releasenotices` VALUES ('1111012023-06-25 11:45:02', '1111', '01', '尽快提交作业', '2023-06-25 11:45:02', '请大家尽快前往“基本工具”模块完成相应实验并提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:19:00', '1111', '01', '\"地信2001班“属性查询”实验\"作业发布通知', '2023-06-25 10:19:00', '\"地信2001班“属性查询”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往属性查询模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:19:10', '1111', '01', '\"地信2001班“图形查询”实验\"作业发布通知', '2023-06-25 10:19:10', '\"地信2001班“图形查询”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往图形查询模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:20:00', '1111', '01', '\"地信2001班“加载数据”实验\"作业发布通知', '2023-06-25 10:20:00', '\"地信2001班“加载数据”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往加载数据模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:20:01', '1111', '01', '\"地信2001班“导出数据”实验\"作业发布通知', '2023-06-25 10:20:01', '\"地信2001班“导出数据”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往导出数据模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:20:02', '1111', '01', '\"地信2001班“地图整饰与输出”实验\"作业发布通知', '2023-06-25 10:20:02', '\"地信2001班“地图整饰与输出”实验\"作业已经由佚名1老师发布，请于2024-7-1 00:00:00之前，按教师要求的形式前往地图整饰与输出模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:20:17', '1111', '01', '\"地信2001班“图层通用属性设置”实验\"作业发布通知', '2023-06-25 10:20:17', '\"地信2001班“图层通用属性设置”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往图层通用属性设置模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:21:00', '1111', '01', '\"地信2001班“位置查询”实验\"作业发布通知', '2023-06-25 10:21:00', '\"地信2001班“位置查询”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往位置查询模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:21:09', '1111', '01', '\"地信2001班“最短路径分析”实验\"作业发布通知', '2023-06-25 10:21:09', '\"地信2001班“最短路径分析”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往最短路径分析模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:21:10', '1111', '01', '\"地信2001班“属性编辑”实验\"作业发布通知', '2023-06-25 10:21:10', '\"地信2001班“属性编辑”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往属性编辑模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:21:40', '1111', '01', '\"地信2001班“要素增加”实验\"作业发布通知', '2023-06-25 10:21:40', '\"地信2001班“要素增加”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往要素增加模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:22:00', '1111', '01', '\"地信2001班“图形编辑”实验\"作业发布通知', '2023-06-25 10:22:00', '\"地信2001班“图形编辑”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往图形编辑模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:22:55', '1111', '01', '\"地信2001班“热力图”实验\"作业发布通知', '2023-06-25 10:22:55', '\"地信2001班“热力图”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往热力图模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:23:06', '1111', '01', '\"地信2001班“地理处理”实验\"作业发布通知', '2023-06-25 10:23:06', '\"地信2001班“地理处理”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往地理处理模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 10:24:00', '1111', '01', '\"地信2001班“GIS数据”实验\"作业发布通知', '2023-06-25 10:24:00', '\"地信2001班“GIS数据”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往GIS数据模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111012023-6-25 11:42:25', '1111', '01', '\"地信2001班“基本工具”实验\"作业发布通知', '2023-06-25 11:42:25', '\"地信2001班“基本工具”实验\"作业已经由佚名1老师发布，请于2023-6-28 11:41:25之前，按教师要求的形式前往基本工具模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111022023-03-01 12:11:17', '1111', '02', '关于尽快提交“地图基本操作”实验的通知', '2023-03-01 12:11:17', '请大家尽快完成“地图基本操作”模块实验，提交结果为压缩包，命名方式：“学号+姓名.zip”，其中包括实验报告（word或pdf文档）以及最终结果截图。');
INSERT INTO `releasenotices` VALUES ('1111022023-6-25 10:20:00', '1111', '02', '\"遥感2001班“基本工具”实验\"作业发布通知', '2023-06-25 10:20:00', '\"遥感2001班“基本工具”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往基本工具模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111032023-03-01 12:11:17', '1111', '03', '关于尽快提交“地图基本操作”实验的通知', '2023-03-01 12:11:17', '请大家尽快完成“地图基本操作”模块实验，提交结果为压缩包，命名方式：“学号+姓名.zip”，其中包括实验报告（word或pdf文档）以及最终结果截图。');
INSERT INTO `releasenotices` VALUES ('1111032023-6-25 10:20:00', '1111', '03', '\"遥感2002班“基本工具”实验\"作业发布通知', '2023-06-25 10:20:00', '\"遥感2002班“基本工具”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往基本工具模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111042023-03-01 12:11:17', '1111', '04', '关于尽快提交“地图基本操作”实验的通知', '2023-03-01 12:11:17', '请大家尽快完成“地图基本操作”模块实验，提交结果为压缩包，命名方式：“学号+姓名.zip”，其中包括实验报告（word或pdf文档）以及最终结果截图。');
INSERT INTO `releasenotices` VALUES ('1111042023-6-25 10:20:00', '1111', '04', '\"测绘2001班“基本工具”实验\"作业发布通知', '2023-06-25 10:20:00', '\"测绘2001班“基本工具”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往基本工具模块，并在规定时间内及时提交');
INSERT INTO `releasenotices` VALUES ('1111052023-03-01 12:11:17', '1111', '05', '关于尽快提交“地图基本操作”实验的通知', '2023-03-01 12:11:17', '请大家尽快完成“地图基本操作”模块实验，提交结果为压缩包，命名方式：“学号+姓名.zip”，其中包括实验报告（word或pdf文档）以及最终结果截图。');
INSERT INTO `releasenotices` VALUES ('1111052023-6-25 10:20:00', '1111', '05', '\"测绘2002班“基本工具”实验\"作业发布通知', '2023-06-25 10:20:00', '\"测绘2002班“基本工具”实验\"作业已经由佚名1老师发布，请于2024-6-25 00:00:00之前，按教师要求的形式前往基本工具模块，并在规定时间内及时提交');

-- ----------------------------
-- Table structure for route
-- ----------------------------
DROP TABLE IF EXISTS `route`;
CREATE TABLE `route`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Length` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Speed` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Mint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of route
-- ----------------------------
INSERT INTO `route` VALUES ('route.1', '706', '', '30.877892', '', '2000', '0.926337', NULL);
INSERT INTO `route` VALUES ('route.10', '715', '', '32.984724', '', '2000', '0.989542', NULL);
INSERT INTO `route` VALUES ('route.100', '805', '', '93.572099', '', '5000', '1.122865', NULL);
INSERT INTO `route` VALUES ('route.101', '806', '', '76.634486', '', '5000', '0.919614', NULL);
INSERT INTO `route` VALUES ('route.102', '807', '', '43.956993', '', '2000', '1.31871', NULL);
INSERT INTO `route` VALUES ('route.103', '808', '', '21.118353', '', '2000', '0.633551', NULL);
INSERT INTO `route` VALUES ('route.104', '809', '', '43.772672', '', '2000', '1.31318', NULL);
INSERT INTO `route` VALUES ('route.105', '810', '', '39.859201', '', '2000', '1.195776', NULL);
INSERT INTO `route` VALUES ('route.106', '811', '', '80.273312', '', '5000', '0.96328', NULL);
INSERT INTO `route` VALUES ('route.107', '812', '', '59.848459', '', '5000', '0.718182', NULL);
INSERT INTO `route` VALUES ('route.108', '813', '', '72.090257', '', '5000', '0.865083', NULL);
INSERT INTO `route` VALUES ('route.109', '814', '', '25.39046', '', '2000', '0.761714', NULL);
INSERT INTO `route` VALUES ('route.11', '716', '', '168.0174', '', '45000', '0.224023', NULL);
INSERT INTO `route` VALUES ('route.110', '815', '', '43.214955', '', '2000', '1.296449', NULL);
INSERT INTO `route` VALUES ('route.111', '816', '', '90.796942', '', '5000', '1.089563', NULL);
INSERT INTO `route` VALUES ('route.112', '817', '', '16.274725', '', '2000', '0.488242', NULL);
INSERT INTO `route` VALUES ('route.113', '818', '', '197.853238', '', '45000', '0.263804', NULL);
INSERT INTO `route` VALUES ('route.114', '819', '', '94.739354', '', '5000', '1.136872', NULL);
INSERT INTO `route` VALUES ('route.115', '820', '', '30.176452', '', '2000', '0.905294', NULL);
INSERT INTO `route` VALUES ('route.116', '821', '', '101.475652', '', '20000', '0.304427', NULL);
INSERT INTO `route` VALUES ('route.117', '822', '', '40.448541', '', '2000', '1.213456', NULL);
INSERT INTO `route` VALUES ('route.118', '823', '', '72.912839', '', '5000', '0.874954', NULL);
INSERT INTO `route` VALUES ('route.119', '824', '', '49.819265', '', '2000', '1.494578', NULL);
INSERT INTO `route` VALUES ('route.12', '717', '', '46.399563', '', '2000', '1.391987', NULL);
INSERT INTO `route` VALUES ('route.120', '825', '', '87.329605', '', '5000', '1.047955', NULL);
INSERT INTO `route` VALUES ('route.121', '826', '', '67.463803', '', '5000', '0.809566', NULL);
INSERT INTO `route` VALUES ('route.122', '827', '', '110.493555', '', '20000', '0.331481', NULL);
INSERT INTO `route` VALUES ('route.123', '828', '', '93.259741', '', '5000', '1.119117', NULL);
INSERT INTO `route` VALUES ('route.124', '829', '', '98.70615', '', '5000', '1.184474', NULL);
INSERT INTO `route` VALUES ('route.125', '830', '', '202.835364', '', '45000', '0.270447', NULL);
INSERT INTO `route` VALUES ('route.126', '831', '', '212.469202', '', '45000', '0.283292', NULL);
INSERT INTO `route` VALUES ('route.127', '832', '', '239.728674', '', '45000', '0.319638', NULL);
INSERT INTO `route` VALUES ('route.128', '833', '', '131.910594', '', '20000', '0.395732', NULL);
INSERT INTO `route` VALUES ('route.129', '834', '', '186.217793', '', '45000', '0.24829', NULL);
INSERT INTO `route` VALUES ('route.13', '718', '', '67.466203', '', '5000', '0.809594', NULL);
INSERT INTO `route` VALUES ('route.130', '835', '', '259.809931', '', '45000', '0.346413', NULL);
INSERT INTO `route` VALUES ('route.131', '836', '', '122.108789', '', '20000', '0.366326', NULL);
INSERT INTO `route` VALUES ('route.132', '837', '', '91.635683', '', '5000', '1.099628', NULL);
INSERT INTO `route` VALUES ('route.133', '838', '', '372.981644', '', '50000', '0.447578', NULL);
INSERT INTO `route` VALUES ('route.134', '839', '', '328.342558', '', '50000', '0.394011', NULL);
INSERT INTO `route` VALUES ('route.135', '840', '', '81.849862', '', '5000', '0.982198', NULL);
INSERT INTO `route` VALUES ('route.136', '841', '', '324.134937', '', '50000', '0.388962', NULL);
INSERT INTO `route` VALUES ('route.137', '842', '', '58.461874', '', '5000', '0.701542', NULL);
INSERT INTO `route` VALUES ('route.138', '843', '', '163.494971', '', '45000', '0.217993', NULL);
INSERT INTO `route` VALUES ('route.139', '844', '', '698.180443', '', '50000', '0.837817', NULL);
INSERT INTO `route` VALUES ('route.14', '719', '', '77.436588', '', '5000', '0.929239', NULL);
INSERT INTO `route` VALUES ('route.140', '845', '', '103.879133', '', '20000', '0.311637', NULL);
INSERT INTO `route` VALUES ('route.141', '846', '', '108.333025', '', '20000', '0.324999', NULL);
INSERT INTO `route` VALUES ('route.142', '847', '', '129.912623', '', '20000', '0.389738', NULL);
INSERT INTO `route` VALUES ('route.143', '848', '', '121.972417', '', '20000', '0.365917', NULL);
INSERT INTO `route` VALUES ('route.144', '849', '', '229.872261', '', '45000', '0.306496', NULL);
INSERT INTO `route` VALUES ('route.145', '850', '', '53.159284', '', '5000', '0.637911', NULL);
INSERT INTO `route` VALUES ('route.146', '851', '', '54.737669', '', '5000', '0.656852', NULL);
INSERT INTO `route` VALUES ('route.147', '852', '', '107.376831', '', '20000', '0.32213', NULL);
INSERT INTO `route` VALUES ('route.148', '853', '', '88.769102', '', '5000', '1.065229', NULL);
INSERT INTO `route` VALUES ('route.149', '854', '', '676.134466', '', '50000', '0.811361', NULL);
INSERT INTO `route` VALUES ('route.15', '720', '', '52.542086', '', '5000', '0.630505', NULL);
INSERT INTO `route` VALUES ('route.150', '855', '', '212.044673', '', '45000', '0.282726', NULL);
INSERT INTO `route` VALUES ('route.151', '856', '', '454.502359', '', '50000', '0.545403', NULL);
INSERT INTO `route` VALUES ('route.152', '857', '', '431.567398', '', '50000', '0.517881', NULL);
INSERT INTO `route` VALUES ('route.153', '858', '', '211.645598', '', '45000', '0.282194', NULL);
INSERT INTO `route` VALUES ('route.154', '859', '', '628.485421', '', '50000', '0.754183', NULL);
INSERT INTO `route` VALUES ('route.16', '721', '', '99.036971', '', '5000', '1.188444', NULL);
INSERT INTO `route` VALUES ('route.17', '722', '', '262.217459', '', '45000', '0.349623', NULL);
INSERT INTO `route` VALUES ('route.18', '723', '', '171.097026', '', '45000', '0.228129', NULL);
INSERT INTO `route` VALUES ('route.19', '724', '', '47.870182', '', '2000', '1.436105', NULL);
INSERT INTO `route` VALUES ('route.2', '707', '', '95.647561', '', '5000', '1.147771', NULL);
INSERT INTO `route` VALUES ('route.20', '725', '', '138.63503', '', '20000', '0.415905', NULL);
INSERT INTO `route` VALUES ('route.21', '726', '', '96.156667', '', '5000', '1.15388', NULL);
INSERT INTO `route` VALUES ('route.22', '727', '', '198.839742', '', '45000', '0.26512', NULL);
INSERT INTO `route` VALUES ('route.23', '728', '', '305.080124', '', '50000', '0.366096', NULL);
INSERT INTO `route` VALUES ('route.24', '729', '', '31.898769', '', '2000', '0.956963', NULL);
INSERT INTO `route` VALUES ('route.25', '730', '', '293.71773', '', '45000', '0.391624', NULL);
INSERT INTO `route` VALUES ('route.26', '731', '', '110.000792', '', '20000', '0.330002', NULL);
INSERT INTO `route` VALUES ('route.27', '732', '', '204.617138', '', '45000', '0.272823', NULL);
INSERT INTO `route` VALUES ('route.28', '733', '', '2.237925', '', '2000', '0.067138', NULL);
INSERT INTO `route` VALUES ('route.29', '734', '', '222.828115', '', '45000', '0.297104', NULL);
INSERT INTO `route` VALUES ('route.3', '708', '', '371.483312', '', '50000', '0.44578', NULL);
INSERT INTO `route` VALUES ('route.30', '735', '', '127.676013', '', '20000', '0.383028', NULL);
INSERT INTO `route` VALUES ('route.31', '736', '', '125.415642', '', '20000', '0.376247', NULL);
INSERT INTO `route` VALUES ('route.32', '737', '', '123.645867', '', '20000', '0.370938', NULL);
INSERT INTO `route` VALUES ('route.33', '738', '', '288.685613', '', '45000', '0.384914', NULL);
INSERT INTO `route` VALUES ('route.34', '739', '', '113.571166', '', '20000', '0.340713', NULL);
INSERT INTO `route` VALUES ('route.35', '740', '', '437.253112', '', '50000', '0.524704', NULL);
INSERT INTO `route` VALUES ('route.36', '741', '', '84.749322', '', '5000', '1.016992', NULL);
INSERT INTO `route` VALUES ('route.37', '742', '', '21.931557', '', '2000', '0.657947', NULL);
INSERT INTO `route` VALUES ('route.38', '743', '', '285.999534', '', '45000', '0.381333', NULL);
INSERT INTO `route` VALUES ('route.39', '744', '', '352.899995', '', '50000', '0.42348', NULL);
INSERT INTO `route` VALUES ('route.4', '709', '', '105.117838', '', '20000', '0.315354', NULL);
INSERT INTO `route` VALUES ('route.40', '745', '', '430.210258', '', '50000', '0.516252', NULL);
INSERT INTO `route` VALUES ('route.41', '746', '', '615.708121', '', '50000', '0.73885', NULL);
INSERT INTO `route` VALUES ('route.42', '747', '', '528.319582', '', '50000', '0.633983', NULL);
INSERT INTO `route` VALUES ('route.43', '748', '', '449.190127', '', '50000', '0.539028', NULL);
INSERT INTO `route` VALUES ('route.44', '749', '', '259.056108', '', '45000', '0.345408', NULL);
INSERT INTO `route` VALUES ('route.45', '750', '', '148.229371', '', '20000', '0.444688', NULL);
INSERT INTO `route` VALUES ('route.46', '751', '', '120.896438', '', '20000', '0.362689', NULL);
INSERT INTO `route` VALUES ('route.47', '752', '', '381.241123', '', '50000', '0.457489', NULL);
INSERT INTO `route` VALUES ('route.48', '753', '', '302.281223', '', '50000', '0.362737', NULL);
INSERT INTO `route` VALUES ('route.49', '754', '', '133.939602', '', '20000', '0.401819', NULL);
INSERT INTO `route` VALUES ('route.5', '710', '', '128.463839', '', '20000', '0.385392', NULL);
INSERT INTO `route` VALUES ('route.50', '755', '', '691.409952', '', '50000', '0.829692', NULL);
INSERT INTO `route` VALUES ('route.51', '756', '', '269.051418', '', '45000', '0.358735', NULL);
INSERT INTO `route` VALUES ('route.52', '757', '', '65.099426', '', '5000', '0.781193', NULL);
INSERT INTO `route` VALUES ('route.53', '758', '', '60.10249', '', '5000', '0.72123', NULL);
INSERT INTO `route` VALUES ('route.54', '759', '', '251.258536', '', '45000', '0.335011', NULL);
INSERT INTO `route` VALUES ('route.55', '760', '', '97.533729', '', '5000', '1.170405', NULL);
INSERT INTO `route` VALUES ('route.56', '761', '', '395.017437', '', '50000', '0.474021', NULL);
INSERT INTO `route` VALUES ('route.57', '762', '', '189.298211', '', '45000', '0.252398', NULL);
INSERT INTO `route` VALUES ('route.58', '763', '', '136.431056', '', '20000', '0.409293', NULL);
INSERT INTO `route` VALUES ('route.59', '764', '', '123.237213', '', '20000', '0.369712', NULL);
INSERT INTO `route` VALUES ('route.6', '711', '', '94.545102', '', '5000', '1.134541', NULL);
INSERT INTO `route` VALUES ('route.60', '765', '', '210.516767', '', '45000', '0.280689', NULL);
INSERT INTO `route` VALUES ('route.61', '766', '', '162.627414', '', '45000', '0.216837', NULL);
INSERT INTO `route` VALUES ('route.62', '767', '', '184.845242', '', '45000', '0.24646', NULL);
INSERT INTO `route` VALUES ('route.63', '768', '', '348.397862', '', '50000', '0.418077', NULL);
INSERT INTO `route` VALUES ('route.64', '769', '', '987.326519', '', '50000', '1.184792', NULL);
INSERT INTO `route` VALUES ('route.65', '770', '', '17.794936', '', '2000', '0.533848', NULL);
INSERT INTO `route` VALUES ('route.66', '771', '', '21.95941', '', '2000', '0.658782', NULL);
INSERT INTO `route` VALUES ('route.67', '772', '', '40.688855', '', '2000', '1.220666', NULL);
INSERT INTO `route` VALUES ('route.68', '773', '', '0.016949', '', '2000', '0.000508', NULL);
INSERT INTO `route` VALUES ('route.69', '774', '', '0.090745', '', '2000', '0.002722', NULL);
INSERT INTO `route` VALUES ('route.7', '712', '', '42.130466', '', '2000', '1.263914', NULL);
INSERT INTO `route` VALUES ('route.70', '775', '', '0.096096', '', '2000', '0.002883', NULL);
INSERT INTO `route` VALUES ('route.71', '776', '', '286.552245', '', '45000', '0.38207', NULL);
INSERT INTO `route` VALUES ('route.72', '777', '', '345.00286', '', '50000', '0.414003', NULL);
INSERT INTO `route` VALUES ('route.73', '778', '', '486.61136', '', '50000', '0.583934', NULL);
INSERT INTO `route` VALUES ('route.74', '779', '', '51.175977', '', '5000', '0.614112', NULL);
INSERT INTO `route` VALUES ('route.75', '780', '', '71.641566', '', '5000', '0.859699', NULL);
INSERT INTO `route` VALUES ('route.76', '781', '', '384.385993', '', '50000', '0.461263', NULL);
INSERT INTO `route` VALUES ('route.77', '782', '', '56.591659', '', '5000', '0.6791', NULL);
INSERT INTO `route` VALUES ('route.78', '783', '', '203.328869', '', '45000', '0.271105', NULL);
INSERT INTO `route` VALUES ('route.79', '784', '', '23.583779', '', '2000', '0.707513', NULL);
INSERT INTO `route` VALUES ('route.8', '713', '', '61.996564', '', '5000', '0.743959', NULL);
INSERT INTO `route` VALUES ('route.80', '785', '', '288.053413', '', '45000', '0.384071', NULL);
INSERT INTO `route` VALUES ('route.81', '786', '', '143.014255', '', '20000', '0.429043', NULL);
INSERT INTO `route` VALUES ('route.82', '787', '', '164.428471', '', '45000', '0.219238', NULL);
INSERT INTO `route` VALUES ('route.83', '788', '', '44.117254', '', '2000', '1.323518', NULL);
INSERT INTO `route` VALUES ('route.84', '789', '', '220.958934', '', '45000', '0.294612', NULL);
INSERT INTO `route` VALUES ('route.85', '790', '', '148.417066', '', '20000', '0.445251', NULL);
INSERT INTO `route` VALUES ('route.86', '791', '', '12.353655', '', '2000', '0.37061', NULL);
INSERT INTO `route` VALUES ('route.87', '792', '', '176.020692', '', '45000', '0.234694', NULL);
INSERT INTO `route` VALUES ('route.88', '793', '', '244.108394', '', '45000', '0.325478', NULL);
INSERT INTO `route` VALUES ('route.89', '794', '', '90.150697', '', '5000', '1.081808', NULL);
INSERT INTO `route` VALUES ('route.9', '714', '', '90.000489', '', '5000', '1.080006', NULL);
INSERT INTO `route` VALUES ('route.90', '795', '', '470.530628', '', '50000', '0.564637', NULL);
INSERT INTO `route` VALUES ('route.91', '796', '', '14.20214', '', '2000', '0.426064', NULL);
INSERT INTO `route` VALUES ('route.92', '797', '', '81.815087', '', '5000', '0.981781', NULL);
INSERT INTO `route` VALUES ('route.93', '798', '', '56.619517', '', '5000', '0.679434', NULL);
INSERT INTO `route` VALUES ('route.94', '799', '', '21.318312', '', '2000', '0.639549', NULL);
INSERT INTO `route` VALUES ('route.95', '800', '', '110.648295', '', '20000', '0.331945', NULL);
INSERT INTO `route` VALUES ('route.96', '801', '', '156.249234', '', '45000', '0.208332', NULL);
INSERT INTO `route` VALUES ('route.97', '802', '', '95.045256', '', '5000', '1.140543', NULL);
INSERT INTO `route` VALUES ('route.98', '803', '', '692.112968', '', '50000', '0.830536', NULL);
INSERT INTO `route` VALUES ('route.99', '804', '', '136.46707', '', '20000', '0.409401', NULL);

-- ----------------------------
-- Table structure for sport_facilities
-- ----------------------------
DROP TABLE IF EXISTS `sport_facilities`;
CREATE TABLE `sport_facilities`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CN_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sport_facilities
-- ----------------------------
INSERT INTO `sport_facilities` VALUES ('sport_facilities.1', '1', '335.2', '6821.87', 'Dongyuan Basketball Court', '东苑篮球场', NULL);
INSERT INTO `sport_facilities` VALUES ('sport_facilities.2', '2', '286', '5107.61', 'Dongyuan Tennis Court', '东苑网球场', NULL);
INSERT INTO `sport_facilities` VALUES ('sport_facilities.3', '3', '196.54', '2826.69', 'Yinyue Tai', '音乐台', '音乐台.jpg');
INSERT INTO `sport_facilities` VALUES ('sport_facilities.4', '4', '346.04', '8807.24', 'Nanyuan Playground', '南苑操场', NULL);
INSERT INTO `sport_facilities` VALUES ('sport_facilities.5', '5', '562.06', '17981.42', 'Yaqing Playground', '亚青操场', NULL);
INSERT INTO `sport_facilities` VALUES ('sport_facilities.6', '6', '349.76', '6682.25', 'Xingjian Basketball Court', '行健篮球场', NULL);
INSERT INTO `sport_facilities` VALUES ('sport_facilities.7', '7', '450.13', '13724.37', 'Xingjian Playground', '行健体育场', '行健体育场.jpg');
INSERT INTO `sport_facilities` VALUES ('sport_facilities.8', '8', '366.74', '8472.3', 'Softball court', '垒球场', '垒球场.jpg');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `sid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `spassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sgender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `slocation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sphone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `semail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`sid`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `class` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('201821137022', '$2a$10$j974u3EiI2ScdG1P2wvWz.5W17bk0VRRAv7J02cJUmrC35uRVASbW', '吾拉木·艾力', '男', '江苏省/南京市/浦口区/', '14004008820', '201821137022@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('201921137048', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '苏玉姣', '女', '江苏省/南京市/浦口区/', '14004008820', '201921137048@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021136001', '$2a$10$41PvZjtG3I.5ZpU/6rvBeOfUEXEvWJsjQB7ma7ZFfk5uPkonFXxyO', '佚名001', '女', '江苏省/南京市/浦口区/', '14004008820', '202021136001@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136002', '$2a$10$OwIIA.oU9yJKl9be7rW2wOPZ5OUvXQUymtfq0tH1F3DqOpTCPbdEG', '佚名002', '女', '江苏省/南京市/浦口区/', '14004008820', '202021136002@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136003', '$2a$10$6NL1ocOiSywNw7y1Ckm79ONwosgLIq6zmHci2WqdDxlkBE8jZUsOa', '佚名003', '女', '江苏省/南京市/浦口区/', '14004008820', '202021136003@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136004', '$2a$10$hhdAfB.ujVTMSEch4pxHV.oYxNCVBXotMTxHex6hKESNhbGQTbJ8m', '佚名004', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136004@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136005', '$2a$10$8iGuslq6MqB.rEwOZVOhgOg/2dzZZUMtb2xbp3agB9XPA6jR8ZD0W', '佚名005', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136005@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136006', '$2a$10$T/urXR3cguD7uzU4aT1TP.YnB.9TUViBLHMG36fgbczwAwFpL3HCu', '佚名006', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136006@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136007', '$2a$10$PmrGu7BbP3cnCQ.MMNmCW.iw91sdU.MbMmoaAuaUxZOXjt6409X/O', '佚名007', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136007@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136008', '$2a$10$E65N0tPGH7jVV5qB.EwmGOBrn9fx4MTGdJcHHAa5refznR8m/IaOK', '佚名008', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136008@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136009', '$2a$10$pSLReV65KXyeu/Iqt3aR/.YtgbBfWaSQRafKokGZq5hQibfCAdVYu', '佚名009', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136009@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136010', '$2a$10$W7zbVlX1RVy.39gKp5b7X.H7R1gC5vpHSltkng8WMpMwhPr5/MtUW', '佚名010', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136010@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136011', '$2a$10$j.TdXKomBa4M.oPPs6Ft5.IDs0v65ZfhQySm7B9GOMfp5PqU6hNcO', '佚名011', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136011@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136012', '$2a$10$7LGzxPSZmWTtRb9KAQ0hGuM7KEimQIwh.hKo8OsCpqCtRHYAiQqzy', '佚名012', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136012@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136013', '$2a$10$LVP58UX42VeuLPcB0TQrbO3j3sqjbwihSFky1DFanTqF4.mHrW2em', '佚名013', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136013@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136014', '$2a$10$HMAOBvYphBiWQ5x8qrjKhu9O6joNSVRKljX.hFtSWwODc2VbD5vV6', '佚名014', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136014@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136015', '$2a$10$XlbhqLbLGAeGRYUJD2U.w.Aqz0iH.vxLMGr94H0k.uyif7Q2mvGm6', '佚名015', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136015@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136016', '$2a$10$dSOAaQ6tXgXH/cDg2dAH2.Rw.FnQwg/o3FIAOaqW1Gq1eXZ6B8bZW', '佚名016', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136016@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136017', '$2a$10$PYz1qOiVlmXQ5LEjmmXpJ.Xk8LTs.5UubToJEQBCt5u2dLx52QKuG', '佚名017', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136017@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136018', '$2a$10$/l93wnIFBcIkOJmCgVPs3.RYwH9dnIKFxQxEBT4KggblM29.kX14C', '佚名018', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136018@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136019', '$2a$10$2/z8GkRmhiOWWfExjx/Q0.WYC0jE7gWrX4tDeI3u0mUQw5kbbJXy6', '佚名019', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136019@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136020', '$2a$10$mI9ZZdzoSiBopApWf8R4RebsMVevuQleBhJLKTUDcGlcK6c8.6Xsi', '佚名020', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136020@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136021', '$2a$10$h2WEVALsbpKNK7poyjREQuJ.Y/1NWPc4aIB27Ab.MLua1t.vDsyc.', '佚名021', '男', '江苏省/南京市/浦口区/', '14004008820', '202021136021@outlook.com', '01');
INSERT INTO `student` VALUES ('202021136022', '$2a$10$83MkStiek9hXr9GjoUbBvOE94bDzai/DEl9k9fMxqL96cEjkXFWAW', '苏梦磊', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136022@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136023', '$2a$10$lnfviX5nyXTPFZr6BKKIWugT1/KNDLVTDUgt0bILIIzQRUNGXGtc2', '胥明鑫', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136023@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136024', '$2a$10$TeutNbLvVx3.9tr8z69iW.4n7zdlJOa.JWoTSJLR8kfJV/JGhbnY2', '杨平', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136024@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136025', '$2a$10$i.LUE7/1eMyF7B4QZEQlb.IW5pMUrwRp5gPiDx0Whg3WYASPuO9Mi', '姚毅', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136025@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136026', '$2a$10$NjmaDqHYIchuYQQ2WeGDHO9O4QQCG/2I33SUmptiD7DM/0/nJKWPe', '余克畅', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136026@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021136027', '$2a$10$o8qNF0QOPAEKvrIL4TAqwO2ElRkSM8PWlY48KBS/tDoG0zYGIf9xi', '张同舒', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021136027@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021137001', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈瑜珺', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137001@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137002', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '米合热古丽·阿卜杜艾尼', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137002@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137003', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '倪鸣', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137003@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137005', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '夏尔依旦·奴尔东', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137005@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137006', '$2a$10$FLDlzWkE0ZPJLG.6rmpdnehrfVcwebN.xcWGQJupwHe59HS6MvzUm', '赵雨婷', '女', '江苏省\\南京市\\浦口区\\', '14004008820', '202021137006@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021137007', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '阿力亚尔·阿布力克木', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137007@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137008', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '艾科热木·阿卜拉合麦提', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137008@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137009', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '曹利鹏', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137009@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137010', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈杰', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137010@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137011', '$2a$10$TjVE/qeY76vQ5Iq4RqyrR.watGVeDd0LZw9EMUVAxyJDuaiUqEG6u', '陈慕尧', '男', '江苏省\\南京市\\浦口区\\', '14004008820', '202021137011@njtech.edu.cn', '01');
INSERT INTO `student` VALUES ('202021137012', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '戴金', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137012@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137013', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '邓洪涛', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137013@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137014', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '邓志江', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137014@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137015', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '丁超然', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137015@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137016', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '范雨', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137016@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137017', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李嘉淇', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137017@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137018', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李洋', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137018@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137019', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘洋', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137019@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137020', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '牛连坤', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137020@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137021', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '田永佳', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137021@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137022', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '谢飞', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137022@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137023', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '叶宇翔', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137023@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137024', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '尤凯', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137024@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137025', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '赵宇淏', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137025@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137026', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '朱永灿', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137026@njtech.edu.cn', '04');
INSERT INTO `student` VALUES ('202021137027', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '才央措毛', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137027@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137028', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '地丽努尔·阿布拉', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137028@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137029', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '杨一珩瑞', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137029@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137030', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '张梅', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137030@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137031', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '周晨霞', '女', '江苏省/南京市/浦口区/', '14004008820', '202021137031@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137032', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '丁环宇', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137032@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137033', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '何佳杰', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137033@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137034', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '吉宇烨', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137034@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137035', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '姜楠', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137035@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137036', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李丰超', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137036@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137037', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李云杉', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137037@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137038', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘开志', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137038@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137039', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘秋诚', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137039@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137040', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘耀威', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137040@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137041', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘志杰', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137041@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137042', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '皮尔达吾斯·阿力木', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137042@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137043', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '热比特·依民江', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137043@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137045', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '吴桐', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137045@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137046', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '向家奎', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137046@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137048', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '薛高健', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137048@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137049', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '姚键琳', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137049@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137050', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '于宇恒', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137050@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021137053', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '张以通', '男', '江苏省/南京市/浦口区/', '14004008820', '202021137053@njtech.edu.cn', '05');
INSERT INTO `student` VALUES ('202021143001', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '杜美雯', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143001@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143002', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '龚妤乐', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143002@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143003', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '郭珊珊', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143003@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143004', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '江梅', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143004@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143005', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李嘉睿', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143005@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143006', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '张乐涵', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143006@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143007', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈志辉', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143007@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143008', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈子翊', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143008@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143009', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '杜江涛', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143009@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143011', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '高伟晋', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143011@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143012', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '黄伟群', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143012@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143014', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李斌', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143014@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143015', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李超', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143015@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143017', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘顾洋', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143017@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143018', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陆玉龙', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143018@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143020', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '孙轩', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143020@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143021', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '汪正军', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143021@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143022', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '王恒羽', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143022@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143023', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '王铁', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143023@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143024', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '王毅', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143024@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143025', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '王钰琳', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143025@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143026', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '吴雨洲', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143026@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143027', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '谢峥峰', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143027@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143028', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '徐帅', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143028@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143029', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '闫宝瑞', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143029@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143030', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '朱昊', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143030@njtech.edu.cn', '02');
INSERT INTO `student` VALUES ('202021143031', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '曾媛', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143031@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143032', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '冯紫芸', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143032@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143033', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陆佳宜', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143033@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143034', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '曲铭媛', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143034@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143035', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '王永青', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143035@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143036', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '赵静柔', '女', '江苏省/南京市/浦口区/', '14004008820', '202021143036@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143037', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈江华', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143037@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143038', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈令坤', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143038@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143039', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '陈一鸣', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143039@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143040', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '程思进', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143040@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143041', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '程裕丰', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143041@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143042', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '冯润平', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143042@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143043', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '胡鹏', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143043@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143045', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '吉天哲', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143045@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143046', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '姜海波', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143046@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143047', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '蒋为', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143047@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143048', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李锦程', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143048@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143049', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李文洋', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143049@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143050', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '李想', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143050@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143051', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '刘志峰', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143051@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143052', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '吕骁', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143052@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143053', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '桑凡', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143053@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143054', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '王萧剑', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143054@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143055', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '巫任楠', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143055@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143056', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '谢陈其', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143056@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143057', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '邢彬', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143057@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143058', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '严效坡', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143058@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143059', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '俞飞鸿', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143059@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202021143060', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '朱帅康', '男', '江苏省/南京市/浦口区/', '14004008820', '202021143060@njtech.edu.cn', '03');
INSERT INTO `student` VALUES ('202027137051', '$2a$10$F2hquZksth7wIC4e1MM95e4ktw16u0a6RuBXl7YcXQQLgGlNwQdFa', '俞荣恒', '男', '江苏省/南京市/浦口区/', '14004008820', '202027137051@njtech.edu.cn', '05');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `tid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tpassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tgender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tlevel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1111', '$2a$10$uA3OeygXwirFSypQWgacDOTCBoGDR2D9NCE6reeBc2mGD.kaM4ofS', '佚名1', '女', '教授');
INSERT INTO `teacher` VALUES ('1112', '$2a$10$Hl/byNqSgLHNQ0euAQo28e21yoROrT0/quTG4as7w.PEysinowrma', '佚名2', '男', '副教授');
INSERT INTO `teacher` VALUES ('1113', '$2a$10$S042vWNkmglWjv5XZwKpXe0WMXZB1rBlqbJ4Mjvg8h6ues6WNmml6', '佚名3', '女', '讲师');
INSERT INTO `teacher` VALUES ('1114', '$2a$10$F0o3G0H4H/SCQ6ODDwabUuBfKW9qanYATzrxUNaZefydiTN3TUZQS', '佚名4', '男', '副教授');
INSERT INTO `teacher` VALUES ('2339', '$2a$10$teJMAXGRPr0jRqJpQVJWEucCWCX7rCbepNdnRS.PM55q3.PUl7Ehq', '佚名5', '女', '副教授');
INSERT INTO `teacher` VALUES ('3390', '$2a$10$lNuHMTEa.ulASQBBdWQzfOdxMJWqE3xl/3noCb2g479aR.6.BmJEC', '佚名6', '女', '副教授');
INSERT INTO `teacher` VALUES ('5135', '$2a$10$GRciDh02mxHwFdbHuWhnseF3Zsi8wsi80rlRo.K9LUJAv52CnTGq.', '佚名7', '男', '教授');
INSERT INTO `teacher` VALUES ('5886', '$2a$10$R./B/bAtcSVoQbO5qYp2suIWNCr9Kf0fj8JL1i/JVnJ0AyWz1P5v6', '佚名8', '男', '副教授');
INSERT INTO `teacher` VALUES ('6114', '$2a$10$3iDDUgWEPtfuqTLc1t4i2eFqz8.kKfmICrbtS5g65s/wEMx7EXO2G', '佚名9', '男', '讲师');

-- ----------------------------
-- Table structure for water
-- ----------------------------
DROP TABLE IF EXISTS `water`;
CREATE TABLE `water`  (
  `fid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OBJECTID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Leng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `SHAPE_Area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Layer_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CN_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `imgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water
-- ----------------------------
INSERT INTO `water` VALUES ('water.1', '293', '379.25', '6664.52', 'water', '水体', '水体.jpg');
INSERT INTO `water` VALUES ('water.2', '294', '261.99', '3452.87', 'water', '水体', '水体.jpg');
INSERT INTO `water` VALUES ('water.3', '295', '159.32', '1350.81', 'water', '水体', '水体.jpg');
INSERT INTO `water` VALUES ('water.4', '296', '502.75', '8133.77', 'water', '水体', '水体.jpg');
INSERT INTO `water` VALUES ('water.5', '297', '629.47', '13191.79', 'Jing Lake', '镜湖', '镜湖.jpg');
INSERT INTO `water` VALUES ('water.6', '298', '373.15', '4976.6', 'water', '水体', '水体.jpg');
INSERT INTO `water` VALUES ('water.7', '299', '590.44', '8568.82', 'Yiqing Tongji Lake', '溢清同济湖', '溢清同济湖.jpg');
INSERT INTO `water` VALUES ('water.8', '314', '430.59', '7343.6', 'Junzi Lake', '君子湖', '君子湖.jpg');

SET FOREIGN_KEY_CHECKS = 1;
