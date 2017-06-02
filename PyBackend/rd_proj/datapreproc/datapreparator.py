import numpy as np
from scipy import signal

# here data is 4D array CLASSES x SAMPLES x FEATURES x TIME_STEPS
from rd_proj.datapreproc.dataprovidercore import get_spe_word


def get_average_sample_length(data):
    all_length = np.array([])

    for cls in data:
        for sample in cls:
            np.append(all_length, len(sample[0]))

    # RETURN a INTEGER witch is an average length of all TIME_STEPS
    return np.average(all_length)

# here data is 4D array CLASSES x SAMPLES x FEATURES x TIME_STEPS , length
def normalize_time_series(data, len):
    for cls in data:
        for sample in cls:
            for feature in sample:
                for time_step in feature:
                    time_step = signal.resample(time_step, len)

    # RETURN 4D array with TIME_STEPS of equal length = len
    return data

# here data is 4D array CLASSES x SAMPLES x FEATURES x TIME_STEPS where TIME_STEPS have the same length
def flatten_data(data):

    # RETURN 3D array CLASSES x SAMPLES x (FEATURES x TIME_STEPS)
    pass