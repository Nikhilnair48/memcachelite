# MemCacheLite

A generic, thread-safe in-memory cache with the following features:
- Least Recently Used (LRU): least accessed element is removed upon reaching the limit.
- Time-to-Live (TTL): allowing entries to expire after a specified duration. Also handles the absence of TTL.
- Thread-safe operations: an initial version with synchronized get & set along with Concurrent Map (storage) and Queue (LRU)
- Maximum entries: user may set the upper cap
- Support any data structure using Generics

## Project structure

```
memcachelite/
│-- src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── apis/
│   │   │   │   └── InMemCache.java      # Cache interface 
│   │   │   ├── models/
│   │   │   │   ├── CacheEntry.java      # Cache value and TTL encapsulation
│   │   │   │   ├── InMemCacheState.java # Stores cache entries and LRU list
│   │   │   ├── service/
│   │   │   │   └── CacheManager.java    # Manges the internal state via get/set methods
│   ├── test/
│   │   ├── java/
│   │   │   ├── service/
│   │   │   │   └── CacheManagerTest.java
│-- pom.xml
│-- README.md
```

### Usage

```bash
   git clone git@github.com:Nikhilnair48/memcachelite.git
   cd memcachelite
   
```

### CDK & AWS setup

```bash
    npm run create-tables # create the DynamoDB tables

    cdk bootstrap
    npm run synth
    npm run deploy
```

## CI/CD Pipeline
The project includes an automated CI/CD pipeline built using AWS CodePipeline. It:

- Pulls source code from GitHub.
- Executes tests using CodeBuild.
- Deploys the application to AWS Lambda & updated any necessary resources.
- Make sure the CodePipeline IAM role has access to:
    - The GitHub repository (via CodeStar connection).
    - S3 for artifact storage.
    - Secrets Manager for fetching environment variables.

## Walkthrough

[Video walkthrough](https://drive.google.com/file/d/1LnxiIUPtanereRb_bwKUDavQDzTj0r93/view?usp=sharing)