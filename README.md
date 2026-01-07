<br />
<br />
<p align="center">
  <img src="docs/real-time-tracking.png" alt="trlln coding challenge" width="80" height="80">
</p>
<h1 align="center">
  <b>
    Tracking as a Service - Coding Challenge Solution
  </b>
</h1>
<br />

## Challenge

The goals are:

1. Create a mini system that will ingest telemetry with temperature measurements from devices in JSON format
2. Render a list of all the devices seen and the last temperature measurement

## Running the APP

Check the [Getting Started](./GETTING_STARTED.md)

once is running you can open a terminal and register the telemetry data:

```bash
curl -H 'Content-Type: application/json' -d '{ "deviceId":1, "measurement":10, "date": "2025-01-31T13:00:00Z"}' -X POST http://localhost:8080/telemetry
curl -H 'Content-Type: application/json' -d '{ "deviceId":2, "measurement": 8, "date": "2025-01-31T13:00:01Z"}' -X POST http://localhost:8080/telemetry
curl -H 'Content-Type: application/json' -d '{ "deviceId":1, "measurement":12, "date": "2025-01-31T13:00:05Z"}' -X POST http://localhost:8080/telemetry
curl -H 'Content-Type: application/json' -d '{ "deviceId":2, "measurement":19, "date": "2025-01-31T13:00:06Z"}' -X POST http://localhost:8080/telemetry
curl -H 'Content-Type: application/json' -d '{ "deviceId":2, "measurement":10, "date": "2025-01-31T13:00:11Z"}' -X POST http://localhost:8080/telemetry
```

to get the latest statuses:

```bash
curl -H 'Content-Type: application/json' -X GET http://localhost:8080/device-status | jq
```

and you should see something like this:

```bash
[
  {
    "deviceId": "1",
    "latestMeasurement": 12.0,
    "latestDate": "2025-01-31T13:00:05Z",
    "updatedAt": "2025-12-31T12:18:39.703924"
  },
  {
    "deviceId": "2",
    "latestMeasurement": 10.0,
    "latestDate": "2025-01-31T13:00:11Z",
    "updatedAt": "2025-12-31T12:18:39.725316"
  }
]
```

### Edge cases

When dealing with messaging and asynchronous systems, shit happens! Consider in your solution the following cases:

1. What happens if you receive a telemetry that is older that the latest status?

If telemetry is older than latest status, the update is skipped, and it logs a warning to have record that this
happened, an improvement can be tracking a metric to understand how many times it happens in case we need to take
actions.

2. What happens if by mistake, you receive the same telemetry twice?

if there is a duplicate, the update is skipped, and it logs a warning to keep track of this, a potential improvement
would be having a unique constraint in the DB and handling it as an error.

for both of these edge cases, I would also take into account from a product perspective what is the preferred approach,
for event driven systems sometimes it makes sense to record everything that happens, or we can decide to treat these
cases as errors,
so we can return early.

## Bonus points

1. Using Hexagonal Architecture or any similar (Onion, Clean, etc.) ✅
2. Using CQRS: Command + CommandHandlers / Query + QueryHandlers
3. Using PostgreSQL for storing the telemetry and the projection ✅
4. Using Kafka as messaging system ✅
5. Testing with UnitTesting the logic to store the Telemetry and the logic for calculating the projection ✅
6. Any error handling that you believe it’s necessary ✅

## Improvements

- Error handling can be improved to catch explicit exceptions.