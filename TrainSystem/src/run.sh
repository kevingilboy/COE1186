echo '-------------------'
echo '|                 |'
echo '|    Compiling    |'
echo '|                 |'
echo '-------------------'

javac Shared/Module.java
javac Shared/SimTime.java
javac Modules/Ctc/*.java
javac Modules/TrackController/*.java
javac Modules/TrackModel/*.java
javac Modules/TrainModel/*.java
javac Modules/TrainController/*.java
javac Modules/Mbo/*.java
javac Simulator/Simulator.java

echo '> Done'
sleep 1

echo
echo '-------------------'
echo '|                 |'
echo '|     Running     |'
echo '|                 |'
echo '-------------------'
sleep 1

echo '*        . . . . o o o o o                        '
echo '*               _____      o       ___________    '
echo '*      ____====  ]OO|_n_n__][.     |   HSS   |    '
echo '*     [________]_|__|________)<    |COE 1186 |    '
echo '*      oo    oo   oo OOOO-| oo\\_  ~~~|~~~|~~~    '
echo '*  +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+-'

java Simulator.Simulator
