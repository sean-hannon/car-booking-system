# Car Booking System

This system will allow drivers to book a car.

The project was created to explore how to create search endpoints without having an explosion of methods in the repository classes 

## Endpoints

### Authentication Endpoint
A user will need to be authenticated to make a request against the driver and car endpoints.
This will be done with the following endpoint.

**POST ONLY** **_/authenticate_**<br/> 

Payload: 

`{ "username": "employee", "password": "password" }`

This will return a payload with a Bearer Token for authorizing subsequent calls to the endpoints.<br/>
Example response:

`{
     "token": "TOKEN RETURNED"
 }`
 
 All subsequent calls will need to include this token under Authorization header
 
### Driver Endpoints
**GET** **_v1/drivers/{driverId}_**<br/>
 
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
**GET** **_v1/driver?search={searchquery}_**<br/>

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
 
**POST** **_v1/drivers_**<br/>

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

**DELETE** **_v1/drivers/{driverId}_**<br/>

This will delete a driver for the given ID.

**PUT** **_v1/drivers/{driverId}?longitude={double}&latitude={double}_**<br/>

This endpoint will update the driver's location for the given ID<br/>

**PUT** **_v1/drivers/{driverId}?status={ONLINE/OFFLINE}_**<br/>

This endpoint will update the driver's online status for the given ID<br/>

**PUT** **_v1/drivers/{driverId}/cars/{carId}_**<br/>

This will update the driver with their selected car if it is available, also the driver must be online.

**PUT** **_v1/drivers/{driverId}/cars/_**<br/>

This will deselect the car for the given driverId.

## Future Work
* Extend search feature to cars
* More testing
* Build frontend