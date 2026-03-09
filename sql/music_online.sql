/*
 Navicat Premium Data Transfer

 Source Server         : zhou
 Source Server Type    : MySQL
 Source Server Version : 80037
 Source Host           : localhost:3306
 Source Schema         : music_online

 Target Server Type    : MySQL
 Target Server Version : 80037
 File Encoding         : 65001

 Date: 10/03/2026 01:26:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码(BCrypt加密)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '系统管理员', 1, '2026-03-09 18:54:18', '2026-03-09 20:49:04');
INSERT INTO `admin` VALUES (2, 'admin2', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '运营管理员', 1, '2026-03-09 18:54:18', '2026-03-09 20:46:57');

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '专辑ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '专辑名',
  `artist_id` bigint(0) NOT NULL COMMENT '歌手ID',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面URL',
  `release_date` date NULL DEFAULT NULL COMMENT '发行日期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '专辑描述',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态: 1-正常 0-下架',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_artist_id`(`artist_id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  CONSTRAINT `fk_album_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专辑表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO `album` VALUES (1, '叶惠美', 1, '/uploads/images/960fc146-4edc-450d-b629-b84e58837d2a.webp', '2003-07-31', '周杰伦第四张录音室专辑，收录《晴天》《以父之名》等经典曲目。', 1, '2026-03-09 18:54:18', '2026-03-09 21:41:27');
INSERT INTO `album` VALUES (2, '七里香', 1, '/uploads/images/f2172f39-6bbe-4036-aee0-9ebeaf9843e0.webp', '2004-08-03', '周杰伦第五张录音室专辑，主打歌《七里香》传唱度极高。', 1, '2026-03-09 18:54:18', '2026-03-09 21:41:32');
INSERT INTO `album` VALUES (3, '十一月的萧邦', 1, '/uploads/images/e70a3bbc-8949-4750-b526-a9c809d9d45f.webp', '2005-11-01', '周杰伦第六张录音室专辑，融合古典与流行元素。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:48');
INSERT INTO `album` VALUES (4, '江南', 2, '/uploads/images/dc1b6c33-090d-4566-8239-b2664b69e810.webp', '2004-06-04', '林俊杰第二张录音室专辑，主打歌《江南》成为经典。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:53');
INSERT INTO `album` VALUES (5, '曹操', 2, '/uploads/images/3344add7-7dbd-4d15-bf47-f13a306dc250.webp', '2006-02-13', '林俊杰第四张录音室专辑，展现多样化的音乐风格。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:29');
INSERT INTO `album` VALUES (6, '新的心跳', 3, '/uploads/images/2ca22caf-6fcb-4b3a-8217-ff165b4cb7d0.webp', '2015-11-06', '邓紫棋第五张录音室专辑，收录《泡沫》《喜欢你》等热门歌曲。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:58');
INSERT INTO `album` VALUES (7, '1989', 6, '/uploads/images/9d2c9241-929c-442a-9a3d-854d773197b6.webp', '2014-10-27', 'Taylor Swift第五张录音室专辑，标志着她从乡村音乐转向流行音乐。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:15');
INSERT INTO `album` VALUES (8, 'Lover', 6, '/uploads/images/55ff2c26-990e-45d2-b3b9-55fb17648bb1.webp', '2019-08-23', 'Taylor Swift第七张录音室专辑，以爱情为主题。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:08');
INSERT INTO `album` VALUES (9, '人生海海', 11, '/uploads/images/102c5f6e-05af-4109-abfd-226298f8d790.webp', '2001-07-06', '五月天第四张录音室专辑，收录《倔强》等励志歌曲。', 1, '2026-03-09 18:54:18', '2026-03-09 21:43:04');

-- ----------------------------
-- Table structure for artist
-- ----------------------------
DROP TABLE IF EXISTS `artist`;
CREATE TABLE `artist`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '歌手ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '歌手名',
  `name_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '外文名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `genre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流派: 流行/摇滚/民谣/说唱/电子/古典',
  `language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '语种: 华语/欧美/日韩/其他',
  `initial` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '首字母(A-Z)',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '个人简介',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态: 1-正常 0-下架',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_genre`(`genre`) USING BTREE,
  INDEX `idx_language`(`language`) USING BTREE,
  INDEX `idx_initial`(`initial`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌手表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of artist
-- ----------------------------
INSERT INTO `artist` VALUES (1, '周杰伦', 'Jay Chou', '/uploads/images/9fd7e6ca-336d-4ead-8dcc-9a78db683e05.webp', '流行', '华语', 'Z', '华语流行音乐天王，创作型歌手，擅长融合中国风与现代流行音乐。代表作品有《晴天》《稻香》《青花瓷》等。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (2, '林俊杰', 'JJ Lin', '/uploads/images/f8b65404-6d39-43ce-a4d2-e56506f8270e.webp', '流行', '华语', 'L', '新加坡华语流行音乐创作歌手，音乐制作人。以细腻的情感表达和精湛的唱功著称。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (3, '邓紫棋', 'G.E.M.', '/uploads/images/ec8bf64c-c29f-4340-8631-57433fa4ca51.webp', '流行', '华语', 'D', '香港创作型女歌手，拥有强大的唱功和创作才华，被誉为\"铁肺天后\"。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (4, '薛之谦', 'Joker Xue', '/uploads/images/912e5122-be25-4891-975e-e1b73a095d01.webp', '流行', '华语', 'X', '中国内地流行男歌手、音乐制作人，以幽默风趣的性格和深情的歌曲著称。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (5, '毛不易', 'Mao Buyi', '/uploads/images/cf1d2702-596c-435c-9fbf-fdbda71c7651.webp', '民谣', '华语', 'M', '中国内地民谣歌手，以朴实真挚的歌词和温暖的嗓音打动人心。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (6, 'Taylor Swift', 'Taylor Swift', '/uploads/images/fce21879-9102-42f1-8f15-547c91156eb4.webp', '流行', '欧美', 'T', '美国创作型女歌手，多次获得格莱美奖，以叙事性歌词和多变的音乐风格著称。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (7, 'Ed Sheeran', 'Ed Sheeran', '/uploads/images/ab15a3c5-0777-4aa8-9f27-88c07e0aff0c.webp', '流行', '欧美', 'E', '英国创作型男歌手，以吉他弹唱和真挚的情感表达闻名全球。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (8, 'Adele', 'Adele', '/uploads/images/b2f9f1c0-de46-4fbd-bf21-ed63454d4664.webp', '流行', '欧美', 'A', '英国灵魂乐女歌手，拥有极具辨识度的嗓音和强大的情感表现力。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (9, '米津玄师', 'Kenshi Yonezu', '/uploads/images/6f8e009e-56b9-42b0-980e-3f11f45eb896.webp', '流行', '日韩', 'M', '日本创作型歌手、音乐制作人，以独特的音乐风格和深刻的歌词内涵著称。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (10, 'IU', 'IU', '/uploads/images/9839b6f5-6d25-423f-94c1-3fe6d64cc29a.webp', '流行', '日韩', 'I', '韩国女歌手、演员，被誉为\"国民妹妹\"，以清新的形象和多样的音乐风格受到喜爱。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (11, '五月天', 'Mayday', '/uploads/images/01280035-f414-4ad2-878c-2321c6c7ef4e.webp', '摇滚', '华语', 'W', '台湾摇滚乐团，华语摇滚代表乐队之一，以励志正能量的歌曲著称。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');
INSERT INTO `artist` VALUES (12, 'Beyond', 'Beyond', '/uploads/images/786ae4fd-6b54-4619-a3c1-8bbfaa8d88d5.webp', '摇滚', '华语', 'B', '香港殿堂级摇滚乐队，代表作《海阔天空》《光辉岁月》等影响了几代人。', 1, '2026-03-09 18:54:18', '2026-03-09 18:54:18');

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `target_id` bigint(0) NOT NULL COMMENT '目标ID',
  `target_type` tinyint(0) NOT NULL COMMENT '目标类型: 1-歌曲 2-歌单',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_target`(`user_id`, `target_id`, `target_type`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_target`(`target_id`, `target_type`) USING BTREE,
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (1, 1, 1, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (2, 1, 4, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (3, 1, 12, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (4, 1, 1, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (5, 1, 5, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (6, 2, 19, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (7, 2, 21, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (8, 2, 2, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (9, 2, 4, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (10, 3, 14, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (11, 3, 15, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (12, 3, 3, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (13, 4, 16, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (14, 4, 17, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (15, 4, 2, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (16, 5, 1, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (17, 5, 20, 1, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (18, 5, 5, 2, '2026-03-09 18:54:18');
INSERT INTO `favorite` VALUES (19, 1, 2, 2, '2026-03-10 00:20:13');
INSERT INTO `favorite` VALUES (21, 1, 1, 3, '2026-03-10 00:21:14');

-- ----------------------------
-- Table structure for playlist
-- ----------------------------
DROP TABLE IF EXISTS `playlist`;
CREATE TABLE `playlist`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '歌单ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '歌单名',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面URL',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '描述',
  `creator_id` bigint(0) NOT NULL COMMENT '创建者ID',
  `creator_type` tinyint(0) NOT NULL COMMENT '创建者类型: 1-管理员 2-用户',
  `tags` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签(逗号分隔)',
  `play_count` bigint(0) NULL DEFAULT 0 COMMENT '播放次数',
  `collect_count` bigint(0) NULL DEFAULT 0 COMMENT '收藏次数',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态: 1-公开 0-私密',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_creator`(`creator_id`, `creator_type`) USING BTREE,
  INDEX `idx_play_count`(`play_count`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of playlist
-- ----------------------------
INSERT INTO `playlist` VALUES (1, '华语经典', '/uploads/images/c9589891-5bf7-4f68-bf40-da61c82ff1f3.jpg', '收录华语乐坛经典歌曲，跨越时代的音乐记忆。', 1, 1, '华语,经典,流行', 50000, 5000, 1, '2026-03-09 18:54:18', '2026-03-09 22:13:08');
INSERT INTO `playlist` VALUES (2, '欧美热门', '/uploads/images/fbf5dba2-27fa-4aab-b722-316cc5e54eb4.webp', '欧美流行音乐精选，感受国际音乐魅力。', 1, 1, '欧美,流行,热门', 35000, 3500, 1, '2026-03-09 18:54:18', '2026-03-09 22:13:19');
INSERT INTO `playlist` VALUES (3, '民谣时光', '/uploads/images/05b99e51-ff78-4ed9-a055-261af23b415e.webp', '用民谣记录生活，用音乐治愈心灵。', 1, 1, '民谣,治愈,文艺', 28000, 2800, 1, '2026-03-09 18:54:18', '2026-03-09 22:13:32');
INSERT INTO `playlist` VALUES (4, '摇滚精神', '/uploads/images/fd979d6d-2168-4abe-b59c-8376b1788ea3.webp', '摇滚不死，青春永驻，感受摇滚的力量。', 1, 1, '摇滚,激情,青春', 22000, 2200, 1, '2026-03-09 18:54:18', '2026-03-09 22:13:39');
INSERT INTO `playlist` VALUES (5, '深夜电台', '/uploads/images/6ebc59df-f5c9-4fde-9cbe-cd5df3fb70db.webp', '适合深夜聆听的温柔歌曲，陪你度过每个夜晚。', 1, 1, '深夜,治愈,温柔', 45000, 4500, 1, '2026-03-09 18:54:18', '2026-03-09 22:13:49');
INSERT INTO `playlist` VALUES (6, '我的最爱', '/uploads/images/c10ee9b5-6d0a-4d82-99d4-a8d1f75f4262.webp', '收藏我最喜欢的歌曲', 1, 2, '个人收藏', 100, 10, 1, '2026-03-09 18:54:18', '2026-03-09 22:13:56');
INSERT INTO `playlist` VALUES (7, '运动健身', '/uploads/images/be50050f-7e2a-4dc4-9ab6-e39c45ea14f1.webp', '适合运动时听的动感音乐', 2, 2, '运动,健身,动感', 500, 50, 1, '2026-03-09 18:54:18', '2026-03-09 22:14:02');
INSERT INTO `playlist` VALUES (8, '学习专注', '/uploads/images/a9fa4641-534c-44f5-9b5e-bb4b79c80277.webp', '学习时的背景音乐', 3, 2, '学习,专注,轻音乐', 300, 30, 1, '2026-03-09 18:54:18', '2026-03-09 22:14:16');

-- ----------------------------
-- Table structure for playlist_song
-- ----------------------------
DROP TABLE IF EXISTS `playlist_song`;
CREATE TABLE `playlist_song`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `playlist_id` bigint(0) NOT NULL COMMENT '歌单ID',
  `song_id` bigint(0) NOT NULL COMMENT '歌曲ID',
  `sort_order` int(0) NULL DEFAULT 0 COMMENT '排序序号',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_playlist_song`(`playlist_id`, `song_id`) USING BTREE,
  INDEX `idx_playlist_id`(`playlist_id`) USING BTREE,
  INDEX `idx_song_id`(`song_id`) USING BTREE,
  CONSTRAINT `fk_ps_playlist` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_ps_song` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌单-歌曲关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of playlist_song
-- ----------------------------
INSERT INTO `playlist_song` VALUES (1, 1, 1, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (2, 1, 2, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (3, 1, 3, 3, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (4, 1, 4, 4, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (5, 1, 6, 5, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (6, 1, 9, 6, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (7, 1, 12, 7, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (8, 1, 14, 8, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (9, 2, 16, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (10, 2, 17, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (11, 2, 18, 3, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (12, 2, 19, 4, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (13, 2, 20, 5, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (14, 3, 14, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (15, 3, 15, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (16, 3, 3, 3, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (17, 4, 21, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (18, 4, 22, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (19, 5, 1, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (20, 5, 8, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (21, 5, 9, 3, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (22, 5, 20, 4, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (23, 5, 22, 5, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (24, 6, 1, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (25, 6, 4, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (26, 6, 12, 3, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (27, 7, 19, 1, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (28, 7, 16, 2, '2026-03-09 18:54:18');
INSERT INTO `playlist_song` VALUES (29, 7, 21, 3, '2026-03-09 18:54:18');

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '歌曲ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '歌曲名',
  `artist_id` bigint(0) NOT NULL COMMENT '歌手ID',
  `album_id` bigint(0) NULL DEFAULT NULL COMMENT '专辑ID',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面URL',
  `audio_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '音频文件URL',
  `lyric_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌词文件URL',
  `duration` int(0) NULL DEFAULT NULL COMMENT '时长(秒)',
  `file_size` bigint(0) NULL DEFAULT NULL COMMENT '文件大小(字节)',
  `format` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '格式: MP3/FLAC',
  `play_count` bigint(0) NULL DEFAULT 0 COMMENT '播放次数',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态: 1-正常 0-下架',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_artist_id`(`artist_id`) USING BTREE,
  INDEX `idx_album_id`(`album_id`) USING BTREE,
  INDEX `idx_play_count`(`play_count`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  CONSTRAINT `fk_song_album` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_song_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌曲表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of song
-- ----------------------------
INSERT INTO `song` VALUES (1, '晴天', 1, 1, '/uploads/images/197c295f-7c4b-42a6-89a6-a4ede2ac86f7.webp', '/uploads/audio/4fd86697-8573-4cd5-8388-27a975f5dab6.mp3', '/uploads/lyrics/a6f496a5-6f83-4210-a3d1-0aaeba95a864.lrc', 267, 6442450, 'MP3', 15007, 1, '2026-03-09 18:54:18', '2026-03-09 23:34:26');
INSERT INTO `song` VALUES (2, '七里香', 1, 2, '/uploads/images/aa56bf25-e2b3-42a1-8a5d-e65a77aaabe7.webp', '/uploads/audio/54d526bb-c834-4aad-a22c-8dbfbb29b863.mp3', '/uploads/lyrics/35f2fda7-1502-4379-b487-59f2822172fe.lrc', 300, 7200000, 'MP3', 12008, 1, '2026-03-09 18:54:18', '2026-03-09 23:47:46');
INSERT INTO `song` VALUES (3, '稻香', 1, 2, '/uploads/images/f5ee86b9-4558-4cac-aa52-3749c917b03f.webp', '/uploads/audio/7d55efd4-4048-4952-be26-d99da1429e42.mp3', '/uploads/lyrics/d770987f-bbaa-4eaf-8502-f4f91e084022.lrc', 223, 5352000, 'MP3', 18002, 1, '2026-03-09 18:54:18', '2026-03-09 23:41:16');
INSERT INTO `song` VALUES (4, '青花瓷', 1, NULL, '/uploads/images/eb0bc5a8-3ee3-4f4c-8e71-5af18fa6d5d4.webp', '/uploads/audio/adde201e-e431-46e0-8901-8d3fdeb548ab.mp3', '/uploads/lyrics/6a4cf6f8-a694-4409-be93-1ef2b5fd28b7.lrc', 228, 5472000, 'MP3', 20000, 1, '2026-03-09 18:54:18', '2026-03-09 23:41:22');
INSERT INTO `song` VALUES (5, '夜曲', 1, 3, '/uploads/images/dbd95487-38e5-4861-8af7-759eed93aaad.webp', '/uploads/audio/68cbfb20-34cb-4c90-8755-ac66d2441f76.mp3', '/uploads/lyrics/038502f4-f430-4eb1-a163-92feb21696df.lrc', 213, 5112000, 'MP3', 9000, 1, '2026-03-09 18:54:18', '2026-03-09 23:41:11');
INSERT INTO `song` VALUES (6, '江南', 2, 4, '/uploads/images/9d1aa3ca-2fa1-4cb3-b59e-098bf4f29761.webp', 'https://example.com/audio/jiangnan.mp3', '/uploads/lyrics/6e75994d-e807-4fab-a81c-977525ea3164.lrc', 250, 6000000, 'MP3', 16000, 1, '2026-03-09 18:54:18', '2026-03-09 23:41:27');
INSERT INTO `song` VALUES (7, '曹操', 2, 5, '/uploads/images/cbbce35f-df58-492c-b818-7e3fea0acbb5.webp', 'https://example.com/audio/caocao.mp3', '/uploads/lyrics/20cdc824-bcc7-4636-891a-48179b78a639.lrc', 245, 5880000, 'MP3', 11000, 1, '2026-03-09 18:54:18', '2026-03-09 23:43:03');
INSERT INTO `song` VALUES (8, '修炼爱情', 2, NULL, '/uploads/images/6169f853-ac63-410c-b74e-74d16c5041c4.webp', 'https://example.com/audio/xiulianai.mp3', '/uploads/lyrics/b1233cdc-2a3a-4560-8332-4e6e27f816da.lrc', 268, 6432000, 'MP3', 13000, 1, '2026-03-09 18:54:18', '2026-03-09 23:42:55');
INSERT INTO `song` VALUES (9, '泡沫', 3, 6, '/uploads/images/e49429f3-67c7-4045-b074-6c34c6efa7db.webp', 'https://example.com/audio/paomo.mp3', '/uploads/lyrics/d0a2dbc2-b8b3-48b0-a966-4cf10b9039db.lrc', 243, 5832000, 'MP3', 14000, 1, '2026-03-09 18:54:18', '2026-03-09 23:40:57');
INSERT INTO `song` VALUES (10, '喜欢你', 3, 6, '/uploads/images/131fdf55-b10b-4c6d-a0ef-eed514845246.webp', 'https://example.com/audio/xihuanni.mp3', '/uploads/lyrics/c7dd908e-1d5c-4cb5-a055-81cb465cf539.lrc', 230, 5520000, 'MP3', 17000, 1, '2026-03-09 18:54:18', '2026-03-09 23:41:59');
INSERT INTO `song` VALUES (11, '光年之外', 3, NULL, '/uploads/images/163f0eb2-ecab-4ad6-980f-44a78e3f2e09.webp', 'https://example.com/audio/guangnian.mp3', '/uploads/lyrics/732b6f47-deec-4113-8f51-6aa060c35852.lrc', 255, 6120000, 'MP3', 19000, 1, '2026-03-09 18:54:18', '2026-03-09 23:41:04');
INSERT INTO `song` VALUES (12, '演员', 4, NULL, '/uploads/images/b862da21-c02a-48fb-8a5e-251099688f41.webp', 'https://example.com/audio/yanyuan.mp3', '/uploads/lyrics/edda609f-4c1e-4402-9e2e-a33aee81ad2c.lrc', 265, 6360000, 'MP3', 22000, 1, '2026-03-09 18:54:18', '2026-03-09 23:43:30');
INSERT INTO `song` VALUES (13, '丑八怪', 4, NULL, '/uploads/images/042744f7-9b2e-4d8a-aaa7-0d6bd829086d.webp', 'https://example.com/audio/choubaiguai.mp3', 'https://example.com/lyric/choubaiguai.lrc', 248, 5952000, 'MP3', 15000, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:18');
INSERT INTO `song` VALUES (14, '消愁', 5, NULL, '/uploads/images/43c94633-5a03-4ab0-a1a8-8031631208de.webp', 'https://example.com/audio/xiaochou.mp3', 'https://example.com/lyric/xiaochou.lrc', 293, 7032000, 'MP3', 21001, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:40');
INSERT INTO `song` VALUES (15, '像我这样的人', 5, NULL, '/uploads/images/5cc01f89-f6c0-4360-98b1-84e572668c5f.webp', 'https://example.com/audio/xiangwo.mp3', 'https://example.com/lyric/xiangwo.lrc', 275, 6600000, 'MP3', 16000, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:35');
INSERT INTO `song` VALUES (16, 'Shake It Off', 6, 7, '/uploads/images/8f672fe9-6c67-4903-bcf0-e532f0a9cdd2.webp', 'https://example.com/audio/shakeitoff.mp3', 'https://example.com/lyric/shakeitoff.lrc', 242, 5808000, 'MP3', 25001, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:44');
INSERT INTO `song` VALUES (17, 'Blank Space', 6, 7, '/uploads/images/79c5d3af-e4f6-4edc-bb45-a1c5ab9c6f03.webp', 'https://example.com/audio/blankspace.mp3', 'https://example.com/lyric/blankspace.lrc', 231, 5544000, 'MP3', 23000, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:49');
INSERT INTO `song` VALUES (18, 'Lover', 6, 8, '/uploads/images/6b989f95-06e9-4a84-83f4-3ece6a0f0aa7.webp', 'https://example.com/audio/lover.mp3', 'https://example.com/lyric/lover.lrc', 221, 5304000, 'MP3', 18000, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:53');
INSERT INTO `song` VALUES (19, 'Shape of You', 7, NULL, '/uploads/images/01159330-9273-488e-8c70-5ee7f9826559.webp', 'https://example.com/audio/shapeofyou.mp3', 'https://example.com/lyric/shapeofyou.lrc', 234, 5616000, 'MP3', 30000, 1, '2026-03-09 18:54:18', '2026-03-09 21:45:59');
INSERT INTO `song` VALUES (20, 'Perfect', 7, NULL, '/uploads/images/7d62ed53-6738-4172-82df-31b6d2c11077.webp', 'https://example.com/audio/perfect.mp3', 'https://example.com/lyric/perfect.lrc', 263, 6312000, 'MP3', 28000, 1, '2026-03-09 18:54:18', '2026-03-09 21:46:03');
INSERT INTO `song` VALUES (21, '倔强', 11, 9, '/uploads/images/32a5497a-a9d9-40cc-a04a-8392bc7fc199.webp', 'https://example.com/audio/juejiang.mp3', 'https://example.com/lyric/juejiang.lrc', 256, 6144000, 'MP3', 24000, 1, '2026-03-09 18:54:18', '2026-03-09 21:46:13');
INSERT INTO `song` VALUES (22, '突然好想你', 11, NULL, '/uploads/images/01096d18-df6f-44b3-aa53-a3c1f2d7424e.webp', 'https://example.com/audio/turanhao.mp3', 'https://example.com/lyric/turanhao.lrc', 258, 6192000, 'MP3', 20000, 1, '2026-03-09 18:54:18', '2026-03-09 21:46:18');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码(BCrypt加密)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint(0) NULL DEFAULT 0 COMMENT '性别: 0-未知 1-男 2-女',
  `birthday` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生日 (格式: YYYY-MM-DD)',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `signature` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个性签名',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  INDEX `idx_phone`(`phone`) USING BTREE,
  INDEX `idx_email`(`email`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'user001', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '音乐爱好者', 1, '2026-03-10', '13800138001', 'user001@example.com', '/uploads/images/a9914127-e894-4752-9192-91b20e384ddb.jpg', '1111111', 1, '2026-03-09 18:54:18', '2026-03-09 22:41:27');
INSERT INTO `user` VALUES (2, 'user002', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '摇滚青年', 0, NULL, '13800138002', 'user002@example.com', 'https://via.placeholder.com/150', '摇滚不死', 1, '2026-03-09 18:54:18', '2026-03-09 20:46:47');
INSERT INTO `user` VALUES (3, 'user003', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '民谣诗人', 0, NULL, '13800138003', 'user003@example.com', 'https://via.placeholder.com/150', '用音乐记录生活', 1, '2026-03-09 18:54:18', '2026-03-09 20:46:49');
INSERT INTO `user` VALUES (4, 'user004', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '电音狂热', 0, NULL, '13800138004', 'user004@example.com', 'https://via.placeholder.com/150', 'EDM is life', 1, '2026-03-09 18:54:18', '2026-03-09 20:46:50');
INSERT INTO `user` VALUES (5, 'user005', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '古典乐迷', 0, NULL, '13800138005', 'user005@example.com', 'https://via.placeholder.com/150', '沉浸在古典音乐的世界', 1, '2026-03-09 18:54:18', '2026-03-09 20:46:51');
INSERT INTO `user` VALUES (6, 'test', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', 'nick', 0, NULL, '18873627182', '1663727738@qq.com', NULL, NULL, 1, '2026-03-09 20:36:41', '2026-03-09 20:46:35');
INSERT INTO `user` VALUES (7, 'test1', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', 'nick', 0, NULL, '18273628172', '182737281@qq.com', NULL, NULL, 1, '2026-03-09 20:37:15', '2026-03-09 20:46:34');
INSERT INTO `user` VALUES (8, 'test3', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', 'test3', 0, NULL, '18273728182', '1283238@qq.com', NULL, NULL, 1, '2026-03-09 20:40:10', '2026-03-09 20:46:33');
INSERT INTO `user` VALUES (9, 'test11', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '1111', 0, NULL, '1111111111', '11111@qq.com', NULL, NULL, 1, '2026-03-09 20:44:34', '2026-03-09 20:46:32');
INSERT INTO `user` VALUES (10, 'test12', '$2a$10$.WLTf5eJzDvu3klFxUL5Ce7aqNyEW4S9cwsqaWoz/efigTWttdrDS', '1111', 0, NULL, '11111111121', '111211@qq.com', NULL, NULL, 1, '2026-03-09 20:46:22', '2026-03-09 20:46:22');

SET FOREIGN_KEY_CHECKS = 1;
