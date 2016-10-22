# -*- coding: utf-8 -*-

from .__init__ import app
#from .models import Users, Wants,Clients, Eat
from flask import jsonify, request, Response, send_from_directory
#from sqlalchemy import text
from functools import wraps
import base64
import json
import ipdb
import decimal
import MySQLdb

def check_auth(username, password):
    """This function is called to check if a username /
    password combination is valid.
    """
    print username
    print password
    db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="root",  # your password
                     db="japronto")        # name of the data base
    cur = db.cursor()
    cur.execute("select pseudo, password from client where client.pseudo = '%s' " % username)
    dude = cur.fetchone()
    print dude
    if dude is None:
        return False
    print "end check_auth"
    if username == dude[0] and password == dude[1]:
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

@app.route('/japronto/api/<string:user>', methods=['GET'])
@requires_auth
def connect(user):
    print "connect()"
    db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="root",  # your password
                     db="japronto")        # name of the data base
    cur = db.cursor()
    cur.execute("select id, pseudo, password, name from client where pseudo = '%s'" %user)
    dude = cur.fetchone()
    db.close()
    return jsonify(id = dude[0], pseudo = dude[1], name=dude[2])

@app.route('/japronto/api/user/new/', methods={'POST'})
def signup():

    user = json.loads(request.get_data())

    db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="root",  # your password
                     db="japronto")        # name of the data base
    cur = db.cursor()
    sql = "insert into client values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%d'" % (user['pseudo'], user['password'], user['name'], user['surname'], user['phone'], user['mail'], user['CPF'])
    
    try:
        cur.execute(sql)
        db.commit()
    except:
        db.rollback()
    db.close

    return jsonify(id="14")

@app.errorhandler(404)
def not_found(error):
    return "404 error",404

@app.route('/japronto/api/img/<string:filename>')
def send_pic(filename):
    return send_from_directory('/home/darakt/JaPronto/tmp', filename)

@app.route('/japronto/api/foods/', methods=['GET'])
def get_food():
    return jsonify(name='coucou', pseudo='re')

@app.route('/japronto/api/order/<string:user>', methods=['GET'])
def get_order(user):
    print "OLD"
    orderList = []

    db = MySQLdb.connect(host="localhost",    
                     user="root",         
                     passwd="root",  
                     db="japronto")  
    cur = db.cursor()
    cur.execute("select id, pseudo, password from client where client.pseudo = '%s' " % user)
    dude = cur.fetchone()
    
    sql1 = 'select id_order from listClients where id_gourmet = '+str(dude[0])+';'
    print "id_gourmet: %s"% dude[0]
    cur.execute(sql1)
    res1 = cur.fetchall()
    for row in res1:
        order = {}
        order['customer_pseudo'] = dude[1]
        id_order = row[0]
        print "id_order: %s" % id_order
        sql2 = "select id, id_chef, for_the_date, for_the_time, state, lat, lng, total from wants where id = %s ;" % id_order
        cur.execute(sql2)
        res2 = cur.fetchall()
        menu = {}
        for roww in res2:
            order['id'] = roww[0]
            order['id_chef'] = roww[1]
            order['lat'] = str(roww[5])
            order['lng'] = str(roww[6])
            order['for_the_date'] = roww[2]
            order['for_the_time'] = roww[3]
            order['state'] = str(roww[4])
            order['total'] = str(roww[7])
            id_wants = row[0]

            sql5 = 'select * from chef where id = %s;' %roww[1]
            cur.execute(sql5)
            cheff = cur.fetchone()
            cook = {'id':cheff[0], 'pseudo':cheff[1], 'password':'lol', 'name':cheff[3], 'image':cheff[4], 'surname':cheff[5], 'description':cheff[6], 'phone':cheff[7], 'mail':cheff[8], 'CPF':cheff[9]}
            order['chef']= cook
            
            sql3 = "select id_dish, numeros from listDish where id_order ="+str(id_wants)+" ;"
            cur.execute(sql3)
            res3 = cur.fetchall()
            m =[]
            for eat in res3:
                id_dish = eat[0]
                print eat
                print "id_dish: %s" %id_dish
                sql4 = "select * from dishes where id = %s" %id_dish
                cur.execute(sql4)
                food = cur.fetchone()
                dishes = {}
                dishes['id'] = str(food[0])
                dishes['id_chef'] = str(food[1])
                dishes['name'] = food[2]
                dishes['image'] = food[3]
                dishes['description'] = food[4]
                dishes['disponibility'] = str(food[5])
                dishes['number'] = eat[1]
                dishes['max'] = str(food[7])
                dishes['price'] = str(food[8])
                m.append(dishes)
            menu['dishes'] = m
            order['wanted'] = menu
            orderList.append(order)
    print json.dumps(orderList)
    return json.dumps(orderList)


