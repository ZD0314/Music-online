# 在线音乐播放器系统

基于 Spring Boot + Vue 3 的在线音乐播放器系统，支持音乐播放、歌单管理、用户个人中心等功能。

## 技术栈

### 后端
- Java 8+
- Spring Boot 2.7
- MyBatis-Plus
- MySQL 8.0
- JWT 认证
- Swagger/Knife4j API 文档

### 前端
- Vue 3
- TypeScript
- Element Plus
- Vite
- Pinia 状态管理
- Vue Router

## 功能特性

### 用户端
- 用户注册/登录
- 音乐播放器（支持单曲循环、列表循环、随机播放）
- 歌词同步显示
- 歌手浏览（按首字母/语种/流派筛选）
- 专辑浏览
- 歌单浏览与收藏
- 综合搜索（歌曲/歌手/专辑）
- 个人中心（信息修改、收藏管理、歌单创建）

### 管理员端
- 数据统计
- 用户管理
- 歌手管理
- 专辑管理
- 歌曲管理
- 歌单管理
- 文件上传（图片/音频/歌词）

## 快速开始

### 环境要求

- JDK 8+
- Node.js 16+
- MySQL 8.0
- Maven 3.6+

### 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE music_online CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
导入sql文件
```

### 后端启动

1. 修改数据库配置（如需要）：
```yaml
# backend/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/music_online?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: 123456（记得修改密码）
```

2. 启动后端：
```bash
cd backend
mvn spring-boot:run

