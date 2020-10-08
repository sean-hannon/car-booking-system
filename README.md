# Car Booking System

This system will allow drivers to book a car.

## Endpoints

A user will need to be authenticated to make a request against the driver and car endpoints.
This will be done with the following endpoint.

## Authentication Endpoint
**_/authenticate_**<br/>
**POST ONLY**

Payload: 

`{ "username": "employee", "password": "password" }`

This will return a payload with a Bearer Token for authorizing subsequent calls to the endpoints.<br/>
Example response:

`{
     "token": "TOKEN RETURNED"
 }`
 
 All subsequent calls will need to include this token under Authorization header
 
## Driver Endpoints
 
 **_v1/drivers/{driverId}_**<br/>
 **GET**
 
 This will return a driver object similar to the response below:
 
 ```
 {
  "id": 7,
  "username": "driver07",
  "password": "driver07pw",
  "coordinate": {
                  "latitude": 55.954,
                  "longitude": 9.5
                 },
  "onlineStatus": "OFFLINE"
 }
```
**_v1/driver?search={searchquery}_**<br/>
**GET**

This endpoint will return a list of drivers who meet the search query. Some examples of these requests are shown below:<br/>

**/v1/drivers?search=username==driver01** _Find all drivers with the username driver01_<br/>
**/v1/drivers?search=username==driver&ast;** _Find all drivers with the username that starts with driver_<br/>
**/v1/drivers?search=car.licensePlate==12345** _Find all drivers with the car with the license plate 12345_<br/>
**/v1/drivers?search=username==driver\*;onlineStatus==ONLINE** _Find all drivers with the username starting with driver and that their status is ONLINE_<br/>

Example of the response:<br/>
```
[
    {
        "id": 4,
        "username": "driver04",
        "password": "driver04pw",
        "onlineStatus": "ONLINE"
    },
    {
        "id": 5,
        "username": "driver05",
        "password": "driver05pw",
        "onlineStatus": "ONLINE"
    },
    {
        "id": 6,
        "username": "driver06",
        "password": "driver06pw",
        "onlineStatus": "ONLINE"
    },
    {
        "id": 8,
        "username": "driver08",
        "password": "driver08pw",
        "coordinate": {
            "latitude": 55.954,
            "longitude": 9.5
        },
        "onlineStatus": "ONLINE"
    }
]
```
 
**_v1/drivers_**<br/>
**POST**

To create a new driver use the following endpoint.

The request payload should be like this:<br/>

```
{
	"username": "newDriver",
	"password": "newDriverPW"
}
```

Response will be:<br/>

```
{
    "id": 9,
    "username": "newDriver",
    "password": "newDriverPW",
    "onlineStatus": "OFFLINE"
}
```

**_v1/drivers/{driverId}_**<br/>
**DELETE**

This will delete a driver for the given ID.

**_v1/drivers/{driverId}?longitude={double}&latitude={double}_**<br/>
**PUT**

This endpoint will update the driver's location for the given ID<br/>

**_v1/drivers/{driverId}?status={ONLINE/OFFLINE}_**<br/>
**PUT**

This endpoint will update the driver's online status for the given ID<br/>

**_v1/drivers/{driverId}/cars/{carId}_**<br/>
**PUT**

This will update the driver with their selected car if it is available, also the driver must be online.

**_v1/drivers/{driverId}/cars/_**<br/>
**PUT**

This will deselect the car for the given driverId.