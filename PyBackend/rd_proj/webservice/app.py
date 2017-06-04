from flask import Flask, jsonify, request
import rd_proj.webservice.config as wbs_conf
from rd_proj.webservice.db_connection import DB
import rd_proj.webservice.core as core

app = Flask(__name__)
connection = DB(flask_app=app)
mongo = connection.getDB()

@app.route('/status', methods=['GET'])
def get_status():
    return jsonify(True)

@app.route('/insert_sync', methods=['POST'])
def simple_insert():
    core.insert(mongo.db.realdata, request.json)
    return jsonify({'result': "ok"})

@app.route('/wordlist', methods=['POST'])
def simple_insert_new_word():
    core.insert(mongo.db.wordlist, request.json)
    return jsonify({'result': "ok"})

@app.route('/wordlist', methods=['GET'])
def get_known_words():
    wordlist = core.get_known_words(mongo.db.wordlist)
    return jsonify(wordlist)

if __name__ == '__main__':
    app.run(debug=True, host=wbs_conf.remote_host_ip)

