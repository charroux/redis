# Formation Redis

Support de cours en français pour une formation Redis destinée à des étudiants de master en informatique.

Le dépôt contient deux projets Quarto distincts :

- le cours, construit comme un document continu dans `cours/` ;
- les travaux pratiques dans `tp/`, à utiliser aux moments indiqués par l'enseignant sans les intégrer à la navigation du cours.

Le parcours va des commandes fondamentales à la modélisation, aux index et projections, à l'événementiel avec Redis Streams, puis à Spring Data Redis.

## Consulter le cours

Prérequis : [Quarto](https://quarto.org/docs/get-started/).

```bash
quarto preview
```

La version statique est générée dans `_site/` avec :

```bash
quarto render
```

Pour consulter séparément les travaux pratiques :

```bash
quarto preview tp
```

Le projet Spring minimal est situé dans `tp/spring-redis`.

## Démarrer Redis

Prérequis : Docker avec la commande `docker compose`.

```bash
docker compose up -d
docker compose exec redis redis-cli
```

Pour arrêter et supprimer le conteneur :

```bash
docker compose down
```

Le fichier Compose ne crée volontairement aucun volume persistant : chaque groupe peut repartir d'un environnement propre avec `docker compose down`, puis `docker compose up -d`.

## Corrections progressives

La branche `main` contient les énoncés et les indices, mais pas les corrections. Les corrections sont conservées dans des branches locales indépendantes :

- `corrections/tp1-cli-basique` ;
- `corrections/tp2-cli-avance` ;
- `corrections/tp3-spring`.

L'enseignant peut publier chaque branche au moment voulu. Les étudiants peuvent alors consulter une correction sans modifier la branche principale.
