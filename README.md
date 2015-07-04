Simple HTTP Resources Monitor
============================================

The idea is to test if the endpoints are working properly, currently I'm only
ordering the endpoints by the response time.

This is a simple app to demonstrate akka usage, I'm using:

1. Akka
2. Scala
3. Specs2
4. SBT

TODO:

1. Use Akka Logging instead of println
2. Add Supervision functionality to handle failure
3. Add Distributed functionaly (Allow actors to run on different nodes)