# Davon Library Management System

A full-stack library management system built with enterprise-grade technologies. It supports catalog browsing, borrowing/returning, reservations, user management, and comprehensive admin features.

## 🏗️ Tech Stack

### Backend
- **Runtime**: Java 17
- **Framework**: Quarkus 3.6.4 (Supersonic Subatomic Java)
- **ORM**: Hibernate ORM with Panache (Active Record pattern)
- **REST API**: RESTEasy Reactive with Jackson (JSON serialization)
- **Validation**: Hibernate Validator
- **Security**: BCrypt (jbcrypt 0.4) for password hashing
- **Database Driver**: Microsoft SQL Server JDBC 12.4.2
- **Build Tool**: Maven 3 (via Maven Wrapper)
- **Code Generation**: Lombok 1.18.32
- **API Documentation**: OpenAPI 3.0 + Swagger UI

### Frontend
- **Framework**: Next.js 15.3.2 (React 19)
- **Language**: TypeScript 5
- **Styling**: Tailwind CSS 3.4.17
- **Icons**: Lucide React 0.534.0
- **HTTP Client**: Fetch API with custom service layer
- **State Management**: React Context API
- **Code Quality**: ESLint 9 with Next.js config

### Database
- **RDBMS**: Microsoft SQL Server 2016+
- **Schema**: 23 normalized tables (3NF)
- **Features**: Stored procedures, views, triggers, indexes
- **Design**: Comprehensive referential integrity with foreign keys

### Development & Testing
- **Backend Testing**: JUnit 5, REST Assured, Mockito
- **Code Quality**: Checkstyle 10.12.4, PMD 3.21.2, SpotBugs 4.8.2, Spotless 2.43.0
- **Visual Testing**: Puppeteer 24.17.0 with pixelmatch
- **Containerization**: Docker (JVM, Native, Native-Micro variants)
- **Debug Tools**: Custom shell scripts for backend/frontend/full-stack debugging

## 📋 Prerequisites

- **Java**: JDK 17 or newer
- **Node.js**: 18+ (Node 20+ recommended)
- **Database**: Microsoft SQL Server 2016+ running locally
  - Default credentials: `sa` / `LibraryDB123!`
  - Database name: `LibraryManagementSystem`

## 📁 Project Structure

```
davon-library-system/
├── backend/                          # Quarkus Backend (Java 17)
│   ├── src/main/java/com/davonlibrary/
│   │   ├── entity/                   # JPA entities (Books, Users, Loans, etc.)
│   │   ├── repository/               # Panache repositories
│   │   ├── service/                  # Business logic layer
│   │   ├── resource/                 # REST endpoints (JAX-RS)
│   │   ├── dto/                      # Data Transfer Objects
│   │   ├── dao/                      # Data Access Objects
│   │   └── filter/                   # HTTP filters (CORS, etc.)
│   ├── src/main/resources/
│   │   └── application.properties    # Quarkus configuration
│   ├── src/test/java/                # JUnit tests
│   ├── pom.xml                       # Maven dependencies
│   ├── checkstyle.xml                # Code style rules
│   └── spotbugs-exclude.xml          # Static analysis exclusions
│
├── database/                         # Database Schema & Scripts
│   ├── schema.sql                    # Complete database schema
│   ├── sample_data.sql               # Sample data for testing
│   ├── queries.sql                   # Common queries (34+)
│   └── *.sql                         # Migration & fix scripts
│
├── davon-library-webui/              # Next.js Frontend
│   ├── src/app/
│   │   ├── components/               # Reusable React components
│   │   ├── services/                 # API service layer
│   │   ├── context/                  # React Context (Auth)
│   │   ├── admin/                    # Admin pages (books, users)
│   │   ├── catalog/                  # Book catalog & details
│   │   ├── profile/                  # User profile & loans
│   │   └── login/register/           # Authentication pages
│   ├── prisma/                       # Database schema (optional)
│   ├── tailwind.config.js            # Tailwind CSS config
│   └── tsconfig.json                 # TypeScript config
│
├── davon-library-landing-page/       # Static Marketing Page
│   ├── index.html                    # Landing page
│   └── style.css                     # Custom styles
│
├── puppeteer-landing-page/           # Visual Testing Suite
│   ├── index.js                      # Puppeteer test scripts
│   └── public/screenshots/           # Test screenshots
│
├── debug-*.sh                        # Development helper scripts
├── kill-debug-ports.sh               # Port cleanup script
└── README.md                         # This file
```

## 🗄️ Database Setup

### 1. Create Database

```sql
CREATE DATABASE LibraryManagementSystem;
USE LibraryManagementSystem;
```

