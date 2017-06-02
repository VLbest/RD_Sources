def insert(collection, data):
    collection.insert(data)

def get_known_words(collection):
    words = {}
    for word in collection.find():
        words[str(word['_id'])] = word['word']
    return words

def get_spe_word(collection, word):
    samples = {}
    id = 0
    for sample in collection.find(word):
        samples[id] = sample
        id+=1
    return samples
