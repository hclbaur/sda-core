mkdir tmp
javac -d tmp ..\..\main\java\be\baur\sda\*.java ..\..\main\java\be\baur\sda\serialization\*.java
jar cvf sda-core.jar -C tmp .
javac -d . -cp sda-core.jar ..\java\demo.java
rmdir tmp /s /q