#!flask/bin/python
from flask import Flask, jsonify, abort, make_response, request
from pymongo import MongoClient
from models import db


foods = [
    {
        'id': 1,
        'name': u'feijao',
        'description': u'well cooked',
        'price': 20
    },
    {
        'id': 2,
        'name': u'PVT',
        'description': u'Without meat',
        'price': 18
    }
]

def create_app():
    app = Flask(__name__)
    app.config['DEBUG'] = True
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:root@localhost/japronto'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = 'False'
    db.init_app(app)
    return app

def setup_database():
    with app.app_context():
        db.create_all()



if __name__ == '__main__':
    app = create_app()
    setup_database(app)
    app.run()
