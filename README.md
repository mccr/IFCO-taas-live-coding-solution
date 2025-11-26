<br />
<br />
<p align="center">
  <img src="docs/real-time-tracking.png" alt="trlln coding challenge" width="80" height="80">
</p>
<h1 align="center">
  <b>
    Tracking as a Service - Coding Challenge
  </b>
</h1>
<br />

## Challenge

The goals are:

1. Create a mini system that will ingest telemetry with temperature measurements from devices in JSON format
2. Render a list of all the devices seen and the last temperature measurement

For example, if the application receives a list of the following data:

| device_id | temperature | date |
| --- | --- | --- |
| 1 | 10 | 2025-01-31 13:00:00 |
| 2 | 8 | 2025-01-31 13:00:01 |
| 1 | 12 | 2025-01-31 13:00:05 |
| 2 | 19 | 2025-01-31 13:00:06 |
| 2 | 10 | 2025-01-31 13:00:11 |

The result should be:

| device_id | temperature |  |
| --- | --- | --- |
| 1 | 12 | 2025-01-31 13:00:05 |
| 2 | 10 | 2025-01-31 13:00:11 |

The device #1 has two telemetry messages with 10ºC and 12ºC but the last one received is 12ºC. The device #2 has three telemetry messages (8, 19, and 10ºC) so the last temperature is 10ºC.

Our new real system is going to be a streaming data processing, so we will implement it using Command Query Responsibility Segregation (an arquitectural pattern that splits writing and reading use cases into different operations to maximize performance). At IFCO, we will be implementing the following steps combining CQRS with Hexagonal Architecture, but you can follow a more simplified approach. Here is our approach:

1. Springboot API endpoint converts the HTTP payload into a Command (e.g. RecordTelemetryCommand), typically implemented as a DTO pattern.
2. The endpoint will instantiate a Command Handler (e.g. RecordTelemetryCommandHandler) that will handle such Command and persist the Telemetry via a TelemetryRepository and publish a Domain Event (e.g. TelemetryRecorded) into a Messaging system (e.g. Kafka, RabbitMQ, etc.) via an Event Bus
3. A message worker (aka here a EventHandler) will listen to messages of type TelemetryRecorded and will calculate the logic to keep updated the Projection (the table, redis records, or other storage that keeps track of the latest temperature for all the devices)
4. You can create another API endpoint or command line to list all the devices with the latest temperature registered.

## Deliverable

1. A Springboot API endpoint that will receive the following payloads. You should be able to test it via a `curl` call like the following one:

```bash
curl -H 'Content-Type: application/json' -d '{ "deviceId":1, "measurement":10, "date": "2025-01-31T13:00:00Z"}' -X POST http://localhost:port/your-url
curl -H 'Content-Type: application/json' -d '{ "deviceId":2, "measurement": 8, "date": "2025-01-31T13:00:01Z"}' -X POST http://localhost:port/your-url
curl -H 'Content-Type: application/json' -d '{ "deviceId":1, "measurement":12, "date": "2025-01-31T13:00:05Z"}' -X POST http://localhost:port/your-url
curl -H 'Content-Type: application/json' -d '{ "deviceId":2, "measurement":19, "date": "2025-01-31T13:00:06Z"}' -X POST http://localhost:port/your-url
curl -H 'Content-Type: application/json' -d '{ "deviceId":2, "measurement":10, "date": "2025-01-31T13:00:11Z"}' -X POST http://localhost:port/your-url
```

1. We need to see the Telemetry stored in any storage (e.g. Database, Key-Value store, etc.)
2. Event has to be published into a messaging system (e.g. Redis, Kafka, RabbitMQ, etc.)
3. A worker has to consume the event, calculate the latest status and persist the status in any storage (e.g. Database, Key-Value store, etc.)
4. A way of listing the devices and the last temperature (e.g. GET API endpoint, CLI command, etc.)

### Edge cases

When dealing with messaging and asynchronous systems, shit happens! Consider in your solution the following cases:

1. What happens if you receive a telemetry that is older that the latest status?
2. What happens if by mistake, you receive the same telemetry twice?

## Bonus points

1. Using Hexagonal Architecture or any similar (Onion, Clean, etc.)
2. Using CQRS: Command + CommandHandlers / Query + QueryHandlers
3. Using PostgreSQL for storing the telemetry and the projection
4. Using Kafka as messaging system
5. Testing with UnitTesting the logic to store the Telemetry and the logic for calculating the projection
6. Any error handling that you believe it’s necessary

## Supporting Material

- Exercise inspired on https://github.com/tpierrain/CQRS/
- Explanation of CQRS by Microsoft [https://docs.microsoft.com/en-us/previous-versions/msp-n-p/jj591573(v=pandp.10)](https://docs.microsoft.com/en-us/previous-versions/msp-n-p/jj591573(v=pandp.10))