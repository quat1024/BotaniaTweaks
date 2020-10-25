Botania Tweaks
==============

A little mod containing tweaks to Botania. Mainly focused around adding more configuration options, tiny conveniences, increasing the challenge, or just silly things. All the tweaks are configurable.

Build Instructions
==================

Ok sometimes it doesn't remap properly because stable_39 renamed some methods and for whatever reason some of the mods still look for the old names even though they should be getting remapped... If you pass "botaniatweaks.awful=true" as a system properly I throw in an extra class transformer that patches ItemStack and ResourceLocation to add the methods back under their old names. I love working on forge mods with like 12 dependencies, highly recommended 

Featureset
==========

I used to have a giant list here but it always got out of sync with the one I keep on [my CurseForge page](https://minecraft.curseforge.com/projects/botania-tweaks) so just go there!

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