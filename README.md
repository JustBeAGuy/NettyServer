ScreenShots
===========


ScreenShot Of Status command "/status"
![ScreenShot](https://github.com/JustBeAGuy/NettyServer/blob/master/status.jpg)

ScreenShot of "ab – c 100 – n 10000 http://somedomain/status"
![ScreenShot](https://github.com/JustBeAGuy/NettyServer/blob/master/ab.jpg)

ScreenShot of "/status" after AB
![ScreenShot](https://github.com/JustBeAGuy/NettyServer/blob/master/afterAB.jpg)

Implementation
=============

Версия Netty - 4.0.26.Final

В связи с комментариями к заданию и вывод всего 16 последних запросов выбрал реализацию без базы данных(показать работу с многопоточностью), хотя реализация с базой данных, к примеру H2 была бы эффективна до определенного количества запросов.

Приложение собирается Maven'ом, а так же подключен Log4j

код прокомментирован

Классы :

-HttpServer - запуск сервера. Создаем EventLoopGroup босса, который распредляет по рабочим(потокам), NioEventLoopGroup() - как самая оптимальная реализация;

-HttpServerHandler - Handler для обработки запроса;

-TrafficCounterHandler - подсчет входящего и исходящего трафика;

-HttpServerInitializer - инициализация сервер;

-ServerStatistics - класс который хранит статистику запросов;

Пакеты :

-commands - реализация паттерна COMMAND, где через контроллер (Controller) передается команда на CommandFactory, получаем экземпляр расширенный от абстр. класса Command и выполняем метод execute, куда передаем параметры, контекст и реквест. Так же в этом классе создается и пишеться Response в контекст;

-controller - обрабатывает команды с запроса;

-entities - сущности, в частости один класс Request.
