# MARTA-SmartWatch-App
This project is a lightweight Node.js backend that provides real-time arrival predictions for both MARTA rail and CobbLinc bus systems. It is designed to power a WearOS smartwatch app that displays a simple countdown showing when the next train or bus arrives, along with an urgency indicator.

The backend automatically detects which transit system to use based on the query parameters provided, normalizes the data into a consistent JSON format, and adjusts for walking time to determine whether the user needs to leave immediately.

---

## Features

- Unified `/next-arrival` endpoint
- Automatic detection of MARTA (rail) vs CobbLinc (bus)
- Real-time MARTA rail arrivals via official API
- Real-time CobbLinc bus predictions via GTFS-RT protobuf feed
- Normalized JSON output for wearable UI
- Walking-time adjustment and urgency classification (GREEN / YELLOW / RED)

---

## API Usage

### Endpoint
GET /next-arrival

### Parameters
- `station=` → Fetch MARTA rail arrivals
- `stopId=` → Fetch CobbLinc bus arrivals

Only one parameter should be provided at a time.

---

## Example: MARTA Rail

Request:
GET /next-arrival?station=Lindbergh%20Center

Sample Response:
{
  "system": "MARTA",
  "routeName": "GOLD",
  "stopName": "Lindbergh Center",
  "arrivalTime": "5/21/2026 11:05:00 AM",
  "timeLeftSeconds": 240,
  "walkingTimeSeconds": 180,
  "effectiveLeftSeconds": 60,
  "urgency": "YELLOW"
}

---

## Example: CobbLinc Bus

Request:
GET /next-arrival?stopId=1234

Sample Response:
{
  "system": "CobbLinc",
  "routeName": "45_1234",
  "stopId": "1234",
  "arrivalTime": "2026-05-21T15:10:00.000Z",
  "timeLeftSeconds": 420,
  "walkingTimeSeconds": 180,
  "effectiveLeftSeconds": 240,
  "urgency": "GREEN"
}

---

## How It Works

### MARTA
- Fetches official real-time rail data
- Filters by station
- Converts “X min” strings into seconds
- Computes urgency based on walking time

### CobbLinc
- Downloads GTFS-RT Trip Updates (tripupdates.pb)
- Decodes protobuf feed
- Matches the requested stop ID
- Converts UNIX timestamps to arrival times
- Normalizes output to match MARTA format

---

## Tech Stack

- Node.js
- Express
- Axios
- gtfs-realtime-bindings

---

## License

MIT License.


