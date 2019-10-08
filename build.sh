
export CP="$CLASSPATH:$(pwd)/src:$(pwd)/bin"

javac -cp $CP src/console/*.java \
                src/consts/*.java \
                src/exception/*.java \
                src/property/*.java \
                src/se/*.java \
                src/finance/*.java \
                src/user/*.java \
                src/user/customer/*.java \
                src/user/employee/*.java  -d bin
java -ea -cp $CP console.Console