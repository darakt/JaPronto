# -*- coding: utf-8 -*-
#model

from . import db
import uuid

class Users(db.Model):
    id = db.Column(db.Integer , primary_key='true')
    pseudo = db.Column(db.String(100))
    password = db.Column(db.String(100))
    name = db.Column(db.String(100))
    surname = db.Column(db.String(100))
    phone = db.Column(db.String(100))
    mail = db.Column(db.String(100))
    cpf = db.Column(db.String(100))
    status = db.Column(db.Integer)
    wants = db.relationship('Wants', backref='users', lazy='dynamic')
    dishes = db.relationship('Dishes', backref='users', lazy='dynamic')
    
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

    def __str__(self):
        return "<User: id: %s pseudo: %s password: %s name: %s CPF: %s status: %d>" % ( self.id, self.pseudo, self.password, self.name, self.cpf, self.status)


class Wants(db.Model):
    id = db.Column(db.Integer, primary_key=True, default=lambda: uuid.uuid4().hex)
    chef_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    for_the = db.Column(db.DateTime)
    state = db.Column(db.Integer)
    address = db.Column(db.String(100))
    ts = db.Column(db.DateTime)



class Dishes(db.Model):
    id = db.Column(db.Integer, primary_key='true')
    chef_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    image = db.Column(db.LargeBinary)
    description = db.Column(db.Text)
    disponibility = db.Column(db.Boolean)

    #def __init__(self, **kwargs):
    #    super(User, self).__init__(**kwargs)