# -*- coding: utf-8 -*-

from .__init__ import app, db
from .models import Users
from flask import jsonify, request, Response
from sqlalchemy import text
from functools import wraps
import base64
import json
import ipdb
import decimal

def check_auth(username, password):
    """This function is called to check if a username /
    password combination is valid.
    """
    print "check_auth"
    print username
    print password
    dude = Users.query.filter_by(pseudo = username).first()
    if dude is None:
        return False
    print dude
    print "end check_auth"
    if username == dude.pseudo and password == dude.password:
        print "Hi!!!!!"
        return True
    else:
        return False

def authenticate():
    """Sends a 401 response that enables basic auth"""
    return Response(
    'Could not verify your access level for that URL.\n'
    'You have to login with proper credentials', 401,
    {'WWW-Authenticate': 'Basic realm="Login Required"'})

def requires_auth(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        encoded = request.headers.get('Authorization')
        decoded = base64.b64decode(encoded[6:])
        #ipdb.set_trace()
        username, password = decoded.split(':')
        if not encoded or not check_auth(username, password):
            return authenticate()
        return f(*args, **kwargs)
    return decorated

@app.route('/japronto/api/<string:user>', methods={'GET'})
@requires_auth
def connect(user):
    print "connect()"
    dude = Users.query.filter_by(pseudo = user).first()
    return jsonify(id = dude.id, pseudo = dude.pseudo, name=dude.name)

@app.route('/japronto/api/user/new/', methods={'POST'})
def signup():
    user = json.loads(request.get_data())
    m1 = Users(user['pseudo'], user['password'], user['name'], user['surname'], user['phone'], user['mail'], user['CPF'], int(user['status']))
    db.session.add(m1)
    db.session.commit()
    test = Users.query.filter_by(pseudo = user['pseudo']).first()
    return jsonify(id="14")

@app.errorhandler(404)
def not_found(error):
    return "404 error",404


@app.route('/japronto/api/foods/', methods={'GET'})
def get_food():
    dude = Users.query.filter_by(status = 2).first()
    #dude = jsonify(pseudo=dude.pseudo, name=dude.name)
    return jsonify(name=dude.name, pseudo=dude.pseudo)

@app.route('/japronto/api/restaurant/near', methods={'GET'})
def get_near():
    print "NEAR"
    lat = str(request.args.get('lat'))
    lng = str(request.args.get('lng'))

    print lat

    sql = text('SELECT id, id_chef, lat, lng,(6371 * acos( cos( radians('+lat+' ) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians( '+lng+' ) ) + sin(radians('+lat+' )) * sin(radians(lat)))) distance FROM area HAVING distance < 0.5 ORDER BY distance')
    result = db.engine.execute(sql)
    places = result.fetchall()
    response = []
    for tmp in places:
        print "in the first for"
        print tmp
        chef = Users.query.filter_by(id = tmp.id_chef).first()
        print chef.id
        chefObj = {'id':chef.id, 'pseudo':chef.pseudo, 'password':'lol', 'name':chef.name, 'surname':chef.surname, 'phone':chef.phone, 'mail':chef.mail, 'CPF':chef.cpf, 'status':chef.status}
        sql = text('select ifnull(avg(note),0) as average from  reviews where id_chef='+str(tmp.id_chef)+';')
        res = db.engine.execute(sql)
        resp = res.fetchone()
        avg = resp[0]
        print avg
        sq = text('select * from dishes where id_chef = '+str(tmp.id_chef)+' and disponibility = true;')
        res = db.engine.execute(sq)
        menu = res.fetchall()
        print menu
        foods =[]
        for dish in menu:
            print dish
            one = {'id':dish.id, 'id_chef':dish.id_chef, 'name':dish.name, 'image':dish.image, 'description':dish.description, 'disponibility':dish.disponibility}
            foods.append(one)
            print one

        dictTmp={'id':tmp.id, 'id_chef':tmp.id_chef, 'lat':str(tmp.lat), 'lng':str(tmp.lng), 'distance':str(tmp.distance), 'chef':chefObj, 'menu':foods}
        response.append(dictTmp)
        print json.dumps(response[0])
        return json.dumps(response[0])

"""
@app.route('/japronto/api/foods', methods=['POST'])
def create_food():
    if not request.json or not 'name' in request.json:
        abort(400)

    food = {
        'id': foods[-1]['id'] + 1,
        'name': request.json['name'],
        'description': request.json.get('description', "ta mere"),
        'price': request.json['price']
    }
    foods.append(food)

    return jsonify({'food': food}), 201


@app.route('/japronto/api/foods/<int:food_id>', methods=['PUT'])
def update_food(food_id):
    food = [food for food in foods if food['id'] == food_id]
    if len(food) == 0:
        abort(404)
    if not request.json:
        abort(400)
    if 'name' in request.json and type(request.json['name']) != unicode:
        abort(400)
    if 'description' in request.json and type(request.json['description']) is not unicode:
        abort(400)
    food[0]['name'] = request.json.get('name', food[0]['name'])
    food[0]['description'] = request.json.get(
        'description', food[0]['description'])
    food[0]['price'] = request.json.get('price', food[0]['price'])
    return jsonify({'food': food[0]})


@app.route('/japronto/api/foods/<int:food_id>', methods=['DELETE'])
def delete_food(food_id):
    food = [food for food in foods if food['id'] == food_id]
    if len(food) == 0:
        abort(404)
    foods.remove(food[0])
    return jsonify({'result': True})

"""


