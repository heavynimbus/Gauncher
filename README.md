# Gauncher

## Présentation

Ce projet est réalisé dans le cadre d'un projet d'étude au sein de la formation d'ingénieur informatique de Junia ISEN
par Léo Lamorille, Édouard Houllegatte et Hugo Wieder.

## Dépendances

Assurez vous d'avoir sur votre machine:

* maven: *version 3.6.3*

`sudo apt install maven`

* Java: *version 11.0.13*

`sudo apt install openjdk-11-jdk`

* docker / docker-compose

`sudo apt install docker docker-compose`

## Développement

*Assurez vous que votre compte github contienne une clé ssh, ainsi que d'avoir les droits d'éditions sur le projet
auprès des éditeurs.*

Copiez la command suivante et collez la dans un terminal:

```shell
# could be replaced by the your working path
cd ~

# clone the project
git clone git@github.com:heavynimbus/Gauncher.git && cd Gauncher
```

### Database

La base de donnée est une image docker PostgreSQL dont la configuration se trouve à la racine du projet dans le
fichier `docker-compose.yaml`. Afin de l'instancier, executez les commandes suivantes à la racine du projet:

```shell
# start the docker container
docker-compose up -d gauncher-db

# connect to the databse
psql -h localhost -p 5432 -U user -p pass gauncher-db
```

La database est initialisée au lancement du backend via le fichier `src/main/resources/init-db.sql`, qui est chargé et 
exécuté par le service `gauncher.backend.service.InitDatabaseService`.

### Backend

Le serveur est un serveur TCP java écoutant sur le port 8080. Afin de l'instancier, executez les commandes suivantes à
la racine du projet:

```shell
# go into the backend module
cd backend

# compile the backend
mvn compile

# run the server
mvn exec:java
```

Voir la [documentation](doc/md/doc-login.md) du serveur.

### Frontend

Le clientEntity est un clientEntity lourd javaFX. Afin de l'instancier, executez les commandes suivantes à la racine du projet:

```shell
# go into the frontend module
cd frontend

# compile the frontend
mvn compile

# start a new clientEntity
mvn javafx:run
```