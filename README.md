# single-auth-starter

## Назначение
* Стартер предназначен для реализации механизма кэшированной авторизации к корпоративной системе keycloak;

## Описание
* Подробнее о том, как работать с starter описано [тут](https://wiki.alfastrah.ru/pages/viewpage.action?pageId=352976500);

## В чем польза?
* Реализует механизм аутентификации к корпоративному keycloak, с помощью:
  * Указание параметров необходимых для запроса через 
  заполнение атрибутов в ```application.yml``` использующего сервиса;
* Для снижения нагрузки на трафик реализует кэширование полученного;
из keycloak ```access_token```, с помощью:
  * Указание параметров подключения - **url**;
  * Указание параметров, необходимых для кэширования - 
  время сохранения кэша;

## Как его использовать?
* Добавить в проект зависимость:
```xml
<dependency>
  <groupId>ru.alfastrah.odm</groupId>
  <artifactId>single-auth-starter</artifactId>
  <version>[actual version from pom.xml]</version>
</dependency>
```
* В использующем сервисе, через загрузку параметров 
```application.yml``` или ```application.properties``` 
должны быть переданы параметры:  
```yaml
keycloak-request:
  grantType: [тип grant_type для получения данных из keycloak]
  clientId: [значение client_id для получения данных из keycloak]
  clientSecret: [значение client_secret для получения данных из keycloak]
keycloak-connection:
  url: [url для подключения к keycloak]
  cachetime: [время кэширования токена]
```

## Стоит обратить внимание
* Для кэширования токена создается КЭШ с именем - "KeycloakToken";

## Контакты
| Участник     | Контакт                |
|--------------|------------------------|
| Команда ilog | team_ilog@alfastrah.ru |
