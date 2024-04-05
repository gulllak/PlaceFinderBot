<div align="center">
<a href="https://t.me/PlacesFinder_bot">
<img src="location_maps.png" width="300" alt="ragflow logo">
</a>
</div>

## 💡 PlaceFinder

[PlaceFinder](https://t.me/PlacesFinder_bot) - ваш помощник по поиску популярных мест, который всегда под рукой. 
Бот создан, чтобы помочь вам обнаружить достопримечательности, а также лучшие кафе и рестораны, находящиеся неподалёку.

Идея написать бота по поиску заведений пришла мне во время путешествия в Азию, где много уличных кафе и ресторанов, но 
в попытке найти что-то на Google картах, сталкиваешься с тем, что поиск засоряет множетсво заведений у которых рейтинг 
5*, но при этом нет ни одного отзыва. К сожалению в приложении карт нет никакого фильтра по количеству отзывов, поэтому 
я создал его самостоятельно используя API Google. 

Побробовать https://t.me/PlacesFinder_bot

## 🎬 Как запустить

1. Склонировать проект.
    ```bash
   $ git clone https://github.com/gulllak/PlaceFinderBot.git
   ```
2. Создать БД и прописать значиния в application.properties
    ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/***
   spring.datasource.username=***
   spring.datasource.password=***
   ```
3. Указать свои API KEY
    ```
    BOT_TOKEN=***
    GOOGLE_PLACES_API_KEY=***
   ```
4. Запустить проект локально.
5. Перейти в телеграм для тестирования бота. 


### 🏄 Стек: 
Java 21, SpringBoot 3, Docker, PostgreSQL, Liquibase, Maven.