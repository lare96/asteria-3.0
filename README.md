# Asteria-3.0
Asteria is an open source Runescape emulator for the #317 protocol that has been in ongoing development since late 2013. It aims to be fast, reliable, scalable, and easy-to-use. It currently runs on Java 8.

Please check out the successor to this project [Luna](https://github.com/lare96/luna), which aims to correct the many mistakes I made throughout the various releases of Asteria.

# Development History
[Asteria 1.0 (Jan 2014)](http://www.rune-server.org/runescape-development/rs2-server/downloads/530739-asteria-317-runesource.html)

[Asteria 2.0 (May 2014)](http://www.rune-server.org/runescape-development/rs2-server/downloads/551082-asteria-2-0-a.html)

[Asteria 3.0 (July 2015)](http://www.rune-server.org/runescape-development/rs2-server/downloads/599705-asteria-3-0-a.html)

# Ideas for 4.0 (If developed)
~~These are things that could possibly be added to the 4.0 release if developed, entries marked with * will be added for certain. Alternatively, a 4.0 release will probably be scrapped entirely and a new project will be started from scratch containing everything below and more.~~

Everything below will be added to my new project [Luna](https://github.com/lare96/luna). I will no longer be doing any major overhauls on this repository, but I will still be fixing any basic issues that come up. Treat my new project as if it was Asteria 4.0, because it's goal is to correct everything I did wrong with this release.

- Maven to manage dependencies *
- JUnit for unit testing *
- Log4j2 for usage as the logging framework *
- Log file for serious errors (in conjunction with log4j2) *
- Classes like Item, Position, etc. made immutable *
- Usage of [Guava's service model](https://code.google.com/p/guava-libraries/wiki/ServiceExplained) in place of the Service/ServiceQueue model *
- Usage of [Guava's stopwatch](http://docs.guava-libraries.googlecode.com/git-history/master/javadoc/com/google/common/base/Stopwatch.html) in place of Asteria's stopwatch (Guava does it better) *
- ForkJoinPool instead of GameSyncTask and GameSyncExecutor? Or at the least a redesign of the two aforementioned classes
- General design and performance optimizations, some of which were mentioned on the official 3.0 release thread on Rune-Server *
- Better networking model and decoupling of message encoding/decoding *
- Update content to OSRS revision (is it really worth the time though?)
- Better task/action decoupling, support for OneInstanceTask's as well as OneTimeTask's, and support for actions and action policies such as triggering events with certain policies, etc.
- Swapping of the Groovy plugin system for something more practical like Xtend, or even Scala
- More of the content code added as plugins
- Completely null-free character lists, for the iterator, general methods, etc.
- Improving the plugin model by adding functionality for determining which plugins listeners in the chain are executed (if multiple plugin listeners are mapped to a plugin context class). Apollo does this very nicely
- Hyperion's region system with an algorithm that prioritizes player by distance and combat state
- Apollo's file system and pathfinding implementation (thanks Major)
- JAGGRAB
- YAML instead of JSON for data serialization. Much more readable and easier to write
- Usage of [Guava's EventBus](https://code.google.com/p/guava-libraries/wiki/EventBusExplained) instead of event listeners for plugins *
