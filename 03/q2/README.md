# Multithreaded Graph Coloring

The java code in this folder generates a large graph and colors it using a multithreaded
graph coloring algorithm.

The file `Main.java` (or if you'd rather run `run.sh`) takes 3 arguments:

1. `n` the number of nodes in the graph to be generated
2. `e` the number of edges in the graph to be generated
3. `t` the number of threads to use to color the graph

# Plotting the number of the threads `t` against execution time

If you run `createcsv.sh` (which takes an hour), you will try the following values of `t`,
five times each:

* 1
* 2
* 4
* 8
* 16
* 32
* 64
* 128
* 256
* 512

The execution time, max node degree, and max node color are then put into `results.csv` for each
run of the program.

Finally, `createplot.py` plots execution time (in milliseconds) against the number of threads used
(for a fixed size graph), and stores this plot as `time.png`.  `createplot.py` also outputs to
`stdout` the average max node degree and average max node color.
