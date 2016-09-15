#model

from . import db

class Users(db.Model):
    id = db.Column(db.Integer, primary_key='true')
    pseudo = db.Column(db.String(100))
    name = db.Column(db.String(100))
    CPF = db.Column(db.String(100))
    status = db.Column(db.Integer)
    wants = db.relationship('Wants', backref='users', lazy='dynamic')
    dishes = db.relationship('Dishes', backref='users', lazy='dynamic')
    
    def __init__(self, pseudo, name, cpf, st):
        self.pseudo = pseudo 
        self.name = name
        self.CPF = cpf
        self.status = st


class Wants(db.Model):
    id = db.Column(db.Integer, primary_key='true')
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

    def __init__(self, **kwargs):
        super(User, self).__init__(**kwargs)