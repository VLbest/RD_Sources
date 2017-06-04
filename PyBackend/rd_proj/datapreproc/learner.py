import numpy as np
from rd_proj.datapreproc import rd_kmeans

DS = np.load("rd_DS5.npy")

def prepare_for_kmeans():
    print("DS shape: ", np.shape(DS))
    X = np.reshape(a=DS, newshape=(np.shape(DS)[0] * np.shape(DS)[1], np.shape(DS)[2]), order='F')
    print("X_kmeans shape: ", np.shape(X))
    return X



rd_kmeans.try_k_means(4, prepare_for_kmeans())
