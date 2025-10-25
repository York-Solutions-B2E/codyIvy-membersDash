# Member Benefits Dashboard

A full-stack web application for managing member benefits, claims, and health plan information. Built with React frontend and Spring Boot backend.

## 🚀 Features

### Authentication & Security
- **Google OAuth Integration** - Secure single sign-on with Google accounts
- **JWT Token Authentication** - Stateless authentication with JSON Web Tokens
- **Protected Routes** - Frontend route protection based on authentication status

### Dashboard
- **Active Plan Overview** - Current health plan details including coverage dates and network information
- **Benefit Accumulators** - Real-time tracking of deductibles, out-of-pocket maximums, and other benefit limits
- **Recent Claims Summary** - Quick view of the 5 most recent claims with status and amounts

### Claims Management
- **Claims List View** - Paginated list of all member claims with sorting and filtering
- **Detailed Claim View** - Comprehensive claim details including:
  - Provider information and specialty
  - Financial breakdown (billed, allowed, paid amounts)
  - Line-by-line service details with CPT codes
  - Claim status history and processing timeline
  - Member responsibility calculations

### Technical Features
- **Responsive Design** - Mobile-friendly Material-UI components
- **Clean Architecture** - Service layer pattern with proper separation of concerns
- **Custom Exception Handling** - Meaningful error messages and proper error boundaries
- **Database Integration** - PostgreSQL with JPA/Hibernate for data persistence
- **RESTful API** - Well-structured REST endpoints with proper HTTP status codes

## 🛠️ Technology Stack

### Frontend
- **React 19.1.1** - Modern React with hooks and functional components
- **React Router 7.9.4** - Client-side routing and navigation
- **Material-UI 7.3.4** - Google's Material Design component library
- **Vite** - Fast build tool and development server
- **Google OAuth Library** - OAuth integration for authentication

### Backend
- **Spring Boot 3.5.6** - Enterprise Java framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **PostgreSQL 15** - Relational database
- **Maven** - Dependency management and build tool
- **Lombok** - Code generation for boilerplate reduction

### Infrastructure
- **Docker Compose** - Database containerization
- **JWT** - Stateless authentication tokens

## 📋 Prerequisites

- **Java 21** or higher
- **Node.js 18** or higher
- **npm** or **yarn**
- **Docker** and **Docker Compose**

## ⚠️ Required Setup Before Running

**This application will NOT work out of the box!** You need to:

1. **Set up Google OAuth credentials** (most important step)
2. **Configure environment variables** using the provided example files
3. **Copy `.env.example` files and add your credentials**

