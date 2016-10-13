Hello all!

This is my first attempt at a -large- project, and my first attempt at working on a longer term
project outside of school activity.

Goals:
	* Create a program that generates (randomly or partially defined) objects from the Song of Ice and Fire
		Roleplaying (SIFRP) core rulebook. 

Completed:
	* Dice class rolls a number of six-sided dice (d6's) based on input (die.roll() returns the results
		of 1d6, die.roll(num) returns the result of (num)d6).
	* House
		- Rolls starting stats
			> Applies realm modifiers
			> Generates History
				' Applies historical event modifiers to stats
		- Purchase Holdings
			> Power
				' Purchases Banner Houses
			> Land
				' "Domain" consists of a terrain type and a list of features.
			> Defense (Castles, Forts, etc)
			> Influence (heirs)
				' Now purchases Heirs, but needs to purchase them according to "Heirs" table on p. 107

In-progress:
	* House
		- Purchase Holdings

Planned:
	* House
		- Purchase Holdings
			> Power (Units)
			> Wealth ()
			> Law
				' Isn't an object, just needs to adjust "House Fortune Rolls"
			> Population
				' Isn't an object, just needs to adjust "House Fortune Rolls"