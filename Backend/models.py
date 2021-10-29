from sqlalchemy import Column, String, create_engine, Integer, Float, DateTime
from flask_sqlalchemy import SQLAlchemy
import json
import os
import datetime

print('Start of models')

# database_path = os.environ['DATABASE_URL']

# Used to run the application on a local computer 
# database_path = os.environ.get('DATABASE_URL')
# if not database_path:
database_name = "restaurants"
database_path = "postgresql://{}/{}".format('localhost:5432', database_name)

print('DATABASE PATH : ',database_path)

db = SQLAlchemy()

'''
setup_db(app)
    binds a flask application and a SQLAlchemy service
'''
def setup_db(app, database_path=database_path):
    app.config["SQLALCHEMY_DATABASE_URI"] = database_path
    app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
    db.app = app
    db.init_app(app)
# Used to create the db in the first run
    db.create_all()


'''
Restaurant
The locations database 
'''
class Restaurant(db.Model):  
    __tablename__ = 'Restaurant'

    id = Column(Integer, primary_key=True)
    name = Column(String)
    formatted_adress = Column(String)
    formatted_phone_number = Column(String)
    geomtry = Column(String)
    icon = Column(String)
    rating = Column(Float)
    website = Column(String)
    place_id = Column(Integer)

    one_star = Column(Integer)
    two_star = Column(Integer)
    three_star = Column(Integer)
    four_star = Column(Integer)
    five_star = Column(Integer)

    city = Column(String)
    zone = Column(String)
    type_of = Column(String)

    def __init__(self, name, formatted_adress,formatted_phone_number,geomtry,icon,rating,website,place_id,
                one_star,two_star,three_star,four_star,five_star,city,zone,type_of):
        self.name = name
        self.formatted_adress = formatted_adress
        self.formatted_phone_number = formatted_phone_number
        self.geomtry = geomtry
        self.icon = icon
        self.rating = rating
        self.website = website
        self.place_id = place_id
        self.one_star = one_star
        self.two_star = two_star
        self.three_star = three_star
        self.four_star = four_star
        self.five_star = five_star
        self.city=city
        self.zone=zone
        self.type_of=type_of

        

    def format(self):
        return {
        'id': self.id,
        'name': self.name,    
        'formatted_adress' : self.formatted_adress,
        'formatted_phone_number' : self.formatted_phone_number,
        'geomtry' : self.geomtry,
        'icon' : self.icon,
        'rating' : self.rating,
        'website' : self.website,
        'place_id' : self.place_id,
        'one_start' : self.one_star,
        'two_start' : self.two_star,
        'three_start' : self.three_star,
        'four_start' : self.four_star,
        'five_start' : self.five_star,
        'city' : self.city,
        'zone' : self.zone,
        'type' : self.type_of}

    def insert(self):
        db.session.add(self)
        db.session.commit()

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()

    def calculaterating(self, ratingNew):
        # print("rating input = ",ratingNew)
        # print("Number of 1 stars before = ",self.one_star)
        if (ratingNew==1):
            self.one_star = self.one_star+1
        elif (ratingNew==2):
            self.two_star = self.two_star+1
        elif (ratingNew==3):
            self.three_star = self.three_star+1
        elif (ratingNew==4):
            self.four_star = self.four_star+1
        elif (ratingNew==5):
            self.five_star = self.five_star+1
        
        # print("Number of 1 stars After before commit = ",self.one_star)
        # db.session.commit()
        # print("Number of 1 stars After After commit = ",self.one_star)
        self.rating = (self.one_star+(self.two_star*2)+(self.three_star*3)+(self.four_star*4)+(self.five_star*5))/(self.one_star+self.two_star+self.three_star+self.four_star+self.five_star)
        db.session.commit()

