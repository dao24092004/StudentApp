# Security Guidelines
- **Code**: Kiểm tra bằng SonarQube và SpotBugs.
- **Dependencies**: Dependabot và OWASP Dependency-Check.
- **CI/CD**: GitHub Actions (build, test, security check).
- **API**: Spring Security yêu cầu đăng nhập cho `/students/**`.
- **Testing**: OWASP ZAP kiểm tra lỗ hổng.
- **Monitoring**: Spring Actuator (/actuator/health, /actuator/metrics).