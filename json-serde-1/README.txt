Issues with JSON serialization.

1. Document is a property bag with properties that can be any data type. What is the best way to represent that as JSON?
We currently have a decent format working but it has a lot of custom code and has some gaps/issues:

TODO DataType: can we avoid maintaining a hardcoded list of data types and all the special data type handling??
TODO DataType: how do we handle a "random" object?
TODO DataType: what about collections and maps?

2. Polymorphic JSON serde with preservation of class type information,
with Message being an example (Message subclasses Resource which subclasses Document).
We can't do a @JsonSubtypeInfo on Document since Document is actually in another module and must be unaware of its subclasses.

TODO: DataType: can we avoid maintaining a hardcoded list of data types and all the special data type handling??
TODO: DataType: how do we handle a "random" object?
TODO: DataType: what about collections and maps?
TODO: Document: presumably, either JsonSerialize/JsonDeserialize or @JsonTypeInfo (how?)
TODO: Document.deserializeWithType? Need to figure out if this is needed and if so, is this the right implementation.



