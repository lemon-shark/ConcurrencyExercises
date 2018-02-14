- `q1.java` contains my solution to question 1
- the plot of time needed vs max radius is in `graph.png`
- My synchronization strategy was as follows:
  - there will be two shared circle variables, circle1 and circle2
  - neither thread will set their respective circle to a new value unless that new value
  does not overlap with the existing circle of the other thread
  - if it does overlap, the thread must wait for the existing circle to be done being drawn
  - if it does not overlap, the thread may set its circle to the new value
  - once a thread has set its circle, it is free to draw its circle
  - when a thread finishes drawing its circle, it notifies the other thread that it may now set
  its circle if it was waiting
