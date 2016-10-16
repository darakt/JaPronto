# -*- coding: utf-8 -*-

from flask import Flask, abort, make_response, request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.engine import reflection
from sqlalchemy import create_engine
from sqlalchemy.schema import (
    MetaData,
    Table,
    DropTable,
    ForeignKeyConstraint,
    DropConstraint,
    )

app = Flask(__name__)
app.config['DEBUG'] = True

app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:root@localhost/japronto'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = 'False'

db = SQLAlchemy(app)

db.init_app(app)

from models import Users, Wants, Dishes, Eat

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

'''
''' --------->>>>>>> Drop Everything 
engine = create_engine('mysql://root:root@localhost/japronto', echo=True)

conn = engine.connect()

# the transaction only applies if the DB supports
# transactional DDL, i.e. Postgresql, MS SQL Server
trans = conn.begin()

inspector = reflection.Inspector.from_engine(engine)

# gather all data first before dropping anything.
# some DBs lock after things have been dropped in 
# a transaction.

metadata = MetaData()

tbs = []
all_fks = []

for table_name in inspector.get_table_names():
    fks = []
    for fk in inspector.get_foreign_keys(table_name):
        if not fk['name']:
            continue
        fks.append(
            ForeignKeyConstraint((),(),name=fk['name'])
            )
    t = Table(table_name,metadata,*fks)
    tbs.append(t)
    all_fks.extend(fks)

for fkc in all_fks:
    conn.execute(DropConstraint(fkc))

for table in tbs:
    conn.execute(DropTable(table))

trans.commit()
'''

import japronto.views 