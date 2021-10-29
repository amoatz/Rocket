import os
from flask import Flask, request, abort, jsonify
from models import setup_db, Restaurant, Ratingdb, Customers, Reviews, CodeGenerator, Questions, Customratingdb
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS, cross_origin
import random
# from auth import AuthError, requires_auth

def create_app(test_config=None):
  # create and configure the app
  app = Flask(__name__)
  setup_db(app)
  CORS(app)

  ########## GET Endpoints all the restaurants for test ######################
  @app.route('/',methods=['GET'])
  # @requires_auth('get:restaurants')
  def get_all():

    restaurant_data = Restaurant.query.all() # query all the restaurants
    restaurant_data_format = [] # Create an empty list
    for restaurant in restaurant_data: # format the movies is JSON to response
      restaurant_data_format.append(restaurant.format()) 
    # Return the restaurants in JSON format
    return jsonify({
          'success': True,
          'restaurants': restaurant_data_format
          })

  ########## GET Endpoints all the restaurants ######################

  @app.route('/search', methods=['GET'])
  # @requires_auth('get:restaurants')
  def get_search():  # adding self while using server

    restaurant_data = Restaurant.query.all() # query the restaurants
    restaurant_data_format = [] # Create an empty list
    # Format the restaurants is JSON to response
    for restaurant in restaurant_data: 
      restaurant_data_format.append(restaurant.format()) 
    # Return the restaurants in JSON format
    return jsonify({
          'success': True,
          'restaurants': restaurant_data_format
          })

  ########## GET with search word without filters Endpoints ######################

  @app.route('/search/<search_word>', methods=['GET'])
  # @requires_auth('get:restaurants')
  def get_search_word(search_word):  # adding self while using server

    restaurant_data = Restaurant.query.filter(Restaurant.name.ilike('%'+search_word+'%') | Restaurant.type_of.ilike('%'+search_word+'%')) # query using ilike 
    print('After Query filter the word search is ',search_word) # print for test
    restaurant_data_format = [] # Create an empty list
    for restaurant in restaurant_data: # format the restaurants is JSON to response
      restaurant_data_format.append(restaurant.format()) 
    # Return Restaurants in JSON format
    return jsonify({
          'success': True,
          'restaurants': restaurant_data_format
          })

  ########## GET with search word with filters Endpoints ######################

  @app.route('/search/<search_word>/<int:unrated>/<int:one_star>/<int:two_star>/<int:three_star>/<int:four_star>/<int:five_star>', methods=['GET'])
  # @requires_auth('get:restaurants')
  def get_search_word_filter(search_word,unrated,one_star,two_star,three_star,four_star,five_star):  # adding self while using server

    restaurant_data = Restaurant.query.filter((Restaurant.name.ilike('%'+search_word+'%') | Restaurant.type_of.ilike('%'+search_word+'%'))\
                           & ((Restaurant.rating==unrated) | \
                             ((Restaurant.rating>one_star) & (Restaurant.rating<(one_star+1)))| \
                             ((Restaurant.rating>=two_star) & (Restaurant.rating<(two_star+1)))| \
                             ((Restaurant.rating>=three_star) & (Restaurant.rating<(three_star+1)))| \
                             ((Restaurant.rating>=four_star) & (Restaurant.rating<(four_star+1)))| \
                             (Restaurant.rating == five_star)))  # Query using ilike and filters

    print('After Query filter the word search is ',search_word) # print for test
    restaurant_data_format = [] # Create an empty list
    for restaurant in restaurant_data: # format the restaurants is JSON to response
      restaurant_data_format.append(restaurant.format()) 
    # Return in JSON format
    return jsonify({
          'success': True,
          'restaurants': restaurant_data_format
          })

  ########## GET Endpoints search for retaurants by Location ID ######################
  @app.route('/location/<int:location_id>',methods=['GET'])
  def get_location(location_id): # adding self while using server

    restaurant_data = Restaurant.query.filter(Restaurant.id==location_id) # query the restaurants
    restaurant_data_format = [] # Create an empty list
    for restaurant in restaurant_data: # format the restaurants is JSON to response
      restaurant_data_format.append(restaurant.format()) 

    print(" COUNT IS : ",restaurant_data.count()) # print count for test
    # Return false if the location Id is not found or there is several location Id(error in the database)
    if (restaurant_data.count() == 0) or (restaurant_data.count()>1) :
      return jsonify({
        'success': False,
        'restaurants': restaurant_data_format
      })
    # Return restaurnat details in JSON format
    return jsonify({
          'success': True,
          'restaurants': restaurant_data_format
          })


  ########## GET Reviews Endpoints ######################
  @app.route('/Reviews/<int:location_id>',methods=['GET'])
  def get_Review(location_id):  # adding self while using server

    print('The location Id is ',location_id)
    restaurant_data = Restaurant.query.filter(Restaurant.id==location_id) # query the restaurants
    review_data = Reviews.query.filter(Reviews.location_id==location_id) # query the Review
    if restaurant_data.count() != 1:
      return jsonify({
          'success': False,
          'Reviews': []
          })
    print('the number of reviews is ',review_data.count())
    review_data_format = [] # Create an empty list
    for review in review_data: # format the reviews is JSON to response
      review_data_format.append(review.format()) 
    # Return reviews in JSON format
    return jsonify({
          'success': True,
          'Reviews': review_data_format
          })

    ########## GET Reviews Code ######################
  @app.route('/CodeGenerator/<int:location_id>',methods=['GET'])
  # @requires_auth('get:restaurantscode')
  def get_Review_Code(location_id): # Adding self while using server

    restaurant_data = Restaurant.query.filter(Restaurant.id==location_id) # query the restaurants
    question_data = Questions.query.filter(Questions.location_id==location_id) # Filter to know if there is customized questions for that location
    if restaurant_data.count() !=1:
        return jsonify({
          'success': False,
          'Review_id': 0
          })
    # If there is no customized question the code will be in range from 1000 to 10000
    # If there is customized questions the code will be in range from 10000 to 100000
    if question_data.count()==0: 
      review_id = random.randrange(1000, 10000, 1)
    else:
      review_id = random.randrange(10000, 100000, 1)
    code_generator = CodeGenerator(location_id=location_id,code_id=review_id)
    code_generator.insert() # Insert code in the database CodeGenerator
    # Return JSON with the review Id
    return jsonify({
          'success': True,
          'Review_id': review_id
          })

  ########## GET location Id from Reviews Code ######################
  # The customer will return the location id by the review id
  @app.route('/Codecheck/<int:review_id>',methods=['GET'])
  def get_Locationid_from_Code(review_id): # Adding self while using server

    code_data = CodeGenerator.query.filter(CodeGenerator.code_id==review_id) # Query by review id 
    if(code_data.count()!=1):
      print("The wrong location id is ",review_id)
      return jsonify({
        'success':False,
        'Location_id':'0',
        'image_url':'',
        'location_name':''
      })
    # Return the location id, name and image_url
    location_data = Restaurant.query.filter(Restaurant.id==code_data[0].location_id)
    print("The location id is ",code_data[0].location_id)

    return jsonify({
          'success': True,
          'Location_id': code_data[0].location_id,
          'image_url':location_data[0].icon,
          'location_name':location_data[0].name
          })

    ########## GET Questions from Reviews Code ######################
  @app.route('/questions/<int:location_id>',methods=['GET'])
  def get_questions_from_Code(location_id): # Adding self while using server

    questions_data = Questions.query.filter(Questions.location_id==location_id) # Query the question for the location id
    if(questions_data.count()==0):
      print("The wrong location id is ",location_id)
      return jsonify({
        'success':False,
        'questions':[]
      })
    questions_data_format=[] # Create an empty list
    for question_data in questions_data:
      questions_data_format.append(question_data.format()) 
    # Return JSON format
    return jsonify({
          'success': True,
          'Location_id': location_id,
          'questions':questions_data_format
          })
  
