# 在线音乐播放器系统 - 后端

## 项目说明

这是在线音乐播放器系统的后端项目，基于 Spring Boot 2.7 + MyBatis-Plus 开发。

## 技术栈

- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3.1
- MySQL 8.0
- JWT 0.11.5
- Knife4j 3.0.3 (Swagger UI)

## 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

## 快速开始

### 1. 初始化数据库

```bash
# 进入项目根目录
cd music-online

# 执行数据库脚本
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_test_data.sql
```

### 2. 修改配置

编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/music_online?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: your_password
```

### 3. 编译项目

```bash
cd backend
mvn clean package
```

### 4. 运行项目

```bash
# 方式1: 使用 Maven
mvn spring-boot:run

# 方式2: 运行 JAR 包
java -jar target/music-online-1.0.0.jar
```

### 5. 访问接口文档

启动成功后，访问: http://localhost:8080/api/doc.html

## 测试账号

### 管理员账号
- 用户名: `admin`
- 密码: `admin123`

### 用户账号
- 用户名: `user001`
- 密码: `123456`
- 手机号: `13800138001`

## 已实现的接口

### 认证接口
- `POST /api/admin/login` - 管理员登录
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录

### 管理员接口
- `GET /api/admin/statistics` - 获取统计数据
- `GET /api/admin/artists` - 分页查询歌手列表
- `GET /api/admin/artists/{id}` - 获取歌手详情
- `POST /api/admin/artists` - 创建歌手
- `PUT /api/admin/artists/{id}` - 更新歌手
- `DELETE /api/admin/artists/{id}` - 删除歌手

## 项目结构

```
backend/
├── src/main/java/com/music/online/
│   ├── MusicOnlineApplication.java    # 启动类
│   ├── common/                        # 公共类
│   │   ├── Result.java                # 统一响应结果
│   │   └── BusinessException.java     # 业务异常
│   ├── config/                        # 配置类
│   │   ├── GlobalExceptionHandler.java # 全局异常处理
│   │   ├── WebMvcConfig.java          # Web MVC 配置
│   │   └── SwaggerConfig.java         # Swagger 配置
│   ├── controller/                    # 控制器
│   │   ├── AdminController.java       # 管理员控制器
│   │   ├── UserController.java        # 用户控制器
│   │   └── ArtistController.java      # 歌手控制器
│   ├── dto/                           # 数据传输对象
│   │   ├── AdminLoginDTO.java         # 管理员登录请求
│   │   ├── UserRegisterDTO.java       # 用户注册请求
│   │   └── UserLoginDTO.java          # 用户登录请求
│   ├── entity/                        # 实体类
│   │   ├── Admin.java                 # 管理员实体
│   │   ├── User.java                  # 用户实体
│   │   └── Artist.java                # 歌手实体
│   ├── mapper/                        # Mapper 接口
│   │   ├── AdminMapper.java           # 管理员 Mapper
│   │   ├── UserMapper.java            # 用户 Mapper
│   │   └── ArtistMapper.java          # 歌手 Mapper
│   ├── service/                       # 服务层
│   │   ├── AdminService.java          # 管理员服务
│   │   ├── UserService.java           # 用户服务
│   │   └── ArtistService.java         # 歌手服务
│   ├── util/                          # 工具类
│   │   ├── JwtUtil.java               # JWT 工具类
│   │   └── PasswordUtil.java          # 密码工具类
│   └── vo/                            # 视图对象
│       └── LoginVO.java               # 登录响应
└── src/main/resources/
    └── application.yml                # 配置文件
```

## 开发规范

1. 所有接口返回统一的 `Result<T>` 格式
2. 使用 `@Validated` 进行参数校验
3. 使用 `BusinessException` 抛出业务异常
4. 密码使用 BCrypt 加密
5. 使用 JWT 进行身份认证
6. 所有实体类使用 Lombok 简化代码

## 下一步开发

- [ ] 实现文件上传功能
- [ ] 实现歌曲管理接口
- [ ] 实现专辑管理接口
- [ ] 实现歌单管理接口
- [ ] 实现搜索功能
- [ ] 实现收藏功能
- [ ] 完善统计功能
- [ ] 添加单元测试

## 联系方式

如有问题，请联系开发者。
