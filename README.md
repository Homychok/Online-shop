Дипломная работа "Профессия Java-разработчик ISA" от Skypro
Описание проекта:
Приложение – аналог Market place. С помощью этого приложения ппользователи размещают объявления о своих товарвх и оставляют комментарии к объявлениям других пользователей.

Функции:
Авторизация и аутентификация пользователей;
CRUD-операции для объявлений на сайте;
CRUD-операции для комментариев к объявлениям;
Пользователи могут создавать, удалять или редактировать свои собственные объявления и комментарии. Администраторы могут удалять или редактировать все объявления и комментарии;
Поиск объявлений по названию в шапке сайта;
Загрузка и отображение изображений объявлений и аватаров пользователей.
Задание:
[Technical task](https://skyengpublic.notion.site/02df5c2390684e3da20c7a696f5d463d)
Инструменты, используемые в проекте:
Backend:
Java 11
Maven
Spring Boot
Spring Web
Spring Data JPA
Spring Security
Stream API
REST
GIT
Swagger
Lombok
SQL:
PostgreSQL
Liquibase
Test:
Junit
Mockito
Frontend:
Docker image
Запуск приложения:
Для запуска приложения нужно:
Клонировать проект и открыть его в среде разработки IntelliJ IDEA
В файле application.properties указать путь к Вашей базе данных;
Запустить Docker;
В командной строке прописать docker pull ghcr.io/bizinmitya/front-react-avito:latest и скачать образ;
Запустить Docker image с помощью команды docker run -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:latest;
Запустить метод main в файле MarketplaceApplication.java.
Разработчик:
[Волчихина Александра](https://github.com/Homychok)