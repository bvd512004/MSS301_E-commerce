CREATE DATABASE IF NOT EXISTS bookmart_user
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bookmart_product
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bookmart_order
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'bookmart'@'%' IDENTIFIED BY 'BookMart@2026';
GRANT ALL PRIVILEGES ON bookmart_user.* TO 'bookmart'@'%';
GRANT ALL PRIVILEGES ON bookmart_product.* TO 'bookmart'@'%';
GRANT ALL PRIVILEGES ON bookmart_order.* TO 'bookmart'@'%';
FLUSH PRIVILEGES;
