import matplotlib.pyplot as plt
from matplotlib import style
import numpy as np
from sklearn.cluster import KMeans

style.use('ggplot')

def try_k_means(clusters, X):

    clf = KMeans(n_clusters=clusters)
    clf.fit(X)

    centroids = clf.cluster_centers_
    labels = clf.labels_

    colors = 10*["g.", "r.", "c.", "b.", "k."]

    for i in range(len(X)):
        plt.plot(X[i][0], X[i][1], colors[labels[i]], markersize=20)
    plt.scatter(centroids[:, 0], centroids[:, 1], marker='x', s=150, linewidths=5)

    # plt.scatter(X[:, 0], X[:,1], s=150, linewidths=5)
    plt.show()
