# 数据库使用说明

## 1. 数据库初始化

### 1.1 执行顺序

按照以下顺序执行 SQL 脚本：

```bash
# 1. 创建数据库和表结构
mysql -u root -p < sql/01_schema.sql

# 2. 导入测试数据
mysql -u root -p < sql/02_test_data.sql
```

### 1.2 验证安装

```sql
-- 连接数据库
USE music_online;

-- 查看所有表
SHOW TABLES;

-- 查看数据统计
SELECT 'admin' as table_name, COUNT(*) as count FROM admin
UNION ALL
SELECT 'user', COUNT(*) FROM user
UNION ALL
SELECT 'artist', COUNT(*) FROM artist
UNION ALL
SELECT 'album', COUNT(*) FROM album
UNION ALL
SELECT 'song', COUNT(*) FROM song
UNION ALL
SELECT 'playlist', COUNT(*) FROM playlist
UNION ALL
SELECT 'playlist_song', COUNT(*) FROM playlist_song
UNION ALL
SELECT 'favorite', COUNT(*) FROM favorite;
```

预期结果：
- admin: 2
- user: 5
- artist: 12
- album: 9
- song: 22
- playlist: 8
- playlist_song: 28
- favorite: 18

## 2. 测试账号

### 2.1 管理员账号

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | admin123 | 系统管理员 |
| admin2 | admin123 | 运营管理员 |

### 2.2 用户账号

| 用户名 | 密码 | 手机号 | 邮箱 |
|--------|------|--------|------|
| user001 | 123456 | 13800138001 | user001@example.com |
| user002 | 123456 | 13800138002 | user002@example.com |
| user003 | 123456 | 13800138003 | user003@example.com |
| user004 | 123456 | 13800138004 | user004@example.com |
| user005 | 123456 | 13800138005 | user005@example.com |

## 3. 测试数据说明

### 3.1 歌手数据

包含 12 位歌手：
- **华语**: 周杰伦、林俊杰、邓紫棋、薛之谦、毛不易、五月天、Beyond
- **欧美**: Taylor Swift、Ed Sheeran、Adele
- **日韩**: 米津玄师、IU

### 3.2 歌曲数据

包含 22 首歌曲，涵盖：
- 流行、民谣、摇滚等多种风格
- 华语、欧美、日韩等不同语种
- 播放次数从 9000 到 30000 不等

### 3.3 歌单数据

包含 8 个歌单：
- **官方歌单** (5个): 华语经典、欧美热门、民谣时光、摇滚精神、深夜电台
- **用户歌单** (3个): 我的最爱、运动健身、学习专注

### 3.4 收藏数据

5 个用户共有 18 条收藏记录，包括歌曲收藏和歌单收藏。

## 4. 常用查询示例

### 4.1 查询歌手及其歌曲数量

```sql
SELECT
    a.id,
    a.name,
    a.genre,
    a.language,
    COUNT(s.id) as song_count
FROM artist a
LEFT JOIN song s ON a.id = s.artist_id
GROUP BY a.id
ORDER BY song_count DESC;
```

### 4.2 查询热门歌曲 TOP 10

```sql
SELECT
    s.id,
    s.name as song_name,
    a.name as artist_name,
    s.play_count
FROM song s
JOIN artist a ON s.artist_id = a.id
ORDER BY s.play_count DESC
LIMIT 10;
```

### 4.3 查询歌单及其歌曲数量

```sql
SELECT
    p.id,
    p.name,
    p.creator_type,
    COUNT(ps.song_id) as song_count,
    p.play_count,
    p.collect_count
FROM playlist p
LEFT JOIN playlist_song ps ON p.id = ps.playlist_id
GROUP BY p.id
ORDER BY p.play_count DESC;
```

### 4.4 查询用户收藏的歌曲

