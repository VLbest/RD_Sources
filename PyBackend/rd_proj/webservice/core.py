import matplotlib
matplotlib.use("Qt5Agg", force=True)

def insert(collection, data):
    collection.insert(data)

def get_known_words(collection):
    words = {}
    for word in collection.find():
        words[str(word['_id'])] = word['word']
    return words