### 2. Run Schema Scripts

Execute the following scripts in order:
1. `database/schema.sql` - Creates all 23 tables, indexes, constraints
2. `database/sample_data.sql` - Populates with test data

### 3. Backend Configuration

Connection details are in `backend/src/main/resources/application.properties`:

```properties
quarkus.datasource.jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=LibraryManagementSystem;encrypt=true;trustServerCertificate=true
quarkus.datasource.username=sa
quarkus.datasource.password=LibraryDB123!
```

### Database Features
- **23 Tables**: Normalized design (3NF) with 9 enumeration tables
- **Stored Procedures**: `sp_borrow_book`, `sp_return_book`, `sp_search_books`
- **Views**: Available books, overdue loans, active reservations, outstanding fines
- **Triggers**: Automatic status updates for book copies and loan returns
- **Indexes**: Strategic indexing on search fields, foreign keys, and dates

## 🚀 Running Locally

### Backend (Quarkus Dev Mode)

```bash
cd backend
./mvnw compile quarkus:dev
```

**Available endpoints:**
- REST API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI Spec: http://localhost:8080/openapi
- Dev UI: http://localhost:8080/q/dev/

**Features enabled:**
- Hot reload for code changes
- Continuous testing mode
- Dev services (if configured)
- Remote debugging on port 5005

### Frontend (Next.js)

```bash
cd davon-library-webui
npm install
npm run dev
```

**Available at:**
- Next.js App: http://localhost:3000
- Hot Module Replacement (HMR) enabled
- TypeScript type checking in watch mode

### Quick Start (Full Stack)

Use the convenience scripts:

```bash
# Start backend only
./debug-backend.sh

# Start frontend only
./debug-frontend.sh

# Start both (recommended)
./debug-full-stack.sh

# Clean up ports if needed
./kill-debug-ports.sh
```

## 🎯 Core Features

### User Management
- User registration and authentication (BCrypt password hashing)
- Role-based access control (USER, ADMIN)
- Profile management with loan history
- Multi-library membership support

### Catalog & Search
- Browse book catalog with pagination
- Search by title, author, ISBN
- Filter by availability, genre, library
- Book details with copy availability

### Borrowing System
- **Borrow Flow**: Users can borrow books when copies are available
  - Backend validates availability and updates `Book.availableCopies`
  - Marks `BookCopy` status as `CHECKED_OUT`
  - Creates `Loan` record with due date (14-day default)
  - Supports loan extensions
  
- **Return Flow**: Users return books via profile page
  - Updates loan `returnDate` and marks copy as `AVAILABLE`
  - Automatically calculates and creates fines for overdue books
  - Updates book status back to `AVAILABLE`

### Reservation Queue
- Reserve unavailable books and join queue
- Queue position tracking (`queuePosition`)
- Automatic notification when book becomes available
- Duplicate reservation prevention (HTTP 409)
- Cancel reservations at any time

### Admin Features
- User management (view, edit, suspend accounts)
- Book inventory management (add, edit, delete books)
- View all loans and reservations
- Generate reports and analytics

### Notifications
- Email notifications for due dates
- Overdue reminders
- Reservation availability alerts
- Priority-based delivery (urgent, high, normal, low)

## 📡 API Reference

**Base URL**: `http://localhost:8080/api`  
**Documentation**: http://localhost:8080/swagger-ui

### Authentication
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: { "userId": 1, "email": "...", "role": "USER" }
```

```http
POST /auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Books
```http
GET /books?page=0&size=20                    # List books with pagination
GET /books/{id}                               # Get book details
GET /books/search?query=harry&available=true  # Search books
POST /books                                   # Create book (admin only)
PUT /books/{id}                               # Update book (admin only)
DELETE /books/{id}                            # Delete book (admin only)
```

### Library Actions
```http
POST /library/borrow
Content-Type: application/json

{
  "userId": 1,
  "bookId": 5
}

Responses:
  200 - Success, returns LoanDTO
  409 - No available copies
  404 - Book or user not found
```

```http
POST /library/reserve
Content-Type: application/json

{
  "userId": 1,
  "bookId": 5
}

Responses:
  201 - Success, returns ReservationDTO
  409 - User already has active reservation for this book
  409 - Book is currently available (should borrow instead)
```

### Loans
```http
GET /loans/user/{userId}                # User's active loans
PUT /loans/{id}/return                  # Return a book
GET /loans/recent                       # Recent loans (admin)
GET /loans/loaned-out/count             # Total active loans (admin)
GET /loans/overdue/count                # Overdue loan count (admin)
PUT /loans/{id}/extend                  # Extend loan period
```

