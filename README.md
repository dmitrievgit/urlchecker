# Сервис проверки доступности списка URL

Сервис предоставляет отчет доступности списка URL, для этого периодически отправляет на заданные URL запросы методом GET, если получает ответ со статусом 200 то считает URL доступным.

Список URL и частота их опроса могут быть изменены в процессе работы сервиса при помощи вызова REST API.

## Пример запуска в контейнере

Чтобы создать и запустить контейнер с сервисом нужно выполнить команды
```
docker build -t url-checker-img .
docker run -dp 8081:8080 --name url-checker url-checker-img
```
При этом API сервиса будет доступно по URL http://localhost:8081

## Настройка через переменные окружения
Для передачи значения настроек используются переменные окружения:

* **SCHEDULER_INTERVAL** - интервал запуска проверок доступности URL в миллисекундах, по-умолчанию `30000`
* **REQUEST_TIMEOUT** - время ожидания ответа URL `2000`.

Пример настройки переменных окружения при запуске сервиса в контейнере:
```
docker run -dp 8081:8080 -e SCHEDULER_INTERVAL=30000 -e REQUEST_TIMEOUT=10000 --name url-checker url-checker-img
```

## Настройка через вызов REST API

### Список URL
Для задания списка проверяемых URL, нужно отправить его в формате JSON в теле POST запроса

**Относительный адрес**: /api/v1/config/urls

| Параметры запроса |     тип      | обязательность | описание               |
|-------------------|:------------:|:--------------:|:-----------------------|
| urls              | массив строк |       +        | Список проверяемых URL |

**Пример**
```
curl -L -X POST 'http://localhost:8081/api/v1/config/urls' \
-H 'Content-Type: application/json' \
--data-raw '{
    "urls": ["http://ya.ru" ,"http://mail.ru"]
}'
```

#### Интервал опроса
Для настройки интервала опроса, нужно отправить его в формате JSON в теле POST запроса

**Относительный адрес**: /api/v1/config/interval

| Параметры запроса |  тип  | обязательность | описание                                   |
|-------------------|:-----:|:--------------:|:-------------------------------------------|
| interval          | Целое |       +        | Интервал между проверками, в миллисекундах |

**Пример**
```
curl -L -X POST 'http://localhost:8081/api/v1/config/interval' \
-H 'Content-Type: application/json' \
--data-raw '{
    "interval": 20000
}'
```

## Получение отчета о проверках доступности списка URL
Для получения отчета нужно отправить GET запрос без параметров 

**Относительный адрес**: /api/v1/report/availability

**Пример**
```
curl -L -X GET 'http://localhost:8081/api/v1/report/availability'
```