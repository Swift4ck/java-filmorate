# java-filmorate
![Диаграмма базы данных Filmorate](ER.png)

Диаграмма  показывает связи между пользователями и фильмами, а так же как ставятся лайки к фильмам.

Примеры запросов 

Топ 10 самых популярных фильмов по лайкам
SELECT
      f.name,
      COUNT(l.film_id) AS like_count
FROM films f
LEFT JOIN likes l ON f.id = l.film_id
GROUP BY f.id
ORDER BY like_count DESC
LIMIT 10;

Друзья пользователя с айди 111
SELECT u.name
FROM users u
JOIN friendship f ON u.id = f.friend_id
WHERE f.user_id = 111
AND f.status = 'CONFIRMED';

Ищем фильм жанра COMEDY
SELECT f.name, f.release_date
FROM films f
JOIN film_genres fg ON f.id = fg.film_id
JOIN genres g ON fg.genre_id = g.id
WHERE g.genre_name = 'COMEDY';

Фильмы выпущены после 2020 года 
SELECT name, release_date
FROM films
WHERE release_date > '2020-01-01';


Template repository for Filmorate project.
