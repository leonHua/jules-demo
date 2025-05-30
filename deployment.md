# 系统详细部署文档 (System Deployment Document)

## 1. 环境准备 (Prerequisites)
在部署面试管理系统之前，请确保您的环境中已安装并配置好以下软件：

*   **Java Development Kit (JDK):** 版本 1.8 或更高版本。
*   **Maven:** 版本 3.x 或更高版本 (用于构建后端项目)。
*   **MySQL:** 版本 5.7 或更高版本。
*   **Web 服务器 (可选):** 用于托管前端静态文件，例如 Nginx, Apache。对于基本使用或开发环境，可以直接在浏览器中打开 HTML 文件。

## 2. 后端部署 (Backend Deployment)

### 2.1. 数据库设置 (Database Setup)
1.  **启动 MySQL 服务:** 确保您的 MySQL 数据库服务正在运行。
2.  **创建数据库:**
    *   连接到您的 MySQL 实例。
    *   执行以下 SQL 命令创建名为 `interview_db` 的数据库，并设置字符集：
        ```sql
        CREATE DATABASE interview_db 
        CHARACTER SET utf8mb4 
        COLLATE utf8mb4_unicode_ci;
        ```
        The application is configured to automatically create the necessary tables (`interview_appointment`, `interview_evaluation`) on startup by executing the `schema.sql` file located in `interview-backend/src/main/resources/`. This is enabled by `spring.datasource.initialization-mode=always` in the `application.properties` file. Additionally, if a `data.sql` file is present in the same directory, it will be executed after `schema.sql` to populate the database with initial/test data. This project includes a `data.sql` file with 30 sample records for each table.
3.  **配置数据库连接:**
    *   打开后端项目中的配置文件: `interview-backend/src/main/resources/application.properties`。
    *   修改以下属性以匹配您的 MySQL 设置：
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/interview_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
        spring.datasource.username=your_mysql_username # 替换为您的 MySQL 用户名
        spring.datasource.password=your_mysql_password # 替换为您的 MySQL 密码
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        ```
        **注意:**
        *   将 `your_mysql_username` 和 `your_mysql_password` 替换为您的实际 MySQL 凭据。
        *   如果 MySQL 不在本地主机 (`localhost`) 或端口不是 `3306`，请相应修改 `spring.datasource.url`。

### 2.2. 构建项目 (Build Project)
1.  打开命令行/终端。
2.  导航到后端项目的根目录: `cd path/to/your/interview-backend`。
3.  执行 Maven 构建命令:
    ```bash
    mvn clean package
    ```
    此命令会编译代码、运行测试（如果有）并将项目打包成一个可执行的 `.jar` 文件。
4.  构建成功后，您将在 `interview-backend/target/` 目录下找到该文件，例如 `interview-backend-0.0.1-SNAPSHOT.jar`。

### 2.3. 运行后端 (Run Backend)
1.  在命令行/终端中，确保您仍位于 `interview-backend` 目录下，或者能够访问到 `target` 目录中的 `.jar` 文件。
2.  执行以下命令启动后端服务:
    ```bash
    java -jar target/interview-backend-0.0.1-SNAPSHOT.jar
    ```
3.  如果一切配置正确，Spring Boot 应用将会启动。您将在控制台看到日志输出，表明服务已在默认端口 `8080` 上运行。
    *   后端 API 服务地址: `http://localhost:8080`

## 3. 前端部署 (Frontend Deployment)

### 3.1. 获取 Layui (Get Layui)
1.  **检查 Layui 文件:**
    *   导航到 `interview-frontend/layui/` 目录。
    *   如果此目录为空，或者包含一个名为 `MANUAL_DOWNLOAD_REQUIRED.txt` (或类似) 的占位符文件，您需要手动下载 Layui。
