#!/usr/bin/env python3

import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("timing.csv")
df2 = pd.DataFrame(df.groupby(["p", "q", "n"])["synchTime", "lockfreeTime"].mean()).reset_index()

plt.plot(df2["q"], df2["synchTime"], "b-", label="Synchronized Queue")
plt.plot(df2["q"], df2["lockfreeTime"], "r-", label="Lock-Free Queue")
plt.legend()
plt.title("p=1, n=100")
plt.savefig("plot.png")
plt.show()
