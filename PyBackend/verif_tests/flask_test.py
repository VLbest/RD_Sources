from flask import Flask, jsonify, request
from flask_pymongo import PyMongo

app = Flask(__name__)

app.config['MONGO_DBNAME'] = 'test'
app.config['MONGO_URI'] = 'mongodb://localhost:27017/test'

mongo = PyMongo(app)

""" Function accepts a json array and insert ot into a database """
@app.route('/data', methods=['POST'])
def add_star():
    data = request.json
    db = mongo.db.dummy
    db.insert(data)
    return jsonify({'result' : "hi"})

if __name__ == '__main__':
    app.run(debug=True, host='10.132.0.2')