check:
	javac -cp src src/tests/test.java -Xlint -d bin
	java -cp bin -ea tests/test
	
