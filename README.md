JSONic
======

JSONic, a fast JSON parsing library for Java with simple usage and no dependencies.

Requires Java 7 or higher.

Usage
-----

Access to the parser is provided through ```JsonParser```. Both static and non-static methods are provided.

### Static methods

Static usage allow for quick access to the parser, but settings must be given each time the parser is used.

```java
//Returns an array or object depending on what's provided
JsonElement parsedElement = JsonParser.parse(jsonString);
JsonElement delayedElement = JsonParser.parse(jsonString, true);

//Returns an array, but throws a JsonParseException if an object is given
JsonArray parsedArray = JsonParser.parseArray(jsonArrayString);
JsonArray delayedArray = JsonParser.parseArray(jsonArrayString, true);

//Returns an object, but throws a JsonParseException if an array is given
JsonObject parsedObject = JsonParser.parseObject(jsonObjectString);
JsonObject delayedObject = JsonParser.parseObject(jsonObjectString, true);
```

### Non-static methods

Non-static usage allows for parser settings to be saved in a parser object and used as the default settings.

```java
//Creates a new default parser instance
JsonParser parser = new JsonParser();
//Creates a new parser instance to delay parsing
JsonParser delayedParser = new JsonParser(true);

//Returns an array or object depending on what's provided
JsonElement parsedElement = parser.parse(jsonString);
JsonElement delayedElement = delayedParser.parse(jsonString);

//Returns an array, but throws a JsonParseException if an object is given
JsonArray parsedArray = parser.parseArray(jsonArrayString);
JsonArray delayedArray = delayedParser.parseArray(jsonArrayString);

//Returns an object, but throws a JsonParseException if an array is given
JsonObject parsedObject = parser.parseObject(jsonObjectString);
JsonObject delayedObject = delayedParser.parseObject(jsonObjectString);
```

License
-------

Copyright EnigmaBlade.net, 2013

Distributed under the Boost Software License, Version 1.0

See LICENSE.txt or at http://www.boost.org/LICENSE_1_0.txt
