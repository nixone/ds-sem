# Discrete simulation - Semestral work no.3

*Created as a semestral work for Discrete Simulation course, Author: Martin Olešnaník, academic year 2014/2015. Author is revoking any responsibility for using this work or any part of this work as a basis of a work of anybody else.*

# Objective

Objective of this semestral work was to create a simulation program for transporting people to ice hockey game and using this program to create a simulation study, on basis of which we will suggest a transport strategy to a transport company. Further specification can be found in attached document ``zadanie.pdf`` *(Slovak language only)*.

# Required output

We are required to suggest the transporting company:

*	Chosen strategy, which should specify whether his vehicles should wait for travelers after all travelers are picked up from the station, or whether it should leave immediately.
*	Quantities of vehicle types on certain transporting lines.

# Design

## Agent diagram

Because we use agent oriented simulation, for simplicity of designing the final program, we created an agent diagram:

![Agent diagram]
(https://github.com/nixone/ds-sem/raw/master/img/agent_diagram.png)

*	**Model Agent** - Agent responsible for communication between other agents
*	**Surrounding Agent** - Agent responsible for traveler arrivals to stations
*	**Movement Agent** - Agent responsible for moving vehicles between stations
*	**Boarding Agent** - Agent responsible for boarding travelers onto vehicles
*	**Exiting Agent** - Agent responsible for exiting travelers form vehicles

## Animator

As a part of the assembled program we created an animator, which enables user to closely observe simulation processes.

![Animator]
(https://github.com/nixone/ds-sem/raw/master/img/animator.png)

# Simulation study

## Decisions and conclusions

During this work we weren't provided with all the specific informations about parts of the modeled system, so we were required to proceed with our own decisions and conclusions, which should be nevertheless thoughtful and logical.

### Only bus type 2!

One of these decisions is **to ignore first type of bus**. It is useless to even consider using bus type no.1 for traveler transport. Since there is a possibility for many vehicles to occupy the same station, also all the vehicles have the same traveling, boarding and exiting speeds, we can compare departing 2 buses of type no.2 to departing 1 bus of type no.1. In this case we come with conclusion that in any situation it is more effective for us to use service of 2 buses of type no.2, since it is less expensive and more time and capacity efficient. It is therefore obvious, that we don't even need to consider using a bus of type no.1 in our solutions.

### Cumulative forecast!

Another decision is the **delay of specific buses on specific lines**. Since we have lot of information about people arrivals to stations, it is relatively easy to build so called *cumulative forecast for people count on station* for a certain time, which didn't occur yet. This would be a function of time for a specific line, which would tell us the estimate number of people on the station able to be picked up when we depart the vehicle right in that time. Since we have all the necessary information to build this function, it is relatively easy to schedule vehicles in a way that is trying to maximize their utility over time.

### When to depart buses of a private transporter?

Departing private buses is meaningful if there is a time space on the stations which is not covered by the transporter and people need to wait for "second turn around" of buses. During normal buses operation, these are designed to not occur, since they are minimizing also the wait of people on the stations. 

We are therefore trying to depart the private transporter vehicles during a time when normal buses are returning for a second turn around.

## Experiments

By experiments with our simulation model we came to data, on which we can clearly decide our strategy for both transporter and also specific depart schedule to maximize utility, time effectivity and customer satisfaction.

*Timestamps are indicated since the moment 1h 13 minutes before the ice hockey game start, since this moment marks the start of people arriving to stations.*

### Strategy without waiting

With strategy without waiting, the bus leaves immediately after filling up or getting all people from the station aboard.

 Vehicle | Line A | Line B | Line C
 --- | --- | --- | ---
 **Bus no. 2 x** | **16** | **7** | **7**
 *Departure* | 3.3m +~3m | 17m +~5.5m | 11.8m +~5m
 **Microbus x** | **2** | **1** | **10**
 *Departure* | 59m +0.5m | 62m +0.5m | 53m +0.5m

 Variant | % people who missed the game | Wait time for bus | Bus usage | Private gain
 --- | --- | --- | --- | ---
 Without private transporter | 7.5% | 6m 29s | 98.5% | -
 With private transporter | 5.0% | 5m 55s | 98.2% | 3118 Kč

Expenses using this bus configuration: **193 500 000 Kč**.
 
### Strategy with waiting 1.5 minutes

With strategy with waiting the bus is waiting for 1.5 minutes after it picks up everybody at the station (if it can pick up anybody else), and leaves only after that.

 Vehicle | Line A | Line B | Line C
 --- | --- | --- | ---
 **Bus no. 2 x** | **18** | **10** | **10**
 *Departure* | 0m +~0.3-3.5m | 10m +~3-6m | 6.3m +~4-7m
 **Microbus x** | **13** | - | -
 *Departure* | 58m +1m | - | -

 Variant | % people who missed the game | Wait time for bus | Bus usage | Private gain
 --- | --- | --- | --- | ---
 Without private transporter | 7.7% | 3m 40s | 98.0% | -
 With private transporter | 5.4% | 3m 23s | 98.0% | 3120 Kč

Expenses using this bus configuration: **245 100 000 Kč**.

## Observations

1.	**Waiting is useful for people, wasteful for transporter.** As we can see, waiting reduces time people need to spend waiting for bus. On the other hand we need to depart more buses which makes the solution unwanted for transporters.
2.	**Private transporter could use more vehicles**. During simulation we can clearly observe a space for private transporter having more than specified amount of vehicles to use. Maximum gain in the system (since "microbuses" can't make the turn around since their usual departure) is 3120 Kč, and in both cases the gain is almost reaching the maximum possible value.

## Correctness of the solution

It is obvious that more used are the buses, more effective is the final solution. To see, how full our buses are, we evaluate a statistic. Its value in all solutions doesn't fall under 98%. This value describing how effectively we use our buses can be intuitively understood as very good, even optimistic, since in real life we don't usually see transport vehicles used this well.

**Since we can "use our buses to 98%" and also any descrease in bus number on any line would violate the service quality conditions, we accept our solution as correct and acceptable.**

## Evaluation and conclusion

On the basis of our observations and simulation we can suggest to our transporter **strategy without waiting with the use of 30 buses of no. 2**.

To private transporter we can suggest to divide his **13 microbuses in ratios 2:1:10 on lines A:B:C respectively**.