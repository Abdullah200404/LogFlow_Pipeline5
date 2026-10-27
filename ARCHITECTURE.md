# LogFlow Pipeline — Architecture (v3–v5)

## Pipeline flow
`FileLineSource -> ParserStage -> configured stages -> FileSink`
With `DEAD_LETTER`, failed records are written to a second independent output path.

## Stage responsibilities
- `ParserStage`: parses Common Log Format (CLF) lines into `LogRecord`.
- `PredicateFilterStage`: filters records using a predicate.
- `ProjectionStage`: limits output to selected fields.
- `ValidationStage`: checks timestamp, IP-like syntax, HTTP status and non-negative bytes.
- `FileSink`: owns its writer and closes it in all normal/error paths.
- `StageRegistry`: maps configuration names to factory functions; adding a stage requires registration, not a switch in the loader.
- `PipelineLoader`: validates required structure and builds the pipeline before reading input.

## Error model
- `StageException`: intended recoverable record/stage failure type (the runtime currently records stage failures using the same diagnostic information).
- `PipelineException`: fatal configuration or I/O failure that prevents reliable continuation.
- `FAIL_FAST`: stop immediately.
- `SKIP`: skip failed record and increment skipped count.
- `DEAD_LETTER`: write raw input, stage, exception and timestamp to a separate output.
- `RETRY(n)`: reserved for retryable I/O stages; not implemented in this version.

## Filter ordering experiment
For pure predicates `status >= 400` and `path startsWith /api`, the result set is identical in either order because both predicates only remove records and do not mutate them. Performance can differ: the more selective/cheaper predicate first can reduce the number of evaluations in the next stage. Parsing must precede filters because filters require a `LogRecord`. Projection should generally follow filters when filters need fields that projection would remove.

## Configuration schema
Each YAML config contains `pipeline.source.type=file`, `pipeline.source.params.path`, a non-empty `pipeline.stages` list, and `pipeline.sink.type=file` plus `pipeline.sink.params.path`. Each stage has `type` and optional `params`. `errorPolicy` is `FAIL_FAST`, `SKIP`, or `DEAD_LETTER`; dead-letter requires `deadLetter`.

## Known limitations
The project currently implements the core v3–v5 workflow, basic validation, three configurations and two error policies plus fail-fast. RETRY(n), aggregation by client for bandwidth, byte-level invalid UTF-8 detection, and the full adversarial 100 KB/timestamp test matrix remain extension tasks.
