# 🍽️ Cheko Restaurant Backend

A comprehensive Spring Boot backend application for Cheko Restaurant system with menu management, location services, and advanced search capabilities.

## 🏗️ Architecture

This application follows the **Layered Architecture** pattern with clear separation of concerns:

```
com.cheko.backend/
├── ChekoBackendApplication.java    # Main Application Class
├── model/                          # Entity Layer (JPA Entities)
├── repository/                     # Data Access Layer
├── service/                        # Business Logic Layer
├── controller/                     # REST API Layer
├── dto/                           # Data Transfer Objects
├── config/                        # Configuration Layer
└── util/                          # Utility Classes
```

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## 🗄️ Database Schema

### Core Tables:
- **categories** - Menu categories (Soup, Rice, Others)
- **branches** - Restaurant branches
- **locations** - Geographic data for branches (Mapbox integration)
- **items** - Menu items with search/filter capabilities

### Key Features:
- ✅ **Soft Delete** - All tables support soft deletion
- ✅ **Audit Trail** - Created/Updated timestamps
- ✅ **Best Seller Tracking** - Order count-based calculation
- ✅ **Calorie Analysis** - Second-highest calorie queries

## 🚀 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Docker & Docker Compose (optional)

### Database Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cheko
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Running the Application

#### Option 1: Using Docker Compose (Recommended)
```bash
# Start PostgreSQL and the application
docker-compose up -d

# View logs
docker-compose logs -f cheko-backend
```

#### Option 2: Local Development
```bash
# Ensure PostgreSQL is running locally
# Create database 'cheko'
createdb cheko

# Run the application
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🌐 API Endpoints

### 🏠 Menu APIs (Home Tab)
```
GET    /api/menu/items                              # All menu items (paginated)
GET    /api/menu/items/{id}                         # Item details (popup)
GET    /api/menu/items/search?q=soup                # Search by name/description
GET    /api/menu/items/filter?type=Soup             # Filter by category
GET    /api/menu/items/combined?q=soup&type=Rice    # Combined search + filter
GET    /api/menu/items/best-sellers                 # Best seller items
POST   /api/menu/items/{id}/order                   # Increment order count
GET    /api/menu/categories/counts                  # Category item counts (cards)
GET    /api/menu/second-highest-calorie             # 🎯 Special requirement
```

### 🗺️ Map APIs (Map Tab)
```
GET    /api/map/markers                             # All branch markers
GET    /api/map/markers/search?q=downtown           # Search markers
GET    /api/map/markers/filter?city=Riyadh          # Filter markers
GET    /api/map/markers/combined?q=mall&city=Riyadh # Combined search + filter
GET    /api/map/markers/nearby?lat=24.7&lng=46.6&radius=5 # Nearby locations
GET    /api/map/cities                              # Available cities
GET    /api/map/states                              # Available states
```

### 🔧 Utility APIs
```
GET    /api/health                                  # Health check
GET    /api/                                        # Welcome message
```

## 🎯 Key Features Implementation

### ✅ Search Functionality
- **Requirement**: Search by menu item `name` OR `description` containing word
- **Implementation**: Case-insensitive ILIKE with OR condition
- **Endpoint**: `GET /api/menu/items/search?q=soup`

### ✅ Filter by Dish Type  
- **Requirement**: Filter by categories (Soup, Rice, Others)
- **Implementation**: JOIN with categories table
- **Endpoint**: `GET /api/menu/items/filter?type=Soup`

### ✅ Category Count Cards
- **Requirement**: Display number of items in each category
- **Implementation**: GROUP BY query with COUNT
- **Endpoint**: `GET /api/menu/categories/counts`
- **Response**: `{"Soup": 12, "Rice": 8, "Others": 15, "total": 35}`

### ✅ Second-Highest Calorie Analysis
- **Requirement**: Backend service for second-highest calorie meal per category
- **Implementation**: LIMIT 1 OFFSET 1 with ORDER BY calories DESC
- **Endpoint**: `GET /api/menu/second-highest-calorie`

### ✅ Best Seller System
- **Logic**: Based on `total_orders` count (auto-calculated)
- **Scheduled Task**: Updates every hour
- **Manual Refresh**: `PUT /api/menu/best-sellers/refresh`

### ✅ Map Integration (Mapbox GL JS Ready)
- **Markers**: All branch locations with lat/lng
- **Search**: Branch name, address, description
- **Filter**: City, state, active status
- **Nearby**: Radius-based search using Haversine formula
- **Tooltips**: HTML popup content included

## 📊 Sample Data

The application includes comprehensive sample data:
- **3 Categories**: Soup, Rice, Others
- **3 Branches**: Downtown, Mall, North locations
- **15 Menu Items**: 5 items per category with realistic data
- **Geographic Data**: Riyadh-based coordinates for Mapbox
- **Best Sellers**: Auto-calculated based on order counts

## 🧪 Testing the API

### Health Check
```bash
curl http://localhost:8080/api/health
```

### Search Example
```bash
curl "http://localhost:8080/api/menu/items/search?q=chicken"
```

### Second-Highest Calorie
```bash
curl http://localhost:8080/api/menu/second-highest-calorie
```

### Map Markers
```bash
curl http://localhost:8080/api/map/markers
```

## 📚 **Swagger/OpenAPI Documentation**

### **Interactive API Documentation:**
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs JSON**: `http://localhost:8080/api-docs`

