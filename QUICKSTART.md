# StreamFlix - Quick Start Guide

## Your Services Are Already Running!

All microservices and databases are up and healthy. You're ready to showcase the UI!

---

## Access the UI

### Open your browser and go to:
```
http://localhost:8080/ui/index.html
```

---

## What You Can Do in the UI

### 1. Register a New User
   - Click "User Service" section
   - Fill in: Username, Email, Password, Tier
   - Click "Register User"
   - You'll see the created user with ID and subscription tier

### 2. Create Content (Movies/TV Series)
   - Click "Content Service" section
   - Choose type: MOVIE or TV_SERIES
   - Fill in title, description, genre, etc.
   - Click "Create Content"
   - Factory pattern creates the appropriate content type

### 3. Record Watch Events
   - Click "Video Service" section
   - Enter User ID and Content ID (from steps above)
   - Set progress in seconds (e.g., 3600 = 1 hour)
   - Mark as completed (true/false)
   - Click "Record Watch Event"
   - Observer pattern triggers - check Docker logs!

### 4. Rate Content
   - In "Video Service" section
   - Enter User ID and Content ID
   - Rate from 0.0 to 5.0
   - Click "Rate Content"
   - Observer pattern triggers again!

### 5. Get Recommendations
   - Click "Recommendation Service" section
   - Enter your User ID
   - Click "Get Recommendations"
   - Strategy pattern selects the best algorithm for you!

### 6. Test Singleton Pattern
   - Scroll to "Demo" section
   - Click "Run Singleton Test"
   - See proof that ConfigurationManager is a true singleton

---

## Complete Demo Flow

1. Open UI: http://localhost:8080/ui/index.html

2. Register a user (User Service section)
   - Username: demo_user
   - Email: demo@streamflix.com
   - Password: Demo123!
   - Tier: PREMIUM
   - Note the returned userId

3. Create a movie (Content Service section)
   - Type: MOVIE
   - Title: Inception
   - Description: Mind-bending thriller
   - Genre: Sci-Fi
   - Release Year: 2010
   - Duration: 148
   - Director: Christopher Nolan
   - Note the returned contentId

4. Record a watch event (Video Service section)
   - User ID: (from step 2)
   - Content ID: (from step 3)
   - Progress: 3600
   - Completed: true
   - Observer pattern triggers!

5. Rate the content (Video Service section)
   - User ID: (from step 2)
   - Content ID: (from step 3)
   - Score: 5.0
   - Observer pattern triggers again!

6. Get recommendations (Recommendation Service section)
   - User ID: (from step 2)
   - Limit: 5
   - Strategy pattern selects algorithm based on your activity!

7. Test Singleton (Demo section)
   - Click "Run Singleton Test"
   - See all 3 instances have identical hash codes

---

## If You Need to Restart Services

```bash
# Stop all services
docker-compose down

# Start all services
docker-compose up -d

# Wait 30 seconds for services to be healthy, then check
docker-compose ps

# View logs for any service
docker-compose logs -f api-gateway
docker-compose logs -f user-service
```

---

## See Design Patterns in Action

### Singleton Pattern (User Service)
```bash
docker-compose logs user-service | grep "ConfigurationManager"
```

### Factory Pattern (Content Service)
```bash
docker-compose logs content-service | grep -i "factory"
```

### Observer Pattern (Video Service)
```bash
docker-compose logs video-service | grep -i "observer"
```

### Strategy Pattern (Recommendation Service)
```bash
docker-compose logs recommendation-service | grep -i "strategy"
```

---

## Service URLs

| Service | URL | Description |
|---------|-----|-------------|
| UI | http://localhost:8080/ui/index.html | Web interface |
| API Gateway | http://localhost:8080/api | Routes all API calls |
| Gateway Health | http://localhost:8080/actuator/health | Check all services |
| User Service | http://localhost:8081 | User management (Singleton) |
| Content Service | http://localhost:8082 | Content management (Factory) |
| Video Service | http://localhost:8083 | Watch events & ratings (Observer) |
| Recommendation | http://localhost:8084 | Recommendations (Strategy) |

---

## Troubleshooting

### UI shows 404?
The API Gateway was recently rebuilt with UI support. It's working now!

### API calls fail?
Check if all services are healthy:
```bash
docker-compose ps
```
All services should show "Up X hours (healthy)"

### Need fresh start?
```bash
docker-compose down
docker-compose up -d
# Wait 30 seconds
```

---

Open http://localhost:8080/ui/index.html and start showcasing!
