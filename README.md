# Example MultiMC modpack using Modsman

This repo is an example and starting point for putting together a modpack that can be loaded in MultiMC using [Modsman](https://github.com/sargunv/modsman/) to download content like mods and resource packs instead of distributing them directly. This readme contains information on what the experience is like for users, usage instructions for modpack creators, and details on how it works under the hood.

This example pack consists of all my mods, their dependencies, and the Faithful texture pack. Import this URL into MultiMC to check it out for yourself:

```
https://gitlab.com/sargunv-mc-mods/modsman-modpack-example/-/archive/master/modsman-modpack-example-master.zip
```

## Users' experience

Users don't need to know anything about Modsman. To them, the pack works like any other MultiMC instance.

1. Download the modpack zip distributed by the pack creator
2. Import the modpack zip into MultiMC
3. Launch the game
4. On first run, the included Modsman binary will be invoked to download mods and resource packs from CurseForge
5. On subsequent runs, Modsman will NOT be invoked again, just like a regular pack

## User guide for modpack creators

Some knowledge of Modsman and MultiMC instances is required. Modsman is pretty straightforward to figure out if you're already familiar with using the command line on your platform.

1. Clone this repo
2. [Install modsman via Scoop or Homebrew](https://github.com/sargunv/modsman/blob/master/README.md), or add the included Modsman to your PATH
3. Set up your pack inside this instance, ensuring files from CurseForge are tracked in the *.modlist.json*
   * if your pack is for a different MC version or modloader, you'll need to modify the `config` block of the *.modlist.json*
   * option 1, install the mods with Modsman using, `modsman-cli add`
   * option 2, install the mods manually and add them to the *.modlist.json* after, using `modsman-cli discover`
4. Edit the *.FIRST_RUN_MARKER* in .minecraft to include the subdirectories you want Modsman to be invoked
   * usually this will be *mods* and/or *resourcepacks*
5. Create your pack zip **EXCLUDING** the mod jars/zips tracked by *.modlist.json* but **INCLUDING** the actual *.modlist.json*, *.FIRST_RUN_MARKER*, and *.modsman/*
   * make sure the `PreLaunchCommand` in *instance.cfg* is preserved too!
6. Distribute the pack zip to users after testing in MultiMC

## How it works

This system is inspired by the technique used in the [AOF 3 by AK9](https://github.com/AllOfFabric/All-of-Fabric-3) pack.

 * MultiMC includes a `PreLaunchCommand` setting which allows us to set up a program to run before Minecraft runs.
 * The included FirstRun program is a single-class Java program responsible for invoking Modsman via the correct runner for the user's platform, in all the directories specified by .FIRST_RUN_MARKER if present.
 * So, this just bundles Modsman, makes MultiMC invoke FirstRun before launching Minecraft, and FirstRun removes the .FIRST_RUN_MARKER after install to ensure it'll only operate once.

If you want to tweak the FirstRun logic for your own pack, simply edit *FirstRun.java* and run the build script (*build.ps1* on Windows or *build.sh* on macOS/Linux). This will place the *FirstRun.class* file into *.modsman/bin*. For convenience, I've included the built class file in this repo even though that's not standard practice for version control.