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
