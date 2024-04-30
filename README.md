# theses-api-recherche

[![build-test-pubtodockerhub](https://github.com/abes-esr/theses-api-recherche/actions/workflows/build-test-pubtodockerhub.yml/badge.svg)](https://github.com/abes-esr/theses-api-recherche/actions/workflows/build-test-pubtodockerhub.yml) [![Docker Pulls](https://img.shields.io/docker/pulls/abesesr/theses.svg)](https://hub.docker.com/r/abesesr/theses/)

Le moteur de recherche theses.fr recense l’ensemble des thèses de doctorat soutenues en France depuis 1985, les sujets de thèse préparés au sein des établissements de l’enseignement supérieur français, et les personnes impliquées dans la recherche doctorale française. 

Ce dépôt héberge le code source de l'API Recherche de theses.fr.

L’API permet de lancer une recherche dans les données de theses.fr décrivant les thèses et les personnes liées aux thèses. Les données sont récupérables au format JSON.

L'API s'adresse à toutes les personnes qui souhaitent interroger le site theses.fr, récupérer les données relatives aux thèses de doctorat sur un périmètre donné pour les réutiliser au sein de leur propre système d'information, à des fins de recherche et de statistiques, pour constituer une base de données ou faire de la veille, c'est à dire les gestionnaires de métadonnées, enseignants-chercheurs, data scientist, bibliothécaires, etc.

URL publique : [https://theses.fr/api/v1/recherche/](https://theses.fr/api/v1/recherche/openapi.yaml)

![logo theses.fr](https://theses.fr/icone-theses-beta.svg)

L'application complète peut être déployée via Docker à l'aide du dépôt https://github.com/abes-esr/theses-docker

## Données exposées par l'API : 

L’API permet de récupérer les données relatives :

* au statut des thèses : soutenues, en préparation, accessibles en ligne
* à la description des thèses : titre, mots-clés libres, mots-clés contrôlés (libellés et identifiants), résumés, discipline, langue, Numéro National de Thèse ou identifiant de thèse en préparation, date de soutenance ou date de première inscription en doctorat aux 
personnes liées aux thèses : auteur / autrice (nom, prénom et identifiant), directeur / directrice (nom, prénom et identifiant), président / présidente du jury (nom, prénom et identifiant), rapporteurs / rapporteuses (nom, prénom et identifiant), membres du jury (nom, prénom et identifiant)
* aux structures liées aux thèses : établissement de soutenance (libellé et identifiant), établissement de cotutelle (libellé et identifiant), école doctorale (libellé et identifiant), partenaire de recherche (libellé et identifiant)

Le détail des champs interrogeables est accessible dans la documentation de l'API : https://documentation.abes.fr/aidetheses/thesesfr/index.html#PrincipeAPI

Ainsi que sur [api.gouv.fr](https://api.gouv.fr/les-api/api-interroger-donnees-these)

## Architecture technique

Il y a 3 API pour Theses.fr : 
* **https://github.com/abes-esr/theses-api-recherche pour la recherche et l'affichage de theses** correspondant à ce dépot
* https://github.com/abes-esr/theses-api-export pour les exports des theses en différents formats (CSV, XML, BIBTEX, etc)
* https://github.com/abes-esr/theses-api-diffusion pour la mise à disposition des documents (PDFs et autres)

L'API présente est écrite en Java 17, à l'aide du framework Spring Boot 3.

Elle est déployée automatiquement dans le SI de l'Abes sous forme d'un container docker, à l'aide de la chaine CI/CD Github.

## Mode maintenance

L'API est dotée d'un mode maintenance, qui met en place une réponse avec le code d'erreur 503 et un message personnalisé pour toutes les requêtes.

Le message affiché est modifiable dans le .env ```THESES_MAINTENANCE_MESSAGE```. Un redémarrage du container est nécessaire lors de la mise à jour du message de maintenance.


Pour passer l'application en mode maintenance, il suffit de passer la valeur de ```THESES_MAINTENANCE``` dans le .env à ```true```, puis de relancer le container : ```sudo docker compose up -d```

Une fois la maintenance terminée, effectuer la manipulation inverse, remettre ```THESES_MAINTENANCE``` dans le .env à ```false```, puis de relancer le container : ```sudo docker compose up -d```



> [!NOTE]  
> L'erreur 503 sera retourné sur l'intégralité des requêtes de l'API.
>
> Cela rend l'interface Theses.fr (le front vueJS) inopérant, il est donc conseillé de mettre également ce dernier en mode maintenance si cette API est passée en mode maintenance.
