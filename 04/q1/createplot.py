#!/usr/bin/env python3

import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv("results.csv")
df["avgNumMoves"] = df.loc[:, "moves1":"moves4"].mean(axis=1)

title = "Average Number of Moves per GameCharacter vs Minutes Played\n(n=4, r=60, k=8)"
plt.title(title)
plt.plot(df["p"], df["avgNumMoves"], "r-")
plt.xlabel("Number) of Minutes")
plt.ylabel("Average Number of Moves")
plt.savefig("time.png")
