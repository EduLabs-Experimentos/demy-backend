---
name: Performance Issue
about: Report and track performance or scalability problems
title: "[Perf] - "
labels: performance
assignees: ""
---

## Scenario
Describe the use case or endpoint where the performance issue appears.

## Baseline metrics
What was observed? (p95 latency, throughput, CPU/mem usage, etc.)

## Expected performance
What should the target or acceptable range be?

## Steps to reproduce / load conditions
1. Endpoint called: ...
2. Payload used:
   `json
   { ... }
   `
3. Concurrent requests: ...

## Profiling / traces
Attach flamegraphs, JFR, or any profiling data if available.

## Hypothesis / plan
What do you think might be causing the issue? How can it be tested?
