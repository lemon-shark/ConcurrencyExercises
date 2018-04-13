#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <omp.h>
#include <sys/time.h>

/* Size of the DFA */
#define MAXSTATES 5
/* Number of characters in the alphabet */
#define ALPHABETSIZE 4
/* Size of the string to match against.  You may need to adjust this. */
#define STRINGSIZE 100000000

// some DFA states
#define REJECT 4
#define ACCEPT 3
#define START 0

/* State transition table (ie the DFA) */
int stateTable[MAXSTATES][ALPHABETSIZE];

/* Initialize the table */
void initTable() {
    /* Note that characters values are assumed to be 0-based. */
    stateTable[0][0] = 1;
    stateTable[0][1] = REJECT;
    stateTable[0][2] = REJECT;
    stateTable[0][3] = REJECT;

    stateTable[1][0] = 1;
    stateTable[1][1] = 2;
    stateTable[1][2] = REJECT;
    stateTable[1][3] = REJECT;

    stateTable[2][0] = REJECT;
    stateTable[2][1] = 2;
    stateTable[2][2] = 3;
    stateTable[2][3] = 3;

    stateTable[3][0] = 1;
    stateTable[3][1] = REJECT;
    stateTable[3][2] = 3;
    stateTable[3][3] = 3;

    // REJECT state
    stateTable[4][0] = REJECT;
    stateTable[4][1] = REJECT;
    stateTable[4][2] = REJECT;
    stateTable[4][3] = REJECT;
}


/* Construct a sample string to match against.  Note that this uses characters, encoded in ASCII,
   so to get 0-based characters you'd need to subtract 'a'. */
char *buildString() {
    int i;
    char *s = (char *)malloc(sizeof(char)*(STRINGSIZE));
    if (s==NULL) {
        printf("\nOut of memory!\n");
        exit(1);
    }
    int max = STRINGSIZE-3;

    /* seed the rnd generator (use a fixed number rather than the time for testing) */
    srand((unsigned int)time(NULL));

    /* And build a long string that might actually match */
    int j=0;
    while(j<max) {
        s[j++] = 'a';
        while (rand()%1000<997 && j<max)
            s[j++] = 'a';
        if (j<max)
            s[j++] = 'b';
        while (rand()%1000<997 && j<max)
            s[j++] = 'b';
        if (j<max)
            s[j++] = (rand()%2==1) ? 'c' : 'd';
        while (rand()%1000<997 && j<max)
            s[j++] = (rand()%2==1) ? 'c' : 'd';
    }
    s[max] = 'a';
    s[max+1] = 'b';
    s[max+2] = (rand()%2==1) ? 'c' : 'd';
    return s;
}

int runStateMachine(int initialState, char *str, int n) {
    int currState = initialState;
    int nextState;
    int i = 0;
    while (i++ < n) {
        nextState = stateTable[currState][(*str)-'a'];

        if (nextState == REJECT) return REJECT;
        currState = nextState;

        str++;
    }
    return currState;
}

long long current_timestamp() { // from stackoverflow
    struct timeval te;
    gettimeofday(&te, NULL); // get current time
    long long milliseconds = te.tv_sec*1000LL + te.tv_usec/1000; // calculate milliseconds
    // printf("milliseconds: %lld\n", milliseconds);
    return milliseconds;
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("expecting one argument: number of optimistic threads");
        return 1;
    }
    int verbose = 0;
    if (argc > 2) verbose = 1;

    int numOptThreads = atoi(argv[1]);
    int totalNumThreads = numOptThreads + 1;

    initTable();
    char *s = buildString();

    int *statesComputed = (int *)calloc(numOptThreads * MAXSTATES, sizeof(int));
    omp_set_num_threads(totalNumThreads);

    int masterWorkQty = 0;
    int optimWorkQty = 0;
    if (STRINGSIZE % totalNumThreads == 0) {
        masterWorkQty = STRINGSIZE / totalNumThreads;
        optimWorkQty = masterWorkQty;
    } else {
        int remainder = STRINGSIZE % totalNumThreads;
        masterWorkQty = (STRINGSIZE / totalNumThreads) + remainder;
        optimWorkQty = STRINGSIZE / totalNumThreads;
    }

    long long startTime = current_timestamp();

    int masterEndState = 0;
    #pragma omp parallel
    {
        if (omp_get_thread_num() == 0) { // master thread
            masterEndState = runStateMachine(START, s, masterWorkQty);
        } else {
            int id = omp_get_thread_num() - 1; // non master thread IDs start at 1
            for (int i = 1; i < MAXSTATES; ++i) {
                statesComputed[(id * MAXSTATES) + i] = runStateMachine(i, s+masterWorkQty+(id*optimWorkQty), optimWorkQty);
            }
        }
    }

    long long totalTime = current_timestamp() - startTime;

    if (verbose) {
        printf("masterEndState: %d\n", masterEndState);
        for (int i = 0; i < numOptThreads; ++i) {
            int val1 = statesComputed[(i * MAXSTATES)];
            int val2 = statesComputed[(i * MAXSTATES) + 1];
            int val3 = statesComputed[(i * MAXSTATES) + 2];
            int val4 = statesComputed[(i * MAXSTATES) + 3];
            int val5 = statesComputed[(i * MAXSTATES) + 4];
            printf("%d: %d %d %d %d %d\n", i+1, val1, val2, val3, val4, val5);
        }

        int currState = masterEndState;
        for (int i = 0; i < numOptThreads; ++i) {
            currState = statesComputed[(i * MAXSTATES) + currState];
        }
        printf("final state: %d\n", currState);
    }

    printf("%d,%lld\n", numOptThreads, totalTime);

    return 0;
}
