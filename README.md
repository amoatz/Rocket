# Rocket/Rock backend Project

## Introduction

Rocket is an application that help the users to find the best place to eat according to the ratings of previous customers.

On the other side the restaurant’s owners can subscribe to use Rock application to know the feedback of their customers through our application. Rock will be a cheaper option than building their own application.   


The backend of rocket/Rock project is responsible for building the databases of the restaurants, customers, customer’s feedback and restaurant owners. The mobile application will communicate with the server to get, update and add new feedbacks.

## files 

    for local server:
        -Backend
        -Rocket
        -Rock
    for the Heroku server:
        -Backend_HEROKU
        -Rocket_HEROKU
        -Rock_HEROKU

## URL where the application is hosted

https://rocket-app-heroku.herokuapp.com/ and the endpoints as follows:

{{host}}/search/<search_word> GET : get the restaurants with the search word

/search/Zam GET 
Sample response:{
    "restaurants": [
        {
            "city": "Dubai",
            "five_start": 50,
            "formatted_adress": "4664+QJX - Al Barsha - Al Barsha 1 - Dubai - United Arab Emirates",
            "formatted_phone_number": " +971 4 354 9994",
            "four_start": 200,
            "geomtry": "",
            "icon": "https://assets.website-files.com/5f17df844aa1828cfb25bd26/5f3cea80f51c3f08f722aaff_7.jpg",
            "id": 5,
            "name": "The Grill Restaurant",
            "one_start": 3,
            "place_id": 550,
            "rating": 3.9,
            "three_start": 210,
            "two_start": 10,
            "type": "Steakhouse, Barbecue",
            "website": "",
            "zone": "Al Barsha"
        }
    ],
    "success": true
}

## End points as follows:

    get:search
    get:search/<search_word>
    get:search/<search_word>/<int:unrated>/<int:one_star>/<int:two_star>/<int:three_star>/<int:four_star>/<int:five_star>
    get:/location/<int:location_id>
    get:/Reviews/<int:location_id>    
    get:/Codecheck/<int:review_id>
    get:questions/<location_id>
    post:/vote/<int:review_id>
    post:/questionsoVte
    post:/register

    #### only restaurant owners request
    get:/CodeGenerator/<int:location_id>
    post:/questions

    #### only admin of the application
    post:/add
    post:/addlist
    DELETE:/deleterest/<location_id>

    #### only customer registered
    post:/vote
    


## TEST

In the file attached /POSTMAN there is 2 files one for collection inclucing all the tests for the 2 roles with a newly generated Bearer Token, and results I just run now.

The tests are:

    /search GET: test to return the list of the restaurant
    /search/<search_word> GET: test to search for a restaurants by name
    /search/<search_word>/unrated/onestar/twostar/threestar/fourstar/fivestar GET: search for restaurants by name with certain filter
    /location/<int:location_id> GET: test to get the detail of a restaurant by location id
    /location/<int:location_id> GET(unavailable location id): test to get the detail of a restaurant by unavilable location id

    /Reviews/<int:location_id> GET: test to get the reviews of certain restaurant using location id
    /Reviews/<int:location_id> GET(unavailabe location id): test to get the reviews of certain restaurant using unavailable location id
    
    /CodeGenerator/<int:location_id> GET: The restaurant's owner is requesting a code to give it to his customer to be able to giv a feedback
    /CodeGenerator/<int:location_id> GET(unavailabe location id): test if the location id is not correct

    /Codecheck/<int:review_id> GET: The application is requesting to make sure that the code the customer entered is correct and replay with location_id, location name and image_url of the restaurant.
    /Codecheck/<int:review_id> GET(wrong code): The application will feedback that the code is incorrect

    /questions/<location_id> GET: get the questions for the customer survey
    /questions/<location_id> (location with no registered questions)GET: get the questions for the customer survey for location without registered questions

    /add POST: the admin wants to add a new restaurant
    /addlist POST: the admin wants to add a list of new restaurant
    /vote POST: the customer is submitting a feedback
    /vote/<int:review_id> POST: the customer is submitting a feedback using code (real customer)
    /register POST: the customer wants to register

    /questions POST: add, modify and delete questions
    /questionsVote POST: test to post the questions results
    /deleterest/<location_id> DELETE: delete location

### Instructions to set up authentication to test the endpoint on local computer rather using Heroku server

1) create the db  on local computer:
    i) uncomment the lines from line 14 to 16 (database on local device)
    ii) uncommnent line 33 to create the db in the first run only

2) create a db with name 'restaurant'

3) Install all the requiremnts.txt

4) run the following

python manage.py db init
python manage.py db migrate
python manage.py db upgrade

5) run 
    export FLASK_APP=app.py  
    flask run

6) run Postman tests (OMAC - Restaurants)

### Testing the endpoints on Heroku server

using postman there is 2 files:

    OMAC - Restaurants.postman_collection.json: the 21 test of the local server
    OMAC - Restaurants - HEROKU.postman_collection.json: the 21 test of Heroku server



