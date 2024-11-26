JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \

		Message.java\
		Acceptor.java\
		Proposer.java\
		PaxosDemo.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class