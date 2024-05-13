# Kafka-spring-parser

Kafka spring parser - сайт, который предназначен и разработан для автоматического парсинга по запросу, таких сервисов как: Youtube , Olx , Kolesa kz . Проект реализован на микросервисной архитектуре, где каждый микросервис выполняет свою задачу. В качестве брокера сообщений была использована apache kafka. А также для каждого микросервиса был написан свой dockerfile с автоматической "упаковки" проекта в jar и запуска 

# Technology stack 

## Backend 

+ Java: Основной язык программирования для разработки
+ Spring boot: Фреймвопрк для создания веб - приложения
+ Selenuim & Jsou: Фреймворки для парсинга страниц
+ Spring MVC: Для отображения страниц по логике Model-View-Controller
+ Kafka & ZooKeeper:  Apache Kafka используется для построения распределенных и устойчивых систем обмена сообщениями, а Apache ZooKeeper служит для обеспечения координации и управления конфигурацией в таких системах.
+ Lombok: Библиотека, предоставляющая аннотации для сокращениия кода
+ Docker: Для создания контейнеров для микросервисов и их развертывания
+ Maven: Инструмент для управления зависимостями проекта

## Frontend

- Thymeleaf: Шаблонизатор для динамических создания страниц для отображения результата парсинга, взаимодействующих с Spring.
- HTML/CSS/JS : Базовый стек для создания страниц
- Bootstrap: Фреймворк для создания страниц на основе шаблонов


# Microservices:

+ Main-service:

Main-service - "главный сервис" который нужен для отправки сообщений от frontend-а к message broker и отображения результата. Она нужна чтобы "связать" frontend и kafka, чтобы запросы пользователя могли дальше пробрасываться к брокеру


+ Youtube-Parser-Service

Как может быть понятно по названию, данный микросервис - парсер для ютуба. Она берет название канала и парсит все его видео. Она использует selenium для автоматического запуска страницы для прогрузки скриптов.

+ Kolesa-Parser-Service

Парсер для Kolesa очень похож на парсер youtube-а, только он будет работать гораздо быстрее, так как ему не нужно прогружать скрипты страницы, соответсвенно не нужно использовать и запускать барузер для парсинга. Она может парсить как и марки машин так и модели (Например bmw - марка, а bmw m3 это уже модель)

+ Olx-Parser-Service

Olx parser service также парсит вещь по названию. То есть, она берет название предмета для парсинга а затем парсит все результаты поиска. Она также использует seleniuim для прогрузки скриптов.


# Как работает система?

Так как проект построен на микросервисной архитектуре, значит ее компоненты взаимодейтсвуют между собой и далее будет понятно как:
Как было описано выше у нас есть main service, который представляет из себя frontend. Так вот, мы получаем нужные данные, скажем название канала, далее main service кидает сообщение в youtube-parser-topic вместе с названием канала (KafkaTemplate<String, String>). Далее, так как топик "прослушивается" youtube-parser-serivce и его Consumer-ом (Получатель) он обрабатывает запрос и далее кидает ответ.

Все это более понятней на диаграмме:

![alt text](https://github.com/heiphin7/Kafka-spring-parser/blob/main/microservices.png)

# Frontend

# Main Page

![alt text](https://github.com/heiphin7/Kafka-spring-parser/blob/main/main_page.png)

# Parsign result №1

![alt text](https://github.com/heiphin7/Kafka-spring-parser/blob/main/youtube-resluts.png)

# Parsing result №2

![alt text](https://github.com/heiphin7/Kafka-spring-parser/blob/main/parser-example.png)


# Установка & Запуск

Для установки проекта нужно скопировать репозиторий:

     ```bash
    git clone https://github.com/heiphin7/Kafka-spring-parser
    ```

Также вам нужно установить и запустить kafka & zookeeper

1. Обычный запуск:

   Откройте проект в IDE, далее вам нужно запустить каждый из сервисов. далее по localhost:4322/main будет доступен проект.
   !!! Проверьте чтобы у вас не были заняты порты: 1029, 1030, 1031, 4322. Если заняты, вы можете изменить их в application.yaml

2. Запуск через docker-контейнеры:

   Скачайте docker при необходимости, далее вам нужно собрать каждый из dockerfile используя команду:
   
   ```bash
   docker build -t <tag> .
   ```

  Далее нужно запустить уже докер image-ы, используя:

  ```bash
  docker run [docker ports] <image_name>
  ```
   

