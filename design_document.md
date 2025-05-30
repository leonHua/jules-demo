# 系统详细设计文档 (System Design Document)

## 1. 引言 (Introduction)
本文档详细描述了面试管理系统的设计，包括技术选型、项目结构、系统架构和API接口等。

## 2. 技术选型 (Technology Stack)

### 2.1. 后端 (Backend)
*   **语言 (Language):** Java 8
*   **框架 (Framework):** Spring Boot 2.3.12.RELEASE
*   **ORM:** MyBatis-Plus 3.4.2
*   **数据库 (Database):** MySQL 5.7+
*   **构建工具 (Build Tool):** Maven

### 2.2. 前端 (Frontend)
*   **UI 框架 (UI Framework):** Layui (latest stable version, e.g., v2.9.x)
*   **语言 (Languages):** HTML, CSS, JavaScript (ES6+)

## 3. 项目结构 (Project Structure)

### 3.1. `interview-backend/`
包含所有后端 Java Spring Boot 代码。
```
interview-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/interviewbackend/
│   │   │       ├── controller/       # REST API 控制器
│   │   │       ├── service/          # 业务逻辑服务
│   │   │       ├── mapper/           # MyBatis-Plus 数据访问接口
│   │   │       └── entity/           # 数据库实体类
│   │   └── resources/
│   │       ├── application.properties # Spring Boot 配置文件 (数据库连接等)
│   │       ├── mapper/               # MyBatis XML 映射文件 (如果使用)
│   │       └── static/               # 静态资源 (如果有)
│   │       └── templates/            # 服务端模板 (如果使用)
├── pom.xml                           # Maven 项目配置文件
└── target/                           # 构建输出目录 (e.g., .jar 文件)
```

### 3.2. `interview-frontend/`
包含所有前端 HTML, CSS, JS, 和 Layui 资产。
```
interview-frontend/
├── layui/                    # Layui 框架文件 (css, js, font, etc.)
├── pages/                    # 各模块的 HTML 页面
│   ├── appointments.html     # 面试预约管理页面
│   └── evaluations.html      # 面试评价管理页面
├── index.html                # 系统主入口和导航页面
└── README.md                 # 前端项目说明
```

## 4. 架构图 (Architecture Diagram)

采用经典的前后端分离架构：

```
用户浏览器 (Layui HTML/JS on User's Device)
       ▲
       | (HTTP/HTTPS Requests)
       ▼
Web 服务器 (例如 Nginx, Apache, 或用于开发的本地文件服务)
       ▲
       | (Serves static frontend files)
       | (Proxies API requests if configured)
       ▼
后端 API 服务 (Spring Boot Application running on a Server)
       ▲  (HTTP REST API: JSON over HTTP/HTTPS)
       |
       ▼
数据库 (MySQL Server)
```

*   **用户浏览器:** 运行前端HTML、CSS和JavaScript（Layui框架）。用户通过浏览器与系统交互。
*   **Web 服务器:** (可选，对于生产环境推荐) 托管前端静态文件。也可以配置为反向代理，将API请求转发到后端服务。对于简单部署，可以直接在浏览器中打开 `index.html`。
*   **后端 API 服务:** Spring Boot应用，提供RESTful API接口，处理业务逻辑和数据持久化。
*   **数据库:** MySQL数据库，存储所有应用数据，如预约信息和评价信息。

## 5. API 接口文档 (API Documentation - Overview)

所有API均以 `/api` 为前缀。

### 5.1. 面试预约管理 (Interview Appointments) - `/api/appointments`
*   **`GET /`**: 获取面试预约列表（支持分页和按候选人姓名搜索）。
    *   请求参数: `page` (页码), `size` (每页数量), `candidateName` (可选, 候选人姓名)。
    *   成功响应: `{ "code": 200, "message": "Success", "data": { "content": [...], "totalElements": ... } }`
*   **`POST /`**: 新增一个面试预约。
    *   请求体: `InterviewAppointment` JSON 对象。
    *   成功响应: `{ "code": 201, "message": "Appointment added successfully.", "data": { ... } }`
*   **`GET /{id}`**: 根据ID获取指定的面试预约信息。
    *   成功响应: `{ "code": 200, "message": "Appointment found.", "data": { ... } }`
*   **`PUT /{id}`**: 根据ID更新指定的面试预约信息。
    *   请求体: `InterviewAppointment` JSON 对象。
    *   成功响应: `{ "code": 200, "message": "Appointment updated successfully.", "data": { ... } }`
*   **`DELETE /{id}`**: 根据ID删除指定的面试预约。
    *   成功响应: `{ "code": 200, "message": "Appointment deleted successfully.", "data": null }`

### 5.2. 面试评价管理 (Interview Evaluations) - `/api/evaluations`
*   **`GET /`**: 获取面试评价列表（支持分页和按 `appointmentId` 筛选）。
    *   请求参数: `page` (页码), `size` (每页数量), `appointmentId` (可选, 关联的预约ID)。
    *   成功响应: `{ "code": 200, "message": "Success", "data": { "content": [...], "totalElements": ... } }`
*   **`POST /`**: 新增一个面试评价。
    *   请求体: `InterviewEvaluation` JSON 对象 (其中 `evaluationTime` 由后端自动设置)。
    *   成功响应: `{ "code": 201, "message": "Evaluation added successfully.", "data": { ... } }`
*   **`GET /{id}`**: 根据ID获取指定的面试评价信息。
    *   成功响应: `{ "code": 200, "message": "Evaluation found.", "data": { ... } }`
*   **`PUT /{id}`**: 根据ID更新指定的面试评价信息。
    *   请求体: `InterviewEvaluation` JSON 对象。
    *   成功响应: `{ "code": 200, "message": "Evaluation updated successfully.", "data": { ... } }`
*   **`DELETE /{id}`**: 根据ID删除指定的面试评价。
    *   成功响应: `{ "code": 200, "message": "Evaluation deleted successfully.", "data": null }`
*   **`GET /appointment/{appointmentId}`**: 根据预约ID获取该预约下的所有评价（支持分页）。
    *   请求参数: `page` (页码), `size` (每页数量)。
    *   成功响应: `{ "code": 200, "message": "Success", "data": { "content": [...], "totalElements": ... } }`

## 6. 数据库设计 (Database Design)
(ER图或表结构描述) - 此处省略，实体类已定义表结构。主要表：
*   `interview_appointment`: 存储面试预约信息。
    *   `id` (PK, BIGINT, AUTO_INCREMENT)
    *   `candidate_name` (VARCHAR)
    *   `interview_time` (DATETIME)
    *   `interviewer` (VARCHAR)
    *   `status` (VARCHAR) - e.g., "Scheduled", "Completed", "Cancelled"
    *   `notes` (TEXT)
*   `interview_evaluation`: 存储面试评价信息。
    *   `id` (PK, BIGINT, AUTO_INCREMENT)
    *   `appointment_id` (FK, BIGINT) - 关联 `interview_appointment.id`
    *   `interviewer_comments` (TEXT)
    *   `rating` (INT) - e.g., 1-5
    *   `result` (VARCHAR) - e.g., "Hired", "Rejected", "Pending"
    *   `evaluation_time` (DATETIME) - 评价创建时间

---
文档结束。
