# LinkedIn Alumni Profile Scraper 

![Java](https://img.shields.io/badge/Java-17+-blue.svg)  ![Jsoup](https://img.shields.io/badge/Jsoup-1.17+-orange.svg) 

**Production-grade LinkedIn alumni scraper** that extracts complete professional profiles at scale using **stealth browsing**,  and **multi-threaded concurrency**.

##  Core Features

| Feature | Description |
|---------|-------------|
| **Stealth Detection** | Chrome headless + randomized delays to evade LinkedIn anti-bot |
| **Multi-profile** | Concurrent scraping of 100+ alumni profiles |
| **Complete Data** | Name, job title, company, education, experience, skills |
| **Resume Export** | JSON/CSV output with structured professional data |
| **Rate Limiting** | Adaptive throttling to avoid IP bans |

##  System Architecture

```mermaid
graph TD
    A[Alumni Search URL] --> B[LinkedInScraper]
    B --> C[ChromeDriver Pool]
    B --> D[ProfileQueue]
    C --> E[StealthBrowser]
    E --> F[ProfileParser]
    F --> G[Jsoup HTML Parser]
    G --> H[ProfileDTO]
    D --> I[ConcurrentExecutor]
    H --> J[JSON/CSV Export]
