# LogFlow Pipeline (Java 17, Maven)

## Requirements
- JDK 17+
- Maven 3.9+

## Build and run
```bash
mvn clean package
java -jar target/logflow-pipeline-5.0.0.jar --config configs/error-report.yaml
java -jar target/logflow-pipeline-5.0.0.jar --config configs/api-traffic.yaml
java -jar target/logflow-pipeline-5.0.0.jar --config configs/bandwidth-by-client.yaml
```

Run from the project root so relative paths in YAML resolve correctly.

## Example errors
- `ERROR: Unknown stage type: unknown-name`
- `ERROR: Missing required parameter: path`
- `ERROR: Pipeline stages must not be empty`
- `ERROR: Pipeline I/O failure: ...`

## Scope
Implements file reading, CLF parsing, predicate filtering, projection, file sink lifecycle, fluent builder, YAML configuration loading, registry/factory, validation, dead-letter output, and run summary. See `ARCHITECTURE.md` for explicit limitations.
