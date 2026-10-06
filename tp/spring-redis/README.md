# Projet Spring Redis minimal

## Prérequis

- Java 21 ;
- Maven 3.6.3 ou supérieur ;
- Redis démarré depuis la racine du dépôt avec `docker compose up -d`.

## Démarrage

```bash
mvn spring-boot:run
```

L'application écoute sur `http://localhost:8080`.

```bash
curl http://localhost:8080/api/redis/ping
```

Le fichier `requests.http` contient des requêtes utilisables depuis les IDE compatibles.

## Organisation

- `RedisConfiguration` configure le template JSON typé ;
- `RedisLabService` montre la correspondance entre les opérations Spring et Redis ;
- `RedisLabController` fournit une interface HTTP minimale ;
- `StudentProfile` est l'objet sérialisé en JSON.

Le projet est volontairement petit. Les exercices demandent de l'étendre sans introduire de couche de persistance ou de framework supplémentaire.

