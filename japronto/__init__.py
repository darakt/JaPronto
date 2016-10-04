# -*- coding: utf-8 -*-

from flask import Flask, abort, make_response, request
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['DEBUG'] = True

app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:root@localhost/japronto'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = 'False'

db = SQLAlchemy(app)

db.init_app(app)

from models import Users, Wants, Dishes

with app.app_context():
    db.create_all()

'''
m1 = Users( '1', '123', 'al', 'oui', 1)
m2 = Users( '2', '123', 'bob', 'oui', 2)
m3 = Users( '3', '123', 'carol', 'oui', 3)
m4 = Users( '4', '123', 'dany', 'oui', 4)

db.session.add(m1)
db.session.add(m2)
db.session.add(m3)
db.session.add(m4)

db.session.commit()

test = Users.query.filter_by(name = 'bob').first()
print test

// to drop the whole db (data+tables)
meta = db.metadata
for table in reversed(meta.sorted_tables):
    print 'Clear table %s' % table
    db.session.execute(table.delete())
db.session.commit()
'''

import japronto.views 