### **Features:**
- ✅ **Interactive Testing** - Try all endpoints directly from the browser
- ✅ **Complete Documentation** - All endpoints with descriptions and examples  
- ✅ **Request/Response Examples** - See expected data formats
- ✅ **Parameter Documentation** - Understand all query parameters
- ✅ **Business Requirements Highlighted** - Special annotations for key features

### **Key Endpoints in Swagger:**

#### **🏠 Menu Management**
- **Search Items** - `GET /api/menu/items/search?q=soup`
- **Filter Items** - `GET /api/menu/items/filter?type=Soup`
- **🎯 Second-Highest Calorie** - `GET /api/menu/second-highest-calorie`
- **Category Counts** - `GET /api/menu/categories/counts`

#### **🗺️ Map & Location Services**  
- **All Markers** - `GET /api/map/markers`
- **Search Markers** - `GET /api/map/markers/search?q=downtown`
- **Nearby Search** - `GET /api/map/markers/nearby?lat=24.7&lng=46.6&radius=5`

#### **🔧 Health & System**
- **Health Check** - `GET /api/health`

## 🔧 Configuration

### CORS Configuration
- Configured for React frontend integration
- Allows all origins in development
- Supports credentials and all HTTP methods

### Scheduling
- Best seller recalculation runs every hour
- Can be manually triggered via API

### Soft Delete
- All entities support soft deletion
- Uses `@SQLDelete` and `@Where` annotations
- Deleted records excluded from queries automatically

## 🐳 Docker Support

### Services:
- **cheko-backend**: Spring Boot application
- **postgres**: PostgreSQL database with sample data

### Volumes:
- **postgres_data**: Persistent database storage

### Networks:
- **cheko-network**: Internal communication

## 📝 Development Notes

### Database Migrations
- Uses Hibernate DDL auto-update
- Sample data loaded via `init-db.sql`
- Schema changes handled automatically

### Performance Considerations
- Pagination implemented for all list endpoints
- Indexed queries for search operations
- Lazy loading for entity relationships

### Frontend Integration
- CORS configured for React development
- RESTful API design
- Consistent DTO structure

## 🚀 Production Deployment

1. **Environment Variables**:
   ```bash
   SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/cheko
   SPRING_DATASOURCE_USERNAME=prod_user
   SPRING_DATASOURCE_PASSWORD=secure_password
   ```

2. **Security Considerations**:
   - Restrict CORS origins
   - Enable HTTPS
   - Use connection pooling
   - Implement authentication for admin endpoints

3. **Monitoring**:
   - Health check endpoint available
   - Application metrics via Spring Actuator
   - Database connection monitoring

---

## 🎉 Success! 
**Cheko Restaurant Backend is fully implemented and ready for frontend integration!**

**Key Achievements:**
- ✅ Complete Layered Architecture
- ✅ All business requirements implemented  
- ✅ PostgreSQL integration with sample data
- ✅ Docker containerization ready
- ✅ Mapbox GL JS integration prepared
- ✅ React frontend ready APIs
