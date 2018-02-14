import numpy as np
from matplotlib import pyplot as plt

data = np.genfromtxt("data.csv", delimiter=",")

plt.plot(data[:, 0], data[:, 1])
plt.title("ms needed to draw 100 circles as a function of radius")
plt.xlabel("max radius")
plt.ylabel("ms needed to draw")
plt.savefig("graph.png")
