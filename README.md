Botania Tweaks
==============

A little mod containing tweaks to Botania. Mainly focused around adding more configuration options, tiny conveniences, increasing the challenge, or just silly things. All the gameplay-affecting tweaks are configurable.

Build Instructions
==================

This mod has (soft) dependencies on a bunch of other mods, some of which I couldn't find on Mavens. SO, here you go.

* Clone this repo
* Populate `libs/` with mods from CurseForge:
  * Agricraft: agricraft-2.12.2-1.12.0-a4.jar
  * Extended Crafting: extendedcrafting-1.12-1.4.2.jar
  * InfinityLib: infinitylib-1.12.0.jar
  * Cucumber Library: cucumber-1.12-1.1.1.jar
* Run those through a tool like BON2 so you have deobfuscated versions (make sure to use the same mappings version)
* Build and run as usual

Featureset
==========

* mana fluxfields that you can tweak to match your modpack's FE ecosystem
* use dispensers on glass bottles in the End to make Ender Air *(tentative mechanic)*
* crafttweakable agglomeration plate
  * with a custom multiblock, too!
  * option to consume or replace the blocks in the multiblock
  * jei support *(that's only KINDA broken!)*
  * create your own Lexica Botania pages for your cool recipes, too!
* POTATIC EXPANSION: 
  * put tiny potato in flower pot!!!
  * octuple compressed tiny potato w/ glorius rainbow color
  * LOVABLE, HUGGABLE, and PETTABLE. Just like the original.
  * spent way too much time on this
* auto-place corporea sparks (from your hotbar) when you place funnels, crystal cubes, and such
  * also: automatically place floral powder on the spark
* sheep can eat the custom Botania grass blocks, as well as the vanilla grass, to regrow wool
* the Spork - crafting ingredient to get in to corporea much, much earlier
  * also comes in rainbow shitpost size
* crafty crate "advanced mode"
  * now uses mana? more mana for recipes with more items. configurable amount, too.
  * also a "hard mode" in case that's not enough: very strict mana input requirements, very challenging to automate
  * made upon request for a modpack where the crafty crate was actually *too easy*
  * I am also in disbelief
* if Avaritia is present: the Dire Crafty Crate will be added
  * yeah it's a 9x9 crafty crate that crafts Extreme Recipes
  * ridiculously challenging to automate
  * what are you going to do about it
* if Extended Crafting is present: four Extended Crafty Crates will be added
  * same thing really
* enable Garden of Glass water bowls on non-Gog maps

The following features also exist, using a little ASM hackery: 

* passive decay "hard mode"
  * make any generating flower you want passive decay
  * reduce the passive decay timer
* GIANT buffs to underutilized & cool mana sources
  * entropinnyum buff - 8x mana output
    * you can run it off a cobbleworks
  * spectrolus buff - 10x mana output
    * doesn't need 1 whole double chest of wool to fill a pool, lmao
  * big buff to manastorm charge mana output 
    * "wait, those even had mana output?" you ask?
    * mmmhmm, yea, they did
* unlocked petal apothecary items (put any items in Petal Apothecary)
  * except seeds and buckets, duh
* use Garden of Glass's much cheaper prices to run the Orechid, even if GoG is not enabled
* make the Entropinnyum ignore TNT from a TNT duplicator device
* make the Entropinnuim only accept vanilla TNT
* readd Horn of the Wild compat with Agricraft crops (which got lost somewhere in an update)
  * dooting a crop with a horn harvests and replants it while keeping the crop sticks, like in 1.7
  * I patch Agricraft for this instead of Botania (smh scope creep)
  * Patch shouldn't crash if the Agricraft developers implement it themselves
* change the amount of mana stored in the Guilty Mana Pool; added on request
* keep statistics on how much mana all flowers generate
  * all players on the server can earn advancements when a certain amount of mana is generated
  * you can also filter it per-flower; "generate a pool of mana using only munchdews"
  * debug the statistics with `/botaniatweaks-debug-stats` or read from the nbt file

License and Attribution
=======================

---

> This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.

---

In short, this means that your modifications of Botania Tweaks must use the same license, but your modpacks that use it do not; however, modpacks that distribute a custom modified build of this mod must make their changes available under the same license. Read LICENSE.md for more details.

Please also familiarize yourself with the [license of Botania](https://botaniamod.net/license.php). The Mozilla Public License was (in part) chosen to be in compliance with the Copyleft Clause. Also because I like it.

Portions of this code were adapted from Botania, by Vazkii. I used to maintain a list of classes that I copied from and/or modified but it got really long and outdated. Oh well.

Portions of this code were adapted from Gotta Go Fast, by Thiakil. That mod is distributed under the terms of the MIT license. The license is available in CLASSWRITER_LICENSE.md.

* `WorkaroundClassWriter` copied from `MCClassWriter`.

Previous (pre-release) versions of this mod were CC0.

I take this all too seriously.