@app.route('/japronto/api/restaurant/near', methods=['GET'])
def get_near():
    print "NEAR"
    lat = str(request.args.get('lat'))
    lng = str(request.args.get('lng'))

    db = MySQLdb.connect(host="localhost",    
                     user="root",         
                     passwd="root",  
                     db="japronto")  
    cur = db.cursor()
    sql = 'select id, id_chef, lat, lng,(6371 * acos( cos( radians('+lat+' ) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians( '+lng+' ) ) + sin(radians('+lat+' )) * sin(radians(lat)))) distance FROM area HAVING distance < 0.5 ORDER BY distance'
    cur.execute(sql)
    places = cur.fetchall()
    
    response = []

    for tmp in places:
        sql1 = "select * from chef where id = '%s'" % tmp[1]
        cur.execute(sql1)
        chef = cur.fetchone()
        chefObj = {'id':chef[0], 'pseudo':chef[1], 'password':'lol', 'name':chef[3], 'image':chef[4], 'surname':chef[5], 'description':chef[6], 'phone':chef[7], 'mail':chef[8], 'CPF':chef[9]}
        
        sql2 = 'select ifnull(avg(note),0) as average from  reviews where id_chef='+str(tmp[1])+';'
        cur.execute(sql2)
        avg = cur.fetchone()[0]

        sql3 = 'select * from dishes where id_chef ='+str(chef[0])+' and disponibility = true;'
        cur.execute(sql3)
        menu = cur.fetchall()
        foods =[] 

        for dish in menu:
            one = {'id':dish[0], 'id_chef':dish[1], 'name':dish[2], 'image':dish[3], 'description':dish[4], 'disponibility':dish[5], 'max':dish[7], 'price':dish[8]}
            foods.append(one)
        dictTmp={'id':tmp[0], 'id_chef':tmp[1], 'lat':str(tmp[2]), 'lng':str(tmp[3]), 'distance':str(tmp[4]), 'chef':chefObj, 'menu':foods}
        response.append(dictTmp)
    db.close()
    return json.dumps(response)

@app.route('/japronto/api/order', methods=['POST'] )
def new_order():
    print "ORDER"

    db = MySQLdb.connect(host="localhost",    
                     user="root",         
                     passwd="root",  
                     db="japronto")  
    cur = db.cursor()

    order = json.loads(request.get_data())
    print order
    sql1 = "select * from client where pseudo = '%s'" % order['customer_pseudo']
    cur.execute(sql1)
    client = cur.fetchone()

    sql2 = 'insert into wants values (null,'+ str(order['id_chef'])+', \''+order['for_the_date']+'\' , \''+order['for_the_time']+'\', 0,'+order['lat']+', '+order['lng']+', null,'+str(order['total'])+');'
    print sql2
    cur.execute(sql2)
    db.commit()

    sql3 = ' insert into listClients values (null, LAST_INSERT_ID(),'+str(client[0])+');'
    cur.execute(sql3)
    db.commit()

    cur.execute('select last_insert_id()')
    result = cur.fetchone()
    lastID = result[0]

    print lastID

    listD = order['wanted']['dishes'] 
    for d in listD:
        print "SUPER INTERESTING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
        print d
        sql4 = 'insert into listDish values (null,'+str(lastID)+','+str(d['id'])+', '+str(d['number'])+');'
        cur.execute(sql4)
        db.commit()
           
    return jsonify(id="14")

def encodeB64(path):
    print path
    with open("/home/darakt/JaPronto/tmp/prato 3.png", "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read())
    return encoded_string

def decodeB64(imgData, name):
    path = "/home/darakt/JaPronto/tmp/"+name+".png"
    fh = open(path, "wb")
    fh.write(imgData.decode('base64'))
    fh.close()
    return path
