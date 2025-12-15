# Spring Boot Blog RESTful API 🚀

這是一個基於 Spring Boot 3 與 Spring Security 6 建構的部落格後端系統，具備完整的 JWT 身份驗證、基於角色的存取控制 (RBAC) 以及 RESTful 風格的 API 設計。

## 更新說明
[2025/12/15]

新增 前端頁面，實作 前後端分離 + Fetch API

新增 "/blog/posts" 為查詢全部的文章前端頁面

[2025/12/08]

新增 最高管理員權限的判斷，最高管理員可以刪除不同使用者的文章。


## ✨ 專案亮點 (Key Features)

* **安全性核心**：
    * 整合 **Spring Security 6** 與 **JWT (JSON Web Token)** 實作無狀態 (Stateless) 身份驗證。
    * 實作 **BCrypt** 密碼加密與加鹽 (Salting)。
    * 自訂 `AccessDeniedHandler` 與 `AuthenticationEntryPoint` 處理授權例外。

* **業務邏輯授權**：
    * 實作精細的**資料級權限控制**：確保文章只能由作者本人修改或刪除。

* **RESTful API 設計**：
    * 遵循 REST 規範設計資源路徑 (Resources)。
    * 實作分頁 (Pagination) 與排序功能。
    * 整合 **SpringDoc (Swagger UI)** 自動生成 API 文件。

* **架構設計**：
    * 採用經典的三層架構 (Controller-Service-Repository)。
    * 使用 DTO (Data Transfer Object) 隔離資料庫實體與 API 請求。
    * 實作全域例外處理 (`@ControllerAdvice`) 統一錯誤回應格式。

## 🛠️ 技術堆疊 (Tech Stack)

* **Language**: Java 17
* **Framework**: Spring Boot 3.5.7, Spring Security 6, Spring Data JPA
* **Database**: MySQL 8.0
* **Build Tool**: Maven
* **API Documentation**: SpringDoc OpenAPI 3 (Swagger UI)
* **Utilities**: Lombok, JJWT (0.12.5)

## 🚀 快速開始 (Getting Started)

### 前置需求
* JDK 17+
* MySQL 8.0+
* Maven 3.8+

### 安裝步驟

1.  **複製專案**
    ```bash
    git clone https://github.com/JimmyWu987-987/SideProject_blogByRESTful.git
    cd SideProject_blogByRESTful
    ```

2.  **資料庫設定**
    * 在 MySQL 中建立資料庫：`CREATE DATABASE sideproject_blog;`
    * 修改 `src/main/resources/application.properties` 中的資料庫連線資訊。
    * 修改 JWT 密鑰（可選）

3.  **執行專案**
    ```bash
    mvn spring-boot:run
    ```

4.  **瀏覽 API 文件**
    * 啟動後訪問：`http://localhost:8080/swagger-ui/index.html`

## 📝 API 文件概覽

| Method | Endpoint | Description | Auth Required |

| `POST` | `/api/v1/auth/signup` | 註冊新會員 | ❌ |

| `POST` | `/api/v1/auth/signin` | 會員登入 (取得 JWT) | ❌ |

| `GET` | `/api/v1/posts` | 取得文章列表 (分頁) | ❌ |

| `POST` | `/api/v1/posts` | 新增文章 | ✅ |

| `PUT` | `/api/v1/posts/{id}` | 修改文章 (限本人) | ✅ |

| `DELETE` | `/api/v1/posts/{id}` | 刪除文章 (限本人) | ✅ |

## 📝 前端頁面
| `/blog/posts ` | 查詢全部文章

---
**Created by JimmyWU**