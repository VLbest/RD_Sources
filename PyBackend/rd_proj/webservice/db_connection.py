from flask_pymongo import PyMongo
import rd_proj.webservice.config as wbs_conf

class DB:

    def __init__(self, flask_app):
        self.flask_app = flask_app
        self.flask_app .config['MONGO_DBNAME'] = wbs_conf.db_name
        self.flask_app .config['MONGO_URI'] = 'mongodb://'+ wbs_conf.db_address +'/'+wbs_conf.db_name
        self.mongodb = PyMongo(self.flask_app)

    def getDB(self):
        return self.mongodb