# WhereAreYou

Device locator.

## Actors

1. Location Requester
2. Location Provider
3. Permission Administrator
4. User Manager
5. Settings Administrator

## Use Cases

1. UC-SEND-SMS: Send SMS
2. UC-RECEIVE-SMS: Receive SMS
3. UC-REQUEST-LOCATION-UPDATES: Request location updates
    1. Request single location update
4. UC-CANCEL-LOCATION-UPDATES: Cancel location updates
5. UC-DECODE-LOCATION-REQUEST: Decode location request
6. Encode location response

## User Stories

1. As Location Requester, I'd like to know location of Location Provider
    1. As Location Requester, I'd like to query location of Location Provider once
2. As Location Provider, I'd like to share my location with Location Requester
    1. As LocationProvider, I'd like to share my location once
3. As Permission Administrator, I'd like to grant location, SMS receiving and SMS sending permissions
4. As User Manager, I'd like to select users from contacts
5. As Settings Administrator, I'd like to define location sharing settings
