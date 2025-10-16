# CircleCI Configuration Guide

This directory contains CircleCI configuration files for automated CI/CD pipelines.

## Configuration Files

### `config.yml` - Full Pipeline (Recommended for Production)

**Features:**
- Multiple jobs running in parallel
- Separate build, test, verify, and package stages
- Caching for faster builds
- Coverage threshold validation
- Artifact storage
- Scheduled nightly builds
- Security scan placeholder

**Jobs:**
1. **build** - Compiles the application
2. **test** - Runs all tests and generates coverage
3. **verify** - Validates coverage thresholds
4. **package** - Creates JAR artifact (main/develop branches only)
5. **security-scan** - Security checks (main branch only)

**Workflow:**
```
build → test → verify → package → security-scan
```

### `config-simple.yml` - Single Job (Good for Getting Started)

**Features:**
- Single job that does everything
- Simpler to understand and debug
- No Docker Hub authentication required
- Good for small projects or testing

**Usage:**
```bash
# To use simple config, rename it:
mv .circleci/config-simple.yml .circleci/config.yml
```

## Setup Instructions

### 1. Connect to CircleCI

1. Go to https://circleci.com/
2. Sign up or log in with your GitHub/Bitbucket account
3. Click "Set Up Project" for your repository
4. CircleCI will automatically detect `.circleci/config.yml`
5. Click "Start Building"

### 2. Configure Environment Variables (Optional)

To avoid Docker Hub rate limits, add these environment variables:

1. Go to Project Settings → Environment Variables
2. Add variables:
   - `DOCKERHUB_USERNAME`: Your Docker Hub username
   - `DOCKERHUB_PASSWORD`: Your Docker Hub password or access token

### 3. Create Context for Docker Hub (Optional)

1. Go to Organization Settings → Contexts
2. Create a context named `docker-hub-creds`
3. Add environment variables:
   - `DOCKERHUB_USERNAME`
   - `DOCKERHUB_PASSWORD`

## Pipeline Behavior

### Branch-Specific Behavior

- **All branches**: Run build → test → verify
- **main/develop**: Also run package job (creates JAR)
- **main only**: Also run security-scan job
- **gh-pages**: Ignored (documentation branch)

### Scheduled Builds

- **Nightly builds** run at midnight UTC on the `main` branch
- Helps catch issues that develop over time
- Full test suite execution

## Viewing Results

### In CircleCI UI

1. **Test Results Tab**: JUnit test reports with pass/fail details
2. **Artifacts Tab**: 
   - JAR files (target/*.jar)
   - Test reports (test-results/)
   - Coverage reports (coverage-report/)
3. **Insights**: Build time trends and success rates

### Downloading Artifacts

```bash
# Coverage report available at:
https://app.circleci.com/pipelines/github/YOUR_ORG/YOUR_REPO/JOB_NUMBER/artifacts

# Or via CircleCI CLI:
circleci tests download --job-number JOB_NUMBER
```

## Local Testing

### Install CircleCI CLI

**Mac/Linux:**
```bash
curl -fLSs https://raw.githubusercontent.com/CircleCI-Public/circleci-cli/master/install.sh | bash
```

**Windows (PowerShell):**
```powershell
choco install circleci-cli
```

### Validate Configuration

```bash
# Validate config file
circleci config validate

# Process config (shows final YAML after processing)
circleci config process .circleci/config.yml
```

### Run Jobs Locally

```bash
# Run specific job
circleci local execute --job build

# Run with environment variables
circleci local execute --job test -e MAVEN_OPTS="-Xmx2048m"
```

**Note:** Local execution has limitations:
- Workflows are not supported
- Caching behavior may differ
- Some features require CircleCI cloud

## Optimization Tips

### 1. Caching

The configuration caches Maven dependencies:
```yaml
- restore_cache:
    keys:
      - v1-dependencies-{{ checksum "pom.xml" }}
      - v1-dependencies-
```

**Cache invalidation:** Change `v1` to `v2` to force cache rebuild.

### 2. Parallelism

Enable test parallelism:
```yaml
parallelism: 4
```

Then split tests:
```bash
circleci tests glob "src/test/**/*.java" | circleci tests split
```

### 3. Resource Class

Upgrade to larger containers for faster builds:
```yaml
resource_class: medium+  # 3 vCPUs, 6GB RAM
# or
resource_class: large    # 4 vCPUs, 8GB RAM
```

## Troubleshooting

### Common Issues

**1. Docker Hub Rate Limits**
```
Error: toomanyrequests: You have reached your pull rate limit
```
**Solution:** Add Docker Hub credentials (see setup instructions)

**2. Out of Memory**
```
java.lang.OutOfMemoryError: Java heap space
```
**Solution:** Increase MAVEN_OPTS in config:
```yaml
environment:
  MAVEN_OPTS: -Xmx4096m
```

**3. Tests Failing in CI but Passing Locally**
- Check environment differences
- Ensure H2 database is properly configured
- Review CircleCI logs for specific errors

**4. Coverage Threshold Not Met**
```
Rule violated for bundle challenge
```
**Solution:** Either:
- Add more tests to increase coverage
- Adjust thresholds in pom.xml (not recommended)

## CI/CD Best Practices

✅ **DO:**
- Keep builds fast (< 10 minutes)
- Cache dependencies
- Run tests in parallel when possible
- Store artifacts for debugging
- Use contexts for sensitive data

❌ **DON'T:**
- Commit secrets to config files
- Skip tests to speed up builds
- Ignore failing tests
- Deploy from non-main branches

## Advanced Configuration

### Matrix Builds

Test against multiple Java versions:
```yaml
parameters:
  java-version:
    type: string
    default: "21.0"

executors:
  java-executor:
    docker:
      - image: cimg/openjdk:<< parameters.java-version >>
```

### Deployment

Add deployment job:
```yaml
deploy:
  executor: java-executor
  steps:
    - attach_workspace:
        at: ~/project
    - run:
        name: Deploy to Production
        command: |
          # Your deployment script here
          ./deploy.sh
```

### Notifications

Configure Slack notifications:
```yaml
orbs:
  slack: circleci/slack@4.1

workflows:
  build-test-deploy:
    jobs:
      - test
      - slack/notify:
          event: fail
          template: basic_fail_1
```

## Resources

- [CircleCI Documentation](https://circleci.com/docs/)
- [Java/Maven Guide](https://circleci.com/docs/language-java-maven/)
- [Configuration Reference](https://circleci.com/docs/configuration-reference/)
- [CircleCI CLI](https://circleci.com/docs/local-cli/)

## Support

For issues with this configuration:
1. Check CircleCI logs for specific errors
2. Validate configuration: `circleci config validate`
3. Review this README for common issues
4. Check CircleCI community forum

