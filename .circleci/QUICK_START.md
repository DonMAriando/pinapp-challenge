# CircleCI Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Step 1: Push Code to Repository
```bash
git add .
git commit -m "Add CircleCI configuration"
git push origin main
```

### Step 2: Connect to CircleCI
1. Go to https://circleci.com/
2. Sign in with your GitHub/Bitbucket account
3. Click "Projects" in the sidebar
4. Find your repository and click "Set Up Project"
5. CircleCI will auto-detect `.circleci/config.yml`
6. Click "Start Building"

**That's it!** Your first build will start automatically.

## 📊 What Runs on Each Build

```
┌──────────┐
│  Build   │  Compiles Java code
└────┬─────┘
     │
┌────▼─────┐
│   Test   │  Runs 89 tests (unit + integration + E2E)
└────┬─────┘
     │
┌────▼──────┐
│  Verify   │  Validates 90% coverage threshold
└────┬──────┘
     │
┌────▼────────┐
│  Package    │  Creates JAR file (main/develop only)
└────┬────────┘
     │
┌────▼─────────────┐
│ Security Scan    │  Placeholder for security checks (main only)
└──────────────────┘
```

## 🔧 Configuration Files

- **`.circleci/config.yml`** - Full production pipeline (recommended)
- **`.circleci/config-simple.yml`** - Simplified single-job version

## 📈 Viewing Build Results

### In CircleCI Dashboard:
1. Click on your project
2. Click on a specific build
3. View:
   - **Steps**: Each job's execution log
   - **Tests**: JUnit test results with pass/fail details
   - **Artifacts**: JAR files, test reports, coverage reports

### Download Coverage Reports:
1. Go to build page
2. Click "Artifacts" tab
3. Find `coverage-report/index.html`
4. Download and open in browser

## ⚙️ Optional: Avoid Docker Hub Rate Limits

CircleCI pulls the Java Docker image for each build. Without authentication, you may hit Docker Hub rate limits (100 pulls per 6 hours).

### Add Docker Hub Credentials:

1. **In CircleCI:**
   - Go to Project Settings → Environment Variables
   - Click "Add Environment Variable"
   - Add `DOCKERHUB_USERNAME` = your Docker Hub username
   - Add `DOCKERHUB_PASSWORD` = your Docker Hub password or access token

2. **Create Context:**
   - Go to Organization Settings → Contexts
   - Click "Create Context"
   - Name it `docker-hub-creds`
   - Add environment variables:
     - `DOCKERHUB_USERNAME`
     - `DOCKERHUB_PASSWORD`

The config.yml is already configured to use this context.

## 📝 Build Status Badge

Add to your README.md:

```markdown
[![CircleCI](https://circleci.com/gh/YOUR_USERNAME/YOUR_REPO.svg?style=svg)](https://circleci.com/gh/YOUR_USERNAME/YOUR_REPO)
```

Replace `YOUR_USERNAME` and `YOUR_REPO` with your actual GitHub/Bitbucket details.

## 🐛 Common Issues

### ❌ "Parameters 'rules' are missing" Error
**Problem:** JaCoCo check fails with "parameters 'rules' for goal... are missing or invalid"

**Solution:** The error was from running `mvn jacoco:check` directly. This is now fixed - the CircleCI config uses `mvn verify -DskipTests` which properly executes the coverage check.

### ❌ Docker Hub Rate Limit
**Problem:** "toomanyrequests: You have reached your pull rate limit"

**Solution:** Add Docker Hub credentials (see section above)

### ❌ Build Takes Too Long
**Problem:** Build takes more than 10 minutes

**Solution:** The pipeline uses caching for Maven dependencies. First build may be slow, but subsequent builds will be faster.

## 🔍 Testing Locally

Install CircleCI CLI to test configuration before pushing:

### Install CLI:
**Windows (PowerShell):**
```powershell
choco install circleci-cli
```

**Mac:**
```bash
brew install circleci
```

**Linux:**
```bash
curl -fLSs https://raw.githubusercontent.com/CircleCI-Public/circleci-cli/master/install.sh | bash
```

### Validate Configuration:
```bash
# Check if config.yml is valid
circleci config validate

# Process config (shows final YAML)
circleci config process .circleci/config.yml
```

### Run Jobs Locally (requires Docker):
```bash
# Run the build-and-test job from simple config
circleci local execute --job build-and-test

# Note: Full workflows with multiple jobs cannot be run locally
```

## 📚 Learn More

- **Detailed Documentation:** See `.circleci/README.md`
- **CircleCI Docs:** https://circleci.com/docs/
- **Java/Maven Guide:** https://circleci.com/docs/language-java-maven/

## ✅ Checklist

- [ ] Code pushed to GitHub/Bitbucket
- [ ] Project connected to CircleCI
- [ ] First build completed successfully
- [ ] (Optional) Docker Hub credentials added
- [ ] (Optional) Build status badge added to README

## 🎉 You're All Set!

CircleCI will now automatically:
- Build your code on every push
- Run all 89 tests
- Validate 90% code coverage
- Create JAR artifacts
- Show results in the dashboard

Happy building! 🚀

