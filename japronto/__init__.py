from flask import Flask, abort, make_response, request
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['DEBUG'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:root@localhost/japronto'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = 'False'


db = SQLAlchemy(app)

db.init_app(app)

#test

from models import Users, Wants, Dishes


with app.app_context():
    db.create_all()

m1 = Users( '1', 'al', 'oui', 1)
m2 = Users( '2', 'bob', 'oui', 2)
m3 = Users( '3', 'carol', 'oui', 3)
m4 = Users( '4', 'dany', 'oui', 4)

db.session.add(m1)
db.session.add(m2)
db.session.add(m3)
db.session.add(m4)

db.session.commit()