# DaData gateway
Приложение для получения данных из сервиса DaData.  
Умеет получать данные из эндпоинта [suggest/address](https://dadata.ru/api/suggest/address/) (v0.0.1) 
и сохранять результаты обращений к эндпоинту в redis (будет добавлено в версии v0.0.2).

## Установка приложения
Перед запуском приложения, необходимо получить токен авторизации из DaData.  
При запуске приложения токен передаётся в переменную окружения `DADATA_AUTH_TOKEN`:
```
DADATA_BASE_URL=https://suggestions.dadata.ru/suggestions/api/4_1/rs
DADATA_AUTH_TOKEN=<dadata_token>
```