# Demigods RPG - Utilities

**Demigods RPG** is split up into modules to prevent cyclical dependencies between different parts of the mod. This module is dedicated to different utitlities and associated classes that can be used independently of Demigods RPG.

The different utilities and associated classes will be listed and explained below:

## Data Section

The **DataSection** utility, contained in the ```datasection``` package, is made up of the following classes:

```DataSection.java``` is an interface declaring how data can be accessed from a *DataSection*. Each *DataSection* type needs to implement this interface to interact with the rest of the utility.

```FJsonSection.java``` is one of the default implementations of a *DataSection*. Data is serialized as a json string and then exported into a file. This is the simplist method of the two default implementations.

```PJsonSection.java``` is the second of the two default implementations of a *DataSection*. Data is serialized as a json string and then exported into a compatible PostgreSQL database (v9.3+) as the json type. This is useful when sharing data accross multiple instances of the Demigods RPG mod.

```DataSectionUtil.java``` is the static utility class that handles various serialization and unserialization methods for the two default impelmentations of a *DataSection*.

```Model.java``` is a simple interface representing a model data object.

```AbstractPersistentModel.java``` is an abstract class representing a persistent type of Model.

```AbstractDataRegistry.java``` is the complete, optimized, and easy to use registry that holds all data in the form of models extending ```AbstractPersistentModel``` with the id type ```String```. All data is read from a cache (```ConcurrentHashMap```) and saved to both the cache and the persistence method--either ```FJsonSection``` or ```PJsonSection```.

## Inventory GUI

The **Inventory GUI** utility is represented by the simple ```InventoryGUI.java``` class, and ```SlotFunction.java``` enum. Because of the specific nature of the inventory GUI, the declaration is all that can be seen as a utility, not any of the implementations.

## Location Utility

The **Location Utility** (```LocationUtil.java```) is a very simple static utility class that provides a String serialization method for a Bukkit ```Location``` object.

## Zone & WorldGuard Utilities

The **Zone Utility** (```ZoneUtil.java```) and **WorldGuard Utility** (```WorldGuardUtil.java```) house common boilerplate code when dealing with specific areas and location where certain activities may be restricted.

The ```WorldGuardUtil.java``` class in particular deals with the WorldGuard plugin, making use of (and manipulating) the already existing framework for world protection.
