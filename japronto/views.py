from japronto import db, app

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)


@app.route('/japronto/api/foods', methods=['GET'])
def get_foods():
    # return jsonify({'foods':foods})
    dude = 'Mon Cul'
    print 'Mon cul'
    return jsonify({'users': dude})


@app.route('/japronto/api/foods/<int:food_id>', methods={'GET'})
def get_food(food_id):
    food = [food for food in foods if food['id'] == food_id]
    if len(food) == 0:
        abort(404)
    return jsonify({'food': food[0]})


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
