Botania Tweaks
==============

A little mod containing tweaks to Botania. Mainly focused around adding more configuration options, tiny conveniences, increasing the challenge, or just silly things. All the gameplay-affecting tweaks are configurable.

Build Instructions
==================

Yeah so I couldn't find a working maven for Agricraft, and Jitpack seemed to choke on it too, so here's what you do:

* Clone this repo
* Populate `libs/` with mods from CurseForge:
* * Agricraft: agricraft-2.12.2-1.12.0-a4.jar
* * InfinityLib: infinitylib-1.12.0.jar
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
* POTATIC EXPANSION: 
  * put tiny potato in flower pot!!!
  * octuple compressed tiny potato w/ glorius rainbow color
  * LOVABLE, HUGGABLE, and PETTABLE. Just like the original.
  * spent way too much time on this
* auto-place corporea sparks (from your hotbar) when you place funnels, crystal cubes, and such
  * also: automatically place floral powder on the spark
* sheep can eat the custom Botania grass blocks, as well as the vanilla grass, to regrow wool
* the Spork - crafting ingredient to get in to corporea much, much earlier

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

License and Attribution
=======================

---

> This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.

---

In short, this means that your modifications of Botania Tweaks must use the same license, but your modpacks that use it do not; however, modpacks that distribute a custom modified build of this mod must make their changes available under the same license. Read LICENSE.md for more details.

Please also familiarize yourself with the [license of Botania](https://botaniamod.net/license.php). The Mozilla Public License was (in part) chosen to be in compliance with the Copyleft Clause. Also because I like it.

Portions of this code were adapted from Botania, by Vazkii. A list of copied/modified classes is available below:

* `BlockCompressedTinyPotato` adapted from `BlockTinyPotato`
* `TileCompressedTinyPotato` adapted from `TileTinyPotato`
* `RenderTileCompressedTinyPotato` adapted from `RenderTileTinyPotato`
* `ModelCompressedTinyPotato` adapted from `ModelTinyPotato`
* `BotaniaTweakerHooks` contains content from `TileAltar`
* `brighter_grayscale_tiny_potato` and `rainbowtato` adapted from `tiny_potato.png`
* `terrasteeloverlay.png` adapted from `terrasteeloverlay.png`
* `PacketCustomTerraPlate` adapted from `PacketBotaniaEffect`
* `PageCustomTerrasteel` adapted from `PageTerrasteel`

Portions of this code were adapted from Gotta Go Fast, by Thiakil. That mod is distributed under the terms of the MIT license. The license is available in CLASSWRITER_LICENSE.md.

* `WorkaroundClassWriter` copied from `MCClassWriter`.

Previous (pre-release) versions of this mod were CC0.

I take this all too seriously.