### Reservations
```http
GET /reservations/user/{userId}                      # User's reservations with queue position
GET /reservations/queue-position?userId=1&bookId=5   # Check queue position
PUT /reservations/{id}/cancel                        # Cancel reservation
GET /reservations/active                             # All active reservations (admin)
```

### Users
```http
GET /users                               # List all users (admin)
GET /users/{id}                          # Get user details
PUT /users/{id}                          # Update user
DELETE /users/{id}                       # Delete user (admin)
GET /users/{id}/loans                    # User's loan history
GET /users/{id}/fines                    # User's fines
```

### DTOs

**LoanDTO**
```typescript
{
  id: number;
  title: string;
  dueDate: string;          // ISO date
  returnedDate: string | null;
  daysLeft: number | null;  // negative if overdue, null if returned
}
```

**ReservationDTO**
```typescript
{
  id: number;
  bookId: number;
  bookTitle: string;
  reservationDate: string;  // ISO date
  status: string;           // ACTIVE, CANCELLED, FULFILLED
  queuePosition: number | null;
}
```

**BookDTO**
```typescript
{
  id: number;
  title: string;
  author: string;
  isbn: string;
  totalCopies: number;
  availableCopies: number;
  status: string;           // AVAILABLE, UNAVAILABLE
  publishedYear: number;
  genre: string;
}
```

## 🎨 Frontend Architecture

### Technology Stack
- **Framework**: Next.js 15 with App Router
- **UI Library**: React 19 with TypeScript
- **Styling**: Tailwind CSS 3.4 (utility-first CSS)
- **Icons**: Lucide React (optimized icon library)
- **State Management**: React Context API for authentication
- **Form Handling**: Controlled components with validation
- **Routing**: File-based routing with dynamic routes

### Key Pages
- `/landing` - Marketing landing page
- `/catalog` - Book catalog with search & filters
- `/catalog/[id]` - Book detail page with borrow/reserve actions
- `/profile` - User dashboard (loans, history, reservations)
- `/login` & `/register` - Authentication pages
- `/admin` - Admin dashboard
- `/admin/books` - Book management
- `/admin/users` - User management

### Features
- **Responsive Design**: Mobile-first, works on all screen sizes
- **Error Handling**: Graceful error messages for user actions
- **Loading States**: Skeleton loaders and loading indicators
- **Form Validation**: Client-side validation before API calls
- **Pagination**: Server-side pagination for large datasets
- **Search & Filter**: Real-time search with debouncing

## 🛠️ Development Tools & Code Quality

### Backend Tools
- **Checkstyle**: Enforces Java code style (Google style guide)
- **PMD**: Static code analysis (currently disabled)
- **SpotBugs**: Bug pattern detection (currently disabled)
- **Spotless**: Automatic code formatting
- **Lombok**: Reduces boilerplate (getters, setters, builders)
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework for tests
- **REST Assured**: REST API testing

### Frontend Tools
- **ESLint**: TypeScript/React linting
- **TypeScript**: Static type checking
- **Puppeteer**: Visual regression testing
- **PostCSS**: CSS transformations with Autoprefixer

### Build Commands

**Backend:**
```bash
./mvnw clean compile               # Compile
./mvnw test                         # Run tests
./mvnw quarkus:dev                  # Dev mode with hot reload
./mvnw package                      # Build JAR
./mvnw spotless:apply               # Format code
./mvnw checkstyle:check             # Check code style
```

**Frontend:**
```bash
npm run dev                         # Development server
npm run build                       # Production build
npm run start                       # Start production server
npm run lint                        # Run ESLint
```

## 💡 Development Best Practices

### Backend
- Follow Repository pattern with Panache
- Use DTOs for API responses (never expose entities)
- Validate inputs with Hibernate Validator annotations
- Handle errors with appropriate HTTP status codes
- Log business operations at INFO level
- Write unit tests for service layer

### Frontend
- Use TypeScript strictly (no `any` types)
- Component structure: components → services → API
- Extract reusable logic into custom hooks
- Use proper error boundaries
- Implement loading and error states
- Keep components small and focused

### Database
- Never modify schema directly in production
- Use migration scripts for all changes
- Test scripts on local environment first
- Maintain referential integrity
- Create indexes for frequently queried columns
- Use stored procedures for complex operations

## 🐛 Troubleshooting

### Common Issues

**Backend won't start**
```bash
# Check if port 8080 is already in use
lsof -i :8080

# Kill process if needed
kill -9 <PID>

# Or use the cleanup script
./kill-debug-ports.sh
```