See the [Environment Setup](#environment-setup) section below, or use our [Quick Local Demo](#quick-local-demo) approach to bypass authentication for testing.

## 🚀 Quick Local Demo (Skip OAuth for Testing)

If you just want to see the app running locally without setting up OAuth:

1. **Start the database**: `docker compose up`
2. **Start backend**: `cd backend && ./mvnw spring-boot:run`
3. **Start frontend**: `cd frontend && npm run dev`
4. **Access test endpoint**: Visit `http://localhost:8080/api/test-data/user/test@example.com` to see sample data

> **Note**: Without OAuth, the main UI won't work since it requires authentication. You can only access the test endpoints.

## 🔧 Full Setup Instructions (With Authentication)

### 1. Clone the Repository
```bash
git clone <repository-url>
cd codyivy-members
```

### 2. Database Setup
Start the PostgreSQL database using Docker Compose:
```bash
docker compose up
```

This will start a PostgreSQL container with:
- Database: `memberdb`
- Username: `devuser`
- Password: `devpass`
- Port: `5432`

### 3. Google Cloud Console Setup

Before configuring the application, you need to create OAuth credentials in Google Cloud Console:

#### Step 1: Create a Google Cloud Project
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Click **"Select a project"** → **"New Project"**
3. Enter a project name (e.g., "Member Benefits App")
4. Click **"Create"**

#### Step 2: Enable Google+ API
1. In your project, go to **"APIs & Services"** → **"Library"**
2. Search for **"Google+ API"** or **"People API"**
3. Click on it and press **"Enable"**

#### Step 3: Create OAuth 2.0 Credentials
1. Go to **"APIs & Services"** → **"Credentials"**
2. Click **"Create Credentials"** → **"OAuth 2.0 Client IDs"**
3. If prompted, configure the **OAuth consent screen** first:
   - Choose **"External"** user type
   - Fill in required fields (App name, User support email, Developer email)
   - Add your email to **"Test users"** section
4. For **Application type**, select **"Web application"**
5. Add these **Authorized redirect URIs** (for local development):
   - `http://localhost:5173` (frontend - where Google redirects after auth)

> **Important**: Only add the frontend URL. The frontend handles the OAuth callback and then communicates with the backend API.

6. Click **"Create"**
7. **Copy your Client ID and Client Secret** - you'll need these for the `.env` files

### 4. Environment Setup

#### Backend Configuration
1. Create the environment file:
```bash
cd backend
cp .env.example .env
```

2. Edit `backend/.env` with your Google OAuth credentials:
```env
GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here
GOOGLE_REDIRECT_URI=http://localhost:5173
```

#### Frontend Configuration
1. Create the environment file:
```bash
cd frontend
cp .env.example .env
```

2. Edit `frontend/.env` with your Google Client ID:
```env
VITE_GOOGLE_CLIENT_ID=your_google_client_id_here
```

### 5. Backend Setup

#### Start the Backend
```bash
cd backend

# Load environment variables from .env file and start the backend
export $(grep -v '^#' .env | xargs) && ./mvnw spring-boot:run
```

> **Note**: The `export` command loads your OAuth credentials from the `.env` file before starting Spring Boot.

The backend will be available at `http://localhost:8080`

### 6. Frontend Setup

#### Install Dependencies
```bash
cd frontend
npm install
```

#### Start the Frontend
```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

## 🔑 Environment Variables

The application uses environment variables for secure credential management. Example files are provided:

### Backend (`backend/.env.example`)
```env
GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here
JWT_SECRET=your_jwt_secret_here
```

### Frontend (`frontend/.env.example`)
```env
VITE_GOOGLE_CLIENT_ID=your_google_client_id_here
```

> **Note**: The `.env` files are excluded from version control for security. Copy the `.env.example` files and add your actual credentials.


## 🚦 Running the Application

1. **Start Database**: `docker compose up`
2. **Configure Environment**: Copy and edit `.env.example` files (see [Environment Setup](#environment-setup))
3. **Start Backend**: `cd backend && ./mvnw spring-boot:run`
4. **Start Frontend**: `cd frontend && npm run dev`

Visit `http://localhost:5173` and sign in with your Google account to access the application.

## 📁 Project Structure

```
codyivy-members/
├── backend/
│   ├── src/main/java/com/example/benefits/
│   │   ├── controller/          # REST API endpoints
│   │   ├── service/             # Business logic layer
│   │   ├── repository/          # Data access layer
│   │   ├── model/               # JPA entities and exceptions
│   │   ├── dto/                 # Data transfer objects
│   │   └── config/              # Security and configuration
│   └── src/main/resources/
│       └── application.properties
├── frontend/
│   ├── src/
│   │   ├── components/          # Reusable UI components
│   │   ├── pages/               # Page-level components
│   │   └── assets/              # Static assets
│   ├── package.json
│   └── vite.config.js
└── docker-compose.yml           # Database container setup
```

## 🔍 API Endpoints

### Authentication
- `POST /api/auth/google` - Google OAuth authentication
- `GET /api/auth/me` - Get current user information

### Dashboard
- `GET /api/dashboard` - Get dashboard data (plans, accumulators, recent claims)

### Claims
- `GET /api/claims` - Get paginated claims list
- `GET /api/claims/{claimNumber}` - Get detailed claim information

### Test Data
- `GET /api/test-data/user/{email}` - Get all data for a user (development only)

## 🧪 Development Features

- **Auto-reload** - Both frontend and backend support hot reloading during development
- **Test Data Generation** - Automatic dummy data creation for new users
- **Database Recreation** - Database schema is recreated on each startup for development

## 🚨 Troubleshooting

### OAuth Redirect URI Mismatch Error

If you get a "redirect_uri_mismatch" error, ensure your Google Cloud Console settings match exactly:

1. In Google Cloud Console → **APIs & Services** → **Credentials**
2. Edit your OAuth 2.0 Client ID
3. In **Authorized redirect URIs**, make sure you have:
   - `http://localhost:5173` (this is where Google redirects after authentication)

> **Note**: The app is configured to redirect to the frontend (`localhost:5173`), not the backend. The frontend then handles the OAuth response and communicates with the backend API.

### Common Issues:
- ✅ **Correct**: `http://localhost:5173`
- ❌ **Wrong**: `http://localhost:8080/api/auth/google/callback`
- ❌ **Wrong**: `https://localhost:5173` (no HTTPS in development)
- ❌ **Wrong**: Missing the exact port number
