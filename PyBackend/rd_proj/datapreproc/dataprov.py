from pymongo import MongoClient
import numpy as np
import pandas as pd
from scipy import signal
import sklearn.preprocessing as prep


mongo_client = MongoClient("mongodb://localhost:27017/")
db = mongo_client['test']
collection = db['realdata']

Y = np.array(["OK", "NO", "QUICK", "PISTOL"])



NB_CLASSES = len(Y)
NB_SAMPLES = 40
NB_FEATURES = 18
NB_TIME_STEPS = 0
NB_FEATURIZED_TS = 11

def get_average_sample_length():
    all_length = []
    for word in Y:
        for raw_data in collection.find({'GestureName': word}):
            sample_len = np.shape(raw_data['Data'])[0]
            all_length = np.append(all_length, sample_len)
    return int(np.round(np.average(all_length)))

def process_features(raw_data):
    print("\t Processing features.")
    time_step = 0
    # data  = FEATURES x TIME_STEPS
    data = np.zeros(shape=(NB_FEATURES, 0))
    for frture_set in raw_data['Data']:
        features = np.array([frture_set[key] for key in frture_set.keys()]).T
        features = np.delete(features, NB_FEATURES, 0)
        data = np.insert(data,time_step,features,1)
        time_step += 1
    print("\t\t Done.")
    return data


def resample_features(data):
    print("\t Resample features to:", NB_TIME_STEPS)
    feature_num = 0
    resampled_data = np.zeros(shape=(0,NB_TIME_STEPS))
    for feature in data:
        resampled_feature = signal.resample(feature, NB_TIME_STEPS)
        resampled_data = np.insert(resampled_data, feature_num, resampled_feature, 0)
        feature_num += 1
    print("\t\t Done.")
    return resampled_data


def get_data_for_word(word):
    # Counting a number of samples for this word
    count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
    print(count, "entries for :", word)
    print("Processing :", word)
    sample_num = 0
    # word_data = SAMPLES x FEATURES x TIME_STEPS
    word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_TIME_STEPS))
    for raw_data in collection.find({'GestureName': word}):

        data = process_features(raw_data)
        resampled_data = resample_features(data)
        normalized_data = prep.minmax_scale(X=resampled_data, copy=True)
        word_data = np.insert(word_data,sample_num, normalized_data, 0)
        sample_num += 1
    print("\t Done.")
    return word_data

def flaten_time(X):
    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_TIME_STEPS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_TIME_STEPS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1
    return new_X

def get_normal_X(normalize = True):
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_TIME_STEPS))
    for data_class in Y:
        word_data = get_data_for_word(data_class)
        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    print("Final X Shape: ", np.shape(X) )
    print("Final Y Shape: ", np.shape(Y) )
    print("hola hola hola, oleeee")
    return X

def featurize_time_series(ts):
    from cesium import featurize
    features_to_use = ["amplitude",
                       "percent_beyond_1_std",
                       "maximum",
                       "max_slope",
                       "median",
                       "median_absolute_deviation",
                       "percent_close_to_median",
                       "minimum",
                       "skew",
                       "std",
                       "weighted_average"]

    new = featurize.featurize_time_series(times=np.arange(0, np.shape(ts)[0]),
                                          values=ts,
                                          errors=None,
                                          features_to_use=features_to_use)
    return new.values

