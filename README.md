# WordMeaningCollector

A modern, high-performance web application that extracts vocabulary from subtitle files and provides AI-powered word meanings using multiple dictionary APIs with intelligent fallback mechanisms.

![Java](https://img.shields.io/badge/Java-21+-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3+-brightgreen)
![React](https://img.shields.io/badge/React-18+-blue)
![TypeScript](https://img.shields.io/badge/TypeScript-5+-blue)
![Virtual Threads](https://img.shields.io/badge/Virtual_Threads-21+-purple)

## 📋 Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Usage](#usage)
- [Architecture](#architecture)
  - [High Level Design (HLD)](#high-level-design-hld)
  - [Low Level Design (LLD)](#low-level-design-lld)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Development](#development)
- [Testing](#testing)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

WordMeaningCollector is an intelligent vocabulary extraction tool that analyzes subtitle files (.txt, .srt, .sub, .vtt) and provides comprehensive word meanings using multiple AI-powered dictionary APIs. The system features:

- **Advanced File Processing**: Extracts unique words from subtitle files
- **AI-Powered Meanings**: Uses multiple dictionary APIs with intelligent fallbacks
- **High Performance**: Leverages Java 21+ Virtual Threads for concurrent processing
- **Modern UI**: Beautiful React TypeScript frontend with drag-and-drop functionality
- **Real-time Feedback**: Live processing status and progress updates

## 🛠️ Technology Stack

### Backend
- **Java 21+** - Latest LTS with Virtual Threads support
- **Spring Boot 3.3+** - Modern web framework
- **Spring WebFlux** - Reactive web programming
- **Maven** - Dependency management and build tool
- **H2 Database** - In-memory database for development

### Frontend
- **React 18+** - Modern JavaScript library with hooks
- **TypeScript 5+** - Type-safe JavaScript
- **Vite** - Fast build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **Axios** - HTTP client for API calls
- **Lucide React** - Modern icon library

### External APIs
- **DictionaryAPI.dev** - Primary dictionary API
- **Datamuse API** - Secondary fallback API
- **Urban Dictionary API** - Informal definitions fallback

## ✨ Features

### Core Functionality
- ✅ **File Upload**: Drag-and-drop file upload interface
- ✅ **Multi-format Support**: .txt, .srt, .sub, .vtt files
- ✅ **Vocabulary Extraction**: Intelligent word extraction and deduplication
- ✅ **AI Dictionary Integration**: Multiple APIs with fallback support
- ✅ **CSV Export**: Automatic download of processed vocabulary
- ✅ **Real-time Status**: Live processing updates and progress indicators

### Performance Features
- ✅ **Virtual Threads**: Java 21+ virtual threads for high concurrency
- ✅ **Parallel Processing**: Concurrent API calls for maximum speed
- ✅ **Rate Limiting**: Intelligent API call management
- ✅ **Error Recovery**: Comprehensive error handling and retries
- ✅ **Memory Efficient**: Optimized resource usage

### User Experience
- ✅ **Responsive Design**: Works on desktop, tablet, and mobile
- ✅ **Modern UI**: Clean, professional interface with animations
- ✅ **Accessibility**: Keyboard navigation and screen reader support
- ✅ **Offline Ready**: Graceful degradation when APIs are unavailable
- ✅ **File Validation**: Client and server-side file type validation

## 📋 Prerequisites

### System Requirements
- **Operating System**: Windows 10+, macOS 10.15+, Linux (Ubuntu 18.04+)
- **Memory**: Minimum 4GB RAM, Recommended 8GB+
- **Storage**: 500MB free space

### Software Requirements
- **Java 21+ JDK** - [Download from Oracle](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- **Node.js 20+** - [Download from Node.js](https://nodejs.org/)
- **npm 10+** - Comes with Node.js
- **Git** - For cloning the repository

### Network Requirements
- **Internet Connection**: Required for dictionary API calls
- **Firewall**: Allow outbound HTTPS connections to:
  - `api.dictionaryapi.dev`
  - `api.datamuse.com`
  - `api.urbandictionary.com`

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/WordMeaningCollector.git
cd WordMeaningCollector
```

### 2. Backend Setup

#### Environment Configuration
```bash
# Set Java 21+ as default
export JAVA_HOME=/path/to/java21
export PATH=$JAVA_HOME/bin:$PATH

# Verify Java version
java -version  # Should show Java 21+
```

#### Build and Run Backend
```bash
# Navigate to project root
cd WordMeaningCollector

# Clean and compile
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

**Backend will start on:** `http://localhost:9999`

### 3. Frontend Setup

#### Install Dependencies
```bash
# Navigate to frontend directory
cd frontend

# Install all dependencies
npm install
```

#### Run Development Server
```bash
# Start development server
npm run dev
```

**Frontend will start on:** `http://localhost:5173`

### 4. Verify Installation

#### Test Backend Health
```bash
curl http://localhost:9999/actuator/health
# Should return: {"status":"UP"}
```

#### Test Frontend Accessibility
Open browser and visit: `http://localhost:5173`

## 📖 Usage

### Basic Workflow

1. **Access Application**
   - Open browser to `http://localhost:5173`

2. **Upload Subtitle File**
   - Drag and drop a subtitle file (.txt, .srt, .sub, .vtt)
   - Or click "Click to select a file" to browse

3. **Process Vocabulary**
   - Click "Process & Download CSV" button
   - Watch real-time processing status

4. **Download Results**
   - CSV file downloads automatically
   - Contains words and their meanings

### Advanced Usage

#### Custom Configuration
Edit `application.properties` for custom settings:
```properties
# API timeouts
api.read-timeout=5000
api.connect-timeout=10000
api.call-delay=5000

# Logging
logging.level.com.example.vocabulary=DEBUG
```

#### File Format Support
- **.txt**: Plain text files
- **.srt**: SubRip subtitle format
- **.sub**: MicroDVD subtitle format
- **.vtt**: WebVTT subtitle format

## 🏗️ Architecture

### High Level Design (HLD)

```
┌─────────────────────────────────────────────────────────────┐
│                    WordMeaningCollector                      │
│                          System                             │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Frontend  │    │   Backend   │    │  External   │     │
│  │   (React)   │◄──►│ (Spring)    │◄──►│    APIs     │     │
│  │             │    │             │    │             │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │ File Upload │    │ Processing  │    │ Dictionary  │     │
│  │  Interface  │    │   Engine    │    │    APIs     │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   H2 DB     │    │ Virtual     │    │   REST      │     │
│  │ (In-memory) │    │  Threads    │    │   APIs      │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

#### System Components

1. **Frontend Layer**: React TypeScript SPA with file upload UI
2. **API Gateway**: Spring Boot REST controllers
3. **Processing Engine**: Virtual threads for concurrent API calls
4. **Data Layer**: H2 database for temporary storage
5. **External APIs**: Multiple dictionary services with fallbacks

### Low Level Design (LLD)

#### Component Architecture

```
Frontend (React/TypeScript)
├── App.tsx (Main Component)
├── components/
│   ├── ui/ (Reusable UI Components)
│   └── sections/ (Page Sections)
├── hooks/
│   └── usePortfolioData.ts (Data Management)
├── constants/
│   └── api.ts (API Configuration)
└── types/ (TypeScript Definitions)

Backend (Spring Boot/Java 21+)
├── controllers/
│   ├── Controller.java (Main API Endpoints)
│   └── SpaController.java (Frontend Serving)
├── services/
│   ├── WordMeaningFinderService.java (API Integration)
│   ├── MeaningFinderApiCaller.java (API Caller)
│   └── TextFileToCsvConverter.java (File Processing)
├── config/
│   └── AppConfig.java (Configuration)
└── beans/ (Data Models)
```

#### Data Flow Architecture

```
1. File Upload
   User → Frontend → Backend → File Storage

2. Processing Pipeline
   File → Word Extraction → Deduplication → API Calls → CSV Generation

3. API Fallback Chain
   DictionaryAPI.dev → Datamuse API → Urban Dictionary → Error Handling

4. Response Flow
   CSV Generation → File Download → User Browser
```

#### Concurrency Model

```
Main Thread
├── Virtual Thread Pool (Executors.newVirtualThreadPerTaskExecutor())
│   ├── Thread-1: API Call to DictionaryAPI.dev
│   ├── Thread-2: API Call to Datamuse API
│   ├── Thread-3: File Processing
│   └── Thread-N: Concurrent Operations
└── Main Thread: Coordination & Response
```

## 📚 API Documentation

### Backend Endpoints

#### File Upload
```http
POST /api/upload
Content-Type: multipart/form-data

Parameters:
- file: MultipartFile (subtitle file)

Response:
200 OK - File uploaded successfully
400 Bad Request - Invalid file
500 Internal Server Error - Upload failed
```

#### Processing Trigger
```http
GET /meaningFinder/trigger

Response:
200 OK - Processing started
500 Internal Server Error - Processing failed
```

#### CSV Download
```http
GET /api/download/csv
Accept: text/csv

Response:
200 OK - CSV file download
404 Not Found - File not found
500 Internal Server Error - Download failed
```

#### Health Check
```http
GET /actuator/health

Response:
{
  "status": "UP",
  "components": {
    "diskSpace": {"status": "UP"},
    "ping": {"status": "UP"}
  }
}
```

### Frontend API Integration

```typescript
// File Upload
const formData = new FormData();
formData.append('file', selectedFile);
await axios.post('/api/upload', formData);

// Processing Trigger
await axios.get('/meaningFinder/trigger');

// CSV Download
const response = await axios.get('/api/download/csv', {
  responseType: 'blob'
});
```

## ⚙️ Configuration

### Application Properties

```properties
# Server Configuration
server.port=9999

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update

# API Configuration
api.read-timeout=5000
api.connect-timeout=10000
api.call-delay=5000

# Logging Configuration
logging.level.com.example.vocabulary=DEBUG
logging.level.org.springframework.web=INFO
logging.file.name=logs/portfolio.log
```

### Environment Variables

```bash
# Java Configuration
export JAVA_HOME=/path/to/java21
export MAVEN_OPTS="-Xmx1024m -Xms512m"

# Node.js Configuration
export NODE_ENV=development
export NODE_OPTIONS="--max-old-space-size=4096"
```

## 🧪 Testing

### Backend Testing
```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify

# Run with specific profile
./mvnw test -Dspring.profiles.active=test
```

### Frontend Testing
```bash
# Run tests
npm test

# Run tests in watch mode
npm run test:watch

# Generate coverage report
npm run test:coverage
```

### End-to-End Testing
```bash
# Manual testing steps:
1. Start backend: ./mvnw spring-boot:run
2. Start frontend: npm run dev
3. Upload test file
4. Verify processing
5. Download CSV
6. Validate results
```

## 🚀 Deployment

### Production Build

#### Backend
```bash
# Create JAR file
./mvnw clean package -DskipTests

# Run JAR
java -jar target/*.jar
```

#### Frontend
```bash
# Build for production
npm run build

# Preview production build
npm run preview

# Deploy dist/ folder to web server
```

### Docker Deployment

#### Backend Dockerfile
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/*.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","/app.jar"]
```

#### Frontend Dockerfile
```dockerfile
FROM nginx:alpine
COPY dist/ /usr/share/nginx/html/
EXPOSE 80
```

### Cloud Deployment Options

#### Backend (Recommended)
- **AWS**: Elastic Beanstalk, ECS, Lambda
- **Azure**: App Service, Container Instances
- **GCP**: App Engine, Cloud Run

#### Frontend (Recommended)
- **Vercel**: Automatic deployments from Git
- **Netlify**: Static site hosting with CDN
- **AWS S3 + CloudFront**: Cost-effective static hosting

## 🔧 Troubleshooting

### Common Issues

#### Backend Won't Start
```bash
# Check Java version
java -version  # Should be 21+

# Check port availability
netstat -ano | findstr :9999

# Kill process using port
taskkill /PID <PID> /F
```

#### Frontend Build Fails
```bash
# Clear node_modules
rm -rf node_modules package-lock.json

# Reinstall dependencies
npm install

# Clear cache
npm run clean
```

#### API Connection Issues
```bash
# Test backend connectivity
curl http://localhost:9999/actuator/health

# Check CORS configuration
# Verify API_BASE_URL in frontend/src/constants/api.ts
```

#### File Upload Issues
```bash
# Check file permissions
# Verify supported file types
# Check file size limits
# Validate network connectivity
```

### Debug Mode

#### Enable Debug Logging
```properties
# application.properties
logging.level.com.example.vocabulary=DEBUG
logging.level.org.springframework.web=DEBUG
```

#### Frontend Debug Mode
```bash
# Open browser DevTools (F12)
# Check Console tab for errors
# Check Network tab for API calls
# Check Application tab for storage
```

### Performance Issues

#### Memory Problems
```bash
# Increase JVM memory
export MAVEN_OPTS="-Xmx2048m -Xms1024m"

# Increase Node.js memory
export NODE_OPTIONS="--max-old-space-size=4096"
```

#### Slow API Responses
```properties
# Increase timeouts
api.read-timeout=10000
api.connect-timeout=15000
api.call-delay=3000
```

## 🤝 Contributing

### Development Setup

1. **Fork the repository**
2. **Clone your fork**
   ```bash
   git clone https://github.com/your-username/WordMeaningCollector.git
   cd WordMeaningCollector
   ```

3. **Create feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

4. **Setup development environment**
   ```bash
   # Backend
   ./mvnw clean compile

   # Frontend
   cd frontend
   npm install
   ```

5. **Make your changes**
6. **Run tests**
   ```bash
   # Backend tests
   ./mvnw test

   # Frontend tests
   npm test
   ```

7. **Commit your changes**
   ```bash
   git commit -m "Add your feature description"
   ```

8. **Push to your branch**
   ```bash
   git push origin feature/your-feature-name
   ```

9. **Create Pull Request**

### Code Standards

#### Backend (Java)
- Follow Spring Boot conventions
- Use meaningful variable names
- Add JavaDoc comments
- Write unit tests for new features

#### Frontend (TypeScript/React)
- Use TypeScript for type safety
- Follow React best practices
- Use meaningful component names
- Add PropTypes or TypeScript interfaces

### Commit Message Format
```
type(scope): description

Types: feat, fix, docs, style, refactor, test, chore
Scope: component name (optional)
Description: brief description of changes
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### MIT License Summary

- ✅ **Commercial Use**: Allowed
- ✅ **Modification**: Allowed
- ✅ **Distribution**: Allowed
- ✅ **Private Use**: Allowed
- ⚠️ **Liability**: No liability
- ⚠️ **Warranty**: No warranty

## 🙏 Acknowledgments

- **Spring Boot Team** - For the excellent framework
- **React Team** - For the modern frontend library
- **Dictionary API Providers** - For word meaning services
- **OpenJDK Team** - For Java 21+ and Virtual Threads
- **Vite Team** - For the fast build tool
- **Tailwind CSS Team** - For the utility-first CSS framework

## 📞 Support

### Getting Help

1. **Check the Troubleshooting section** above
2. **Search existing issues** on GitHub
3. **Create a new issue** with detailed information
4. **Include logs and error messages**

### Issue Template

When reporting bugs, please include:

- **OS and Version**: Windows 10, macOS 12, etc.
- **Java Version**: `java -version` output
- **Node Version**: `node --version` output
- **Browser**: Chrome 120, Firefox 115, etc.
- **Steps to reproduce**: Detailed steps
- **Expected behavior**: What should happen
- **Actual behavior**: What actually happens
- **Error logs**: Console output, stack traces

---

**Built with ❤️ using Java 21+ Virtual Threads & React TypeScript**

**Transforming subtitle files into vocabulary gold! ✨📚**