2.  **下载 Layui:**
    *   访问 Layui 官方网站 ([https://www.layui.com/](https://www.layui.com/)) 或其 Gitee 发布页面 ([https://gitee.com/sentsin/layui/releases](https://gitee.com/sentsin/layui/releases))。
    *   下载最新稳定版本的 Layui (通常是一个 `.zip` 或 `.tar.gz` 文件)。
3.  **解压 Layui:**
    *   将下载的压缩文件解压。
    *   将其中的 `layui` 核心文件（通常是一个名为 `layui` 的目录，包含 `css`, `font`, `images`, `layui.js` 等子目录和文件）复制到 `interview-frontend/layui/` 目录下。
    *   确保 `interview-frontend/layui/layui.js` 和 `interview-frontend/layui/css/layui.css` 路径有效。

### 3.2. 运行前端 (Run Frontend)

#### 选项 1: 直接在浏览器中打开 (Simple - For Development/Basic Use)
1.  导航到 `interview-frontend` 目录。
2.  直接在您的现代 Web 浏览器 (如 Chrome, Firefox, Edge) 中打开 `index.html` 文件。
    *   例如，文件路径可能类似于: `file:///path/to/your/interview-frontend/index.html`
    *   **注意:** 某些浏览器可能会对通过 `file:///` 协议加载的本地 AJAX 请求施加安全限制。如果遇到 API 调用问题，建议使用选项 2。

#### 选项 2: 使用 Web 服务器 (Recommended - For Stable Use & Production)
1.  选择一个 Web 服务器，如 Nginx (推荐) 或 Apache HTTP Server。
2.  将 `interview-frontend` 目录的全部内容部署到您的 Web 服务器的静态文件托管根目录。
3.  配置 Web 服务器：
    *   确保 `index.html` 是默认的索引文件。
    *   (可选，但推荐) 配置一个反向代理，将前端发往 `/api` 的请求转发到后端服务 `http://localhost:8080/api`，以避免跨域问题。

    **Nginx 配置示例 (`nginx.conf` 或站点配置文件):**
    ```nginx
    server {
        listen 80; # 前端访问端口，例如 80 或自定义端口如 8081
        server_name localhost; # 或您的前端域名/IP

        # 前端文件根目录
        root /path/to/your/interview-frontend; 
        index index.html index.htm;

        location / {
            try_files $uri $uri/ /index.html;
        }

        # (可选) 反向代理到后端 API
        location /api/ {
            proxy_pass http://localhost:8080/api/; # 后端 API 地址
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
    ```
    *   将 `/path/to/your/interview-frontend` 替换为实际路径。
    *   重启 Nginx 服务使配置生效。

**Note on API Requests:**
The frontend is configured to make API requests to relative paths (e.g., `/api/appointments`). For this to work correctly:
*   When opening `index.html` directly in the browser (Option 1), this assumes the browser can make requests to the backend (e.g., `http://localhost:8080/api/...`) without CORS issues, or the backend has CORS configured appropriately. (Note: CORS is not explicitly configured in this project).
*   When using a web server (Option 2), the web server should be configured to proxy requests starting with `/api/` to the backend service (e.g., `http://localhost:8080`).
For example, if using Nginx, your location block for `/api/` (if backend is on port 8080 and frontend on another, or same with proxy) would be:
```nginx
location /api/ {
    proxy_pass http://localhost:8080/api/; # Note the trailing slash if your backend expects /api/
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```
This ensures that frontend requests like `/api/appointments` are correctly routed to the backend service. If your backend is accessible at `http://localhost:8080` and you want to proxy all requests starting with `/api` to it, the `proxy_pass` directive should correctly point to the backend's base URL for the API.

## 4. 访问系统 (Accessing the System)

*   **如果使用 Web 服务器 (选项 2):**
    *   打开浏览器，访问您配置的 Web 服务器地址和端口。例如: `http://localhost/` (如果监听80端口) 或 `http://localhost:8081` (如果监听自定义端口)。
*   **如果直接在浏览器中打开 (选项 1):**
    *   直接访问 `index.html` 的本地文件路径。

系统加载后，您应该能看到主界面，并可以开始使用面试预约和评价管理功能。确保后端服务正在运行，以便前端能够成功调用 API。

---
部署文档结束。
