# local set up

Here you can find configuration details of our set-up. 
Each application is described in a separate chapter.

## Back-office:

### General

port: 8090

swagger: http://localhost:8090/swagger-ui.html
```
./gradlew bootRun backoffice
```

### Security

<b>How to authorize yourself?</b> <br/>
You should make a HTTP POST request to```http://localhost:8090/login```  endpoint. 
Provide your username/password in request body: 
```
{
    "username": "grant-author",
    "password": "password"
}
``` 
In case if your username and password are correct, you will receive a jwt in ```Authorization``` header

<b>What do I need to make any other request to back-office API?</b> <br/>
You should provide jwt in ```Authorization``` header

<b>What information can be retrieved from token?</b> <br/>
- authorId:String
- role:String

<b>What is a role?</b> <br/>
Your role describes a set of rights you have. Right now we provide only one role=AUTHOR.
This part will be revisited in the future.


## Public-application

### General

port: 8080

swagger: http://localhost:8080/swagger-ui.html
```
./gradlew bootRun public-application
```

### Flow

<b> 1. Join a room </b> <br/>
method: HTTP POST<br/>
url: to http://localhost:8080/room/access <br/>
body: room password<br/>
```
{
    "password": "as5df15asdf"
}
```

As a result, you will receive jwt token

This token includes the following information:
- playerId
- roomId
- role (PLAYER)

<b> 2. Set your username </b> <br/>

route: join-room <br/>
You need to establish an RSocket connect (over websocket).<br/>
You need to provide jwt in a SETUP frame.<br/>
You need to send a username in a payload.<br/>
```
{
    "username": "player1"
}
```

As a result you will be added to a waiting room

<b> 2. Ask for a game status </b> <br/>
route: get-room-status <br/>
You will start receiving room statuses every 3 seconds.<br/>

<b> 3. Ask for a quiz content when room status is READY </b> <br/>
route: get-quiz <br/>

... 

## Database 

run this script from the root directory:
```
docker-compose run -p 5432:5432 -d postgres
```

- port: 5432
- db name: kahoot_db
- username: kahoot_user
- password: kahoot_password
- schema: kahoot

Liquibase changelogs are located in 'common' module. 

## build jars and docker images
./buildAllImages.sh
you can add all gradlew build flags to be applied, e.g.

```
./buildAllImages.sh -x [task]
```

**NB! requires WLS for Windows machines**  
**NB! If you have Linux machine then make sure that Docker runs without sudo rights**

## run everything in docker containers
**NB! you need to have built docker images with ./buildAllImages.sh and separately built kahoot-front image from front directory**

```
docker-compose up -d
```

## SELENIUM TEST
**NB! To run selenium tests you have to launch `kahoot-backoffice frontend`,`backoffice` and `database`**

You don't need any drivers, just run tests. Sometimes they might not pass, this might be due to changes in front-end layout
or java thinks that some element will receive click. To make sure that it is a java problem you can open 
`backoffice/src/test/java/ee/taltech/backoffice/game/selenium/kahoot-ui.side` in Selenium IDE and run tests there.
