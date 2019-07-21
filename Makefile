.PHONY : all clean

all : .java.class

.java.class :
	javac *.java

clean :
	rm -f *.class
