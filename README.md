
✅ Architecture Overview (What We Will Build)

                     +---------------------+
                     |     NGINX (LB)      |
                     +----------+----------+
                                |
          --------------------------------------------------
          |                     |                          |
+----------------+     +----------------+      +----------------+
|  user-service  |     | order-service  |      | payment-service |
+----------------+     +----------------+      +----------------+
          |                     |                          |
+------------------+   +------------------+     +------------------+
| Kafka (Producer) |   | Kafka (Consumer) |     | RabbitMQ Listener|
+------------------+   +------------------+     +------------------+
          |                     |                          |
          --------------------------------------------------
                                |
                       +------------------+
                       |  MySQL (Central) |
                       +------------------+

Other:
- Redis used optionally for caching in each service
- RabbitMQ is used only in payment-service


multi-model-project/
├── docker-compose.yml
├── nginx/
│   └── default.conf
├── user-service/
│   ├── Dockerfile
│   ├── build.gradle
│   └── src/main/java/com/example/userservice/
│       ├── controller/UserController.java
│       ├── model/User.java
│       ├── service/UserService.java
│       └── config/
│           ├── KafkaProducerConfig.java
│           ├── MySQLConfig.java
│           └── RedisConfig.java
├── order-producer/
│   ├── Dockerfile
│   ├── build.gradle
│   └── src/main/java/com/order/producer/
│       ├── controller/OrderController.java
│       ├── model/Order.java
│       ├── service/OrderService.java
│       └── config/
│           ├── KafkaConsumerConfig.java
│           ├── RedisConfig.java
│           └── MySQLConfig.java
├── payment-service/
│   ├── Dockerfile
│   ├── build.gradle
│   └── src/main/java/com/example/paymentservice/
│       ├── listener/PaymentListener.java
│       ├── model/Payment.java
│       ├── service/PaymentService.java
│       └── config/
│           ├── RabbitMQConfig.java
│           └── MySQLConfig.java
├── README.md

                         +-------------------+
                         |     NGINX         |  <-- Load Balancer
                         +--------+----------+
                                  |
        +-----------+------------+------------+------------+
        |           |                         |            |
    +-----v--+   +-----v---+               +-----v----+  +----v----+
    | user-1 |   | user-2  | replicas      | order-1  |  | payment |
    +--------+   +---------+               +----------+  +---------+
        |             |                         |            |
        +------+      +--------------------+    +------------+
               |                           |
    Kafka, Redis, MySQL, RabbitMQ (centralized from Docker Hub)