def get_DS1():
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_TIME_STEPS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_TIME_STEPS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, NB_FEATURES, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Resample features to:", NB_TIME_STEPS)
            feature_num = 0
            resampled_data = np.zeros(shape=(0,NB_TIME_STEPS))
            for feature in data:
                resampled_feature = signal.resample(feature, NB_TIME_STEPS)
                resampled_data = np.insert(resampled_data, feature_num, resampled_feature, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, resampled_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1
    return X

def get_DS2():
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_TIME_STEPS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_TIME_STEPS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, NB_FEATURES, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Resample features to:", NB_TIME_STEPS)
            feature_num = 0
            resampled_data = np.zeros(shape=(0,NB_TIME_STEPS))
            for feature in data:
                resampled_feature = signal.resample(feature, NB_TIME_STEPS)
                normalized_data = prep.minmax_scale(X=resampled_feature, copy=True)
                resampled_data = np.insert(resampled_data, feature_num, normalized_data, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, resampled_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1
    return X

def get_DS3():
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_TIME_STEPS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_TIME_STEPS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, NB_FEATURES, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Resample features to:", NB_TIME_STEPS)
            feature_num = 0
            resampled_data = np.zeros(shape=(0,NB_TIME_STEPS))
            for feature in data:
                resampled_feature = signal.resample(feature, NB_TIME_STEPS)
                resampled_data = np.insert(resampled_data, feature_num, resampled_feature, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, resampled_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_TIME_STEPS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_TIME_STEPS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS4():
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_TIME_STEPS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_TIME_STEPS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, NB_FEATURES, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Resample features to:", NB_TIME_STEPS)
            feature_num = 0
            resampled_data = np.zeros(shape=(0,NB_TIME_STEPS))
            for feature in data:
                resampled_feature = signal.resample(feature, NB_TIME_STEPS)
                resampled_data = np.insert(resampled_data, feature_num, resampled_feature, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, resampled_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_TIME_STEPS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_TIME_STEPS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, normalized_data, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS5():
    NB_FEATURES = 7
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, 18, 0)
                features = np.delete(features, 17, 0)
                features = np.delete(features, 16, 0)
                features = np.delete(features, 15, 0)
                features = np.delete(features, 14, 0)
                features = np.delete(features, 13, 0)
                features = np.delete(features, 12, 0)
                features = np.delete(features, 11, 0)
                features = np.delete(features, 10, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            #normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS6():
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, NB_FEATURES, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, normalized_data, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS7():
    NB_FEATURES = 14
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_TIME_STEPS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_TIME_STEPS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, 18, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Resample features to:", NB_TIME_STEPS)
            feature_num = 0
            resampled_data = np.zeros(shape=(0,NB_TIME_STEPS))
            for feature in data:
                resampled_feature = signal.resample(feature, NB_TIME_STEPS)
                resampled_data = np.insert(resampled_data, feature_num, resampled_feature, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, resampled_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_TIME_STEPS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_TIME_STEPS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS8():
    NB_FEATURES = 14
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, 18, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            #normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS9():
    NB_FEATURES = 11
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 22, 0)
                features = np.delete(features, 18, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)
                features = np.delete(features, 5, 0)
                features = np.delete(features, 4, 0)
                features = np.delete(features, 3, 0)
                features = np.delete(features, 2, 0)
                features = np.delete(features, 1, 0)
                features = np.delete(features, 0, 0)
                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            #normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS10():
    NB_FEATURES = 7
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 21, 0)
                features = np.delete(features, 20, 0)
                features = np.delete(features, 19, 0)
                features = np.delete(features, 18, 0)
                features = np.delete(features, 17, 0)
                features = np.delete(features, 16, 0)
                features = np.delete(features, 15, 0)
                features = np.delete(features, 14, 0)
                features = np.delete(features, 13, 0)
                features = np.delete(features, 12, 0)
                features = np.delete(features, 11, 0)
                features = np.delete(features, 10, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)

                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            #normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS11():
    NB_FEATURES = 4
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 18, 0)
                features = np.delete(features, 17, 0)
                features = np.delete(features, 16, 0)
                features = np.delete(features, 15, 0)
                features = np.delete(features, 14, 0)
                features = np.delete(features, 13, 0)
                features = np.delete(features, 12, 0)
                features = np.delete(features, 11, 0)
                features = np.delete(features, 10, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)
                features = np.delete(features, 5, 0)
                features = np.delete(features, 4, 0)
                features = np.delete(features, 3, 0)
                features = np.delete(features, 2, 0)
                features = np.delete(features, 1, 0)
                features = np.delete(features, 0, 0)

                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            #normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, flat_features, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

def get_DS12():
    NB_FEATURES = 4
    class_num = 0
    X = np.zeros(shape=(0, NB_SAMPLES, NB_FEATURES, NB_FEATURIZED_TS))
    for data_class in Y:
        count = collection.find({'GestureName': 'Hola'},{'GestureName':1}).count()
        print(count, "entries for :", data_class)
        print("Processing :", data_class)
        sample_num = 0
        # word_data = SAMPLES x FEATURES x TIME_STEPS
        word_data = np.zeros(shape=(sample_num, NB_FEATURES, NB_FEATURIZED_TS))
        for raw_data in collection.find({'GestureName': data_class}):

            print("\t Processing features.")
            time_step = 0
            # data  = FEATURES x TIME_STEPS
            data = np.zeros(shape=(NB_FEATURES, 0))
            for frture_set in raw_data['Data']:
                features = np.array([frture_set[key] for key in frture_set.keys()]).T
                features = np.delete(features, 18, 0)
                features = np.delete(features, 17, 0)
                features = np.delete(features, 16, 0)
                features = np.delete(features, 15, 0)
                features = np.delete(features, 14, 0)
                features = np.delete(features, 13, 0)
                features = np.delete(features, 12, 0)
                features = np.delete(features, 11, 0)
                features = np.delete(features, 10, 0)
                features = np.delete(features, 9, 0)
                features = np.delete(features, 8, 0)
                features = np.delete(features, 7, 0)
                features = np.delete(features, 6, 0)
                features = np.delete(features, 5, 0)
                features = np.delete(features, 4, 0)
                features = np.delete(features, 3, 0)
                features = np.delete(features, 2, 0)
                features = np.delete(features, 1, 0)
                features = np.delete(features, 0, 0)

                data = np.insert(data,time_step,features,1)
                time_step += 1
            print("\t\t Done.")

            print("\t Featurize")
            feature_num = 0
            featurized_data = np.zeros(shape=(0,NB_FEATURIZED_TS))
            for feature in data:
                featurized_ts = featurize_time_series(feature)
                featurized_data = np.insert(featurized_data, feature_num, featurized_ts, 0)
                feature_num += 1
            print("\t\t Done.")

            word_data = np.insert(word_data,sample_num, featurized_data, 0)
            sample_num += 1

        X = np.insert(X, class_num, word_data, 0)
        class_num +=1

    class_num2 = 0
    new_X = np.zeros(shape=(0, NB_SAMPLES, (NB_FEATURES * NB_FEATURIZED_TS)))
    for class_ in X:
        sample_num2 = 0
        word_data = np.zeros(shape=(0, (NB_FEATURES * NB_FEATURIZED_TS)))
        for sample in class_:
            flat_features = np.ravel(sample, order='F')
            normalized_data = prep.minmax_scale(X=flat_features, copy=True)
            word_data = np.insert(word_data,sample_num2, normalized_data, 0)
            sample_num2+=1
        new_X = np.insert(new_X, class_num2, word_data, 0)
        class_num2 += 1

    return new_X

NB_TIME_STEPS = get_average_sample_length()