#   ########## POST Endpoints Add new Restaurant ######################
  @app.route('/add', methods=['POST'])
  # @requires_auth('post:admin')
  def post_restaurant():  # add self when using server
    content = request.get_json()
    print('The name:')
    # print(content)
    # Make sure that the JSON sent is in correct format
    try:

      name = str(content['name'])
      formatted_adress = str(content['formatted_adress'])
      formatted_phone_number = str(content['formatted_phone_number'])
      geomtry = str(content['geomtry'])
      icon = str(content['icon'])
      rating = float(content['rating'])
      website = str(content['website'])
      place_id = int(content['place_id'])

      one_star = int(content['one_star'])
      two_star = int(content['two_star'])
      three_star = int(content['three_star'])
      four_star = int(content['four_star'])
      five_star = int(content['five_star'])

      city = str(content['city'])
      zone = str(content['zone'])

      type_of = str(content['type'])

      # print(city)
      # print(zone)

    except:
      abort(400)

    # create a new Restaurant and insert in the db
    restaurant = Restaurant(
      name = name,
      formatted_adress = formatted_adress,
      formatted_phone_number = formatted_phone_number,
      geomtry = geomtry,
      icon = icon,
      rating = rating,
      website = website,
      place_id = place_id,

      one_star = one_star,
      two_star = two_star,
      three_star = three_star,
      four_star = four_star,
      five_star = five_star,

      city = city,
      zone = zone,

      type_of=type_of
    )

    # print(restaurant.city)
    restaurant.insert()