# 或使用快捷脚本
restart-backend.bat
```

3. 访问 API 文档：
   - Knife4j: http://localhost:8080/api/doc.html
   - Swagger UI: http://localhost:8080/api/swagger-ui.html

### 前端启动

1. 安装依赖：
```bash
cd frontend
npm install
```

2. 启动开发服务器：
```bash
npm run dev
```

3. 访问应用：
   - 用户端: http://localhost:3000
   - 管理员端: http://localhost:3000/admin/login

### 默认账号

**管理员账号：**
- 用户名: admin
- 密码: 123456

**测试用户账号：**
- 用户名: user001
- 密码: 123456

下载歌曲网址：
https://www.qqmp3.vip/
下载歌词网址：
https://www.lequhou.com/geci/724774.htm
## 项目结构

```
music-online/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/music/online/
│   │   │   │       ├── config/      # 配置类
│   │   │   │       ├── controller/  # 控制器
│   │   │   │       ├── dto/         # 数据传输对象
│   │   │   │       ├── entity/      # 实体类
│   │   │   │       ├── mapper/      # MyBatis Mapper
│   │   │   │       ├── service/     # 业务逻辑
│   │   │   │       ├── util/        # 工具类
│   │   │   │       └── vo/          # 视图对象
│   │   │   └── resources/
│   │   │       ├── application.yml  # 配置文件
│   │   │       └── mapper/          # MyBatis XML
│   │   └── test/
│   └── pom.xml
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── api/            # API 接口
│   │   ├── assets/         # 静态资源
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── stores/         # Pinia 状态管理
│   │   ├── types/          # TypeScript 类型
│   │   ├── utils/          # 工具函数
│   │   ├── views/          # 页面组件
│   │   │   ├── admin/      # 管理员页面
│   │   │   └── user/       # 用户页面
│   │   ├── App.vue
│   │   └── main.ts
│   ├── package.json
│   └── vite.config.ts
├── sql/                    # SQL 脚本
│   ├── schema.sql          # 表结构
│   └── data.sql            # 测试数据
├── doc/                    # 项目文档
├── init-database.bat       # 数据库初始化脚本
└── restart-backend.bat     # 后端重启脚本
```

## API 接口说明

### 用户端接口

**认证接口：**
- POST `/api/auth/register` - 用户注册
- POST `/api/auth/login` - 用户登录

**音乐接口：**
- GET `/api/artists` - 获取歌手列表
- GET `/api/artists/{id}` - 获取歌手详情
- GET `/api/albums` - 获取专辑列表
- GET `/api/albums/{id}` - 获取专辑详情
- GET `/api/playlists` - 获取歌单列表
- GET `/api/playlists/{id}` - 获取歌单详情
- GET `/api/songs/{id}` - 获取歌曲详情
- GET `/api/songs/{id}/lyric` - 获取歌词
- POST `/api/songs/{id}/play` - 增加播放次数

**搜索接口：**
- GET `/api/search?keyword={keyword}` - 综合搜索

**收藏接口：**
- GET `/api/favorites` - 获取收藏列表
- POST `/api/favorites` - 添加收藏
- DELETE `/api/favorites/{id}` - 取消收藏
- GET `/api/favorites/check` - 检查是否已收藏

**个人中心：**
- GET `/api/user/info` - 获取用户信息
- PUT `/api/user/info` - 更新用户信息
- PUT `/api/user/password` - 修改密码
- GET `/api/user/playlists` - 获取个人歌单
- POST `/api/user/playlists` - 创建歌单
- PUT `/api/user/playlists/{id}` - 更新歌单
- DELETE `/api/user/playlists/{id}` - 删除歌单

### 管理员接口

**认证接口：**
- POST `/api/admin/login` - 管理员登录

**数据统计：**
- GET `/api/admin/statistics` - 获取统计数据

**资源管理：**
- GET `/api/admin/artists` - 歌手列表
- POST `/api/admin/artists` - 创建歌手
- PUT `/api/admin/artists/{id}` - 更新歌手
- DELETE `/api/admin/artists/{id}` - 删除歌手
- GET `/api/admin/albums` - 专辑列表
- POST `/api/admin/albums` - 创建专辑
- PUT `/api/admin/albums/{id}` - 更新专辑
- DELETE `/api/admin/albums/{id}` - 删除专辑
- GET `/api/admin/songs` - 歌曲列表
- POST `/api/admin/songs` - 创建歌曲
- PUT `/api/admin/songs/{id}` - 更新歌曲
- DELETE `/api/admin/songs/{id}` - 删除歌曲
- GET `/api/admin/playlists` - 歌单列表
- POST `/api/admin/playlists` - 创建歌单
- PUT `/api/admin/playlists/{id}` - 更新歌单
- DELETE `/api/admin/playlists/{id}` - 删除歌单

**文件上传：**
- POST `/api/admin/upload/image` - 上传图片
- POST `/api/admin/upload/audio` - 上传音频
- POST `/api/admin/upload/lyric` - 上传歌词

## 开发说明

### 后端开发

1. 使用 MyBatis-Plus 进行数据库操作
2. 使用 JWT 进行身份认证
3. 统一异常处理和响应格式
4. Swagger/Knife4j 自动生成 API 文档

### 前端开发

1. 使用 Vue 3 Composition API
2. TypeScript 类型检查
3. Pinia 状态管理
4. Vite 代理解决跨域问题
5. Element Plus UI 组件库

### 文件上传

支持的文件类型：
- 图片：JPG, PNG（歌手头像 ≤1MB，歌单封面 ≤2MB）
- 音频：MP3, FLAC（≤50MB）
- 歌词：LRC（≤1MB）

上传路径：`uploads/` 目录

## 常见问题

### 1. 后端启动失败

- 检查 MySQL 是否启动
- 检查数据库配置是否正确
- 检查端口 8080 是否被占用

### 2. 前端无法访问后端

- 检查后端是否正常启动
- 检查 Vite 代理配置
- 清除浏览器缓存

### 3. 文件上传失败

- 检查 `uploads/` 目录是否存在
- 检查文件大小是否超限
- 检查文件格式是否支持

### 4. 跨域问题

- 后端已配置 CORS 过滤器
- 前端使用 Vite 代理
- 确保 `baseURL` 配置正确

## 许可证

MIT License

## 联系方式

如有问题，请提交 Issue 或 Pull Request。