```sql
SELECT
    u.nickname,
    s.name as song_name,
    a.name as artist_name,
    f.create_time
FROM favorite f
JOIN user u ON f.user_id = u.id
JOIN song s ON f.target_id = s.id AND f.target_type = 1
JOIN artist a ON s.artist_id = a.id
WHERE u.id = 1
ORDER BY f.create_time DESC;
```

### 4.5 查询歌单详情（包含歌曲列表）

```sql
SELECT
    p.id as playlist_id,
    p.name as playlist_name,
    s.id as song_id,
    s.name as song_name,
    a.name as artist_name,
    s.duration,
    ps.sort_order
FROM playlist p
JOIN playlist_song ps ON p.id = ps.playlist_id
JOIN song s ON ps.song_id = s.id
JOIN artist a ON s.artist_id = a.id
WHERE p.id = 1
ORDER BY ps.sort_order;
```

## 5. 数据维护

### 5.1 清空所有数据（保留表结构）

```sql
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE favorite;
TRUNCATE TABLE playlist_song;
TRUNCATE TABLE playlist;
TRUNCATE TABLE song;
TRUNCATE TABLE album;
TRUNCATE TABLE artist;
TRUNCATE TABLE user;
TRUNCATE TABLE admin;

SET FOREIGN_KEY_CHECKS = 1;
```

### 5.2 重置自增ID

```sql
ALTER TABLE admin AUTO_INCREMENT = 1;
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE artist AUTO_INCREMENT = 1;
ALTER TABLE album AUTO_INCREMENT = 1;
ALTER TABLE song AUTO_INCREMENT = 1;
ALTER TABLE playlist AUTO_INCREMENT = 1;
ALTER TABLE playlist_song AUTO_INCREMENT = 1;
ALTER TABLE favorite AUTO_INCREMENT = 1;
```

### 5.3 备份数据库

```bash
# 备份整个数据库
mysqldump -u root -p music_online > backup_$(date +%Y%m%d).sql

# 只备份表结构
mysqldump -u root -p --no-data music_online > schema_backup.sql

# 只备份数据
mysqldump -u root -p --no-create-info music_online > data_backup.sql
```

### 5.4 恢复数据库

```bash
# 恢复数据库
mysql -u root -p music_online < backup_20260309.sql
```

## 6. 性能优化建议

### 6.1 已创建的索引

- `user`: username, phone, email, status
- `artist`: name, genre, language, initial
- `song`: name, artist_id, album_id, play_count
- `playlist`: name, creator, play_count
- `favorite`: user_id, target

### 6.2 查询优化建议

1. **分页查询**: 使用 LIMIT 和 OFFSET
2. **避免 SELECT ***: 只查询需要的字段
3. **使用索引**: 在 WHERE、ORDER BY、JOIN 字段上建立索引
4. **避免 N+1 查询**: 使用 JOIN 代替多次查询

### 6.3 监控慢查询

```sql
-- 开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;

-- 查看慢查询
SHOW VARIABLES LIKE 'slow_query%';
```

## 7. 注意事项

1. **密码加密**: 测试数据中的密码已使用 BCrypt 加密，实际开发中需要使用相同的加密方式
2. **外键约束**: 删除数据时注意外键约束，建议使用级联删除或先删除关联数据
3. **文件URL**: 测试数据中的文件 URL 是占位符，实际使用时需要替换为真实的文件路径
4. **数据一致性**: 修改数据时注意维护计数字段（如 play_count、collect_count）的一致性
5. **字符集**: 数据库使用 utf8mb4 字符集，支持 emoji 等特殊字符

## 8. 故障排查

### 8.1 外键约束错误

```sql
-- 临时禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 执行操作...

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;
```

### 8.2 字符集问题

```sql
-- 查看数据库字符集
SHOW CREATE DATABASE music_online;

-- 修改表字符集
ALTER TABLE table_name CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 8.3 连接问题

```bash
# 检查 MySQL 服务状态
systemctl status mysql  # Linux
net start mysql         # Windows

# 测试连接
mysql -u root -p -h localhost -P 3306
```
