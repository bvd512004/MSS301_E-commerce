# Hướng dẫn build và chạy bằng Docker

Tài liệu Giai đoạn 3. Toàn bộ hệ thống BookMart (MySQL, Eureka, API Gateway, ba microservices) chạy trên một Docker network và khởi động bằng một lệnh.

## Cần có trên máy

- Docker Desktop (hoặc Docker Engine + Docker Compose)
- Không cần cài Java hay Maven khi chạy bằng Docker: mỗi `Dockerfile` tự build JAR trong image

## Cấu trúc liên quan

```text
docker-compose.yml
/mysql/init.sql
/discovery-server/Dockerfile
/api-gateway/Dockerfile
/services/user-service/Dockerfile
/services/product-service/Dockerfile
/services/order-service/Dockerfile
```

## Mạng và database

Compose tạo network `bookmart-net`. Các container gọi nhau bằng tên service: `mysql`, `discovery-server`, `user-service`, `product-service`, `order-service`, `api-gateway`.

Một container MySQL 8.4 chứa **ba database riêng**, không dùng chung bảng:

| Service | Database |
| --- | --- |
| user-service | `bookmart_user` |
| product-service | `bookmart_product` |
| order-service | `bookmart_order` |

User ứng dụng: `bookmart` / `BookMart@2026`. Script `mysql/init.sql` chạy lần đầu khi volume trống.

Gateway tra Eureka rồi định tuyến. `order-service` gọi `http://user-service` và `http://product-service` qua LoadBalancer, không hard-code IP.

## Build và chạy

Đứng tại thư mục `MSS301_Project_E-comerce`:

```text
docker-compose up --build
```

Lần đầu Maven tải dependency trong lúc build image, có thể mất vài phút. Khi ổn định:

- Eureka: http://localhost:8761 — bốn client đăng ký (`api-gateway`, `user-service`, `product-service`, `order-service`)
- Gateway: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html — dropdown chọn từng service, **Try it out** đi qua Gateway

Chạy nền:

```text
docker-compose up --build -d
```

Xem log:

```text
docker-compose logs -f
```

Dừng:

```text
docker-compose down
```

Xóa cả dữ liệu MySQL (volume):

```text
docker-compose down -v
```

## Demo API qua Gateway

Đợi Gateway healthy rồi gọi:

```text
curl.exe http://localhost:8080/api/users
curl.exe http://localhost:8080/api/products
curl.exe -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d "{\"userId\":1,\"items\":[{\"productId\":1,\"quantity\":1}]}"
curl.exe "http://localhost:8080/api/orders?userId=1"
```

Luồng request giống Giai đoạn 2, chỉ khác chỗ các process nằm trong container. Chi tiết: [luong-request.md](luong-request.md).

## Cổng host

| Thành phần | Cổng |
| --- | --- |
| API Gateway | 8080 |
| Eureka | 8761 |
| user-service | 8081 |
| product-service | 8082 |
| order-service | 8083 |
| MySQL | 3306 |

Nếu cổng 3306 đã bị MySQL local chiếm, tắt MySQL local hoặc đổi mapping trong `docker-compose.yml`.

## Ghi chú

- `init.sql` chỉ chạy khi volume `mysql_data` mới. Đổi user/mật khẩu thì `docker-compose down -v` rồi lên lại.
- Screenshot/video demo Docker nộp kèm bài (chụp Eureka, gọi API qua Gateway).
