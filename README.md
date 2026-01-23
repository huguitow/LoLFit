# LoLFit

Application web de gestion de collection de skins League of Legends. Projet SAE.

## Technologies

**Backend**
- Java 25 / Spring Boot 4.0.1
- PostgreSQL
- Spring Security + JWT

**Frontend**
- Vue 3 / Vite
- Vuetify 3 (Material Design)
- Pinia (state management)

## Fonctionnalités

- Authentification (inscription/connexion avec JWT)
- Parcourir les champions et leurs skins
- Recherche de skins
- Gestion de sa collection personnelle (wardrobe)
- Synchronisation des données depuis l'API Riot Games

## Installation

### Prérequis
- Java 25+
- Node.js 18+
- PostgreSQL

### Backend

```bash
cd backend
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

L'application démarre sur `http://localhost:5173`

## Configuration

Créer la base de données PostgreSQL et configurer `backend/src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lolfit
spring.datasource.username=votre_user
spring.datasource.password=votre_password
```

## Structure du projet

```
LoLFit/
├── backend/          # API Spring Boot
│   └── src/main/java/com/lolfit/backend/
│       ├── auth/     # Authentification
│       ├── skin/     # Champions, Skins, Wardrobe
│       ├── api/      # Service Riot API
│       └── config/   # Sécurité
└── frontend/         # Application Vue.js
    └── src/
        ├── views/    # Pages
        ├── stores/   # État Pinia
        └── router/   # Routes
```

## API Endpoints

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/auth/register` | Inscription | Non |
| POST | `/api/auth/login` | Connexion | Non |
| GET | `/api/skins/champions` | Liste des champions | Non |
| GET | `/api/skins/champions/{id}` | Détails champion + skins | Non |
| GET | `/api/skins?search=` | Recherche de skins | Non |
| GET | `/api/wardrobe` | Ma collection | Oui |
| POST | `/api/wardrobe` | Ajouter un skin | Oui |
| DELETE | `/api/wardrobe/{skinId}` | Retirer un skin | Oui |