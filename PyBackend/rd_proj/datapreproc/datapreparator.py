from rd_proj.datapreproc.dataprov import *
import sklearn.preprocessing as prep

print("poop")
"""
DS5
3D array CLASSES x SAMPLES x FEATURIZED_TIME_STEPS
scaling = False, resampling = False ,all_features
"""
DS = get_DS5()


np.save(file="rd_DS5", arr=DS5)
print("SAVE DS5 - DONE")