# get the id number of the new entry and send it in the feedback
    restaurant_data = Restaurant.query.all()
    last_id = restaurant_data[-1].id
    
    return jsonify({
        'success': True,
        'id': last_id
        })

#   ########## POST Endpoints Add new LIST of Restaurants ######################
  @app.route('/addlist', methods=['POST'])
  # @requires_auth('post:admin')
  def post_restaurant_list():  # add self when using server
    content = request.get_json()
    print('The name:')
    print(content)
    # Make sure that the JSON sent is in correct format
    try:

      restaurants_list = content['restaurants']

    except:
      abort(400)

    print("first try done")
    print("")
    print(restaurants_list)

    try:
    # if(0==0):

      for one_restaurant in restaurants_list:

        name = one_restaurant['name']
        formatted_adress = one_restaurant['formatted_adress']
        formatted_phone_number = one_restaurant['formatted_phone_number']
        geomtry = one_restaurant['geomtry']
        icon = one_restaurant['icon']
        rating = one_restaurant['rating']
        website = one_restaurant['website']
        place_id = one_restaurant['place_id']
        one_star = one_restaurant['one_star']
        two_star = one_restaurant['two_star']
        three_star = one_restaurant['three_star']
        four_star = one_restaurant['four_star']
        five_star = one_restaurant['five_star']
        city = one_restaurant['city']
        zone = one_restaurant['zone']
        type_of = one_restaurant['type']

        # create a new Restaurant and insert in the db
        restaurant = Restaurant(
          name = name,
          formatted_adress = formatted_adress,
          formatted_phone_number = formatted_phone_number,
          geomtry = geomtry,
          icon = icon,
          rating = rating,
          website = website,
          place_id = place_id,
          one_star = one_star,
          two_star = two_star,
          three_star = three_star,
          four_star = four_star,
          five_star = five_star,
          city = city,
          zone = zone,
          type_of=type_of
        )
        restaurant.insert()
    except:
      abort(400)

# get the id number of the new entry and send it in the feedback
    restaurant_data = Restaurant.query.all()
    last_id = restaurant_data[-1].id
    
    return jsonify({
        'success': True,
        'id': last_id
        })

############# Delete Restaurants ###################
  @app.route('/deleterest/<int:location_id>', methods=['DELETE'])
  # @requires_auth('post:admin')
  def delete(location_id):
    restaurnat = Restaurant.query.filter(Restaurant.id==location_id)

    if restaurnat.count()== 1:
      restaurnat.delete()
      return jsonify({
          'success': True,
          'id': location_id
          })
    else:
      return jsonify({
          'success': False,
          'id': location_id
          })

