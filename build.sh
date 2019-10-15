
export CP="$CLASSPATH:$(pwd)/lib/hamcrest-all-1.3.jar:$(pwd)/lib/junit-4.12.jar:$(pwd)/src:$(pwd)/bin:$(pwd)/test"

javac -cp $CP src/console/*.java \
                src/console/util/*.java \
                src/consts/*.java \
                src/exception/*.java \
                src/property/*.java \
                src/se/*.java \
                src/finance/*.java \
                src/user/*.java \
                src/user/customer/*.java \
                src/user/employee/*.java  \
                'test'/*.java \
                -d bin
java -ea -cp $CP console.Console