**Database connection fails**
- Verify SQL Server is running: `docker ps` or check Windows Services
- Test connection: `sqlcmd -S localhost -U sa -P LibraryDB123!`
- Check `application.properties` for correct credentials
- Ensure database `LibraryManagementSystem` exists

**CORS errors in browser**
- Verify backend CORS config includes frontend origin
- Default origins: `http://localhost:3000`, `http://localhost:3001`
- Check browser console for exact origin being blocked
- Restart backend after changing CORS settings

**Maven build fails**
```bash
# Clean and rebuild
./mvnw clean install -DskipTests

# If Lombok issues, update IDE plugin
# IntelliJ: Enable annotation processing in Settings
```

**Frontend build errors**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install

# Check Node.js version
node --version  # Should be 18+
```

**Duplicate reservation error (409)**
- This is expected behavior if user already has active reservation
- Frontend shows friendly message to user
- Check Profile page to view existing reservations

**Book can't be borrowed (409)**
- No available copies - use Reserve instead
- Check book details page for `availableCopies` count
- Admin can add more copies via admin panel

### Debug Mode

**Backend debugging (port 5005):**
```bash
./debug-backend.sh

# Attach debugger in IntelliJ:
# Run → Attach to Process → localhost:5005
```

**Check backend logs:**
```bash
cd backend
./mvnw quarkus:dev

# SQL logging is enabled by default
# Check console for Hibernate SQL statements
```

**Frontend debugging:**
- Open browser DevTools (F12)
- Check Console tab for errors
- Check Network tab for API calls
- Use React DevTools extension

## 📜 Helper Scripts

All scripts located in project root:

| Script | Purpose |
|--------|---------|
| `debug-backend.sh` | Start backend with enhanced logging |
| `debug-frontend.sh` | Start frontend in development mode |
| `debug-full-stack.sh` | Start both backend and frontend |
| `kill-debug-ports.sh` | Clean up ports 3000, 8080, 5005 |
| `backend/scripts/manage-ports.sh` | Advanced port management |

## 🐳 Docker Support

Multiple Dockerfile variants available in `backend/src/main/docker/`:

- `Dockerfile.jvm` - Standard JVM mode (recommended)
- `Dockerfile.native` - GraalVM native image (fast startup)
- `Dockerfile.native-micro` - Minimal native image (smallest size)
- `Dockerfile.legacy-jar` - Legacy JAR format

Build example:
```bash
# Build JVM image
cd backend
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t library-backend:jvm .

# Run container
docker run -p 8080:8080 library-backend:jvm
```

## 📚 Additional Documentation

- `backend/README.md` - Backend-specific documentation
- `backend/MSSQL_SETUP_GUIDE.md` - Detailed database setup
- `backend/PORT_MANAGEMENT.md` - Port configuration guide
- `backend/CURSOR_IMPORT_FIX.md` - IDE import troubleshooting
- `database/README.md` - Comprehensive database documentation
- `database/QUOTED_IDENTIFIER_FIX.md` - SQL Server configuration
- `DEBUG_SETUP.md` - Debugging configuration guide

## 🔐 Security Notes

- **Passwords**: Hashed with BCrypt (work factor: 10)
- **HTTPS**: Disabled in development (enable for production)
- **CORS**: Restricted to localhost in development
- **SQL Injection**: Prevented via Hibernate ORM/parameterized queries
- **Authentication**: Session-based (consider JWT for production)
- **Authorization**: Role-based access control (USER, ADMIN)

**Production Checklist:**
- [ ] Enable HTTPS/TLS
- [ ] Configure proper CORS origins
- [ ] Use environment variables for secrets
- [ ] Enable all code quality checks (PMD, SpotBugs)
- [ ] Set up proper logging and monitoring
- [ ] Configure database connection pooling
- [ ] Enable Quarkus native compilation for performance
- [ ] Set up automated backups

## 📊 Testing

### Backend Tests
```bash
cd backend
./mvnw test                    # Run all tests
./mvnw test -Dtest=UserServiceTest  # Run specific test
./mvnw verify                  # Run integration tests
```

### Visual Testing
```bash
cd puppeteer-landing-page
npm install
node index.js                  # Run visual regression tests
```

## 🤝 Contributing

This is an internal training project for the Davon Internship Program.

**Code Style:**
- Backend: Google Java Style Guide (enforced by Checkstyle)
- Frontend: ESLint + Prettier (Next.js defaults)
- Commit messages: Conventional Commits format

## 📝 License

Internal training project for the Davon Internship Program.

---

**Version**: 1.0.0-SNAPSHOT  
**Last Updated**: December 2024  
**Java Version**: 17  
**Quarkus Version**: 3.6.4  
**Next.js Version**: 15.3.2