############## POST customer vote ##################
  @app.route('/vote', methods=['POST'])
  def post_vote(): # add self when using server
    content = request.get_json()
    print(content)
    try:
      customer_id = int(content['customerId'])
      rating = int(content['vote'])
      location_id = int(content['locationId'])
      comment_text = str(content['commentText'])

    except:
      abort(400)

    #Check if the location Id is correct else JSON false message
    restaurant_data = Restaurant.query.filter(Restaurant.id==location_id)
    if(restaurant_data.count()!=1):
      print("The wrong location id is ",location_id)
      return jsonify({
        'success':False,
        'reason':'wrong location id '
      })

    #Check if the same customer votted before
    ratingdb = Ratingdb.query.filter(Ratingdb.location_id == location_id , Ratingdb.customer_id==customer_id)
    # if(ratingdb.count()==0): # The customer can use the code one time only
    if(0==0): # Just for the test
      ratinginput = Ratingdb(customer_id=customer_id,rating=rating,location_id=location_id,comment_text=comment_text,internal_flag=0)
      ratinginput.insert()

      reviews = Reviews(location_id=location_id,source="Rocket",author_name="Rocket User",author_url="",language="en",profile_photo_url="",rating=rating,relative_time_description="",text=comment_text,time=0)
      reviews.insert()
      restaurant_data[0].calculaterating(rating)

      return jsonify({
        'success':True,
        'reason':'Great vote submitted'
      })
    
    # else:
    #   print("customerId = ",customer_id," ,locationId = ",location_id)
    #   for ratingdb1 in ratingdb:
    #     print("ratingdb")
    #     print("customerId = ",ratingdb1.customer_id," ,locationId = ",ratingdb1.location_id)
    #   return jsonify({
    #     'success':False,
    #     'reason':'The customer already votted' 
    #   })


  ############## POST INTERNAL customer vote ##################
  @app.route('/vote/<int:review_id>', methods=['POST'])
  def post_vote_internal(review_id): # add self when using server
    content = request.get_json()
    print(content)
    try:
      customer_id = int(content['customerId'])
      rating = int(content['vote'])
      location_id = int(content['locationId'])
      comment_text = str(content['commentText'])

    except:
      abort(400)

    #Check if the location Id is correct
    code_data = CodeGenerator.query.filter(CodeGenerator.code_id==review_id)
    if(code_data.count()!=1):
      print("The wrong location id is ",location_id)
      return jsonify({
        'success':False,
        'reason':'wrong review id '
      })

    ratinginput = Ratingdb(customer_id=customer_id,rating=rating,location_id=location_id,comment_text=comment_text,internal_flag=1)
    ratinginput.insert() # Insert the vote in the Ratingdb

    reviews = Reviews(location_id=location_id,source="Rocket",author_name="Rocket User",author_url="",language="en",profile_photo_url="",rating=rating,relative_time_description="",text=comment_text,time=0)
    reviews.insert() #Insert the review in the Reviews database
    restaurant_data = Restaurant.query.filter(Restaurant.id==location_id)
    restaurant_data[0].calculaterating(rating)

    return jsonify({
        'success':True,
        'reason':'Great vote submitted'
    })
  
  ############# POST customer Register  ###############
  @app.route('/register', methods=['POST'])
  def get_customerId(): # add self when using server
    print("inside register")
    content = request.get_json()
    print(content)
    try:
      name = str(content['name'])
      mail = str(content['mail'])

    except:

      abort(400)
    
    customer = Customers(name=name,mail=mail)
    customerid = customer.insert()

    # customerid = Customers.query.all().count()-1

    return jsonify({
      'success':True,
      'customerId':customerid
    })

  ############# POST Questions  ###############
  @app.route('/questions', methods=['POST'])
  def post_questions(): # add self when using server
    print("inside register")
    content = request.get_json()
    print(content)
    try:
      location_id = str(content['location_id'])
      questions = content['questions']

    except:

      abort(400)

    questions_data = Questions.query.filter(Questions.location_id==location_id)
    if questions_data.count() > 0:
      questions_data.delete()

    for oneQuestion in questions :
      print(oneQuestion)
      print(oneQuestion.keys())
      addedQuestion = Questions(location_id=location_id, question=oneQuestion['question'])
      addedQuestion.insert()

      # customerratingdb = Customratingdb(customer_id=customer_id, rating=question.rating, location_id=location_id, question=question.question)
      # customerratingdb.insert()
    

    # customerid = Customers.query.all().count()-1

    return jsonify({
      'success':True
    })


  ############# POST Questions  ###############
  @app.route('/questionsVote', methods=['POST'])
  def post_questionsVote(): # add self when using server
    print("inside question Vote")
    content = request.get_json()
    print(content)
    print("The end of content")
    try:
      location_id = int(content['location_id'])
      customer_id = int(content['customer_id'])
      questions = content['questions']

    except:

      abort(400)

    for oneQuestion in questions :
      print(oneQuestion)
      print(oneQuestion.keys())

      customerratingdb = Customratingdb(customer_id=customer_id, rating=oneQuestion['rating'], location_id=location_id, question=oneQuestion['question'])
      customerratingdb.insert()
    
    return jsonify({
      'success':True
    })
######## Errors ####################
######## 404 ####################
  @app.errorhandler(404)
  def not_found(error):

        return jsonify({
          "success": False,
          "error": 404,
          "message": "resource not found"
          }), 404
######## 400 ####################
  @app.errorhandler(400)
  def not_found(error):

        return jsonify({
          "success": False,
          "error": 400,
          "message": "Bad Request"
          }), 400

  # @app.errorhandler(AuthError)
  # def unprocessable(error):

  #   return jsonify({
  #       "success": False,
  #       "error": error.status_code,
  #       "message": error.error['code']
  #   }), error.status_code

  return app

app = create_app()

if __name__ == '__main__':
    print('Inside if condition')
    app.run(host='0.0.0.0', port=8080, debug=True)
