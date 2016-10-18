# -*- coding: utf-8 -*-
#model

#from . import db

class Users(db.Model):
    id = db.Column(db.Integer , primary_key='true')
    pseudo = db.Column(db.String(100))
    password = db.Column(db.String(100))
    name = db.Column(db.String(100))
    surname = db.Column(db.String(100))
    description = db.Column(db.Text())
    phone = db.Column(db.String(100))
    mail = db.Column(db.String(100))
    cpf = db.Column(db.String(100))
    status = db.Column(db.Integer)
    clients = db.relationship('Clients', backref='eater', lazy='dynamic')
    chef = db.relationship('Wants', backref='chef', lazy='dynamic')
    def __str__(self):
        return "<User: id: %s pseudo: %s password: %s name: %s CPF: %s status: %d>" % ( self.id, self.pseudo, self.password, self.name, self.cpf, self.status)

    '''
    def __init__(self, pseudo, password, name, surname, phone, mail, cpf, st):
        self.id = None
        self.pseudo = pseudo 
        self.password = password
        self.name = name
        self.surname = surname
        self.phone = phone
        self.mail = mail
        self.cpf = cpf
        self.status = st
    '''
   

class Wants(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    id_chef = db.Column(db.Integer, db.ForeignKey('users.id'))
    for_the_date = db.Column(db.String(100)) 
    for_the_time = db.Column(db.String(100)) 
    state = db.Column(db.Integer)
    lat = db.Column(db.String(100))
    lng = db.Column(db.String(100))
    ts = db.Column(db.DateTime)
    eat = db.relationship('Eat', backref='to_eat', lazy='dynamic')
    clients = db.relationship('Clients', backref='wants', lazy='dynamic')
'''
    def __init__(self, id_c, for_the_d, for_the_t, lat, lng):
        self.id = None
        self.chef_id = id_c
        self.for_the_date = for_the_d
        self.for_the_time = for_the_t
        self.state = 0
        self.lat = lat
        self.lng = lng
        self.ts = None
'''

class Dishes(db.Model):
    id = db.Column(db.Integer, primary_key='true')
    id_chef = db.Column(db.Integer, db.ForeignKey('users.id'))
    name = db.Column(db.String(100))
    image = db.Column(db.String(100))
    description = db.Column(db.Text)
    disponibility = db.Column(db.Boolean)
    num = db.Column(db.Integer)
    dishes = db.relationship('Eat', backref='all', lazy='dynamic')


class Clients(db.Model):
    id = db.Column(db.Integer, primary_key='true')
    id_order = db.Column(db.Integer, db.ForeignKey('clients.id'))
    id_gourmet = db.Column(db.Integer, db.ForeignKey('users.id'))
'''
    def __init__(self, idO, idG):
        self.id = None
        self.id_order = idO
        self.id_gourmet = idG
'''
class Eat(db.Model):
    id = db.Column(db.Integer, primary_key='true')
    id_order = db.Column(db.Integer, db.ForeignKey('wants.id'))
    id_dish = db.Column(db.Integer, db.ForeignKey('dishes.id'))
    def __init__(self, idO, idD):
        self.id = None
        self.id_order = idO
        self.id_dish = idD