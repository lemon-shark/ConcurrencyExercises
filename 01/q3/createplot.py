#!/usr/bin/env python3

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

df = pd.read_csv("results.csv")
df2 = df.mean(axis=0)

labels = df2.index.values
times = df2.values
x_pos = np.arange(len(times))

plt.rcParams["figure.figsize"] = [9,5]

plt.barh(x_pos, times, align='center')
plt.yticks(x_pos, labels)
plt.ylabel("Time (ms)")
plt.tight_layout()
plt.savefig("results.png")
