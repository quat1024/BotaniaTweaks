Botania Tweaks
==============

A little mod containing tweaks to Botania. Mainly focused around adding more configuration options, tiny conveniences, increasing the challenge, or just silly things. All tweaks are configurable.

Current features:
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


Planned features:
* any other little tweaks I can think of
* pre end corporea hnnnng
* does that shitty corproea-spark-augment idea belong here? probably too big to asm
* *maybe* patch entropinnyum tnt duplication

License and Attribution
=======================

---

> This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.

---

In short, this means that your modifications of Botania Tweaks must use the same license, but your modpacks that use it do not.

Please also familiarize yourself with the [license of Botania](https://botaniamod.net/license.php). The Mozilla Public License was (in part) chosen to be in compliance with the Copyleft Clause. Also because I like it.

Portions of this code were adapted from Botania, by Vazkii. A list of copied/modified classes is available below:

* `TileCompressedTinyPotato` adapted from `TileTinyPotato`
* `RenderTileCompressedTinyPotato` adapted from `RenderTileTinyPotato`
* `ModelCompressedTinyPotato` adapted from `ModelTinyPotato`
* `BotaniaTweakerHooks` contains content from `TileAltar`

Previous (pre-release) versions of this mod were CC0.

I take this all too seriously.