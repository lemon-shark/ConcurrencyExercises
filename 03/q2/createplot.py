#!/usr/bin/env python3

import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv("results.csv")

gb = df.groupby(["numNodes","numEdges","numThreads"])
df = pd.DataFrame(gb["milliseconds","maxNodeDegree","maxNodeColor"].mean()).reset_index()

title = "Execution Time (ms) against Number of Threads"
plt.title(title)
plt.plot(df["numThreads"], df["milliseconds"], "r-", label="milliseconds")
plt.savefig("time.png")

print("Average Max Node Degree: {}".format(df["maxNodeDegree"].mean()))
print("Average Max Node Color: {}".format(df["maxNodeColor"].mean()))