'''
Ratingdb
The rating database 
'''
class Ratingdb(db.Model):  
    __tablename__ = 'Ratingdb'

    id = Column(Integer, primary_key=True)
    created_date = Column(DateTime, default=datetime.datetime.utcnow)
    customer_id = Column(Integer)
    rating = Column(Integer)
    location_id = Column(Integer)
    comment_text = Column(String)
    internal_flag = Column(Integer) #0: General 1:Internal

    def __init__(self, customer_id,rating,location_id,comment_text,internal_flag):
        self.customer_id = customer_id
        self.rating = rating
        self.location_id = location_id
        self.comment_text = comment_text
        self.internal_flag = internal_flag
        

    def format(self):
        return {
        'id': self.id,
        'customer_id': self.customer_id,    
        'rating' : self.rating,
        'location_id' : self.location_id,
        'comment_text': self.comment_text}

    def insert(self):
        db.session.add(self)
        db.session.commit()

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()

    
'''
Customers
The list of customers
'''
class Customers(db.Model):  
    __tablename__ = 'Customers'

    id = Column(Integer, primary_key=True)
    name = Column(String)
    mail = Column(String)

    def __init__(self, name,mail):
        self.name = name
        self.mail = mail
        
    def format(self):
        return {
        'id': self.id,
        'name': self.name,    
        'mail' : self.mail}

    def insert(self):
        db.session.add(self)
        db.session.commit()
        return self.id

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()


'''
Comments and Reviews
The list of customers
'''
class Reviews(db.Model):  
    __tablename__ = 'Reviews'

    id = Column(Integer, primary_key=True)
    location_id = Column(Integer)
    source = Column(String)
    author_name= Column(String)
    author_url= Column(String)
    language= Column(String)
    profile_photo_url= Column(String)
    rating= Column(Integer)
    relative_time_description= Column(String)
    text= Column(String)
    time= Column(Integer)


    def __init__(self, location_id,source,author_name,author_url,language,profile_photo_url,rating,relative_time_description,text,time):
        
        self.location_id = location_id
        self.source = source
        self.author_name = author_name
        self.author_url = author_url
        self.language = language
        self.profile_photo_url = profile_photo_url
        self.rating = rating
        self.relative_time_description = relative_time_description
        self.text = text
        self.time =time
        
    def format(self):
        return {
        'location_id': self.location_id,
        'author_name': self.author_name,    
        'mail' : self.author_url,
        'language' : self.language,
        'profile_photo_url' : self.profile_photo_url,
        'rating' : self.rating,
        'relative_time_description' : self.relative_time_description,
        'text' : self.text,
        'time' : self.time}

    def insert(self):
        db.session.add(self)
        db.session.commit()
        return self.id

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()


'''
Code Generator
The list of customers
'''
class CodeGenerator(db.Model):  
    __tablename__ = 'CodeGenerator'

    id = Column(Integer, primary_key=True)
    location_id = Column(Integer)
    code_id = Column(Integer)
    

    def __init__(self, location_id,code_id):
        self.location_id = location_id
        self.code_id = code_id
        
    def format(self):
        return {
        'id': self.id,
        'location_id': self.location_id,    
        'code' : self.code_id}

    def insert(self):
        db.session.add(self)
        db.session.commit()
        return self.id

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()


'''
CustomQuestions
The list of customers
'''
class Questions(db.Model):  
    __tablename__ = 'Questions'

    id = Column(Integer, primary_key=True)
    location_id = Column(Integer)
    question = Column(String)
    

    def __init__(self, location_id,question):
        self.location_id = location_id
        self.question = question
        
    def format(self):
        return {
        # 'id': self.id,
        # 'location_id': self.location_id,    
        'question' : self.question}

    def insert(self):
        db.session.add(self)
        db.session.commit()
        return self.id

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()

'''
CustomeRatingdb
The rating database 
'''
class Customratingdb(db.Model):  
    __tablename__ = 'Customratingdb'

    id = Column(Integer, primary_key=True)
    created_date = Column(DateTime, default=datetime.datetime.utcnow)
    customer_id = Column(Integer)
    rating = Column(Integer)
    location_id = Column(Integer)
    question = Column(String)

    def __init__(self, customer_id,rating,location_id,question):
        self.customer_id = customer_id
        self.rating = rating
        self.location_id = location_id
        self.question = question
        
        

    def format(self):
        return {
        'id': self.id,
        'customer_id': self.customer_id,    
        'rating' : self.rating,
        'location_id' : self.location_id,
        'comment_text': self.comment_text}

    def insert(self):
        db.session.add(self)
        db.session.commit()

    def update(self):
        db.session.commit()
    
    def delete(self):
        db.session.delete(self)
        db.session.commit()
