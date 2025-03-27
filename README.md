## Documentation API

- **[User API](https://github.com/Akbarwp/Spring-Restful-API/blob/main/docs/user.md)**
- **[Contact API](https://github.com/Akbarwp/Spring-Restful-API/blob/main/docs/contact.md)**
- **[Address API](https://github.com/Akbarwp/Spring-Restful-API/blob/main/docs/address.md)**
- **[Category API](https://github.com/Akbarwp/Spring-Restful-API/blob/main/docs/category.md)**
- **[Product API](https://github.com/Akbarwp/Spring-Restful-API/blob/main/docs/product.md)**
- **[Postman](https://github.com/user-attachments/files/19482272/Spring.Boot.RESTful.API.postman_collection.zip)**

## Installation

Follow the steps below to clone and run the project in your local environment:

1. Clone repository:

    ```bash
    git clone https://github.com/Akbarwp/Spring-Restful-API.git
    ```

2. Setup application properties in the `application.properties` file:

    ```plaintext
    spring.application.name=Belajar Spring RESTful API
    server.port=8080
    server.servlet.context-path=/api
    spring.jackson.time-zone=Asia/Jakarta
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.username=root
    spring.datasource.password=
    spring.datasource.url=jdbc:mysql://localhost:3306/spring_restful_api
    spring.datasource.type=com.zaxxer.hikari.HikariDataSource
    spring.datasource.hikari.minimum-idle=10
    spring.datasource.hikari.maximum-pool-size=50
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.show_sql=true
    ```
